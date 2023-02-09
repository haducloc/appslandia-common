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

package com.appslandia.common.jpa;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.base.InitializeException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.jdbc.JdbcSql;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JpaSql extends InitializeObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int DEFAULT_ARRAY_MAX_LENGTH = 32;

    private String pSql;

    private Map<String, Integer> arrayLens;
    private transient Map<String, Integer> paramsMap;

    private transient String translatedSql;

    public JpaSql(String pSql) {
	this.pSql = pSql;
    }

    @Override
    public JpaSql initialize() throws InitializeException {
	super.initialize();
	return this;
    }

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.pSql, "pSql is required.");
	translateSql();
    }

    public JpaSql arrayLen(String parameterName, int maxLength) {
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
	Map<String, Integer> paramsMap = new LinkedHashMap<>();

	int start = 0;

	while (true) {

	    int paramIdx = start;
	    while (paramIdx < sb.length() - 1 && sb.charAt(paramIdx) != ':') {
		paramIdx++;
	    }
	    if (paramIdx >= sb.length() - 1) {
		break;
	    }

	    // Parse parameter
	    Out<Integer> paramEnd = new Out<Integer>();
	    Out<String> paramName = new Out<String>();

	    boolean isParamContext = JdbcSql.isParamContext(sb, paramIdx, paramEnd, paramName);
	    if (!isParamContext) {
		start = paramIdx + 1;
		continue;
	    }

	    // Register parameter
	    paramsMap.put(paramName.value, null);

	    // IN or LIKE_ANY?
	    Out<Integer> fieldIdx = new Out<Integer>();
	    Out<String> fieldName = new Out<String>();

	    boolean isInContext = JdbcSql.isContext(sb, paramIdx, "IN", fieldIdx, fieldName);
	    boolean isLikeAnyContext = JdbcSql.isContext(sb, paramIdx, "LIKE_ANY", fieldIdx, fieldName);

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

		start = paramIdx + paramName.value.length();
		continue;
	    }

	    // IN
	    if (isInContext) {
		sb.replace(paramIdx, paramEnd.value + 1, "()");

		for (int subIdx = arrayLen - 1; subIdx >= 0; subIdx--) {
		    String subParam = JdbcSql.toParamName(paramName.value, subIdx);
		    String expr = null;

		    if (subIdx == arrayLen - 1) {
			expr = String.format(":%s", subParam);
		    } else {
			expr = String.format(":%s, ", subParam);
		    }

		    sb.insert(paramIdx + 1, expr);
		    start += expr.length();
		}
		continue;
	    }

	    // LIKE_ANY
	    sb.delete(fieldIdx.value, paramEnd.value + 1);

	    for (int subIdx = arrayLen - 1; subIdx >= 0; subIdx--) {
		String subParam = JdbcSql.toParamName(paramName.value, subIdx);
		String expr = null;

		if (subIdx == arrayLen - 1) {
		    expr = String.format("%s LIKE :%s", fieldName.value, subParam);
		} else {
		    expr = String.format("%s LIKE :%s OR ", fieldName.value, subParam);
		}

		sb.insert(fieldIdx.value, expr);
		start += expr.length();
	    }
	}

	this.translatedSql = sb.toString();

	this.arrayLens = (this.arrayLens != null) ? Collections.unmodifiableMap(this.arrayLens) : null;
	this.paramsMap = Collections.unmodifiableMap(paramsMap);
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

    public boolean isArrayParam(String parameterName) {
	initialize();
	return this.paramsMap.get(parameterName) != null;
    }

    public int getArrayLen(String parameterName) {
	initialize();
	Integer len = this.paramsMap.get(parameterName);

	return Asserts.notNull(len, () -> STR.fmt("Array parameter '{}' is not found.", parameterName));
    }
}
