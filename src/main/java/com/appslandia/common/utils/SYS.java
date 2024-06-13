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

package com.appslandia.common.utils;

import java.util.regex.Pattern;

import com.appslandia.common.base.AssertException;
import com.appslandia.common.base.BoolFormatException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SYS {

  // System.getProperty

  public static String getProp(String key) {
    String value = System.getProperty(key);
    return (value != null) ? value.trim() : null;
  }

  public static String getProp(String key, String ifNull) {
    String value = getProp(key);
    return (value != null) ? value : ifNull;
  }

  public static String getPropReq(String key) {
    String value = getProp(key);
    if (value == null) {
      throw new AssertException(STR.fmt("No value found for the given property '{}'.", key));
    }
    return value;
  }

  public static boolean getBoolProp(String key) throws BoolFormatException {
    String value = getPropReq(key);
    return ParseUtils.parseBool(value);
  }

  public static int getIntProp(String key) throws NumberFormatException {
    String value = getPropReq(key);
    return ParseUtils.parseInt(value);
  }

  public static long getLongProp(String key) throws NumberFormatException {
    String value = getPropReq(key);
    return ParseUtils.parseLong(value);
  }

  public static double getDoubleProp(String key) throws NumberFormatException {
    String value = getPropReq(key);
    return ParseUtils.parseDouble(value);
  }

  public static boolean getBoolProp(String key, boolean ifNullOrInvalid) {
    String value = getProp(key);
    return (value != null) ? ParseUtils.parseBool(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static int getIntProp(String key, int ifNullOrInvalid) {
    String value = getProp(key);
    return (value != null) ? ParseUtils.parseInt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static long getLongProp(String key, long ifNullOrInvalid) {
    String value = getProp(key);
    return (value != null) ? ParseUtils.parseLong(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static double getDoubleProp(String key, double ifNullOrInvalid) {
    String value = getProp(key);
    return (value != null) ? ParseUtils.parseDouble(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  // System.getenv

  public static String getEnv(String key) {
    String value = System.getenv(key);
    return (value != null) ? value.trim() : null;
  }

  public static String getEnv(String key, String ifNull) {
    String value = getEnv(key);
    return value != null ? value : ifNull;
  }

  public static String getEnvReq(String key) {
    String value = getEnv(key);
    if (value == null) {
      throw new AssertException(STR.fmt("No value found for the given env '{}'.", key));
    }
    return value;
  }

  public static boolean getBoolEnv(String key) throws BoolFormatException {
    String value = getEnvReq(key);
    return ParseUtils.parseBool(value);
  }

  public static int getIntEnv(String key) throws NumberFormatException {
    String value = getEnvReq(key);
    return ParseUtils.parseInt(value);
  }

  public static long getLongEnv(String key) throws NumberFormatException {
    String value = getEnvReq(key);
    return ParseUtils.parseLong(value);
  }

  public static double getDoubleEnv(String key) throws NumberFormatException {
    String value = getEnvReq(key);
    return ParseUtils.parseDouble(value);
  }

  public static boolean getBoolEnv(String key, boolean ifNullOrInvalid) {
    String value = getEnv(key);
    return (value != null) ? ParseUtils.parseBool(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static int getIntEnv(String key, int ifNullOrInvalid) {
    String value = getEnv(key);
    return (value != null) ? ParseUtils.parseInt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static long getLongEnv(String key, long ifNullOrInvalid) {
    String value = getEnv(key);
    return (value != null) ? ParseUtils.parseLong(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public static double getDoubleEnv(String key, double ifNullOrInvalid) {
    String value = getEnv(key);
    return (value != null) ? ParseUtils.parseDouble(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  // {var1}
  // {var1, var2, etc.}

  private static final Pattern VARS_PATTERN = Pattern.compile("\\{\\s*[a-z\\d_.]+\\s*(\\s*\\,\\s*[a-z\\d_.]+\\s*)*}",
      Pattern.CASE_INSENSITIVE);

  public static String resolve(String expr) {
    Asserts.notNull(expr);

    if (!VARS_PATTERN.matcher(expr).matches()) {
      return null;
    }

    String vars = expr.substring(1, expr.length() - 1).trim();
    String[] varArr = SplitUtils.splitByComma(vars);

    for (String var : varArr) {
      String resolvedValue = StringUtils.startsWith(var, "env.") ? getEnv(var.substring(4)) : getProp(var);

      if (resolvedValue != null) {
        return resolvedValue;
      }
    }
    return null;
  }
}
