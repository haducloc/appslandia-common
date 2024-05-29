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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.BoolFormatException;
import com.appslandia.common.base.DateFormatException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ParseUtilsTest {

  @Test
  public void test_parseBool() throws BoolFormatException {
    Assertions.assertTrue(ParseUtils.parseBool("true"));
    Assertions.assertFalse(ParseUtils.parseBool("false"));
    Assertions.assertTrue(ParseUtils.parseBool("invalid", true));
  }

  @Test
  public void test_parseByte() {
    Assertions.assertEquals((byte) 42, ParseUtils.parseByte("42"));
    Assertions.assertEquals((byte) 0, ParseUtils.parseByte("invalid", (byte) 0));
  }

  @Test
  public void test_parseShort() {
    Assertions.assertEquals((short) 42, ParseUtils.parseShort("42"));
    Assertions.assertEquals((short) 0, ParseUtils.parseShort("invalid", (short) 0));
  }

  @Test
  public void test_parseInt() {
    Assertions.assertEquals(42, ParseUtils.parseInt("42"));
    Assertions.assertEquals(0, ParseUtils.parseInt("invalid", 0));
  }

  @Test
  public void test_parseLong() {
    Assertions.assertEquals(42L, ParseUtils.parseLong("42"));
    Assertions.assertEquals(0L, ParseUtils.parseLong("invalid", 0L));
  }

  @Test
  public void test_parseFloat() {
    Assertions.assertEquals(3.14f, ParseUtils.parseFloat("3.14"));
    Assertions.assertEquals(0.0f, ParseUtils.parseFloat("invalid", 0.0f));
  }

  @Test
  public void test_parseDouble() {
    Assertions.assertEquals(3.14, ParseUtils.parseDouble("3.14"));
    Assertions.assertEquals(0.0, ParseUtils.parseDouble("invalid", 0.0));
  }

  @Test
  public void test_parseDecimal() {
    Assertions.assertEquals(new BigDecimal("3.14"), ParseUtils.parseDecimal("3.14", 0.0));
    Assertions.assertEquals(new BigDecimal("0.0"), ParseUtils.parseDecimal("invalid", 0.0));
  }

  @Test
  public void test_parseBoolOpt() throws BoolFormatException {
    Assertions.assertTrue(ParseUtils.parseBoolOpt("true"));
    Assertions.assertFalse(ParseUtils.parseBoolOpt("false"));
    Assertions.assertThrows(BoolFormatException.class, () -> ParseUtils.parseBoolOpt("invalid"));
  }

  @Test
  public void test_parseByteOpt() {
    Assertions.assertEquals(Byte.valueOf((byte) 42), ParseUtils.parseByteOpt("42"));
    Assertions.assertThrows(NumberFormatException.class, () -> ParseUtils.parseByteOpt("invalid"));
  }

  @Test
  public void test_parseShortOpt() {
    Assertions.assertEquals(Short.valueOf((short) 42), ParseUtils.parseShortOpt("42"));
    Assertions.assertThrows(NumberFormatException.class, () -> ParseUtils.parseShortOpt("invalid"));
  }

  @Test
  public void test_parseIntOpt() {
    Assertions.assertEquals(Integer.valueOf(42), ParseUtils.parseIntOpt("42"));
    Assertions.assertThrows(NumberFormatException.class, () -> ParseUtils.parseIntOpt("invalid"));
  }

  @Test
  public void test_parseLongOpt() {
    Assertions.assertEquals(Long.valueOf(42), ParseUtils.parseLongOpt("42"));
    Assertions.assertThrows(NumberFormatException.class, () -> ParseUtils.parseLongOpt("invalid"));
  }

  @Test
  public void test_parseFloatOpt() {
    Assertions.assertEquals(Float.valueOf(3.14f), ParseUtils.parseFloatOpt("3.14"));
    Assertions.assertThrows(NumberFormatException.class, () -> ParseUtils.parseFloatOpt("invalid"));
  }

  @Test
  public void test_parseDoubleOpt() {
    Assertions.assertEquals(Double.valueOf(3.14), ParseUtils.parseDoubleOpt("3.14"));
    Assertions.assertThrows(NumberFormatException.class, () -> ParseUtils.parseDoubleOpt("invalid"));
  }

  @Test
  public void test_parseDecimalOpt() {
    Assertions.assertEquals(new BigDecimal("3.14"), ParseUtils.parseDecimalOpt("3.14"));
    Assertions.assertThrows(NumberFormatException.class, () -> ParseUtils.parseDecimalOpt("invalid"));
  }

  @Test
  public void test_isTrueValue() {
    Assertions.assertTrue(ParseUtils.isTrueValue("true"));
    Assertions.assertTrue(ParseUtils.isTrueValue("t"));
    Assertions.assertTrue(ParseUtils.isTrueValue("yes"));
    Assertions.assertTrue(ParseUtils.isTrueValue("y"));
    Assertions.assertFalse(ParseUtils.isTrueValue("false"));
    Assertions.assertFalse(ParseUtils.isTrueValue("f"));
    Assertions.assertFalse(ParseUtils.isTrueValue("no"));
    Assertions.assertFalse(ParseUtils.isTrueValue("n"));
  }

  @Test
  public void test_isFalseValue() {
    Assertions.assertFalse(ParseUtils.isFalseValue("true"));
    Assertions.assertFalse(ParseUtils.isFalseValue("t"));
    Assertions.assertFalse(ParseUtils.isFalseValue("yes"));
    Assertions.assertFalse(ParseUtils.isFalseValue("y"));
    Assertions.assertTrue(ParseUtils.isFalseValue("false"));
    Assertions.assertTrue(ParseUtils.isFalseValue("f"));
    Assertions.assertTrue(ParseUtils.isFalseValue("no"));
    Assertions.assertTrue(ParseUtils.isFalseValue("n"));
  }

  @Test
  public void test_parseLocalDate() throws DateFormatException {
    Assertions.assertEquals(LocalDate.parse("2024-05-29"), ParseUtils.parseLocalDate("2024-05-29", "yyyy-MM-dd"));
    Assertions.assertNull(ParseUtils.parseLocalDate(null, "yyyy-MM-dd"));
  }

  @Test
  public void test_parseLocalTime() throws DateFormatException {
    Assertions.assertEquals(LocalDate.parse("2024-05-29"),
        ParseUtils.parseLocalDate("2024-05-29", Collections.singletonList("yyyy-MM-dd")));
    Assertions.assertNull(ParseUtils.parseLocalDate(null, Collections.singletonList("yyyy-MM-dd")));
  }

  @Test
  public void test_parseLocalDateTime() throws DateFormatException {
    Assertions.assertEquals(LocalDate.parse("2024-05-29"), ParseUtils.parseLocalDate("2024-05-29", "yyyy-MM-dd"));
    Assertions.assertNull(ParseUtils.parseLocalDate(null, "yyyy-MM-dd"));
  }

  @Test
  public void test_parseOffsetTime() throws DateFormatException {
    Assertions.assertEquals(LocalDate.parse("2024-05-29"),
        ParseUtils.parseLocalDate("2024-05-29", Collections.singletonList("yyyy-MM-dd")));
    Assertions.assertNull(ParseUtils.parseLocalDate(null, Collections.singletonList("yyyy-MM-dd")));
  }

  @Test
  public void test_parseOffsetDateTime() throws DateFormatException {
    Assertions.assertEquals(LocalDate.parse("2024-05-29"), ParseUtils.parseLocalDate("2024-05-29", "yyyy-MM-dd"));
    Assertions.assertNull(ParseUtils.parseLocalDate(null, "yyyy-MM-dd"));
  }
}
