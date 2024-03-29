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
  private static final Pattern PARAM_HOLDER_PATTERN = Pattern.compile("\\$\\{\s*([a-z0-9_]+)(\\?)?(\s*\\|[^}]*)?\s*}",
      Pattern.CASE_INSENSITIVE);

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
      if (prevEnd == 0) {
        out.append(str.substring(0, matcher.start()));
      } else {
        out.append(str.substring(prevEnd, matcher.start()));
      }

      // ${paramName}
      String parameterGroup = matcher.group();
      String parameterName = parameterGroup.substring(parameterGroup.indexOf('{') + 1, parameterGroup.length() - 1)
          .trim();

      int idxVB = parameterName.indexOf('|');

      String pattern = null;
      if (idxVB > 0) {
        pattern = parameterName.substring(idxVB + 1).trim();
        parameterName = parameterName.substring(0, idxVB).trim();
      }

      boolean optional = parameterName.charAt(parameterName.length() - 1) == '?';
      if (optional) {
        parameterName = parameterName.substring(0, parameterName.length() - 1);
      }
      Object parameterValue = parameters.apply(parameterName, parameterGroup);

      if (parameterValue == null || parameterValue == MISSED_VALUE) {
        out.append(optional ? "" : "${MISSED_VALUE}");

      } else {
        if (parameterValue instanceof Iterable) {
          out.append(ObjectUtils.asString((Iterable<?>) parameterValue));

        } else if (parameterValue.getClass().isArray()) {
          out.append(ObjectUtils.asString(parameterValue));
        } else {

          if (StringUtils.isNullOrEmpty(pattern)) {
            out.append(parameterValue.toString());
          } else {

            if (parameterValue instanceof Number) {
              out.append(new DecimalFormat(pattern).format(parameterValue));

            } else if (parameterValue instanceof java.util.Date) {
              out.append(new SimpleDateFormat(pattern).format(parameterValue));

            } else if (parameterValue instanceof TemporalAccessor) {
              out.append(DateUtils.getFormatter(pattern).format((TemporalAccessor) parameterValue));

            } else {
              out.append(parameterValue.toString());
            }
          }
        }
      }
      prevEnd = matcher.end();
    }
    if (prevEnd < str.length()) {
      out.append(str.substring(prevEnd));
    }
  }

  private static final Pattern SEQ_HOLDER_PATTERN = Pattern.compile("\\{\s*(\\?\s*\\|\s*)?([^}\s]*)\s*}");

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
      if (prevEnd == 0) {
        out.append(str.substring(0, matcher.start()));
      } else {
        out.append(str.substring(prevEnd, matcher.start()));
      }

      // {}
      String parameterGroup = matcher.group();
      String parameter = parameterGroup.substring(parameterGroup.indexOf('{') + 1, parameterGroup.length() - 1).trim();

      String pattern = null;
      boolean optional = false;

      if (!parameter.isEmpty()) {

        if (parameter.charAt(0) == '?') {
          if (parameter.length() == 1) {
            optional = true;

          } else {
            String pt = parameter.substring(1).trim();
            if ((pt.length() > 0) && (pt.charAt(0) == '|')) {

              optional = true;
              pattern = pt.substring(1).trim();
            } else {
              pattern = parameter;
            }
          }
        } else {
          pattern = parameter;
        }
      }

      index++;
      Object entryValue = ((0 <= index) && (index < entries.length)) ? entries[index] : MISSED_VALUE;

      if (entryValue == null || entryValue == MISSED_VALUE) {
        out.append(optional ? "" : "{MISSED_VALUE}");

      } else {
        if (entryValue instanceof Iterable) {
          out.append(ObjectUtils.asString((Iterable<?>) entryValue));

        } else if (entryValue.getClass().isArray()) {
          out.append(ObjectUtils.asString(entryValue));
        } else {

          if (StringUtils.isNullOrEmpty(pattern)) {
            out.append(entryValue.toString());
          } else {

            if (entryValue instanceof Number) {
              out.append(new DecimalFormat(pattern).format(entryValue));

            } else if (entryValue instanceof java.util.Date) {
              out.append(new SimpleDateFormat(pattern).format(entryValue));

            } else if (entryValue instanceof TemporalAccessor) {
              out.append(DateUtils.getFormatter(pattern).format((TemporalAccessor) entryValue));

            } else {
              out.append(entryValue.toString());
            }
          }
        }
      }
      prevEnd = matcher.end();
    }
    if (prevEnd < str.length()) {
      out.append(str.substring(prevEnd));
    }
    return out.toString();
  }
}
