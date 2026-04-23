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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import com.appslandia.common.base.BoolFormatException;
import com.appslandia.common.base.TemporalFormatException;

/**
 *
 * @author Loc Ha
 *
 */
public class ParseUtilsTest {

  // Boolean Parsing

  @Test
  void testParseBool_valid() {
    assertTrue(ParseUtils.parseBool("true"));
    assertFalse(ParseUtils.parseBool("false"));
  }

  @Test
  void testParseBool_invalid() {
    assertThrows(BoolFormatException.class, () -> ParseUtils.parseBool("invalid"));
  }

  @Test
  void testParseBool_withDefault() {
    assertTrue(ParseUtils.parseBool("true", false));
    assertFalse(ParseUtils.parseBool(null, false));
    assertFalse(ParseUtils.parseBool("invalid", false));
  }

  @Test
  void testParseBoolOpt() {
    assertTrue(ParseUtils.parseBoolOpt("true"));
    assertNull(ParseUtils.parseBoolOpt(null));
    assertThrows(BoolFormatException.class, () -> ParseUtils.parseBoolOpt("invalid"));
  }

  // Byte Parsing

  @Test
  void testParseByte_valid() {
    assertEquals((byte) 123, ParseUtils.parseByte("123"));
  }

  @Test
  void testParseByte_invalid() {
    assertThrows(NumberFormatException.class, () -> ParseUtils.parseByte("invalid"));
  }

  @Test
  void testParseByte_withDefault() {
    assertEquals((byte) 123, ParseUtils.parseByte("123", (byte) 0));
    assertEquals((byte) 0, ParseUtils.parseByte(null, (byte) 0));
    assertEquals((byte) 0, ParseUtils.parseByte("invalid", (byte) 0));
  }

  @Test
  void testParseByteOpt() {
    assertEquals((byte) 123, ParseUtils.parseByteOpt("123"));
    assertNull(ParseUtils.parseByteOpt(null));
    assertThrows(NumberFormatException.class, () -> ParseUtils.parseByteOpt("invalid"));
  }

  // Short Parsing

  @Test
  void testParseShort_valid() {
    assertEquals((short) 123, ParseUtils.parseShort("123"));
  }

  @Test
  void testParseShort_invalid() {
    assertThrows(NumberFormatException.class, () -> ParseUtils.parseShort("invalid"));
  }

  @Test
  void testParseShort_withDefault() {
    assertEquals((short) 123, ParseUtils.parseShort("123", (short) 0));
    assertEquals((short) 0, ParseUtils.parseShort(null, (short) 0));
    assertEquals((short) 0, ParseUtils.parseShort("invalid", (short) 0));
  }

  @Test
  void testParseShortOpt() {
    assertEquals((short) 123, ParseUtils.parseShortOpt("123"));
    assertNull(ParseUtils.parseShortOpt(null));
    assertThrows(NumberFormatException.class, () -> ParseUtils.parseShortOpt("invalid"));
  }

  // Integer Parsing

  @Test
  void testParseInt_valid() {
    assertEquals(123, ParseUtils.parseInt("123"));
  }

  @Test
  void testParseInt_invalid() {
    assertThrows(NumberFormatException.class, () -> ParseUtils.parseInt("invalid"));
  }

  @Test
  void testParseInt_withDefault() {
    assertEquals(123, ParseUtils.parseInt("123", 0));
    assertEquals(0, ParseUtils.parseInt(null, 0));
    assertEquals(0, ParseUtils.parseInt("invalid", 0));
  }

  @Test
  void testParseIntOpt() {
    assertEquals(123, ParseUtils.parseIntOpt("123"));
    assertNull(ParseUtils.parseIntOpt(null));
    assertThrows(NumberFormatException.class, () -> ParseUtils.parseIntOpt("invalid"));
  }

  // Long Parsing

  @Test
  void testParseLong_valid() {
    assertEquals(123L, ParseUtils.parseLong("123"));
  }

  @Test
  void testParseLong_invalid() {
    assertThrows(NumberFormatException.class, () -> ParseUtils.parseLong("invalid"));
  }

  @Test
  void testParseLong_withDefault() {
    assertEquals(123L, ParseUtils.parseLong("123", 0L));
    assertEquals(0L, ParseUtils.parseLong(null, 0L));
    assertEquals(0L, ParseUtils.parseLong("invalid", 0L));
  }

  @Test
  void testParseLongOpt() {
    assertEquals(123L, ParseUtils.parseLongOpt("123"));
    assertNull(ParseUtils.parseLongOpt(null));
    assertThrows(NumberFormatException.class, () -> ParseUtils.parseLongOpt("invalid"));
  }

  // Float Parsing

  @Test
  void testParseFloat_valid() {
    assertEquals(1.23f, ParseUtils.parseFloat("1.23"));
  }

  @Test
  void testParseFloat_invalid() {
    assertThrows(NumberFormatException.class, () -> ParseUtils.parseFloat("invalid"));
  }

  @Test
  void testParseFloat_withDefault() {
    assertEquals(1.23f, ParseUtils.parseFloat("1.23", 0f));
    assertEquals(0f, ParseUtils.parseFloat(null, 0f));
    assertEquals(0f, ParseUtils.parseFloat("invalid", 0f));
  }

