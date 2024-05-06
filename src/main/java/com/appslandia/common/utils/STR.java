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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.TemporalAccessor;
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

  // ${paramName}
  private static final Pattern PARAM_HOLDER_PATTERN = Pattern
      .compile("\\$\\{\\s*([a-z0-9_]+)(\\?)?(\\|[^}\s][^}]*[^}\s])?\\s*}", Pattern.CASE_INSENSITIVE);

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
      String parameterGroup = matcher.group();
      String parameterName = parameterGroup.substring(parameterGroup.indexOf('{') + 1, parameterGroup.length() - 1)
          .trim();

      int idxVB = parameterName.indexOf('|');
      String pattern = null;

      if (idxVB > 0) {
        pattern = parameterName.substring(idxVB + 1);
        parameterName = parameterName.substring(0, idxVB);
      }

      boolean optional = parameterName.charAt(parameterName.length() - 1) == '?';
      if (optional) {
        parameterName = parameterName.substring(0, parameterName.length() - 1);
      }

      String expr = "${" + parameterName + "}";
      Object parameterValue = parameters.apply(parameterName, expr);

      String valueAsStr = formatParam(parameterValue, optional, pattern, expr);
      out.append(valueAsStr);

      prevEnd = matcher.end();
    }
    if (prevEnd < str.length()) {
      out.append(str.substring(prevEnd));
    }
  }

  // "" or ? or pattern or ?|pattern

  private static final Pattern SEQ_HOLDER_PATTERN = Pattern
      .compile("\\{(\\s*|\\s*\\?\\s*|\\s*[^}\s][^}]*[^}\s]\\s*|\\s*\\?\\|[^}\s][^}]*[^}\s]\\s*)}");

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
      String parameterGroup = matcher.group();
      String parameter = parameterGroup.substring(parameterGroup.indexOf('{') + 1, parameterGroup.length() - 1).trim();

      String pattern = null;
      boolean optional = false;

      if (!parameter.isEmpty()) {
        if (parameter.charAt(0) == '?') {
          optional = true;

          if (parameter.length() > 1) {
            pattern = parameter.substring(2);
          }
        } else {
          pattern = parameter;
        }
      }

      index++;
      Object entryValue = ((0 <= index) && (index < entries.length)) ? entries[index] : MISSED_VALUE;
      String valueAsStr = formatParam(entryValue, optional, pattern, "{}");
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
        chunks.add(new StringFormat.Chunk(chunk, false, false, null, null));
        outLen += chunk.length();
      }

      // ${paramName}
      String parameterGroup = matcher.group();
      String parameterName = parameterGroup.substring(parameterGroup.indexOf('{') + 1, parameterGroup.length() - 1)
          .trim();

      int idxVB = parameterName.indexOf('|');
      String pattern = null;

      if (idxVB > 0) {
        pattern = parameterName.substring(idxVB + 1);
        parameterName = parameterName.substring(0, idxVB);
      }

      boolean optional = parameterName.charAt(parameterName.length() - 1) == '?';
      if (optional) {
        parameterName = parameterName.substring(0, parameterName.length() - 1);
      }

      chunks.add(new StringFormat.Chunk(parameterName, true, optional, pattern, "${" + parameterName + "}"));
      outLen += 16;
      prevEnd = matcher.end();
    }

    if (prevEnd < str.length()) {
      String chunk = str.substring(prevEnd);
      if (!chunk.isEmpty()) {
        chunks.add(new StringFormat.Chunk(chunk, false, false, null, null));
        outLen += chunk.length();
      }
    }
    return new StringFormat(outLen, chunks);
  }

  static String formatParam(Object paramValue, boolean optional, String pattern, String paramExpr) {
    if (paramValue == null || paramValue == MISSED_VALUE) {
      return optional ? "" : paramExpr;
    }
    if (paramValue.getClass() == String.class) {
      return (String) paramValue;
    }
    if (paramValue instanceof Iterable) {
      return ObjectUtils.asString((Iterable<?>) paramValue);
    }
    if (paramValue.getClass().isArray()) {
      return ObjectUtils.asString(paramValue);
    }
    if (StringUtils.isNullOrEmpty(pattern)) {
      return paramValue.toString();
    }
    if (paramValue instanceof Number) {
      return new DecimalFormat(pattern).format(paramValue);
    }
    if (paramValue instanceof java.util.Date) {
      return new SimpleDateFormat(pattern).format(paramValue);
    }
    if (paramValue instanceof TemporalAccessor) {
      return DateUtils.getFormatter(pattern).format((TemporalAccessor) paramValue);
    }
    return paramValue.toString();
  }
}
