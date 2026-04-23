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
