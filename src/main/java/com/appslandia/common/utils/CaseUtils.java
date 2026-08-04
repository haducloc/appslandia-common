// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 *
 * @author Loc Ha
 *
 */
public class CaseUtils {

  private static final Pattern LETTER_LOWER_UPPER_PATTERN = Pattern.compile("([a-z])([A-Z])");
  private static final Pattern LETTER_UPPER_UPPERLOWER_PATTERN = Pattern.compile("([A-Z]+)([A-Z][a-z])");

  public static String toSnakeCase(String input) {
    if (input == null) {
      return null;
    }
    return toSeparatedCase(input, "$1_$2");
  }

  public static String toKebabCase(String input) {
    if (input == null) {
      return null;
    }
    return toSeparatedCase(input, "$1-$2");
  }

  private static String toSeparatedCase(String input, String replacementPattern) {
    var result = LETTER_UPPER_UPPERLOWER_PATTERN.matcher(input).replaceAll(replacementPattern);
    result = LETTER_LOWER_UPPER_PATTERN.matcher(result).replaceAll(replacementPattern);
    return result.toLowerCase(Locale.ROOT);
  }
}