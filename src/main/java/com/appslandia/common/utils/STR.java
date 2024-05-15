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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class STR {

  public static final Object MISSED_VALUE = new Object() {
  };

  // ${paramName} or ${paramName?}

  private static final Pattern PARAM_HOLDER_PATTERN = Pattern
      .compile("\\$\\{\\s*(?!.*\\.\\.)(?!.*\\.$)[a-z\\d][a-z\\d_.]*(\\?)?\\s*}", Pattern.CASE_INSENSITIVE);

  public static String format(String str, Map<String, Object> parameters) {
    if (str == null) {
      return null;
    }
    return format(str, (pname, expr) -> {
      return parameters.containsKey(pname) ? parameters.get(pname) : MISSED_VALUE;
    });
  }

  public static String format(String str, Object... parameters) {
    if (str == null) {
      return null;
    }
    return format(str, (pname, expr) -> {

      int index = -1;
      try {
        index = Integer.parseInt(pname);
      } catch (NumberFormatException ex) {
      }
      return ((0 <= index) && (index < parameters.length)) ? parameters[index] : MISSED_VALUE;
    });
  }

  public static String format(String str, BiFunction<String, String, Object> parameters) {
    if (str == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder((int) (1.5 * str.length()));

    format(str, parameters, sb);
    return sb.toString();
  }

  public static void format(String str, BiFunction<String, String, Object> parameters, StringBuilder out) {
    Asserts.notNull(str);

    // ${paramName}
    Matcher matcher = PARAM_HOLDER_PATTERN.matcher(str);

    int prevEnd = 0;
    while (matcher.find()) {

      // Non parameter
      out.append(str.substring(prevEnd, matcher.start()));

      // ${paramName}
      String paramGroup = matcher.group();
      String paramName = paramGroup.substring(paramGroup.indexOf('{') + 1, paramGroup.length() - 1).trim();

      boolean optional = paramName.charAt(paramName.length() - 1) == '?';
      if (optional) {
        paramName = paramName.substring(0, paramName.length() - 1);
      }

      String expr = "${" + paramName + "}";
      Object parameterValue = parameters.apply(paramName, expr);

      if (parameterValue == MISSED_VALUE) {
        throw new IllegalArgumentException(STR.fmt("The parameter {} must be provided.", expr));
      }
      if (parameterValue == null && !optional) {
        throw new IllegalArgumentException(STR.fmt("The parameter {} must be required.", expr));
      }

      String valueAsStr = formatParam(parameterValue);
      out.append(valueAsStr);

      prevEnd = matcher.end();
    }
    if (prevEnd < str.length()) {
      out.append(str.substring(prevEnd));
    }
  }

  // "" or ?

  private static final Pattern SEQ_HOLDER_PATTERN = Pattern.compile("\\{\\s*(\\?)?\\s*}");

  public static String fmt(String str, Object... entries) {
    if (str == null) {
      return null;
    }
    StringBuilder out = new StringBuilder(str.length() + entries.length * 16);
    Matcher matcher = SEQ_HOLDER_PATTERN.matcher(str);

    int index = -1;
    int prevEnd = 0;
    while (matcher.find()) {

      // Non entry
      out.append(str.substring(prevEnd, matcher.start()));

      // {}
      String paramGroup = matcher.group();
      String parameter = paramGroup.substring(paramGroup.indexOf('{') + 1, paramGroup.length() - 1).trim();

      boolean optional = "?".equals(parameter);

      index++;
      Object entryValue = ((0 <= index) && (index < entries.length)) ? entries[index] : MISSED_VALUE;

      if (entryValue == MISSED_VALUE) {
        throw new IllegalArgumentException("The entry {} must be provided.");
      }
      if (entryValue == null && !optional) {
        throw new IllegalArgumentException("The entry {} must be required.");
      }

      String valueAsStr = formatParam(entryValue);
      out.append(valueAsStr);

      prevEnd = matcher.end();
    }
    if (prevEnd < str.length()) {
      out.append(str.substring(prevEnd));
    }
    return out.toString();
  }

  public static StringFormat compile(String str) {
    Asserts.notNull(str);

    int outLen = 0;
    List<StringFormat.Chunk> chunks = new ArrayList<>();

    // ${paramName}
    Matcher matcher = PARAM_HOLDER_PATTERN.matcher(str);

    int prevEnd = 0;
    while (matcher.find()) {

      // Non parameter
      String chunk = str.substring(prevEnd, matcher.start());
      if (!chunk.isEmpty()) {
        chunks.add(new StringFormat.Chunk(chunk, false, false, null));
        outLen += chunk.length();
      }

      // ${paramName}
      String paramGroup = matcher.group();
      String paramName = paramGroup.substring(paramGroup.indexOf('{') + 1, paramGroup.length() - 1).trim();

      boolean optional = paramName.charAt(paramName.length() - 1) == '?';
      if (optional) {
        paramName = paramName.substring(0, paramName.length() - 1);
      }

      chunks.add(new StringFormat.Chunk(paramName, true, optional, "${" + paramName + "}"));
      outLen += 16;
      prevEnd = matcher.end();
    }

    if (prevEnd < str.length()) {
      String chunk = str.substring(prevEnd);
      if (!chunk.isEmpty()) {
        chunks.add(new StringFormat.Chunk(chunk, false, false, null));
        outLen += chunk.length();
      }
    }
    return new StringFormat(outLen, chunks);
  }

  static String formatParam(Object paramValue) {
    if (paramValue == null) {
      return "";
    }
    return paramValue.toString();
  }
}