  // Double Parsing

  @Test
  void testParseDouble_valid() {
    assertEquals(1.23, ParseUtils.parseDouble("1.23"));
  }

  @Test
  void testParseDouble_invalid() {
    assertThrows(NumberFormatException.class, () -> ParseUtils.parseDouble("invalid"));
  }

  @Test
  void testParseDouble_withDefault() {
    assertEquals(1.23, ParseUtils.parseDouble("1.23", 0.0));
    assertEquals(0.0, ParseUtils.parseDouble(null, 0.0));
    assertEquals(0.0, ParseUtils.parseDouble("invalid", 0.0));
  }

  // BigDecimal Parsing

  @Test
  void testParseDecimal_valid() {
    assertEquals(new BigDecimal("1.23"), ParseUtils.parseDecimal("1.23"));
  }

  @Test
  void testParseDecimal_invalid() {
    assertThrows(NumberFormatException.class, () -> ParseUtils.parseDecimal("invalid"));
  }

  @Test
  void testParseDecimal_withDefault() {
    assertEquals(new BigDecimal("1.23"), ParseUtils.parseDecimal("1.23", 0.0));
    assertEquals(new BigDecimal("0.0"), ParseUtils.parseDecimal(null, 0.0));
    assertEquals(new BigDecimal("0.0"), ParseUtils.parseDecimal("invalid", 0.0));
  }

  // ENUM Parsing

  enum TestEnum {
    VALUE1, VALUE2
  }

  @Test
  void testParseEnum_valid() {
    assertEquals(TestEnum.VALUE1, ParseUtils.parseEnum("VALUE1", TestEnum.class));
  }

  @Test
  void testParseEnum_invalid() {
    assertThrows(IllegalArgumentException.class, () -> ParseUtils.parseEnum("INVALID", TestEnum.class));
  }

  @Test
  void testParseEnum_withDefault() {
    assertEquals(TestEnum.VALUE1, ParseUtils.parseEnum("VALUE1", TestEnum.class, TestEnum.VALUE2));
    assertEquals(TestEnum.VALUE2, ParseUtils.parseEnum("INVALID", TestEnum.class, TestEnum.VALUE2));
  }

  // Temporal Parsing

  @Test
  void testParseLocalDate() {
    assertEquals(LocalDate.of(2023, 1, 1), ParseUtils.parseLocalDate("2023-01-01", "yyyy-MM-dd"));
    assertNull(ParseUtils.parseLocalDate(null, "yyyy-MM-dd"));
    assertThrows(TemporalFormatException.class, () -> ParseUtils.parseLocalDate("invalid", "yyyy-MM-dd"));
  }

  @Test
  void testParseLocalTime() {
    assertEquals(LocalTime.of(12, 30), ParseUtils.parseLocalTime("12:30", "HH:mm"));
    assertNull(ParseUtils.parseLocalTime(null, "HH:mm"));
    assertThrows(TemporalFormatException.class, () -> ParseUtils.parseLocalTime("invalid", "HH:mm"));
  }

  @Test
  void testParseLocalDateTime() {
    assertEquals(LocalDateTime.of(2023, 1, 1, 12, 30),
        ParseUtils.parseLocalDateTime("2023-01-01T12:30", "yyyy-MM-dd'T'HH:mm"));
    assertNull(ParseUtils.parseLocalDateTime(null, "yyyy-MM-dd'T'HH:mm"));
    assertThrows(TemporalFormatException.class, () -> ParseUtils.parseLocalDateTime("invalid", "yyyy-MM-dd'T'HH:mm"));
  }

  @Test
  void testParseOffsetTime() {
    assertEquals(OffsetTime.parse("12:30+01:00"), ParseUtils.parseOffsetTime("12:30+01:00", "HH:mmXXX"));
    assertNull(ParseUtils.parseOffsetTime(null, "HH:mmXXX"));
    assertThrows(TemporalFormatException.class, () -> ParseUtils.parseOffsetTime("invalid", "HH:mmXXX"));
  }

  @Test
  void testParseOffsetDateTime() {
    assertEquals(OffsetDateTime.parse("2023-01-01T12:30+01:00"),
        ParseUtils.parseOffsetDateTime("2023-01-01T12:30+01:00", "yyyy-MM-dd'T'HH:mmXXX"));
    assertNull(ParseUtils.parseOffsetDateTime(null, "yyyy-MM-dd'T'HH:mmXXX"));
    assertThrows(TemporalFormatException.class,
        () -> ParseUtils.parseOffsetDateTime("invalid", "yyyy-MM-dd'T'HH:mmXXX"));
  }

  @Test
  void testParseYearMonth() {
    assertEquals(YearMonth.of(2023, 1), ParseUtils.parseYearMonth("2023-01", "yyyy-MM"));
    assertNull(ParseUtils.parseYearMonth(null, "yyyy-MM"));
    assertThrows(TemporalFormatException.class, () -> ParseUtils.parseYearMonth("invalid", "yyyy-MM"));
  }
}
