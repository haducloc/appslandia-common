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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.YearMonth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ParseUtilsTest {

  @Test
  public void test_parseBool() {
    try {
      boolean val = ParseUtils.parseBool("true");
      Assertions.assertTrue(val);

      val = ParseUtils.parseBool("false");
      Assertions.assertFalse(val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertTrue(ParseUtils.parseBool("invalid", true));
  }

  @Test
  public void test_parseByte() {
    try {
      byte val = ParseUtils.parseByte("42");
      Assertions.assertEquals(42, val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertEquals((byte) 0, ParseUtils.parseByte("invalid", (byte) 0));
  }

  @Test
  public void test_parseShort() {
    try {
      short val = ParseUtils.parseShort("42");
      Assertions.assertEquals(42, val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertEquals((short) 0, ParseUtils.parseShort("invalid", (short) 0));
  }

  @Test
  public void test_parseInt() {
    try {
      int val = ParseUtils.parseInt("42");
      Assertions.assertEquals(42, val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertEquals(0, ParseUtils.parseInt("invalid", 0));
  }

  @Test
  public void test_parseLong() {
    try {
      long val = ParseUtils.parseLong("42");
      Assertions.assertEquals(42, val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertEquals(0L, ParseUtils.parseLong("invalid", 0L));
  }

  @Test
  public void test_parseFloat() {
    try {
      float val = ParseUtils.parseFloat("3.14");
      Assertions.assertEquals(3.14f, val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertEquals(0.0f, ParseUtils.parseFloat("invalid", 0.0f));
  }

  @Test
  public void test_parseDouble() {
    try {
      double val = ParseUtils.parseDouble("3.14");
      Assertions.assertEquals(3.14, val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertEquals(0.0, ParseUtils.parseDouble("invalid", 0.0));
  }

  @Test
  public void test_parseDecimal() {
    try {
      BigDecimal val = ParseUtils.parseDecimalReq("3.14");
      Assertions.assertEquals(new BigDecimal("3.14"), val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertEquals(new BigDecimal("0.0"), ParseUtils.parseDecimal("invalid", 0.0));
  }

  @Test
  public void test_parseBool_ifNullOrInvalid() {
    Assertions.assertFalse(ParseUtils.parseBool(null, false));
    Assertions.assertTrue(ParseUtils.parseBool("true", false));
    Assertions.assertFalse(ParseUtils.parseBool("false", true));
    Assertions.assertFalse(ParseUtils.parseBool("invalid", false));
  }

  @Test
  public void test_parseByte_ifNullOrInvalid() {
    Assertions.assertEquals(0, ParseUtils.parseByte(null, (byte) 0));
    Assertions.assertEquals(123, ParseUtils.parseByte("123", (byte) 0));
    Assertions.assertEquals(-123, ParseUtils.parseByte("-123", (byte) 0));
    Assertions.assertEquals(0, ParseUtils.parseByte("invalid", (byte) 0));
  }

  @Test
  public void test_parseShort_ifNullOrInvalid() {
    Assertions.assertEquals(0, ParseUtils.parseShort(null, (short) 0));
    Assertions.assertEquals(12345, ParseUtils.parseShort("12345", (short) 0));
    Assertions.assertEquals(-12345, ParseUtils.parseShort("-12345", (short) 0));
    Assertions.assertEquals(0, ParseUtils.parseShort("invalid", (short) 0));
  }

  @Test
  public void test_parseInt_ifNullOrInvalid() {
    Assertions.assertEquals(0, ParseUtils.parseInt(null, 0));
    Assertions.assertEquals(123456, ParseUtils.parseInt("123456", 0));
    Assertions.assertEquals(-123456, ParseUtils.parseInt("-123456", 0));
    Assertions.assertEquals(0, ParseUtils.parseInt("invalid", 0));
  }

  @Test
  public void test_parseLong_ifNullOrInvalid() {
    Assertions.assertEquals(0L, ParseUtils.parseLong(null, 0L));
    Assertions.assertEquals(1234567890L, ParseUtils.parseLong("1234567890", 0L));
    Assertions.assertEquals(-1234567890L, ParseUtils.parseLong("-1234567890", 0L));
    Assertions.assertEquals(0L, ParseUtils.parseLong("invalid", 0L));
  }

  @Test
  public void test_parseFloat_ifNullOrInvalid() {
    Assertions.assertEquals(0.0f, ParseUtils.parseFloat(null, 0.0f));
    Assertions.assertEquals(123.456f, ParseUtils.parseFloat("123.456", 0.0f));
    Assertions.assertEquals(-123.456f, ParseUtils.parseFloat("-123.456", 0.0f));
    Assertions.assertEquals(0.0f, ParseUtils.parseFloat("invalid", 0.0f));
  }

  @Test
  public void test_parseDouble_ifNullOrInvalid() {
    Assertions.assertEquals(0.0, ParseUtils.parseDouble(null, 0.0));
    Assertions.assertEquals(123.456, ParseUtils.parseDouble("123.456", 0.0));
    Assertions.assertEquals(-123.456, ParseUtils.parseDouble("-123.456", 0.0));
    Assertions.assertEquals(0.0, ParseUtils.parseDouble("invalid", 0.0));
  }

  @Test
  public void test_parseDecimal_ifNullOrInvalid() {
    Assertions.assertEquals(new BigDecimal("0.0"), ParseUtils.parseDecimal(null, 0.0));
    Assertions.assertEquals(new BigDecimal("123.456"), ParseUtils.parseDecimal("123.456", 0.0));
    Assertions.assertEquals(new BigDecimal("-123.456"), ParseUtils.parseDecimal("-123.456", 0.0));
    Assertions.assertEquals(new BigDecimal("0.0"), ParseUtils.parseDecimal("invalid", 0.0));
  }

  @Test
  public void test_parseLocalDate() {
    try {
      LocalDate val = ParseUtils.parseLocalDate("2024-05-29", DateUtils.ISO8601_DATE);
      Assertions.assertEquals(LocalDate.parse("2024-05-29"), val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      LocalDate val = ParseUtils.parseLocalDate(null, DateUtils.ISO8601_DATE);
      Assertions.assertNull(val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_parseLocalTime() {
    try {
      LocalTime val = ParseUtils.parseLocalTime("12:30:45", DateUtils.ISO8601_TIME_S);
      Assertions.assertEquals(LocalTime.parse("12:30:45"), val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      LocalTime val = ParseUtils.parseLocalTime(null, DateUtils.ISO8601_TIME_S);
      Assertions.assertNull(val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_parseLocalDateTime() {
    try {
      LocalDateTime val = ParseUtils.parseLocalDateTime("2024-05-29T12:30:45", DateUtils.ISO8601_DATETIME_S);
      Assertions.assertEquals(LocalDateTime.parse("2024-05-29T12:30:45"), val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      LocalDateTime val = ParseUtils.parseLocalDateTime(null, DateUtils.ISO8601_DATETIME_S);
      Assertions.assertNull(val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_parseOffsetDateTime() {
    try {
      OffsetDateTime val = ParseUtils.parseOffsetDateTime("2024-05-29T12:30:45Z", DateUtils.ISO8601_DATETIMEZ_S);
      Assertions.assertEquals(OffsetDateTime.parse("2024-05-29T12:30:45Z"), val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      OffsetDateTime val = ParseUtils.parseOffsetDateTime(null, DateUtils.ISO8601_DATETIMEZ_S);
      Assertions.assertNull(val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_parseOffsetTime() {
    try {
      OffsetTime val = ParseUtils.parseOffsetTime("12:30:45+01:00", DateUtils.ISO8601_TIMEZ_S);
      Assertions.assertEquals(OffsetTime.parse("12:30:45+01:00"), val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      OffsetTime val = ParseUtils.parseOffsetTime(null, DateUtils.ISO8601_TIMEZ_S);
      Assertions.assertNull(val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_parseYearMonth() {
    try {
      YearMonth val = ParseUtils.parseYearMonth("2024-05", DateUtils.ISO8601_YEAR_MONTH);
      Assertions.assertEquals(YearMonth.parse("2024-05"), val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      YearMonth val = ParseUtils.parseYearMonth(null, DateUtils.ISO8601_YEAR_MONTH);
      Assertions.assertNull(val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_isTrueValue() {
    Assertions.assertTrue(ParseUtils.isTrueValue("true"));
    Assertions.assertTrue(ParseUtils.isTrueValue("t"));
    Assertions.assertTrue(ParseUtils.isTrueValue("yes"));
    Assertions.assertTrue(ParseUtils.isTrueValue("y"));
    Assertions.assertTrue(ParseUtils.isTrueValue("1"));
  }

  @Test
  public void test_isFalseValue() {
    Assertions.assertTrue(ParseUtils.isFalseValue("false"));
    Assertions.assertTrue(ParseUtils.isFalseValue("f"));
    Assertions.assertTrue(ParseUtils.isFalseValue("no"));
    Assertions.assertTrue(ParseUtils.isFalseValue("n"));
    Assertions.assertTrue(ParseUtils.isFalseValue("0"));
  }
}
