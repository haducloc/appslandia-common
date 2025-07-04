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
    for (Chunk chunk : this.chunks) {
      if (chunk.isParam) {
        var paramValue = parameters.apply(chunk.text, chunk.expr);

        if (paramValue == STR.MISSED_VALUE) {
          throw new IllegalArgumentException(STR.fmt("The parameter '{}' must be passed.", chunk.expr));
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
    var sb = new StringBuilder(this.outLen);

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
