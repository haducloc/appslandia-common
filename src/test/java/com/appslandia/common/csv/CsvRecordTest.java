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

package com.appslandia.common.csv;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvRecordTest {

  @Test
  public void test_null() {
    CsvRecord csvRecord = new CsvRecord(new String[] { null });
    try {
      Integer val = csvRecord.getIntOpt(0);
      Assertions.assertNull(val);

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_byte() {
    CsvRecord csvRecord = new CsvRecord(new String[] { "127" });
    try {
      byte val = csvRecord.getByte(0);
      Assertions.assertEquals(127, val);

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_short() {
    CsvRecord csvRecord = new CsvRecord(new String[] { "30000" });
    try {
      short val = csvRecord.getShort(0);
      Assertions.assertEquals(30000, val);

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_int() {
    CsvRecord csvRecord = new CsvRecord(new String[] { "300000" });
    try {
      int val = csvRecord.getInt(0);
      Assertions.assertEquals(300000, val);

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_long() {
    CsvRecord csvRecord = new CsvRecord(new String[] { "10000000000" });
    try {
      long val = csvRecord.getLong(0);
      Assertions.assertEquals(10000000000L, val);

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_double() {
    CsvRecord csvRecord = new CsvRecord(new String[] { "3.14" });
    try {
      double val = csvRecord.getDouble(0);
      Assertions.assertEquals(3.14, val, 0.001);

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_bigDecimal() {
    CsvRecord csvRecord = new CsvRecord(new String[] { "1234567890.1234567890" });
    try {
      BigDecimal val = csvRecord.getDecimal(0);
      BigDecimal expected = new BigDecimal("1234567890.1234567890");
      Assertions.assertEquals(expected, val);

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_LocalDate() {
    try {
      var expected = LocalDate.parse("2024-01-03", DateUtils.getFormatter(DateUtils.ISO8601_DATE));
      CsvRecord csvRecord = new CsvRecord(new String[] { "2024-01-03" });

      var val = csvRecord.getLocalDate(0);
      Assertions.assertEquals(expected, val);

    } catch (Exception ex) {
      ex.printStackTrace();
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_LocalTime() {
    try {
      var expected = LocalTime.parse("12:34:56", DateUtils.getFormatter(DateUtils.ISO8601_TIME_S));
      CsvRecord csvRecord = new CsvRecord(new String[] { "12:34:56" });

      var val = csvRecord.getLocalTime(0);
      Assertions.assertEquals(expected, val);

    } catch (Exception ex) {
      ex.printStackTrace();
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_LocalDateTime() {
    try {
      var expected = LocalDateTime.parse("2024-01-03T12:34:56", DateUtils.getFormatter(DateUtils.ISO8601_DATETIME_S));
      CsvRecord csvRecord = new CsvRecord(new String[] { "2024-01-03T12:34:56" });

      var val = csvRecord.getLocalDateTime(0);
      Assertions.assertEquals(expected, val);

    } catch (Exception ex) {
      ex.printStackTrace();
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_OffsetTime() {
    try {
      var expected = OffsetTime.parse("12:34:56+01:00", DateUtils.getFormatter(DateUtils.ISO8601_TIMEZ_S));
      CsvRecord csvRecord = new CsvRecord(new String[] { "12:34:56+01:00" });

      var val = csvRecord.getOffsetTime(0);
      Assertions.assertEquals(expected, val);

    } catch (Exception ex) {
      ex.printStackTrace();
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_OffsetDateTime() {
    try {
      var expected = OffsetDateTime.parse("2024-01-03T12:34:56+01:00",
          DateUtils.getFormatter(DateUtils.ISO8601_DATETIMEZ_S));
      CsvRecord csvRecord = new CsvRecord(new String[] { "2024-01-03T12:34:56+01:00" });

      var val = csvRecord.getOffsetDateTime(0);
      Assertions.assertEquals(expected, val);

    } catch (Exception ex) {
      ex.printStackTrace();
      Assertions.fail(ex);
    }
  }
}
