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

import com.appslandia.common.utils.ParseUtils;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.SYS;
import com.appslandia.common.utils.SplitUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public interface Config {

  String getString(String key);

  default Iterator<String> getKeyIterator() {
    throw new UnsupportedOperationException();
  }

  default public String getString(String key, String defaultValue) {
    String value = getString(key);
    return (value != null) ? value : defaultValue;
  }

  default public String getStringReq(String key) {
    String value = getString(key);
    if (value == null) {
      throw new AssertException(STR.fmt("No value found for the given key '{}'.", key));
    }
    return value;
  }

  default public String[] getStringArray(String key) {
    String value = getString(key);
    return (value != null) ? SplitUtils.splitByComma(value) : StringUtils.EMPTY_ARRAY;
  }

  default public String resolve(String key) {
    String value = getString(key);
    if (value == null) {
      return null;
    }
    return STR.format(value, (pname, expr) -> {
      // CONFIG
      String resolvedValue = getString(pname);

      // SYS
      if (resolvedValue == null) {
        resolvedValue = SYS.resolve(expr);
      }
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
      Object resolvedValue = parameters.get(pname);

      // CONFIG
      if (resolvedValue == null) {
        resolvedValue = getString(pname);
      }

      // SYS
      if (resolvedValue == null) {
        resolvedValue = SYS.resolve(expr);
      }
      return (resolvedValue != null) ? resolvedValue : STR.MISSED_VALUE;
    });
  }

  default public String resolve(String key, Object... parameters) {
    String value = getString(key);
    if (value == null) {
      return null;
    }
    return STR.format(value, (pname, expr) -> {

      Object resolvedValue = null;
      try {
        int index = Integer.parseInt(pname);

        // Parameters
        if ((0 <= index) && (index < parameters.length)) {
          resolvedValue = parameters[index];
        }
      } catch (NumberFormatException ex) {
      }

      // SYS
      if (resolvedValue == null) {
        resolvedValue = SYS.resolve(expr);
      }
      return (resolvedValue != null) ? resolvedValue : STR.MISSED_VALUE;
    });
  }

  default public boolean getBool(String key, boolean defaultValIfInvalid) {
    String value = getString(key);
    return (value != null) ? ParseUtils.parseBool(value, defaultValIfInvalid) : defaultValIfInvalid;
  }

  default public boolean getBool(String key) throws BoolFormatException {
    String value = getStringReq(key);
    return ParseUtils.parseBool(value);
  }

  default public Boolean getBoolOpt(String key) throws BoolFormatException {
    String value = getString(key);
    return (value != null) ? ParseUtils.parseBool(value) : null;
  }

  default public int getInt(String key, int defaultValIfInvalid) {
    String value = getString(key);
    return (value != null) ? ParseUtils.parseInt(value, defaultValIfInvalid) : defaultValIfInvalid;
  }

  default public int getInt(String key) throws NumberFormatException {
    String value = getStringReq(key);
    return Integer.parseInt(value);
  }

  default public Integer getIntOpt(String key) throws NumberFormatException {
    String value = getString(key);
    return (value != null) ? Integer.parseInt(value) : null;
  }

  default public long getLong(String key, long defaultValIfInvalid) {
    String value = getString(key);
    return (value != null) ? ParseUtils.parseLong(value, defaultValIfInvalid) : defaultValIfInvalid;
  }

  default public long getLong(String key) throws NumberFormatException {
    String value = getStringReq(key);
    return Long.parseLong(value);
  }

  default public Long getLongOpt(String key) throws NumberFormatException {
    String value = getString(key);
    return (value != null) ? Long.parseLong(value) : null;
  }

  default public double getDouble(String key, double defaultValIfInvalid) {
    String value = getString(key);
    return (value != null) ? ParseUtils.parseDouble(value, defaultValIfInvalid) : defaultValIfInvalid;
  }

  default public double getDouble(String key) throws NumberFormatException {
    String value = getStringReq(key);
    return Double.parseDouble(value);
  }

  default public Double getDoubleOpt(String key) throws NumberFormatException {
    String value = getString(key);
    return (value != null) ? Double.parseDouble(value) : null;
  }

  default public BigDecimal getDecimal(String key, double defaultValIfInvalid) {
    String value = getString(key);
    return ParseUtils.parseDecimal(value, defaultValIfInvalid);
  }

  default public BigDecimal getDecimalReq(String key) throws NumberFormatException {
    String value = getStringReq(key);
    return new BigDecimal(value);
  }

  default public BigDecimal getDecimal(String key) throws NumberFormatException {
    String value = getString(key);
    return (value != null) ? new BigDecimal(value) : null;
  }
}
