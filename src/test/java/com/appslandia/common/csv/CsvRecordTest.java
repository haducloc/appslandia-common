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
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CsvRecordTest {

  private CsvRecord csvRecord;

  @BeforeEach
  public void setUp() {
    String[] values = { "abc", "123", "true", "1.23", null, "2023-09-01", "12:30", "2023-09-01 12:30:00" };
    csvRecord = new CsvRecord(values);
  }

  @Test
  public void testLength() {
    assertEquals(8, csvRecord.length());
  }

  @Test
  public void testApplyProcessor() {
    Function<String, String> processor = String::toUpperCase;
    csvRecord.applyProcessor(processor, 0);
    assertEquals("ABC", csvRecord.getString(0));
  }

  @Test
  public void testGetString() {
    assertEquals("abc", csvRecord.getString(0));
    assertEquals("123", csvRecord.getString(1));
  }

  @Test
  public void testGetStringReq() {
    assertEquals("abc", csvRecord.getStringReq(0));
  }

  @Test
  public void testGetStringReqThrowsException() {
    assertThrows(IllegalStateException.class, () -> csvRecord.getStringReq(4));
  }

  @Test
  public void testGetStringUpperReq() {
    assertEquals("ABC", csvRecord.getStringUpperReq(0));
  }

  @Test
  public void testGetStringUpperReqWithLocale() {
    assertEquals("ABC", csvRecord.getStringUpperReq(0, Locale.US));
  }

  @Test
  public void testGetStringUpper() {
    assertEquals("ABC", csvRecord.getStringUpper(0));
    assertNull(csvRecord.getStringUpper(4));
  }

  @Test
  public void testGetStringUpperWithLocale() {
    assertEquals("ABC", csvRecord.getStringUpper(0, Locale.US));
  }

  @Test
  public void testGetStringUpperWithIfNull() {
    assertEquals("DEFAULT", csvRecord.getStringUpper(4, "default"));
  }

  @Test
  public void testGetStringLowerReq() {
    assertEquals("abc", csvRecord.getStringLowerReq(0));
  }

  @Test
  public void testGetStringLowerReqWithLocale() {
    assertEquals("abc", csvRecord.getStringLowerReq(0, Locale.US));
  }

  @Test
  public void testGetStringLower() {
    assertEquals("abc", csvRecord.getStringLower(0));
    assertNull(csvRecord.getStringLower(4));
  }

  @Test
  public void testGetStringLowerWithLocale() {
    assertEquals("abc", csvRecord.getStringLower(0, Locale.US));
  }

  @Test
  public void testGetStringLowerWithIfNull() {
    assertEquals("default", csvRecord.getStringLower(4, "default"));
  }

  @Test
  public void testGetBool_ifNullOrInvalid() {
    assertTrue(csvRecord.getBool(2, false));
  }

  @Test
  public void testGetByte_ifNullOrInvalid() {
    assertEquals(Byte.valueOf((byte) 123), csvRecord.getByte(1, (byte) 0));
  }

  @Test
  public void testGetShort_ifNullOrInvalid() {
    assertEquals(Short.valueOf((short) 123), csvRecord.getShort(1, (short) 0));
  }

  @Test
  public void testGetInt_ifNullOrInvalid() {
    assertEquals(Integer.valueOf(123), csvRecord.getInt(1, 0));
  }

  @Test
  public void testGetLong_ifNullOrInvalid() {
    assertEquals(Long.valueOf(123), csvRecord.getLong(1, 0L));
  }

  @Test
  public void testGetFloat_ifNullOrInvalid() {
    assertEquals(Float.valueOf(1.23f), csvRecord.getFloat(3, 0f));
  }

  @Test
  public void testGetDouble_ifNullOrInvalid() {
    assertEquals(Double.valueOf(1.23), csvRecord.getDouble(3, 0.0));
  }

  @Test
  public void testGetDecimal_ifNullOrInvalid() {
    assertEquals(new BigDecimal("1.23"), csvRecord.getDecimal(3, BigDecimal.ZERO));
  }

  @Test
  public void testGetBool() {
    assertTrue(csvRecord.getBool(2));
  }

  @Test
  public void testGetByte() {
    assertEquals((byte) 123, csvRecord.getByte(1));
  }

  @Test
  public void testGetShort() {
    assertEquals((short) 123, csvRecord.getShort(1));
  }

  @Test
  public void testGetInt() {
    assertEquals(123, csvRecord.getInt(1));
  }

  @Test
  public void testGetLong() {
    assertEquals(123L, csvRecord.getLong(1));
  }

  @Test
  public void testGetFloat() {
    assertEquals(1.23f, csvRecord.getFloat(3), 0.001f);
  }

  @Test
  public void testGetDouble() {
    assertEquals(1.23, csvRecord.getDouble(3), 0.001);
  }

  @Test
  public void testGetDecimalReq() {
    assertEquals(new BigDecimal("1.23"), csvRecord.getDecimalReq(3));
  }

  @Test
  public void testGetBoolWithIfNull() {
    assertTrue(csvRecord.getBool(2, false));
    assertFalse(csvRecord.getBool(4, false));
  }

  @Test
  public void testGetByteWithIfNull() {
    assertEquals((byte) 123, csvRecord.getByte(1, (byte) 0));
    assertEquals((byte) 0, csvRecord.getByte(4, (byte) 0));
  }

  @Test
  public void testGetShortWithIfNull() {
    assertEquals((short) 123, csvRecord.getShort(1, (short) 0));
    assertEquals((short) 0, csvRecord.getShort(4, (short) 0));
  }

  @Test
  public void testGetIntWithIfNull() {
    assertEquals(123, csvRecord.getInt(1, 0));
    assertEquals(0, csvRecord.getInt(4, 0));
  }

  @Test
  public void testGetLongWithIfNull() {
    assertEquals(123L, csvRecord.getLong(1, 0L));
    assertEquals(0L, csvRecord.getLong(4, 0L));
  }

  @Test
  public void testGetFloatWithIfNull() {
    assertEquals(1.23f, csvRecord.getFloat(3, 0f), 0.001f);
    assertEquals(0f, csvRecord.getFloat(4, 0f), 0.001f);
  }

  @Test
  public void testGetDoubleWithIfNull() {
    assertEquals(1.23, csvRecord.getDouble(3, 0.0), 0.001);
    assertEquals(0.0, csvRecord.getDouble(4, 0.0), 0.001);
  }

  @Test
  public void testGetDecimalWithIfNull() {
    assertEquals(new BigDecimal("1.23"), csvRecord.getDecimal(3, 0.0));
    assertEquals(new BigDecimal("0.0"), csvRecord.getDecimal(4, 0.0));
  }

  @Test
  public void testGetLocalDateReq() {
    LocalDate expectedDate = LocalDate.of(2023, 9, 1);
    assertEquals(expectedDate, csvRecord.getLocalDateReq(5));
  }

  @Test
  public void testGetLocalTimeReq() {
    LocalTime expectedTime = LocalTime.of(12, 30);
    assertEquals(expectedTime, csvRecord.getLocalTimeReq(6));
  }

  @Test
  public void testGetLocalDateTimeReq() {
    LocalDateTime expectedDateTime = LocalDateTime.of(2023, 9, 1, 12, 30);
    assertEquals(expectedDateTime, csvRecord.getLocalDateTimeReq(7));
  }

  @Test
  public void testGetOffsetTimeReq() {
    String[] valuesWithOffsetTime = { "09:00+01:00" };
    csvRecord = new CsvRecord(valuesWithOffsetTime);
    OffsetTime expectedOffsetTime = OffsetTime.parse("09:00+01:00");
    assertEquals(expectedOffsetTime, csvRecord.getOffsetTimeReq(0));
  }

  @Test
  public void testGetOffsetDateTimeReq() {
    String[] valuesWithOffsetDateTime = { "2023-09-01 09:00+01:00" };
    csvRecord = new CsvRecord(valuesWithOffsetDateTime);
    OffsetDateTime expectedOffsetDateTime = OffsetDateTime.parse("2023-09-01T09:00+01:00");
    assertEquals(expectedOffsetDateTime, csvRecord.getOffsetDateTimeReq(0));
  }

  @Test
  public void testGetValueWithConverter() {
    Function<String, Integer> stringToInteger = Integer::parseInt;
    assertEquals(123, csvRecord.getValue(1, stringToInteger));
  }

  @Test
  public void testGetValueWithConverterAndDefault() {
    Function<String, Integer> stringToInteger = Integer::parseInt;
    assertEquals(123, csvRecord.getValue(1, 0, stringToInteger));
    assertEquals(0, csvRecord.getValue(4, 0, stringToInteger));
  }

  @Test
  public void testSetValues() {
    csvRecord.set(0, "value");
    assertEquals("value", csvRecord.getString(0));
  }

  @Test
  public void testSetBooleanValue() {
    csvRecord.set(2, true);
    assertEquals("true", csvRecord.getString(2));
  }

  @Test
  public void testSetIntValue() {
    csvRecord.set(1, 456);
    assertEquals("456", csvRecord.getString(1));
  }

  @Test
  public void testSetLongValue() {
    csvRecord.set(1, 456L);
    assertEquals("456", csvRecord.getString(1));
  }

  @Test
  public void testSetFloatValue() {
    csvRecord.set(3, 123.45f);
    assertEquals("123.45", csvRecord.getString(3));
  }

  @Test
  public void testSetDoubleValue() {
    csvRecord.set(3, 789.12);
    assertEquals("789.12", csvRecord.getString(3));
  }

  @Test
  public void testSetBigDecimalValue() {
    csvRecord.set(3, new BigDecimal("789.12"));
    assertEquals("789.12", csvRecord.getString(3));
  }

  @Test
  public void testSetLocalDateValue() {
    LocalDate date = LocalDate.of(2024, 1, 1);
    csvRecord.set(5, date);
    assertEquals("2024-01-01", csvRecord.getString(5));
  }

  @Test
  public void testSetLocalTimeValue() {
    LocalTime time = LocalTime.of(10, 15);
    csvRecord.set(6, time);
    assertEquals("10:15:00", csvRecord.getString(6));
  }

  @Test
  public void testSetLocalDateTimeValue() {
    LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 10, 15);
    csvRecord.set(7, dateTime);
    assertEquals("2024-01-01 10:15:00", csvRecord.getString(7));
  }

  @Test
  public void testSetOffsetTimeValue() {
    OffsetTime offsetTime = OffsetTime.of(10, 15, 0, 0, ZoneOffset.ofHours(1));
    csvRecord.set(6, offsetTime);
    assertEquals("10:15:00+01:00", csvRecord.getString(6));
  }

  @Test
  public void testSetOffsetDateTimeValue() {
    OffsetDateTime offsetDateTime = OffsetDateTime.of(2024, 1, 1, 10, 15, 0, 0, ZoneOffset.ofHours(1));
    csvRecord.set(7, offsetDateTime);
    assertEquals("2024-01-01 10:15:00+01:00", csvRecord.getString(7));
  }
}
