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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DataRecord extends CaseInsensitiveMap<Object> {
  private static final long serialVersionUID = 1L;

  public DataRecord() {
    super(new LinkedHashMap<>());
  }

  public DataRecord set(String columnLabel, Object value) {
    super.put(columnLabel, value);
    return this;
  }

  public Object getReq(String columnLabel) {
    Object value = super.get(columnLabel);
    if (value == null) {
      throw new IllegalStateException(STR.fmt("No value found for the given label '{}'.", columnLabel));
    }
    return value;
  }

  // Strings

  public String getStringReq(String columnLabel) {
    return (String) getReq(columnLabel);
  }

  public String getString(String columnLabel) {
    return (String) super.get(columnLabel);
  }

  public String getStringUpperReq(String columnLabel) {
    return getStringUpperReq(columnLabel, Locale.ROOT);
  }

  public String getStringUpperReq(String columnLabel, Locale locale) {
    String value = getStringReq(columnLabel);
    return value.toUpperCase(locale);
  }

  public String getStringUpper(String columnLabel) {
    return getStringUpper(columnLabel, Locale.ROOT);
  }

  public String getStringUpper(String columnLabel, Locale locale) {
    String value = getString(columnLabel);
    return (value != null) ? value.toUpperCase(locale) : null;
  }

  public String getStringUpper(String columnLabel, String ifNull) {
    return getStringUpper(columnLabel, ifNull, Locale.ROOT);
  }

  public String getStringUpper(String columnLabel, String ifNull, Locale locale) {
    String value = getString(columnLabel);
    return (value != null) ? value.toUpperCase(locale) : StringUtils.toUpperCase(ifNull, locale);
  }

  public String getStringLowerReq(String columnLabel) {
    return getStringLowerReq(columnLabel, Locale.ROOT);
  }

  public String getStringLowerReq(String columnLabel, Locale locale) {
    String value = getStringReq(columnLabel);
    return value.toLowerCase(locale);
  }

  public String getStringLower(String columnLabel) {
    return getStringLower(columnLabel, Locale.ROOT);
  }

  public String getStringLower(String columnLabel, Locale locale) {
    String value = getString(columnLabel);
    return (value != null) ? value.toLowerCase(locale) : null;
  }

  public String getStringLower(String columnLabel, String ifNull) {
    return getStringLower(columnLabel, ifNull, Locale.ROOT);
  }

  public String getStringLower(String columnLabel, String ifNull, Locale locale) {
    String value = getString(columnLabel);
    return (value != null) ? value.toLowerCase(locale) : StringUtils.toLowerCase(ifNull, locale);
  }

  // Primitives & Wrappers

  public Boolean getBoolOpt(String columnLabel) {
    return (Boolean) super.get(columnLabel);
  }

  public boolean getBool(String columnLabel) {
    Object value = getReq(columnLabel);
    return (boolean) value;
  }

  public boolean getBool(String columnLabel, boolean ifNull) {
    Object value = get(columnLabel);
    return (value != null) ? (boolean) value : ifNull;
  }

  public Byte getByteOpt(String columnLabel) {
    return (Byte) super.get(columnLabel);
  }

  public byte getByte(String columnLabel) {
    Object value = getReq(columnLabel);
    return (byte) value;
  }

  public byte getByte(String columnLabel, byte ifNull) {
    Object value = get(columnLabel);
    return (value != null) ? (byte) value : ifNull;
  }

  public Short getShortOpt(String columnLabel) {
    return (Short) super.get(columnLabel);
  }

  public short getShort(String columnLabel) {
    Object value = getReq(columnLabel);
    return (short) value;
  }

  public short getShort(String columnLabel, short ifNull) {
    Object value = get(columnLabel);
    return (value != null) ? (short) value : ifNull;
  }

  public Integer getIntOpt(String columnLabel) {
    return (Integer) super.get(columnLabel);
  }

  public int getInt(String columnLabel) {
    Object value = getReq(columnLabel);
    return (int) value;
  }

  public int getInt(String columnLabel, int ifNull) {
    Object value = get(columnLabel);
    return (value != null) ? (int) value : ifNull;
  }

  public Long getLongOpt(String columnLabel) {
    return (Long) super.get(columnLabel);
  }

  public long getLong(String columnLabel) {
    Object value = getReq(columnLabel);
    return (long) value;
  }

  public long getLong(String columnLabel, long ifNull) {
    Object value = get(columnLabel);
    return (value != null) ? (long) value : ifNull;
  }

  public Float getFloatOpt(String columnLabel) {
    return (Float) super.get(columnLabel);
  }

  public float getFloat(String columnLabel) {
    Object value = getReq(columnLabel);
    return (float) value;
  }

  public float getFloat(String columnLabel, float ifNull) {
    Object value = get(columnLabel);
    return (value != null) ? (float) value : ifNull;
  }

  public Double getDoubleOpt(String columnLabel) {
    return (Double) super.get(columnLabel);
  }

  public double getDouble(String columnLabel) {
    Object value = getReq(columnLabel);
    return (double) value;
  }

  public double getDouble(String columnLabel, double ifNull) {
    Object value = get(columnLabel);
    return (value != null) ? (double) value : ifNull;
  }

  // Decimal

  public BigDecimal getDecimal(String columnLabel) {
    return (BigDecimal) super.get(columnLabel);
  }

  public BigDecimal getDecimalReq(String columnLabel) {
    Object value = getReq(columnLabel);
    return (BigDecimal) value;
  }

  public BigDecimal getDecimal(String columnLabel, double ifNull) {
    Object value = get(columnLabel);
    return (value != null) ? (BigDecimal) value : new BigDecimal(Double.toString(ifNull));
  }

  // Temporal

  public LocalDate getLocalDateReq(String columnLabel) {
    Object value = getReq(columnLabel);
    return (LocalDate) value;
  }

  public LocalDate getLocalDate(String columnLabel) {
    Object value = get(columnLabel);
    return (LocalDate) value;
  }

  public LocalDateTime getLocalDateTimeReq(String columnLabel) {
    Object value = getReq(columnLabel);
    return (LocalDateTime) value;
  }

  public LocalDateTime getLocalDateTime(String columnLabel) {
    Object value = get(columnLabel);
    return (LocalDateTime) value;
  }

  public LocalTime getLocalTimeReq(String columnLabel) {
    Object value = getReq(columnLabel);
    return (LocalTime) value;
  }

  public LocalTime getLocalTime(String columnLabel) {
    Object value = get(columnLabel);
    return (LocalTime) value;
  }

  public OffsetDateTime getOffsetDateTimeReq(String columnLabel) {
    Object value = getReq(columnLabel);
    return (OffsetDateTime) value;
  }

  public OffsetDateTime getOffsetDateTime(String columnLabel) {
    Object value = get(columnLabel);
    return (OffsetDateTime) value;
  }

  public OffsetTime getOffsetTimeReq(String columnLabel) {
    Object value = getReq(columnLabel);
    return (OffsetTime) value;
  }

  public OffsetTime getOffsetTime(String columnLabel) {
    Object value = get(columnLabel);
    return (OffsetTime) value;
  }

  public Object[] toValues(Table table) {
    return toValues(table.getColumnLabels());
  }

  public Object[] toValues(String[] columnLabels) {
    return Arrays.stream(columnLabels).map(l -> super.get(l)).toArray();
  }
}
