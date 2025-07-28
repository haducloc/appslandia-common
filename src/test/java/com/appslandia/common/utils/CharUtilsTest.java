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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class CharUtilsTest {

  @Test
  public void test_toCharRanges() {
    var charRanges = CharUtils.toCharRanges("a-zA-Z");
    Assertions.assertNotNull(charRanges);
    Assertions.assertTrue(charRanges.length == 52);
  }

  @Test
  public void test_toCharRanges_digits() {
    var charRanges = CharUtils.toCharRanges("0-9");
    Assertions.assertNotNull(charRanges);
    Assertions.assertTrue(charRanges.length == 10);

    charRanges = CharUtils.toCharRanges("1-37-9");
    Assertions.assertNotNull(charRanges);
    Assertions.assertEquals("123789", new String(charRanges));
  }

  @Test
  public void test_toCharRanges_notRange() {
    var charRanges = CharUtils.toCharRanges("0123");
    Assertions.assertNotNull(charRanges);
    Assertions.assertEquals("0123", new String(charRanges));
  }

  @Test
  public void test_toCharRanges_mixed() {
    var charRanges = CharUtils.toCharRanges("1-3a-cABC");
    Assertions.assertNotNull(charRanges);
    Assertions.assertEquals("123abcABC", new String(charRanges));
  }

  @Test
  public void test_toCharRanges_minus() {
    var charRanges = CharUtils.toCharRanges("1-3a-c-");
    Assertions.assertNotNull(charRanges);
    Assertions.assertEquals("123abc-", new String(charRanges));
  }
}
