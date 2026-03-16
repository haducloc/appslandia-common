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

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import com.appslandia.common.base.CaseInsensitiveMap;

/**
 *
 * @author Loc Ha
 *
 */
public class ConfigUtils {

  public static String[] splitPair(String pair, char separator) {
    if (pair == null) {
      return null;
    }

    var idx = pair.indexOf(separator);
    if (idx <= 0) {
      return null;
    }

    var key = pair.substring(0, idx).trim();
    var value = pair.substring(idx + 1).trim();

    if (key.isEmpty()) {
      return null;
    }

    return new String[] { key, value.isEmpty() ? null : value };
  }

  public static Map<String, String> toPairMap(String mtlPairs, char kvSeparator) {
    if (mtlPairs == null) {
      return Collections.emptyMap();
    }
    var pairs = SplitUtils.splitByLine(mtlPairs);

    return Arrays.stream(pairs).filter(p -> !p.startsWith("//")).map(p -> splitPair(p, kvSeparator))
        .filter(p -> p != null).collect(Collectors.toMap(p -> p[0], p -> p[1], (a, b) -> b, CaseInsensitiveMap::new));
  }

  public static String[] toMultilineValues(String multilineValues) {
    if (multilineValues == null) {
      return StringUtils.EMPTY_ARRAY;
    }
    var lines = SplitUtils.splitByLine(multilineValues);

    return Arrays.stream(lines).filter(p -> !p.startsWith("//")).toArray(String[]::new);
  }

  public static String[] toMultilineValues(String multilineValues, char valueSeparator) {
    if (multilineValues == null) {
      return StringUtils.EMPTY_ARRAY;
    }
    var lines = SplitUtils.splitByLine(multilineValues);

    var joined = Arrays.stream(lines).filter(p -> !p.startsWith("//"))
        .collect(Collectors.joining(String.valueOf(valueSeparator)));
    return SplitUtils.split(joined, valueSeparator);
  }

  public static String toSinglelineValues(String multilineValues, char valueSeparator) {
    var values = toMultilineValues(multilineValues, valueSeparator);
    if (values.length == 0) {
      return null;
    }
    return String.join(String.valueOf(valueSeparator), values);
  }
}
