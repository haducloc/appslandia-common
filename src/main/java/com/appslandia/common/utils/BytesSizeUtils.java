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

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Loc Ha
 *
 */
public class BytesSizeUtils {

  private static final Pattern SIZE_AMT_PATTERN = Pattern.compile("((\\d+\\.\\d+|\\d+)([TGMK]i?B|B)\\s*)+",
      Pattern.CASE_INSENSITIVE);

  // @formatter:off
  private static final Map<String, Long> SIZE_UNIT_MULTIPLIERS = Map.ofEntries(
      Map.entry("TIB", 1_099_511_627_776L),
      Map.entry("GIB", 1_073_741_824L),
      Map.entry("MIB", 1_048_576L),
      Map.entry("KIB", 1_024L),

      Map.entry("TB", 1_000_000_000_000L),
      Map.entry("GB", 1_000_000_000L),
      Map.entry("MB", 1_000_000L),
      Map.entry("KB", 1_000L),

      Map.entry("B", 1L)
  );
  // @formatter:on

  public static long translateToBytes(String sizeAmt) {
    Arguments.notNull(sizeAmt, "sizeAmt is required.");
    Arguments.isTrue(SIZE_AMT_PATTERN.matcher(sizeAmt).matches(), "sizeAmt '{}' is invalid.", sizeAmt);

    var result = 0D;
    var i = 0;
    while (i < sizeAmt.length()) {
      var j = i;
      while (j < sizeAmt.length() && (Character.isDigit(sizeAmt.charAt(j)) || sizeAmt.charAt(j) == '.')) {
        j++;
      }

      var k = j;
      while (k < sizeAmt.length() && (Character.isLetter(sizeAmt.charAt(k)) || sizeAmt.charAt(k) == ' ')) {
        k++;
      }

      var amount = Double.parseDouble(sizeAmt.substring(i, j));
      var unit = sizeAmt.substring(j, k).strip().toUpperCase(Locale.ENGLISH);

      var multiplier = SIZE_UNIT_MULTIPLIERS.get(unit);
      Asserts.notNull(multiplier);

      result += amount * multiplier;
      i = k;
    }

    return (long) Math.ceil(result);
  }
}
