// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
