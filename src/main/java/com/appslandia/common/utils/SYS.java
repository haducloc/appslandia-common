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

package com.appslandia.common.utils;

import java.util.regex.Pattern;

import com.appslandia.common.base.BoolFormatException;

/**
 *
 * @author Loc Ha
 *
 */
public class SYS {

  // System.getProperty

  public static String getProp(String key) {
    var value = System.getProperty(key);
    return (value != null) ? value.strip() : null;
  }

  public static String getProp(String key, String ifNull) {
    var value = getProp(key);
    return (value != null) ? value : ifNull;
  }

  public static String getPropReq(String key) {
    var value = getProp(key);
    if (value == null) {
      throw new IllegalStateException(STR.fmt("No value found for the given property '{}'.", key));
    }
    return value;
  }

  public static boolean getBoolProp(String key) throws BoolFormatException {
    var value = getPropReq(key);
    return ParseUtils.parseBool(value);
  }

  public static int getIntProp(String key) throws NumberFormatException {
    var value = getPropReq(key);
    return ParseUtils.parseInt(value);
  }

  public static long getLongProp(String key) throws NumberFormatException {
    var value = getPropReq(key);
    return ParseUtils.parseLong(value);
  }

  public static double getDoubleProp(String key) throws NumberFormatException {
    var value = getPropReq(key);
    return ParseUtils.parseDouble(value);
  }

  public static boolean getBoolProp(String key, boolean ifNullOrInvalid) {
    var value = getProp(key);
    return (value != null) ? ParseUtils.parseBool(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static int getIntProp(String key, int ifNullOrInvalid) {
    var value = getProp(key);
    return (value != null) ? ParseUtils.parseInt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static long getLongProp(String key, long ifNullOrInvalid) {
    var value = getProp(key);
    return (value != null) ? ParseUtils.parseLong(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static double getDoubleProp(String key, double ifNullOrInvalid) {
    var value = getProp(key);
    return (value != null) ? ParseUtils.parseDouble(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  // System.getenv

  public static String getEnv(String key) {
    var value = System.getenv(key);
    return (value != null) ? value.strip() : null;
  }

  public static String getEnv(String key, String ifNull) {
    var value = getEnv(key);
    return value != null ? value : ifNull;
  }

  public static String getEnvReq(String key) {
    var value = getEnv(key);
    if (value == null) {
      throw new IllegalStateException(STR.fmt("No value found for the given env '{}'.", key));
    }
    return value;
  }

  public static boolean getBoolEnv(String key) throws BoolFormatException {
    var value = getEnvReq(key);
    return ParseUtils.parseBool(value);
  }

  public static int getIntEnv(String key) throws NumberFormatException {
    var value = getEnvReq(key);
    return ParseUtils.parseInt(value);
  }

  public static long getLongEnv(String key) throws NumberFormatException {
    var value = getEnvReq(key);
    return ParseUtils.parseLong(value);
  }

  public static double getDoubleEnv(String key) throws NumberFormatException {
    var value = getEnvReq(key);
    return ParseUtils.parseDouble(value);
  }

  public static boolean getBoolEnv(String key, boolean ifNullOrInvalid) {
    var value = getEnv(key);
    return (value != null) ? ParseUtils.parseBool(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static int getIntEnv(String key, int ifNullOrInvalid) {
    var value = getEnv(key);
    return (value != null) ? ParseUtils.parseInt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static long getLongEnv(String key, long ifNullOrInvalid) {
    var value = getEnv(key);
    return (value != null) ? ParseUtils.parseLong(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static double getDoubleEnv(String key, double ifNullOrInvalid) {
    var value = getEnv(key);
    return (value != null) ? ParseUtils.parseDouble(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  // {var1}
  // {var1, var2, etc.}

  public static boolean resolveBool(String expr, boolean defaultValue) {
    var val = resolve(expr);
    if (val == null) {
      return defaultValue;
    }
    return ParseUtils.parseBool(val, defaultValue);
  }

  public static int resolveInt(String expr, int defaultValue) {
    var val = resolve(expr);
    if (val == null) {
      return defaultValue;
    }
    return ParseUtils.parseInt(val, defaultValue);
  }

  public static long resolveLong(String expr, long defaultValue) {
    var val = resolve(expr);
    if (val == null) {
      return defaultValue;
    }
    return ParseUtils.parseLong(val, defaultValue);
  }

  public static double resolveDouble(String expr, double defaultValue) {
    var val = resolve(expr);
    if (val == null) {
      return defaultValue;
    }
    return ParseUtils.parseDouble(val, defaultValue);
  }

  private static final Pattern VARS_PATTERN = Pattern.compile("\\{\\s*[a-z\\d_.]+\\s*(\\s*\\,\\s*[a-z\\d_.]+\\s*)*}",
      Pattern.CASE_INSENSITIVE);

  public static String resolve(String expr, String defaultValue) {
    var resolvedVal = resolve(expr);
    return (resolvedVal != null) ? resolvedVal : defaultValue;
  }

  public static String resolve(String expr) {
    Arguments.notNull(expr);

    if (!VARS_PATTERN.matcher(expr).matches()) {
      throw new IllegalArgumentException("Invalid expression: " + expr);
    }

    var vars = expr.substring(1, expr.length() - 1).strip();
    var varArr = SplitUtils.splitByComma(vars);

    for (String var : varArr) {
      var resolvedValue = StringUtils.startsWith(var, "env.") ? getEnv(var.substring(4)) : getProp(var);

      if (resolvedValue != null) {
        return resolvedValue;
      }
    }
    return null;
  }
}
