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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CsvRecordTest {

  private CsvRecord csvRecord;

  @BeforeEach
  void setUp() {
    String[] values = { "value1", null, "true", null, "123", null, "1.23", null, "2023-01-01", null, "12:30", null,
        "2023-01-01T12:30", null, "09:00+01:00", null, "2023-01-01T09:00+01:00" };
    csvRecord = new CsvRecord(values);
  }

  // Strings
  @Test
  void testGetStringReq() {
    assertEquals("value1", csvRecord.getStringReq(0));
    assertThrows(IllegalStateException.class, () -> csvRecord.getStringReq(1));
  }

  @Test
  void testGetString() {
    assertEquals("value1", csvRecord.getString(0));
    assertNull(csvRecord.getString(1));
  }

  @Test
  void testGetStringUpperReq() {
    assertEquals("VALUE1", csvRecord.getStringUpperReq(0));
    assertThrows(IllegalStateException.class, () -> csvRecord.getStringUpperReq(1));
  }

  @Test
  void testGetStringUpper() {
    assertEquals("VALUE1", csvRecord.getStringUpper(0));
    assertNull(csvRecord.getStringUpper(1));
  }

  @Test
  void testGetStringLowerReq() {
    assertEquals("value1", csvRecord.getStringLowerReq(0));
    assertThrows(IllegalStateException.class, () -> csvRecord.getStringLowerReq(1));
  }

  @Test
  void testGetStringLower() {
    assertEquals("value1", csvRecord.getStringLower(0));
    assertNull(csvRecord.getStringLower(1));
  }

  // Booleans
  @Test
  void testGetBool() {
    assertTrue(csvRecord.getBool(2));
    assertThrows(IllegalStateException.class, () -> csvRecord.getBool(1));
  }

  @Test
  void testGetBool_withDefault() {
    assertTrue(csvRecord.getBool(2, false));
    assertFalse(csvRecord.getBool(1, false));
  }

  // Numbers
  @Test
  void testGetByte() {
    assertEquals(123, csvRecord.getByte(4));
    assertThrows(IllegalStateException.class, () -> csvRecord.getByte(1));
  }

  @Test
  void testGetByte_withDefault() {
    assertEquals(123, csvRecord.getByte(4, (byte) 0));
    assertEquals(0, csvRecord.getByte(1, (byte) 0));
  }

  @Test
  void testGetShort() {
    assertEquals(123, csvRecord.getShort(4));
    assertThrows(IllegalStateException.class, () -> csvRecord.getShort(1));
  }

  @Test
  void testGetShort_withDefault() {
    assertEquals(123, csvRecord.getShort(4, (short) 0));
    assertEquals(0, csvRecord.getShort(1, (short) 0));
  }

  @Test
  void testGetInt() {
    assertEquals(123, csvRecord.getInt(4));
    assertThrows(IllegalStateException.class, () -> csvRecord.getInt(1));
  }

  @Test
  void testGetInt_withDefault() {
    assertEquals(123, csvRecord.getInt(4, 0));
    assertEquals(0, csvRecord.getInt(1, 0));
  }

  @Test
  void testGetLong() {
    assertEquals(123L, csvRecord.getLong(4));
    assertThrows(IllegalStateException.class, () -> csvRecord.getLong(1));
  }

  @Test
  void testGetLong_withDefault() {
    assertEquals(123L, csvRecord.getLong(4, 0L));
    assertEquals(0L, csvRecord.getLong(1, 0L));
  }

  @Test
  void testGetFloat() {
    assertEquals(1.23f, csvRecord.getFloat(6));
    assertThrows(IllegalStateException.class, () -> csvRecord.getFloat(1));
  }

  @Test
  void testGetFloat_withDefault() {
    assertEquals(1.23f, csvRecord.getFloat(6, 0f));
    assertEquals(0f, csvRecord.getFloat(1, 0f));
  }

  @Test
  void testGetDouble() {
    assertEquals(1.23, csvRecord.getDouble(6));
    assertThrows(IllegalStateException.class, () -> csvRecord.getDouble(1));
  }

  @Test
  void testGetDouble_withDefault() {
    assertEquals(1.23, csvRecord.getDouble(6, 0.0));
    assertEquals(0.0, csvRecord.getDouble(1, 0.0));
  }

  // BigDecimal
  @Test
  void testGetDecimal() {
    assertEquals(new BigDecimal("1.23"), csvRecord.getDecimal(6));
    assertNull(csvRecord.getDecimal(1));
  }

  @Test
  void testGetDecimal_withDefault() {
    assertEquals(new BigDecimal("1.23"), csvRecord.getDecimal(6, BigDecimal.ZERO));
    assertEquals(BigDecimal.ZERO, csvRecord.getDecimal(1, BigDecimal.ZERO));
  }

  @Test
  void testGetDecimalReq() {
    assertEquals(new BigDecimal("1.23"), csvRecord.getDecimalReq(6));
    assertThrows(IllegalStateException.class, () -> csvRecord.getDecimalReq(1));
  }

  // Temporal
  @Test
  void testGetLocalDate() {
    assertEquals(LocalDate.parse("2023-01-01"), csvRecord.getLocalDate(8));
    assertNull(csvRecord.getLocalDate(1));
  }

  @Test
  void testGetLocalDateReq() {
    assertEquals(LocalDate.parse("2023-01-01"), csvRecord.getLocalDateReq(8));
    assertThrows(IllegalStateException.class, () -> csvRecord.getLocalDateReq(1));
  }

  @Test
  void testGetLocalTime() {
    assertEquals(LocalTime.parse("12:30"), csvRecord.getLocalTime(10));
    assertNull(csvRecord.getLocalTime(1));
  }

  @Test
  void testGetLocalTimeReq() {
    assertEquals(LocalTime.parse("12:30"), csvRecord.getLocalTimeReq(10));
    assertThrows(IllegalStateException.class, () -> csvRecord.getLocalTimeReq(1));
  }

  @Test
  void testGetLocalDateTime() {
    assertEquals(LocalDateTime.parse("2023-01-01T12:30"), csvRecord.getLocalDateTime(12));
    assertNull(csvRecord.getLocalDateTime(1));
  }

  @Test
  void testGetLocalDateTimeReq() {
    assertEquals(LocalDateTime.parse("2023-01-01T12:30"), csvRecord.getLocalDateTimeReq(12));
    assertThrows(IllegalStateException.class, () -> csvRecord.getLocalDateTimeReq(1));
  }

  @Test
  void testGetOffsetTime() {
    assertEquals(OffsetTime.parse("09:00+01:00"), csvRecord.getOffsetTime(14));
    assertNull(csvRecord.getOffsetTime(1));
  }

  @Test
  void testGetOffsetTimeReq() {
    assertEquals(OffsetTime.parse("09:00+01:00"), csvRecord.getOffsetTimeReq(14));
    assertThrows(IllegalStateException.class, () -> csvRecord.getOffsetTimeReq(1));
  }

  @Test
  void testGetOffsetDateTime() {
    assertEquals(OffsetDateTime.parse("2023-01-01T09:00+01:00"), csvRecord.getOffsetDateTime(16));
    assertNull(csvRecord.getOffsetDateTime(1));
  }

  @Test
  void testGetOffsetDateTimeReq() {
    assertEquals(OffsetDateTime.parse("2023-01-01T09:00+01:00"), csvRecord.getOffsetDateTimeReq(16));
    assertThrows(IllegalStateException.class, () -> csvRecord.getOffsetDateTimeReq(1));
  }
}
