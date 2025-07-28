// The MIT License (MIT)
// Copyright © 2015 Loc Ha

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
import java.util.List;
import java.util.Map;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.base.InitializeException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author Loc Ha
 *
 */
public class SqlQuery extends InitializeObject implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final int DEFAULT_ARRAY_MAX_LENGTH = 32;

  private String pQuery;
  private Map<String, Integer> arrayLens;
  private transient Map<String, Integer> paramsMap;

  private transient String translatedQuery;
  private transient Map<String, List<Integer>> indexesMap;

  /**
   * Constructs an instance of SqlQuery with a given SQL query string. This constructor supports native SQL with
   * parameterized queries, where parameters are prefixed with a colon (':'). For array parameters, use the syntax 'IN
   * :parameters' or 'LIKE_ANY :parameters'.
   *
   * @param pQuery The SQL query string with parameters prefixed by ':'.
   */
  public SqlQuery(String pQuery) {
    this.pQuery = pQuery;
  }

  @Override
  public SqlQuery initialize() throws InitializeException {
    super.initialize();
    return this;
  }

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.pQuery, "pQuery is required.");
    translateQuery();
  }

  public SqlQuery arrayLen(String parameterName, int maxLength) {
    assertNotInitialized();
    Arguments.isTrue(maxLength > 0, "maxLength is required.");

    if (this.arrayLens == null) {
      this.arrayLens = new CaseInsensitiveMap<>();
    }
    this.arrayLens.put(parameterName, maxLength);
    return this;
  }

  private void translateQuery() {
    var sb = new StringBuilder(this.pQuery);
    Map<String, List<Integer>> indexesMap = new CaseInsensitiveMap<>();
    Map<String, Integer> paramsMap = new CaseInsensitiveMap<>();

    var start = 0;
    var index = 0;

    while (true) {

      var paramIdx = start;
      while (paramIdx < sb.length() - 1 && sb.charAt(paramIdx) != ':') {
        paramIdx++;
      }
      if (paramIdx >= sb.length() - 1) {
        break;
      }

      // Parse parameter
      var paramEnd = new Out<Integer>();
      var paramName = new Out<String>();

      var isParamContext = isParamContext(sb, paramIdx, paramEnd, paramName);
      if (!isParamContext) {
        start = paramIdx + 1;
        continue;
      }

      // Register parameter
      paramsMap.put(paramName.value, null);

      // IN or LIKE_ANY?
      var fieldIdx = new Out<Integer>();
      var fieldName = new Out<String>();

      var isInContext = isContext(sb, paramIdx, "IN", fieldIdx, fieldName);
      var isLikeAnyContext = isContext(sb, paramIdx, "LIKE_ANY", fieldIdx, fieldName);

      var isArrayParam = isInContext || isLikeAnyContext;
      var arrayLen = (this.arrayLens != null) ? this.arrayLens.get(paramName.value) : null;

      if (arrayLen != null) {
        Asserts.isTrue(isArrayParam, "Array parameter '{}' is required.", paramName);
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

        for (var subIdx = 0; subIdx < arrayLen; subIdx++) {
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

      for (var subIdx = 0; subIdx < arrayLen; subIdx++) {
        if (subIdx == 0) {
          sb.insert(fieldIdx.value, String.format("%s LIKE ?", fieldName.value));
        } else {
          sb.insert(fieldIdx.value, String.format("%s LIKE ? OR ", fieldName.value));
        }
        putIndex(indexesMap, toParamName(paramName.value, subIdx), ++index);
      }
      start = fieldIdx.value + 1;
    }
    this.translatedQuery = sb.toString();

    this.arrayLens = (this.arrayLens != null) ? Collections.unmodifiableMap(this.arrayLens) : null;
    this.paramsMap = Collections.unmodifiableMap(paramsMap);

    indexesMap.entrySet().forEach(e -> e.setValue(Collections.unmodifiableList(e.getValue())));
    this.indexesMap = Collections.unmodifiableMap(indexesMap);
  }

  private void putIndex(Map<String, List<Integer>> indexesMap, String paramName, int index) {
    indexesMap.compute(paramName, (p, l) -> {
      if (l == null) {
        l = new ArrayList<>(5);
      }
      l.add(index);
      return l;
    });
  }

  public String getPQuery() {
    initialize();
    return this.pQuery;
  }

  public String getTranslatedQuery() {
    initialize();
    return this.translatedQuery;
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
    var indexes = this.indexesMap.get(parameterName);
    return Arguments.notNull(indexes, "Parameter '{}' is not found.", parameterName);
  }

  public boolean isArrayParam(String parameterName) {
    initialize();
    return this.paramsMap.get(parameterName) != null;
  }

  public int getArrayLen(String parameterName) {
    initialize();
    var len = this.paramsMap.get(parameterName);
    return Arguments.notNull(len, "Array parameter '{}' is not found.", parameterName);
  }

  public static boolean isContext(StringBuilder sb, int paramIdx, String context, Out<Integer> fieldIdx,
      Out<String> fieldName) {
    var i = paramIdx - 1;
    while (i >= 0 && Character.isWhitespace(sb.charAt(i))) {
      i--;
    }
    if (i < 0) {
      return false;
    }
    var j = i;
    while (j >= 0 && !Character.isWhitespace(sb.charAt(j))) {
      j--;
    }
    if (!sb.substring(j + 1, i + 1).equalsIgnoreCase(context) || (j < 0)) {
      return false;
    }
    var k = j;
    while (k >= 0 && Character.isWhitespace(sb.charAt(k))) {
      k--;
    }
    if (k < 0) {
      return false;
    }
    var h = k;
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
    var k = paramIdx + 1;

    if ((k == sb.length()) || (!Character.isDigit(sb.charAt(k)) && !Character.isJavaIdentifierStart(sb.charAt(k)))) {
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
}
