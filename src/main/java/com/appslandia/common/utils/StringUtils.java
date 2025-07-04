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

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 *
 * @author Loc Ha
 *
 */
public class StringUtils {

  public static final String EMPTY_STRING = "";
  public static final String NULL_STRING = "null";

  public static final String[] EMPTY_ARRAY = {};

  public static String toLowerCase(String str) {
    return toLowerCase(str, Locale.ROOT);
  }

  public static String toLowerCase(String str, Locale locale) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    return str.toLowerCase(locale);
  }

  public static String toUpperCase(String str) {
    return toUpperCase(str, Locale.ROOT);
  }

  public static String toUpperCase(String str, Locale locale) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    return str.toUpperCase(locale);
  }

  public static String firstLowerCase(String str) {
    return firstLowerCase(str, Locale.ROOT);
  }

  public static String firstLowerCase(String str, Locale locale) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    var sb = new StringBuilder(str.length());
    return sb.append(str.substring(0, 1).toLowerCase(locale)).append(str.substring(1)).toString();
  }

  public static String firstUpperCase(String str) {
    return firstUpperCase(str, Locale.ROOT);
  }

  public static String firstUpperCase(String str, Locale locale) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    var sb = new StringBuilder(str.length());
    return sb.append(str.substring(0, 1).toUpperCase(locale)).append(str.substring(1)).toString();
  }

  public static String trimToNull(String str) {
    return trimToDefault(str, null);
  }

  public static String trimToEmpty(String str) {
    return (str != null) ? str.strip() : EMPTY_STRING;
  }

  public static String trimToDefault(String str, String defaultValue) {
    if (str == null || str.isEmpty()) {
      return defaultValue;
    }
    str = str.strip();
    return !str.isEmpty() ? str : defaultValue;
  }

  public static String trimChar(String str, char charToTrim) {
    if (str == null) {
      return null;
    }
    var start = -1;
    while ((++start < str.length()) && (str.charAt(start) == charToTrim)) {
    }
    var end = str.length();
    while ((--end >= 0) && (str.charAt(end) == charToTrim)) {
    }
    if (start > end) {
      return null;
    }
    return str.substring(start, end + 1);
  }

  private static final Pattern LETTER_LOWER_UPPER_PATTERN = Pattern.compile("([a-z])([A-Z])");
  private static final Pattern LETTER_UPPER_UPPERLOWER_PATTERN = Pattern.compile("([A-Z]+)([A-Z][a-z])");

  public static String toSnakeCase(String objName) {
    if (objName == null) {
      return null;
    }
    var matcher = LETTER_UPPER_UPPERLOWER_PATTERN.matcher(objName);
    var result = matcher.replaceAll("$1_$2");

    matcher = LETTER_LOWER_UPPER_PATTERN.matcher(result);
    result = matcher.replaceAll("$1_$2");

    return result.toLowerCase(Locale.ENGLISH);
  }

  public static boolean isNullOrEmpty(String str) {
    return (str == null) || str.isEmpty();
  }

  public static boolean iequals(String str1, String str2) {
    return (str1 == null) ? (str2 == null) : str1.equalsIgnoreCase(str2);
  }

  private static final Pattern WTSP_PATTERN = Pattern.compile("\\s+");

  public static boolean isNullOrBlank(String str) {
    return (str == null) || str.isEmpty() || WTSP_PATTERN.matcher(str).matches();
  }

  public static boolean startsWith(String str, String substrIgnoreCase) {
    Arguments.notNull(str);
    Arguments.notNull(substrIgnoreCase);

    if (substrIgnoreCase.isEmpty() || str.regionMatches(true, 0, substrIgnoreCase, 0, substrIgnoreCase.length())) {
      return true;
    }
    return false;
  }

  public static boolean endsWith(String str, String substrIgnoreCase) {
    Arguments.notNull(str);
    Arguments.notNull(substrIgnoreCase);

    if (substrIgnoreCase.isEmpty() || str.regionMatches(true, str.length() - substrIgnoreCase.length(),
        substrIgnoreCase, 0, substrIgnoreCase.length())) {
      return true;
    }
    return false;
  }

  public static boolean contains(String str, String substrIgnoreCase) {
    Arguments.notNull(str);
    Arguments.notNull(substrIgnoreCase);

    if (substrIgnoreCase.isEmpty()) {
      return true;
    }
    var maxOffset = str.length() - substrIgnoreCase.length();
    for (var offset = 0; offset <= maxOffset; offset++) {

      if (str.regionMatches(true, offset, substrIgnoreCase, 0, substrIgnoreCase.length())) {
        return true;
      }
    }
    return false;
  }

  public static String join(char delimiter, boolean willWrap, Iterable<String> elements) {
    Objects.requireNonNull(elements);

    if (elements instanceof Collection) {
      Collection<String> col = ObjectUtils.cast(elements);
      if (col.isEmpty()) {
        return null;
      }
    }
    var sep = new String(new char[] { delimiter });
    var joiner = willWrap ? new StringJoiner(sep, sep, sep) : new StringJoiner(sep);

    for (String cs : elements) {
      joiner.add(cs);
    }
    return joiner.toString();
  }

  public static String join(char delimiter, boolean willWrap, String... elements) {
    Objects.requireNonNull(elements);

    if (elements.length == 0) {
      return null;
    }
    return join(delimiter, willWrap, new ArrayUtils.ArrayIterable<>(elements));
  }
}
