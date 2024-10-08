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
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.appslandia.common.base.AssertException;
import com.appslandia.common.base.BoolFormatException;
import com.appslandia.common.base.TemporalFormatException;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ParseUtils;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvRecord {

  final String[] values;

  public CsvRecord(String[] fieldValues) {
    this.values = Asserts.notNull(fieldValues);
  }

  public void applyProcessor(Function<String, String> processor, int... indexes) {
    Asserts.notNull(processor);
    Asserts.hasElements(indexes);

    for (int i : indexes) {
      Objects.checkIndex(i, this.values.length);

      this.values[i] = processor.apply(this.values[i]);
    }
  }

  public int length() {
    return this.values.length;
  }

  // Strings

  public String getStringReq(int index) {
    String value = getString(index);
    if (value == null) {
      throw new AssertException(STR.fmt("No value found for the given index '{}'.", index));
    }
    return value;
  }

  public String getString(int index) {
    Objects.checkIndex(index, this.values.length);
    return this.values[index];
  }

  public String getString(int index, String ifNull) {
    String value = getString(index);
    return (value != null) ? value : ifNull;
  }

  public String getStringUpperReq(int index) {
    return getStringUpperReq(index, Locale.ROOT);
  }

  public String getStringUpperReq(int index, Locale locale) {
    String value = getStringReq(index);
    return value.toUpperCase(locale);
  }

  public String getStringUpper(int index) {
    return getStringUpper(index, Locale.ROOT);
  }

  public String getStringUpper(int index, Locale locale) {
    String value = getString(index);
    return (value != null) ? value.toUpperCase(locale) : null;
  }

  public String getStringUpper(int index, String ifNull) {
    return getStringUpper(index, ifNull, Locale.ROOT);
  }

  public String getStringUpper(int index, String ifNull, Locale locale) {
    String value = getString(index);
    return (value != null) ? value.toUpperCase(locale) : StringUtils.toUpperCase(ifNull, locale);
  }

  public String getStringLowerReq(int index) {
    return getStringLowerReq(index, Locale.ROOT);
  }

  public String getStringLowerReq(int index, Locale locale) {
    String value = getStringReq(index);
    return value.toLowerCase(locale);
  }

  public String getStringLower(int index) {
    return getStringLower(index, Locale.ROOT);
  }

  public String getStringLower(int index, Locale locale) {
    String value = getString(index);
    return (value != null) ? value.toLowerCase(locale) : null;
  }

  public String getStringLower(int index, String ifNull) {
    return getStringLower(index, ifNull, Locale.ROOT);
  }

  public String getStringLower(int index, String ifNull, Locale locale) {
    String value = getString(index);
    return (value != null) ? value.toLowerCase(locale) : StringUtils.toLowerCase(ifNull, locale);
  }

  // Optional Values

  public Boolean getBoolOpt(int index, Boolean ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseBoolOpt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public Byte getByteOpt(int index, Byte ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseByteOpt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public Short getShortOpt(int index, Short ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseShortOpt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public Integer getIntOpt(int index, Integer ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseIntOpt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public Long getLongOpt(int index, Long ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseLongOpt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public Float getFloatOpt(int index, Float ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseFloatOpt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public Double getDoubleOpt(int index, Double ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseDoubleOpt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public BigDecimal getDecimalOpt(int index, BigDecimal ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseDecimalOpt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  // Required Values

  public boolean getBool(int index) throws BoolFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseBool(value);
  }

  public byte getByte(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseByte(value);
  }

  public short getShort(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseShort(value);
  }

  public int getInt(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseInt(value);
  }

  public long getLong(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseLong(value);
  }

  public float getFloat(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseFloat(value);
  }

  public double getDouble(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseDouble(value);
  }

  public BigDecimal getDecimalReq(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return new BigDecimal(value);
  }

  // Required Values, ifNullOrInvalid

  public boolean getBool(int index, boolean ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseBool(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public byte getByte(int index, byte ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseByte(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public short getShort(int index, short ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseShort(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public int getInt(int index, int ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseInt(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public long getLong(int index, long ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseLong(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public float getFloat(int index, float ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseFloat(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public double getDouble(int index, double ifNullOrInvalid) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseDouble(value, ifNullOrInvalid) : ifNullOrInvalid;
  }

  public BigDecimal getDecimal(int index, double ifNullOrInvalid) {
    String value = getString(index);
    return ParseUtils.parseDecimal(value, ifNullOrInvalid);
  }

  // getValue

  public <T> T getValue(int index, Function<String, T> exceptionalConverter) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseValue(value, exceptionalConverter) : null;
  }

  public <T> T getValue(int index, T ifNullOrInvalid, Function<String, T> exceptionalConverter) {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseValue(value, ifNullOrInvalid, exceptionalConverter) : ifNullOrInvalid;
  }

  // Temporal Types

  public LocalDate getLocalDateReq(int index, String... patterns) throws TemporalFormatException {
    String value = getStringReq(index);
    return (patterns.length > 0) ? ParseUtils.parseLocalDate(value, patterns)
        : ParseUtils.parseLocalDate(value, CsvUtils.PATTERNS_DATE);
  }

  public LocalTime getLocalTimeReq(int index, String... patterns) throws TemporalFormatException {
    String value = getStringReq(index);
    return (patterns.length > 0) ? ParseUtils.parseLocalTime(value, patterns)
        : ParseUtils.parseLocalTime(value, CsvUtils.PATTERNS_TIME);
  }

  public LocalDateTime getLocalDateTimeReq(int index, String... patterns) throws TemporalFormatException {
    String value = getStringReq(index);
    return (patterns.length > 0) ? ParseUtils.parseLocalDateTime(value, patterns)
        : ParseUtils.parseLocalDateTime(value, CsvUtils.PATTERNS_DATETIME);
  }

  public OffsetTime getOffsetTimeReq(int index, String... patterns) throws TemporalFormatException {
    String value = getStringReq(index);
    return (patterns.length > 0) ? ParseUtils.parseOffsetTime(value, patterns)
        : ParseUtils.parseOffsetTime(value, CsvUtils.PATTERNS_TIMEZ);
  }

  public OffsetDateTime getOffsetDateTimeReq(int index, String... patterns) throws TemporalFormatException {
    String value = getStringReq(index);
    return (patterns.length > 0) ? ParseUtils.parseOffsetDateTime(value, patterns)
        : ParseUtils.parseOffsetDateTime(value, CsvUtils.PATTERNS_DATETIMEZ);
  }

  public LocalDate getLocalDate(int index, String... patterns) throws TemporalFormatException {
    String value = getString(index);
    if (value != null) {
      return (patterns.length > 0) ? ParseUtils.parseLocalDate(value, patterns)
          : ParseUtils.parseLocalDate(value, CsvUtils.PATTERNS_DATE);
    }
    return null;
  }

  public LocalTime getLocalTime(int index, String... patterns) throws TemporalFormatException {
    String value = getString(index);
    if (value != null) {
      return (patterns.length > 0) ? ParseUtils.parseLocalTime(value, patterns)
          : ParseUtils.parseLocalTime(value, CsvUtils.PATTERNS_TIME);
    }
    return null;
  }

  public LocalDateTime getLocalDateTime(int index, String... patterns) throws TemporalFormatException {
    String value = getString(index);
    if (value != null) {
      return (patterns.length > 0) ? ParseUtils.parseLocalDateTime(value, patterns)
          : ParseUtils.parseLocalDateTime(value, CsvUtils.PATTERNS_DATETIME);
    }
    return null;
  }

  public OffsetTime getOffsetTime(int index, String... patterns) throws TemporalFormatException {
    String value = getString(index);
    if (value != null) {
      return (patterns.length > 0) ? ParseUtils.parseOffsetTime(value, patterns)
          : ParseUtils.parseOffsetTime(value, CsvUtils.PATTERNS_TIMEZ);
    }
    return null;
  }

  public OffsetDateTime getOffsetDateTime(int index, String... patterns) throws TemporalFormatException {
    String value = getString(index);
    if (value != null) {
      return (patterns.length > 0) ? ParseUtils.parseOffsetDateTime(value, patterns)
          : ParseUtils.parseOffsetDateTime(value, CsvUtils.PATTERNS_DATETIMEZ);
    }
    return null;
  }

  public LocalDate getLocalDateReq(int index, Collection<String> patterns) throws TemporalFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseLocalDate(value, patterns);
  }

  public LocalTime getLocalTimeReq(int index, Collection<String> patterns) throws TemporalFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseLocalTime(value, patterns);
  }

  public LocalDateTime getLocalDateTimeReq(int index, Collection<String> patterns) throws TemporalFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseLocalDateTime(value, patterns);
  }

  public OffsetTime getOffsetTimeReq(int index, Collection<String> patterns) throws TemporalFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseOffsetTime(value, patterns);
  }

  public OffsetDateTime getOffsetDateTimeReq(int index, Collection<String> patterns) throws TemporalFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseOffsetDateTime(value, patterns);
  }

  public LocalDate getLocalDate(int index, Collection<String> patterns) throws TemporalFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseLocalDate(value, patterns) : null;
  }

  public LocalTime getLocalTime(int index, Collection<String> patterns) throws TemporalFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseLocalTime(value, patterns) : null;
  }

  public LocalDateTime getLocalDateTime(int index, Collection<String> patterns) throws TemporalFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseLocalDateTime(value, patterns) : null;
  }

  public OffsetTime getOffsetTime(int index, Collection<String> patterns) throws TemporalFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseOffsetTime(value, patterns) : null;
  }

  public OffsetDateTime getOffsetDateTime(int index, Collection<String> patterns) throws TemporalFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseOffsetDateTime(value, patterns) : null;
  }

  // Setters

  public CsvRecord set(int index, String value) {
    Objects.checkIndex(index, this.values.length);
    this.values[index] = value;
    return this;
  }

  public CsvRecord set(int index, boolean value) {
    return set(index, Boolean.toString(value));
  }

  public CsvRecord set(int index, byte value) {
    return set(index, Byte.toString(value));
  }

  public CsvRecord set(int index, short value) {
    return set(index, Short.toString(value));
  }

  public CsvRecord set(int index, int value) {
    return set(index, Integer.toString(value));
  }

  public CsvRecord set(int index, long value) {
    return set(index, Long.toString(value));
  }

  public CsvRecord set(int index, float value) {
    return set(index, Float.toString(value));
  }

  public CsvRecord set(int index, double value) {
    return set(index, Double.toString(value));
  }

  public CsvRecord set(int index, BigDecimal value) {
    return set(index, (value != null) ? value.toPlainString() : null);
  }

  public CsvRecord set(int index, Object value) {
    return set(index, (value != null) ? value.toString() : null);
  }

  public CsvRecord set(int index, LocalDate value) {
    return set(index, value, CsvUtils.getCsvDtPattern(DateUtils.ISO8601_DATE));
  }

  public CsvRecord set(int index, LocalTime value) {
    return set(index, value, CsvUtils.getCsvDtPattern(DateUtils.ISO8601_TIME_S));
  }

  public CsvRecord set(int index, LocalDateTime value) {
    return set(index, value, CsvUtils.getCsvDtPattern(DateUtils.ISO8601_DATETIME_S));
  }

  public CsvRecord set(int index, OffsetTime value) {
    return set(index, value, CsvUtils.getCsvDtPattern(DateUtils.ISO8601_TIMEZ_S));
  }

  public CsvRecord set(int index, OffsetDateTime value) {
    return set(index, value, CsvUtils.getCsvDtPattern(DateUtils.ISO8601_DATETIMEZ_S));
  }

  public CsvRecord set(int index, LocalDate value, String pattern) {
    return setTemporal(index, value, pattern);
  }

  public CsvRecord set(int index, LocalTime value, String pattern) {
    return setTemporal(index, value, pattern);
  }

  public CsvRecord set(int index, LocalDateTime value, String pattern) {
    return setTemporal(index, value, pattern);
  }

  public CsvRecord set(int index, OffsetTime value, String pattern) {
    return setTemporal(index, value, pattern);
  }

  public CsvRecord set(int index, OffsetDateTime value, String pattern) {
    return setTemporal(index, value, pattern);
  }

  private CsvRecord setTemporal(int index, Temporal value, String pattern) {
    Asserts.notNull(pattern);
    return set(index, (value != null) ? DateUtils.getFormatter(pattern).format(value) : null);
  }

  @Override
  public String toString() {
    return Arrays.stream(this.values).map(v -> CsvProcessor.INSTANCE.escape(v)).collect(Collectors.joining(","));
  }
}
