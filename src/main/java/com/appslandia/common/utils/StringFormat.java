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

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
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

        Object parameterValue = parameters.apply(chunk.value, chunk.expr);
        String valueAsStr = STR.formatParam(parameterValue, chunk.pattern);
        out.append(valueAsStr);
      } else {
        out.append(chunk.value);
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

      int index = -1;
      try {
        index = Integer.parseInt(pname);
      } catch (NumberFormatException ex) {
      }
      return ((0 <= index) && (index < parameters.length)) ? parameters[index] : STR.MISSED_VALUE;
    });
  }

  public String format(BiFunction<String, String, Object> parameters) {
    StringBuilder sb = new StringBuilder(this.outLen);

    format(parameters, sb);
    return sb.toString();
  }

  static class Chunk {
    final String value;
    final boolean isParam;

    final String pattern;
    final String expr;

    public Chunk(String value, boolean isParam, String pattern, String expr) {
      this.value = value;
      this.isParam = isParam;

      this.pattern = pattern;
      this.expr = expr;
    }
  }
}
