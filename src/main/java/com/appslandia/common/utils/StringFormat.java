// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 *
 * @author Loc Ha
 *
 */
public class StringFormat {

  final int outLen;
  final List<Chunk> chunks;

  StringFormat(int outLen, List<Chunk> chunks) {
    this.outLen = outLen;
    this.chunks = chunks;
  }

  public void format(BiFunction<String, String, Object> parameters, StringBuilder out) {
    for (Chunk chunk : chunks) {
      if (chunk.isParam) {
        var paramValue = parameters.apply(chunk.text, chunk.expr);

        if (paramValue == STR.MISSED_VALUE) {
          throw new IllegalArgumentException(STR.fmt("Missing value for parameter {}.", chunk.expr));
        }

        var valueAsStr = String.valueOf(paramValue);
        out.append(valueAsStr);

      } else {
        out.append(chunk.text);
      }
    }
  }

  public String format(Map<String, Object> parameters) {
    return format((pname, expr) -> {
      return parameters.containsKey(pname) ? parameters.get(pname) : STR.MISSED_VALUE;
    });
  }

  public String format(Object... parameters) {
    return format((pname, expr) -> {

      var index = -1;
      try {
        index = Integer.parseInt(pname);
      } catch (NumberFormatException ex) {
      }
      return ((0 <= index) && (index < parameters.length)) ? parameters[index] : STR.MISSED_VALUE;
    });
  }

  public String format(BiFunction<String, String, Object> parameters) {
    var sb = new StringBuilder(outLen);

    format(parameters, sb);
    return sb.toString();
  }

  static class Chunk {
    final String text;
    final boolean isParam;
    final String expr;

    public Chunk(String text, boolean isParam, String expr) {
      this.text = text;
      this.isParam = isParam;
      this.expr = expr;
    }
  }
}
