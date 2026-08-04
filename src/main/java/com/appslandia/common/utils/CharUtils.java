// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.Random;
import java.util.regex.Pattern;

/**
 *
 * @author Loc Ha
 *
 */
public class CharUtils {

  // a-z | A-Z, etc.
  private static final Pattern CHAR_RANGE_PATTERN = Pattern.compile(".\\-.");

  public static char[] toCharRanges(String charRanges) {
    var matcher = CHAR_RANGE_PATTERN.matcher(charRanges);
    char[] chars = null;
    var lastEnd = 0;

    while (matcher.find()) {
      var range = matcher.group();

      if (matcher.start() != lastEnd) {
        var notRange = charRanges.substring(lastEnd, matcher.start()).toCharArray();
        chars = (chars == null) ? notRange : ArrayUtils.append(chars, notRange);
      }
      lastEnd = matcher.end();

      var from = range.charAt(0);
      var to = range.charAt(2);
      Arguments.isTrue(from <= to, "charRanges '{}' is invalid.", charRanges);

      var newChars = new char[to - from + 1];
      for (var i = 0; i < newChars.length; i++) {
        newChars[i] = (char) (from + i);
      }
      chars = (chars == null) ? newChars : ArrayUtils.append(chars, newChars);
    }
    if (lastEnd < charRanges.length()) {
      var notRange = charRanges.substring(lastEnd).toCharArray();
      chars = (chars == null) ? notRange : ArrayUtils.append(chars, notRange);
    }
    return chars;
  }

  public static char[] randomChars(int len, char[][] sources, Random random) {
    var rdChars = new char[len];
    var avgLen = ValueUtils.valueOrMin(len / sources.length, 1);

    var rdLen = 0;

    // STEP1: Fill rdChars equally from each source
    for (char[] src : sources) {
      var rdCount = RandomUtils.nextInt(1, avgLen, random);

      for (var i = 0; i < rdCount; i++) {
        if (rdLen + i < len) {
          rdChars[rdLen + i] = src[random.nextInt(src.length)];
          rdLen += 1;
        }
      }
    }

    // STEP2: Fill unfilled positions
    if (rdLen < len) {
      for (var i = 0; i < len; i++) {
        if (rdChars[i] != 0) {
          continue;
        }
        var srcIndex = random.nextInt(sources.length);
        var src = sources[srcIndex];
        rdChars[i] = src[random.nextInt(src.length)];
      }
    }

    // STEP3: shuffle
    ArrayUtils.shuffle(rdChars, random);
    return rdChars;
  }
}
