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

package com.appslandia.common.base;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import com.appslandia.common.utils.ParseUtils;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.SYS;
import com.appslandia.common.utils.SplitUtils;
import com.appslandia.common.utils.SplittingBehavior;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public interface Config {

  Iterator<String> getKeys();

  String getString(String key);

  default public String getString(String key, String ifNull) {
    String value = getString(key);
    return (value != null) ? value : ifNull;
  }

  default public String getStringReq(String key) {
    String value = getString(key);
    if (value == null) {
      throw new AssertException(STR.fmt("No value found for the given key '{}'.", key));
    }
    return value;
  }

  default public String[] getStringArray(String key) {
    return getStringArray(key, SplittingBehavior.SKIP_NULL);
  }

  default public String[] getStringArray(String key, SplittingBehavior behavior) {
    String value = getString(key);
    return (value != null) ? SplitUtils.splitByComma(value, behavior) : StringUtils.EMPTY_ARRAY;
  }

  default public boolean getBool(String key) throws BoolFormatException {
    String value = getStringReq(key);
    return ParseUtils.parseBool(value);
  }

  default public int getInt(String key) throws NumberFormatException {
    String value = getStringReq(key);
    return ParseUtils.parseInt(value);
  }

  default public long getLong(String key) throws NumberFormatException {
    String value = getStringReq(key);
    return ParseUtils.parseLong(value);
  }

  default public double getDouble(String key) throws NumberFormatException {
    String value = getStringReq(key);
    return ParseUtils.parseDouble(value);
  }

  default public BigDecimal getDecimalReq(String key) throws NumberFormatException {
    String value = getStringReq(key);
    return ParseUtils.parseDecimalReq(value);
  }

  default public boolean getBool(String key, boolean ifNullOrInvalid) {
    String value = getString(key);
    return (value != null) ? ParseUtils.parseBool(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  default public int getInt(String key, int ifNullOrInvalid) {
    String value = getString(key);
    return (value != null) ? ParseUtils.parseInt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  default public long getLong(String key, long ifNullOrInvalid) {
    String value = getString(key);
    return (value != null) ? ParseUtils.parseLong(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  default public double getDouble(String key, double ifNullOrInvalid) {
    String value = getString(key);
    return (value != null) ? ParseUtils.parseDouble(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  default public BigDecimal getDecimal(String key, double ifNullOrInvalid) {
    String value = getString(key);
    return ParseUtils.parseDecimal(value, ifNullOrInvalid);
  }

  default public <T> T getValue(String key, Function<String, T> converter) {
    String value = getString(key);
    return (value != null) ? ParseUtils.parseValue(value, converter) : null;
  }

  default public String resolve(String key) {
    String value = getString(key);
    if (value == null) {
      return null;
    }
    return STR.format(value, (pname, expr) -> {

      // Config
      String resolvedValue = getString(pname);
      if (resolvedValue != null) {
        return resolvedValue;
      }

      // SYS
      resolvedValue = SYS.resolve(expr);
      return (resolvedValue != null) ? resolvedValue : STR.MISSED_VALUE;
    });
  }

  default public String resolve(String key, Map<String, Object> parameters) {
    String value = getString(key);
    if (value == null) {
      return null;
    }
    return STR.format(value, (pname, expr) -> {

      // Parameters
      if (parameters.containsKey(pname)) {
        return parameters.get(pname);
      }

      // Config
      String resolvedValue = getString(pname);
      if (resolvedValue != null) {
        return resolvedValue;
      }

      // SYS
      resolvedValue = SYS.resolve(expr);
      return (resolvedValue != null) ? resolvedValue : STR.MISSED_VALUE;
    });
  }

  default public String resolve(String key, Object... parameters) {
    String value = getString(key);
    return (value != null) ? STR.format(value, parameters) : null;
  }
}
