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

package com.appslandia.common.data;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.appslandia.common.base.AssertException;

public class DataRecordTest {

  private DataRecord dataRecord;

  @BeforeEach
  public void setUp() {
    dataRecord = new DataRecord();
  }

  @Test
  public void testSetAndGet() {
    dataRecord.set("name", "John Doe");
    assertEquals("John Doe", dataRecord.get("name"));
  }

  @Test
  public void testGetReqValuePresent() {
    dataRecord.set("name", "John Doe");
    assertEquals("John Doe", dataRecord.getReq("name"));
  }

  @Test
  public void testGetReqValueMissingThrowsException() {
    assertThrows(AssertException.class, () -> dataRecord.getReq("missingColumn"));
  }

  @Test
  public void testGetString() {
    dataRecord.set("name", "John Doe");
    assertEquals("John Doe", dataRecord.getString("name"));
  }

  @Test
  public void testGetStringReq() {
    dataRecord.set("name", "John Doe");
    assertEquals("John Doe", dataRecord.getStringReq("name"));
  }

  @Test
  public void testGetStringReqMissingThrowsException() {
    assertThrows(AssertException.class, () -> dataRecord.getStringReq("missingColumn"));
  }

  @Test
  public void testGetStringUpperReq() {
    dataRecord.set("name", "john doe");
    assertEquals("JOHN DOE", dataRecord.getStringUpperReq("name"));
  }

  @Test
  public void testGetStringLowerReq() {
    dataRecord.set("name", "JOHN DOE");
    assertEquals("john doe", dataRecord.getStringLowerReq("name"));
  }

  @Test
  public void testGetOptionalValues() {
    dataRecord.set("boolean", true);
    dataRecord.set("int", 42);
    dataRecord.set("double", 3.14);
    dataRecord.set("decimal", new BigDecimal("123.45"));

    assertTrue(dataRecord.getBoolOpt("boolean"));
    assertEquals(42, dataRecord.getIntOpt("int"));
    assertEquals(3.14, dataRecord.getDoubleOpt("double"));
    assertEquals(new BigDecimal("123.45"), dataRecord.getDecimal("decimal"));
  }

  @Test
  public void testGetRequiredValues() {
    dataRecord.set("int", 42);
    dataRecord.set("double", 3.14);

    assertEquals(42, dataRecord.getInt("int"));
    assertEquals(3.14, dataRecord.getDouble("double"), 0.001);
  }

  @Test
  public void testGetRequiredValueThrowsException() {
    assertThrows(AssertException.class, () -> dataRecord.getInt("missingInt"));
  }

  @Test
  public void testGetBoolReq() {
    dataRecord.set("bool", true);
    assertTrue(dataRecord.getBool("bool"));
  }

  @Test
  public void testGetBoolReqThrowsException() {
    assertThrows(AssertException.class, () -> dataRecord.getBool("missingBool"));
  }

  @Test
  public void testGetByteReq() {
    dataRecord.set("byte", (byte) 10);
    assertEquals(10, dataRecord.getByte("byte"));
  }

  @Test
  public void testGetByteReqThrowsException() {
    assertThrows(AssertException.class, () -> dataRecord.getByte("missingByte"));
  }

  @Test
  public void testGetShortReq() {
    dataRecord.set("short", (short) 20);
    assertEquals(20, dataRecord.getShort("short"));
  }

  @Test
  public void testGetShortReqThrowsException() {
    assertThrows(AssertException.class, () -> dataRecord.getShort("missingShort"));
  }

  @Test
  public void testGetLongReq() {
    dataRecord.set("long", 100L);
    assertEquals(100L, dataRecord.getLong("long"));
  }

  @Test
  public void testGetLongReqThrowsException() {
    assertThrows(AssertException.class, () -> dataRecord.getLong("missingLong"));
  }

  @Test
  public void testGetFloatReq() {
    dataRecord.set("float", 1.23f);
    assertEquals(1.23f, dataRecord.getFloat("float"), 0.001);
  }

  @Test
  public void testGetFloatReqThrowsException() {
    assertThrows(AssertException.class, () -> dataRecord.getFloat("missingFloat"));
  }

  @Test
  public void testGetDecimalReq() {
    dataRecord.set("decimal", new BigDecimal("123.45"));
    assertEquals(new BigDecimal("123.45"), dataRecord.getDecimalReq("decimal"));
  }

