// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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

    var key = pair.substring(0, idx).strip();
    var value = pair.substring(idx + 1).strip();

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
    // a single space after separator
    var sepStr = (valueSeparator == ' ') ? " " : valueSeparator + " ";
    return String.join(sepStr, values);
  }
}
