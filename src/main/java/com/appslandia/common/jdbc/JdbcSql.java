// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.jdbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.appslandia.common.base.InitializeException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JdbcSql extends InitializeObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int DEFAULT_ARRAY_MAX_LENGTH = 32;

    private String pSql;
    private Map<String, Integer> arrayLens;
    private transient Map<String, Integer> paramsMap;

    private transient String translatedSql;
    private transient Map<String, List<Integer>> indexesMap;

    public JdbcSql(String pSql) {
	this.pSql = pSql;
    }

    @Override
    public JdbcSql initialize() throws InitializeException {
	super.initialize();
	return this;
    }

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.pSql, "pSql is required.");
	translateSql();
    }

    public JdbcSql arrayLen(String parameterName, int maxLength) {
	assertNotInitialized();
	Asserts.isTrue(maxLength > 0, "maxLength is required.");

	if (this.arrayLens == null) {
	    this.arrayLens = new HashMap<>();
	}

	this.arrayLens.put(parameterName, maxLength);
	return this;
    }

    private void translateSql() {
	StringBuilder sb = new StringBuilder(this.pSql);
	Map<String, List<Integer>> indexesMap = new LinkedHashMap<>();
	Map<String, Integer> paramsMap = new LinkedHashMap<>();

	int start = 0;
	int index = 0;

	while (true) {

	    int paramIdx = start;
	    while (paramIdx < sb.length() - 1 && sb.charAt(paramIdx) != getParamPrefix()) {
		paramIdx++;
	    }
	    if (paramIdx >= sb.length() - 1) {
		break;
	    }

	    // Parse parameter
	    Out<Integer> paramEnd = new Out<Integer>();
	    Out<String> paramName = new Out<String>();

	    boolean isParamContext = isParamContext(sb, paramIdx, paramEnd, paramName);
	    if (!isParamContext) {
		start = paramIdx + 1;
		continue;
	    }

	    // Register parameter
	    paramsMap.put(paramName.value, null);

	    // IN or LIKE_ANY?
	    Out<Integer> fieldIdx = new Out<Integer>();
	    Out<String> fieldName = new Out<String>();

	    boolean isInContext = isContext(sb, paramIdx, "IN", fieldIdx, fieldName);
	    boolean isLikeAnyContext = isContext(sb, paramIdx, "LIKE_ANY", fieldIdx, fieldName);

	    boolean isArrayParam = isInContext || isLikeAnyContext;
	    Integer arrayLen = (this.arrayLens != null) ? this.arrayLens.get(paramName.value) : null;

	    if (arrayLen != null) {
		Asserts.isTrue(isArrayParam, () -> STR.fmt("Array parameter '{}' is required.", paramName));

	    } else {
		arrayLen = DEFAULT_ARRAY_MAX_LENGTH;
	    }

	    if (isArrayParam) {
		paramsMap.put(paramName.value, arrayLen);
	    }

	    // Normal parameter?
	    if (!isArrayParam) {
		sb.replace(paramIdx, paramEnd.value + 1, "?");
		putIndex(indexesMap, paramName.value, ++index);

		start = paramIdx + 1;
		continue;
	    }

	    // IN
	    if (isInContext) {
		sb.replace(paramIdx, paramEnd.value + 1, "()");

		for (int subIdx = 0; subIdx < arrayLen; subIdx++) {
		    if (subIdx == 0) {
			sb.insert(paramIdx + 1, '?');
		    } else {
			sb.insert(paramIdx + 1, "?, ");
		    }

		    putIndex(indexesMap, toParamName(paramName.value, subIdx), ++index);
		}

		start = paramIdx + 1;
		continue;
	    }

	    // LIKE_ANY
	    sb.delete(fieldIdx.value, paramEnd.value + 1);

	    for (int subIdx = 0; subIdx < arrayLen; subIdx++) {
		if (subIdx == 0) {
		    sb.insert(fieldIdx.value, String.format("%s LIKE ?", fieldName.value));
		} else {
		    sb.insert(fieldIdx.value, String.format("%s LIKE ? OR ", fieldName.value));
		}

		putIndex(indexesMap, toParamName(paramName.value, subIdx), ++index);
	    }
	    start = fieldIdx.value + 1;
	}

	this.translatedSql = sb.toString();

	this.arrayLens = (this.arrayLens != null) ? Collections.unmodifiableMap(this.arrayLens) : null;
	this.paramsMap = Collections.unmodifiableMap(paramsMap);

	indexesMap.entrySet().forEach(e -> e.setValue(Collections.unmodifiableList(e.getValue())));
	this.indexesMap = Collections.unmodifiableMap(indexesMap);
    }

    private void putIndex(Map<String, List<Integer>> indexesMap, String paramName, int index) {
	indexesMap.compute(paramName, (p, l) -> {
	    if (l == null) {
		l = new ArrayList<Integer>(5);
	    }

	    l.add(index);
	    return l;
	});
    }

    public String getPSql() {
	initialize();
	return this.pSql;
    }

    public String getTranslatedSql() {
	initialize();
	return this.translatedSql;
    }

    public Map<String, Integer> getParamsMap() {
	initialize();
	return this.paramsMap;
    }

    public boolean isParam(String parameterName) {
	initialize();
	return this.paramsMap.containsKey(parameterName);
    }

    public Map<String, List<Integer>> getIndexesMap() {
	initialize();
	return this.indexesMap;
    }

    public List<Integer> getIndexes(String parameterName) {
	initialize();
	List<Integer> indexes = this.indexesMap.get(parameterName);

	return Asserts.notNull(indexes, () -> STR.fmt("Parameter '{}' is not found.", parameterName));
    }

    public boolean isArrayParam(String parameterName) {
	initialize();
	return this.paramsMap.get(parameterName) != null;
    }

    public int getArrayLen(String parameterName) {
	initialize();
	Integer len = this.paramsMap.get(parameterName);

	return Asserts.notNull(len, () -> STR.fmt("Array parameter '{}' is not found.", parameterName));
    }

    public static boolean isContext(StringBuilder sb, int paramIdx, String context, Out<Integer> fieldIdx, Out<String> fieldName) {
	int i = paramIdx - 1;
	while (i >= 0 && Character.isWhitespace(sb.charAt(i))) {
	    i--;
	}
	if (i < 0) {
	    return false;
	}

	int j = i;
	while (j >= 0 && !Character.isWhitespace(sb.charAt(j))) {
	    j--;
	}
	if (!sb.substring(j + 1, i + 1).equalsIgnoreCase(context)) {
	    return false;
	}

	if (j < 0) {
	    return false;
	}
	int k = j;
	while (k >= 0 && Character.isWhitespace(sb.charAt(k))) {
	    k--;
	}
	if (k < 0) {
	    return false;
	}

	int h = k;
	while (h >= 0 && !Character.isWhitespace(sb.charAt(h)) && sb.charAt(h) != '(') {
	    h--;
	}
	fieldName.value = sb.substring(h + 1, k + 1);
	if (fieldName.value.isEmpty()) {
	    return false;
	}

	fieldIdx.value = h + 1;
	return true;
    }

    public static boolean isParamContext(StringBuilder sb, int paramIdx, Out<Integer> paramEnd, Out<String> paramName) {
	int k = paramIdx + 1;

	if (k == sb.length()) {
	    return false;
	}

	if (!Character.isDigit(sb.charAt(k)) && !Character.isJavaIdentifierStart(sb.charAt(k))) {
	    return false;
	}

	k++;
	while (k < sb.length() && Character.isJavaIdentifierPart(sb.charAt(k))) {
	    k++;
	}

	paramName.value = sb.substring(paramIdx + 1, k);
	paramEnd.value = k - 1;
	return true;
    }

    public static String toParamName(String parameterName, int subIdx) {
	return parameterName + "__" + subIdx;
    }

    private static volatile char __paramPrefix;
    private static final Object MUTEX = new Object();

    public static char getParamPrefix() {
	char chr = __paramPrefix;
	if (chr == 0) {
	    synchronized (MUTEX) {
		if ((chr = __paramPrefix) == 0) {
		    __paramPrefix = chr = ':';
		}
	    }
	}
	return chr;
    }

    public static void setParamPrefix(char impl) {
	Asserts.isTrue(__paramPrefix == 0, "JdbcSql.__paramPrefix must be unset.");

	if (__paramPrefix == 0) {
	    synchronized (MUTEX) {
		if (__paramPrefix == 0) {
		    __paramPrefix = impl;
		    return;
		}
	    }
	}
    }
}