  @Test
  public void testGetDecimalReqThrowsException() {
    assertThrows(AssertException.class, () -> dataRecord.getDecimalReq("missingDecimal"));
  }

  @Test
  public void testGetBoolWithIfNull() {
    dataRecord.set("col1", true);
    assertTrue(dataRecord.getBool("col1", false));
    assertFalse(dataRecord.getBool("missingCol", false));
  }

  @Test
  public void testGetByteWithIfNull() {
    dataRecord.set("col1", (byte) 10);
    assertEquals(10, dataRecord.getByte("col1", (byte) 5));
    assertEquals(5, dataRecord.getByte("missingCol", (byte) 5));
  }

  @Test
  public void testGetShortWithIfNull() {
    dataRecord.set("col1", (short) 20);
    assertEquals(20, dataRecord.getShort("col1", (short) 10));
    assertEquals(10, dataRecord.getShort("missingCol", (short) 10));
  }

  @Test
  public void testGetIntWithIfNull() {
    dataRecord.set("col1", 42);
    assertEquals(42, dataRecord.getInt("col1", 50));
    assertEquals(50, dataRecord.getInt("missingCol", 50));
  }

  @Test
  public void testGetLongWithIfNull() {
    dataRecord.set("col1", 100L);
    assertEquals(100L, dataRecord.getLong("col1", 50L));
    assertEquals(50L, dataRecord.getLong("missingCol", 50L));
  }

  @Test
  public void testGetFloatWithIfNull() {
    dataRecord.set("col1", 1.23f);
    assertEquals(1.23f, dataRecord.getFloat("col1", 0.5f), 0.001);
    assertEquals(0.5f, dataRecord.getFloat("missingCol", 0.5f), 0.001);
  }

  @Test
  public void testGetDoubleWithIfNull() {
    dataRecord.set("col1", 3.14);
    assertEquals(3.14, dataRecord.getDouble("col1", 1.5), 0.001);
    assertEquals(1.5, dataRecord.getDouble("missingCol", 1.5), 0.001);
  }

  @Test
  public void testGetDecimalWithIfNull() {
    dataRecord.set("col1", new BigDecimal("100.5"));
    assertEquals(new BigDecimal("100.5"), dataRecord.getDecimal("col1", 50.5));
    assertEquals(new BigDecimal("50.5"), dataRecord.getDecimal("missingCol", 50.5));
  }

  @Test
  public void testGetJava8DateTimes() {
    LocalDate date = LocalDate.of(2023, 9, 10);
    LocalDateTime dateTime = LocalDateTime.of(2023, 9, 10, 12, 30);
    LocalTime time = LocalTime.of(12, 30);
    OffsetDateTime offsetDateTime = OffsetDateTime.now();
    OffsetTime offsetTime = OffsetTime.now();

    dataRecord.set("date", date);
    dataRecord.set("dateTime", dateTime);
    dataRecord.set("time", time);
    dataRecord.set("offsetDateTime", offsetDateTime);
    dataRecord.set("offsetTime", offsetTime);

    assertEquals(date, dataRecord.getLocalDate("date"));
    assertEquals(dateTime, dataRecord.getLocalDateTime("dateTime"));
    assertEquals(time, dataRecord.getLocalTime("time"));
    assertEquals(offsetDateTime, dataRecord.getOffsetDateTime("offsetDateTime"));
    assertEquals(offsetTime, dataRecord.getOffsetTime("offsetTime"));
  }

  @Test
  public void testCaseInsensitiveKeys() {
    dataRecord.set("ColumnLabel", "Value");
    assertEquals("Value", dataRecord.get("columnlabel"));
  }

  @Test
  public void testToValues() {
    dataRecord.set("col1", "value1");
    dataRecord.set("col2", "value2");
    String[] columns = { "col1", "col2" };
    Object[] values = dataRecord.toValues(columns);
    assertArrayEquals(new Object[] { "value1", "value2" }, values);
  }

  @Test
  public void testGetStringUpperWithIfNull() {
    dataRecord.set("col1", "hello");
    assertEquals("HELLO", dataRecord.getStringUpper("col1", "Default"));
    assertEquals("DEFAULT", dataRecord.getStringUpper("missingCol", "Default"));
  }

  @Test
  public void testGetStringLowerWithIfNull() {
    dataRecord.set("col1", "HELLO");
    assertEquals("hello", dataRecord.getStringLower("col1", "Default"));
    assertEquals("default", dataRecord.getStringLower("missingCol", "Default"));
  }
}
