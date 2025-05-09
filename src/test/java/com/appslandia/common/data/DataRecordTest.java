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

package com.appslandia.common.data;

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
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataRecordTest {

  private DataRecord dataRecord;

  @BeforeEach
  void setUp() {
    dataRecord = new DataRecord();
  }

  @Test
  void testSetAndGetReq() {
    dataRecord.set("key", "value");
    assertEquals("value", dataRecord.getReq("key"));
    assertThrows(IllegalStateException.class, () -> dataRecord.getReq("nonExistentKey"));
  }

  @Test
  void testGetStringReq() {
    dataRecord.set("key", "value");
    assertEquals("value", dataRecord.getStringReq("key"));
  }

  @Test
  void testGetString() {
    dataRecord.set("key", "value");
    assertEquals("value", dataRecord.getString("key"));
    assertNull(dataRecord.getString("nonExistentKey"));
  }

  @Test
  void testGetStringUpperReq() {
    dataRecord.set("key", "value");
    assertEquals("VALUE", dataRecord.getStringUpperReq("key"));
    assertEquals("VALUE", dataRecord.getStringUpperReq("key", Locale.ENGLISH));
  }

  @Test
  void testGetStringUpper() {
    dataRecord.set("key", "value");
    assertEquals("VALUE", dataRecord.getStringUpper("key"));
    assertEquals("VALUE", dataRecord.getStringUpper("key", Locale.ENGLISH));
    assertNull(dataRecord.getStringUpper("nonExistentKey"));
    assertEquals("DEFAULT", dataRecord.getStringUpper("nonExistentKey", "default", Locale.ENGLISH));
  }

  @Test
  void testGetStringLowerReq() {
    dataRecord.set("key", "VALUE");
    assertEquals("value", dataRecord.getStringLowerReq("key"));
    assertEquals("value", dataRecord.getStringLowerReq("key", Locale.ENGLISH));
  }

  @Test
  void testGetStringLower() {
    dataRecord.set("key", "VALUE");
    assertEquals("value", dataRecord.getStringLower("key"));
    assertEquals("value", dataRecord.getStringLower("key", Locale.ENGLISH));
    assertNull(dataRecord.getStringLower("nonExistentKey"));
    assertEquals("default", dataRecord.getStringLower("nonExistentKey", "DEFAULT", Locale.ENGLISH));
  }

  @Test
  void testGetBool() {
    dataRecord.set("key", true);
    assertTrue(dataRecord.getBool("key"));
    assertFalse(dataRecord.getBool("nonExistentKey", false));
  }

  @Test
  void testGetByte() {
    dataRecord.set("key", (byte) 1);
    assertEquals(1, dataRecord.getByte("key"));
    assertEquals(0, dataRecord.getByte("nonExistentKey", (byte) 0));
  }

  @Test
  void testGetShort() {
    dataRecord.set("key", (short) 1);
    assertEquals(1, dataRecord.getShort("key"));
    assertEquals(0, dataRecord.getShort("nonExistentKey", (short) 0));
  }

  @Test
  void testGetInt() {
    dataRecord.set("key", 1);
    assertEquals(1, dataRecord.getInt("key"));
    assertEquals(0, dataRecord.getInt("nonExistentKey", 0));
  }

  @Test
  void testGetLong() {
    dataRecord.set("key", 1L);
    assertEquals(1L, dataRecord.getLong("key"));
    assertEquals(0L, dataRecord.getLong("nonExistentKey", 0L));
  }

  @Test
  void testGetFloat() {
    dataRecord.set("key", 1.1f);
    assertEquals(1.1f, dataRecord.getFloat("key"));
    assertEquals(0.0f, dataRecord.getFloat("nonExistentKey", 0.0f));
  }

  @Test
  void testGetDouble() {
    dataRecord.set("key", 1.1);
    assertEquals(1.1, dataRecord.getDouble("key"));
    assertEquals(0.0, dataRecord.getDouble("nonExistentKey", 0.0));
  }

  @Test
  void testGetDecimal() {
    dataRecord.set("key", BigDecimal.ONE);
    assertEquals(BigDecimal.ONE, dataRecord.getDecimal("key"));
    assertTrue(BigDecimal.TEN.compareTo(dataRecord.getDecimal("nonExistentKey", 10.0)) == 0);
  }

  @Test
  void testGetDecimalReq() {
    dataRecord.set("key", BigDecimal.ONE);
    assertEquals(BigDecimal.ONE, dataRecord.getDecimalReq("key"));
  }

  @Test
  void testGetLocalDate() {
    var date = LocalDate.of(2023, 1, 1);
    dataRecord.set("key", date);
    assertEquals(date, dataRecord.getLocalDate("key"));
    assertNull(dataRecord.getLocalDate("nonExistentKey"));
  }

  @Test
  void testGetLocalDateReq() {
    var date = LocalDate.of(2023, 1, 1);
    dataRecord.set("key", date);
    assertEquals(date, dataRecord.getLocalDateReq("key"));
  }

  @Test
  void testGetLocalTime() {
    var time = LocalTime.of(12, 30);
    dataRecord.set("key", time);
    assertEquals(time, dataRecord.getLocalTime("key"));
    assertNull(dataRecord.getLocalTime("nonExistentKey"));
  }

  @Test
  void testGetLocalTimeReq() {
    var time = LocalTime.of(12, 30);
    dataRecord.set("key", time);
    assertEquals(time, dataRecord.getLocalTimeReq("key"));
  }

  @Test
  void testGetLocalDateTime() {
    var dateTime = LocalDateTime.of(2023, 1, 1, 12, 30);
    dataRecord.set("key", dateTime);
    assertEquals(dateTime, dataRecord.getLocalDateTime("key"));
    assertNull(dataRecord.getLocalDateTime("nonExistentKey"));
  }

  @Test
  void testGetLocalDateTimeReq() {
    var dateTime = LocalDateTime.of(2023, 1, 1, 12, 30);
    dataRecord.set("key", dateTime);
    assertEquals(dateTime, dataRecord.getLocalDateTimeReq("key"));
  }

  @Test
  void testGetOffsetDateTime() {
    var offsetDateTime = OffsetDateTime.parse("2023-01-01T12:30+01:00");
    dataRecord.set("key", offsetDateTime);
    assertEquals(offsetDateTime, dataRecord.getOffsetDateTime("key"));
    assertNull(dataRecord.getOffsetDateTime("nonExistentKey"));
  }

  @Test
  void testGetOffsetDateTimeReq() {
    var offsetDateTime = OffsetDateTime.parse("2023-01-01T12:30+01:00");
    dataRecord.set("key", offsetDateTime);
    assertEquals(offsetDateTime, dataRecord.getOffsetDateTimeReq("key"));
  }

  @Test
  void testGetOffsetTime() {
    var offsetTime = OffsetTime.parse("12:30+01:00");
    dataRecord.set("key", offsetTime);
    assertEquals(offsetTime, dataRecord.getOffsetTime("key"));
    assertNull(dataRecord.getOffsetTime("nonExistentKey"));
  }

  @Test
  void testGetOffsetTimeReq() {
    var offsetTime = OffsetTime.parse("12:30+01:00");
    dataRecord.set("key", offsetTime);
    assertEquals(offsetTime, dataRecord.getOffsetTimeReq("key"));
  }
}
