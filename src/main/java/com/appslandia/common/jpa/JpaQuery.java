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

package com.appslandia.common.jpa;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.base.InitializeException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.jdbc.SqlQuery;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class JpaQuery extends InitializeObject implements Serializable {
  private static final long serialVersionUID = 1L;

  private String pQuery;
  private Map<String, Integer> arrayLens;

  private transient Map<String, Integer> paramsMap;
  private transient String translatedQuery;

  /**
   * Constructs an instance of JpaQuery with the specified JPQL query string. This constructor supports JPQL with
   * parameterized queries, where parameters are prefixed with a colon (':'). For array parameters, use the syntax 'IN
   * :parameters' or 'LIKE_ANY :parameters'.
   *
   * @param pQuery The JPQL query string, with parameters prefixed by ':'.
   */
  public JpaQuery(String pQuery) {
    this.pQuery = pQuery;
  }

  @Override
  public JpaQuery initialize() throws InitializeException {
    super.initialize();
    return this;
  }

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.pQuery, "pQuery is required.");
    translateQuery();
  }

  public JpaQuery arrayLen(String parameterName, int maxLength) {
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
    Map<String, Integer> paramsMap = new CaseInsensitiveMap<>();

    var start = 0;
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

      var isParamContext = SqlQuery.isParamContext(sb, paramIdx, paramEnd, paramName);
      if (!isParamContext) {
        start = paramIdx + 1;
        continue;
      }

      // Register parameter
      paramsMap.put(paramName.value, null);

      // IN or LIKE_ANY?
      var fieldIdx = new Out<Integer>();
      var fieldName = new Out<String>();

      var isInContext = SqlQuery.isContext(sb, paramIdx, "IN", fieldIdx, fieldName);
      var isLikeAnyContext = SqlQuery.isContext(sb, paramIdx, "LIKE_ANY", fieldIdx, fieldName);

      var isArrayParam = isInContext || isLikeAnyContext;
      var arrayLen = (this.arrayLens != null) ? this.arrayLens.get(paramName.value) : null;

      if (arrayLen != null) {
        Asserts.isTrue(isArrayParam, "Array parameter '{}' is required.", paramName);
      } else {
        arrayLen = SqlQuery.DEFAULT_ARRAY_MAX_LENGTH;
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

        for (var subIdx = arrayLen - 1; subIdx >= 0; subIdx--) {
          var subParam = SqlQuery.toParamName(paramName.value, subIdx);
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

      for (var subIdx = arrayLen - 1; subIdx >= 0; subIdx--) {
        var subParam = SqlQuery.toParamName(paramName.value, subIdx);
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
    this.translatedQuery = sb.toString();

    this.arrayLens = (this.arrayLens != null) ? Collections.unmodifiableMap(this.arrayLens) : null;
    this.paramsMap = Collections.unmodifiableMap(paramsMap);
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

  public boolean isArrayParam(String parameterName) {
    initialize();
    return this.paramsMap.get(parameterName) != null;
  }

  public int getArrayLen(String parameterName) {
    initialize();
    var len = this.paramsMap.get(parameterName);
    if (len == null) {
      throw new IllegalArgumentException(STR.fmt("Array parameter '{}' is not found.", parameterName));
    }
    return len;
  }
}
