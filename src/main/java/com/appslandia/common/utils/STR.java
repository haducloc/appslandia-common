// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 *
 * @author Loc Ha
 *
 */
public class STR {

  public static final Object MISSED_VALUE = new Object() {
  };

  // ${paramName}

  private static final Pattern PARAM_HOLDER_PATTERN = Pattern.compile("\\$\\{\\s*[a-z\\d_.-]+\\s*}",
      Pattern.CASE_INSENSITIVE);

  /**
   * Replaces placeholders in the format {@code ${parameter}}.
   * 
   */
  public static String format(String str, Map<String, Object> parameters) {
    if (str == null) {
      return null;
    }
    return format(str, (pname, expr) -> {

      // parameters
      if (parameters.containsKey(pname)) {
        return parameters.get(pname);
      }

      // SYS
      var resolvedVal = SYS.resolve(expr);
      return (resolvedVal != null) ? resolvedVal : MISSED_VALUE;
    });
  }

  /**
   * Replaces placeholders in the format {@code ${parameter}}.
   * 
   */
  public static String format(String str, BiFunction<String, String, Object> parameters) {
    if (str == null) {
      return null;
    }
    var out = new StringBuilder((int) (1.25 * str.length()));

    // ${paramName}
    var matcher = PARAM_HOLDER_PATTERN.matcher(str);

    var prevEnd = 0;
    while (matcher.find()) {

      // Non parameter
      out.append(str.substring(prevEnd, matcher.start()));

      // ${paramName}
      var paramGroup = matcher.group();
      var paramName = paramGroup.substring(2, paramGroup.length() - 1).strip();

      var expr = "${" + paramName + "}";
      var paramValue = parameters.apply(paramName, expr);

      if (paramValue == MISSED_VALUE) {
        throw new IllegalArgumentException(STR.fmt("Missing value for parameter {}.", expr));
      }

      var valueAsStr = String.valueOf(paramValue);
      out.append(valueAsStr);

      prevEnd = matcher.end();
    }
    if (prevEnd < str.length()) {
      out.append(str.substring(prevEnd));
    }
    return out.toString();
  }

  /**
   * Replaces indexed placeholders in the format {@code ${index}} with the corresponding parameter values.
   * 
   */
  public static String format(String str, Object... parameters) {
    if (str == null) {
      return null;
    }
    return format(str, (pname, expr) -> {

      var index = -1;
      try {
        index = Integer.parseInt(pname);
      } catch (NumberFormatException ex) {
      }
      return ((0 <= index) && (index < parameters.length)) ? parameters[index] : MISSED_VALUE;
    });
  }

  // {}
  private static final Pattern SEQ_HOLDER_PATTERN = Pattern.compile("\\{\\s*}");

  /**
   * Replaces sequential {@code {}} placeholders with the given entries.
   * 
   */
  public static String fmt(String str, Object... entries) {
    if (str == null) {
      return null;
    }
    var out = new StringBuilder(str.length() + entries.length * 16);
    var matcher = SEQ_HOLDER_PATTERN.matcher(str);

    var index = -1;
    var prevEnd = 0;
    while (matcher.find()) {

      // Non entry
      out.append(str.substring(prevEnd, matcher.start()));

      index++;
      var entryValue = ((0 <= index) && (index < entries.length)) ? entries[index] : MISSED_VALUE;

      if (entryValue == MISSED_VALUE) {
        throw new IllegalArgumentException("Missing value for parameter {}.");
      }

      var valueAsStr = String.valueOf(entryValue);
      out.append(valueAsStr);

      prevEnd = matcher.end();
    }
    if (prevEnd < str.length()) {
      out.append(str.substring(prevEnd));
    }
    return out.toString();
  }

  public static StringFormat toStringFormat(String format) {
    Arguments.notNull(format);

    var outLen = 0;
    List<StringFormat.Chunk> chunks = new ArrayList<>();

    // ${paramName}
    var matcher = PARAM_HOLDER_PATTERN.matcher(format);

    var prevEnd = 0;
    while (matcher.find()) {

      // Non parameter
      var chunk = format.substring(prevEnd, matcher.start());
      if (!chunk.isEmpty()) {
        chunks.add(new StringFormat.Chunk(chunk, false, null));
        outLen += chunk.length();
      }

      // ${paramName}
      var paramGroup = matcher.group();
      var paramName = paramGroup.substring(2, paramGroup.length() - 1).strip();

      chunks.add(new StringFormat.Chunk(paramName, true, "${" + paramName + "}"));
      outLen += 16;
      prevEnd = matcher.end();
    }

    if (prevEnd < format.length()) {
      var chunk = format.substring(prevEnd);
      if (!chunk.isEmpty()) {
        chunks.add(new StringFormat.Chunk(chunk, false, null));
        outLen += chunk.length();
      }
    }
    return new StringFormat(outLen, chunks);
  }
}
