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
import com.appslandia.common.base.DateFormatException;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ParseUtils;
import com.appslandia.common.utils.STR;

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

  public String getStringReq(int index) {
    String value = getString(index);
    if (value == null) {
      throw new AssertException(STR.fmt("The value read under the index '{}' must be not null.", index));
    }
    return value;
  }

  public String getString(int index) {
    Objects.checkIndex(index, this.values.length);
    return this.values[index];
  }

  public String getString(int index, String defaultValue) {
    String value = getString(index);
    return (value != null) ? value : defaultValue;
  }

  public String getStringUC(int index) {
    String value = getString(index);
    return (value != null) ? value.toUpperCase(Locale.ROOT) : null;
  }

  public String getStringUC(int index, String valueIfNull) {
    Asserts.notNull(valueIfNull);

    String value = getString(index);
    return (value != null) ? value.toUpperCase(Locale.ROOT) : valueIfNull.toUpperCase(Locale.ROOT);
  }

  public String getStringUCReq(int index) {
    String value = getStringReq(index);
    return value.toUpperCase(Locale.ROOT);
  }

  public String getStringLC(int index) {
    String value = getString(index);
    return (value != null) ? value.toLowerCase(Locale.ROOT) : null;
  }

  public String getStringLC(int index, String valueIfNull) {
    Asserts.notNull(valueIfNull);

    String value = getString(index);
    return (value != null) ? value.toLowerCase(Locale.ROOT) : valueIfNull.toLowerCase(Locale.ROOT);
  }

  public String getStringLCReq(int index) {
    String value = getStringReq(index);
    return value.toLowerCase(Locale.ROOT);
  }

  public boolean getBool(int index) throws BoolFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseBool(value);
  }

  public boolean getBool(int index, boolean defaultValIfInvalid) {
    String value = getString(index);
    return ParseUtils.parseBool(value, defaultValIfInvalid);
  }

  public Boolean getBoolOpt(int index) throws BoolFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseBool(value) : null;
  }

  public byte getByte(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseByte(value);
  }

  public byte getByte(int index, byte defaultValIfInvalid) {
    String value = getString(index);
    return ParseUtils.parseByte(value, defaultValIfInvalid);
  }

  public Byte getByteOpt(int index) throws NumberFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseByte(value) : null;
  }

  public short getShort(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseShort(value);
  }

  public short getShort(int index, short defaultValIfInvalid) {
    String value = getString(index);
    return ParseUtils.parseShort(value, defaultValIfInvalid);
  }

  public Short getShortOpt(int index) throws NumberFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseShort(value) : null;
  }

  public int getInt(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseInt(value);
  }

  public int getInt(int index, int defaultValIfInvalid) {
    String value = getString(index);
    return ParseUtils.parseInt(value, defaultValIfInvalid);
  }

  public Integer getIntOpt(int index) throws NumberFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseInt(value) : null;
  }

  public long getLong(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseLong(value);
  }

  public long getLong(int index, long defaultValIfInvalid) {
    String value = getString(index);
    return ParseUtils.parseLong(value, defaultValIfInvalid);
  }

  public Long getLongOpt(int index) throws NumberFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseLong(value) : null;
  }

  public float getFloat(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseFloat(value);
  }

  public float getFloat(int index, float defaultValIfInvalid) {
    String value = getString(index);
    return ParseUtils.parseFloat(value, defaultValIfInvalid);
  }

  public Float getFloatOpt(int index) throws NumberFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseFloat(value) : null;
  }

  public double getDouble(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return ParseUtils.parseDouble(value);
  }

  public double getDouble(int index, double defaultValIfInvalid) {
    String value = getString(index);
    return ParseUtils.parseDouble(value, defaultValIfInvalid);
  }

  public Double getDoubleOpt(int index) throws NumberFormatException {
    String value = getString(index);
    return (value != null) ? ParseUtils.parseDouble(value) : null;
  }

  public BigDecimal getDecimalReq(int index) throws NumberFormatException {
    String value = getStringReq(index);
    return new BigDecimal(value);
  }

  public BigDecimal getDecimal(int index) throws NumberFormatException {
    String value = getString(index);
    return (value != null) ? new BigDecimal(value) : null;
  }

  public BigDecimal getDecimal(int index, double defaultValIfInvalid) {
    String value = getString(index);
    return ParseUtils.parseDecimal(value, defaultValIfInvalid);
  }

  // Temporal Types

  public LocalDate getLocalDateReq(int index, String... patterns) throws DateFormatException {
    String value = getStringReq(index);
    return (patterns.length > 0) ? ParseUtils.parseLocalDate(value, patterns)
        : ParseUtils.parseLocalDate(value, CsvUtils.PATTERNS_DATE);
  }

  public LocalDate getLocalDate(int index, String... patterns) throws DateFormatException {
    String value = getString(index);
    if (value != null) {
      return (patterns.length > 0) ? ParseUtils.parseLocalDate(value, patterns)
          : ParseUtils.parseLocalDate(value, CsvUtils.PATTERNS_DATE);
    }
    return null;
  }

  public LocalTime getLocalTimeReq(int index, String... patterns) throws DateFormatException {
    String value = getStringReq(index);
    return (patterns.length > 0) ? ParseUtils.parseLocalTime(value, patterns)
        : ParseUtils.parseLocalTime(value, CsvUtils.PATTERNS_TIME);
  }

  public LocalTime getLocalTime(int index, String... patterns) throws DateFormatException {
    String value = getString(index);
    if (value != null) {
      return (patterns.length > 0) ? ParseUtils.parseLocalTime(value, patterns)
          : ParseUtils.parseLocalTime(value, CsvUtils.PATTERNS_TIME);
    }
    return null;
  }

  public LocalDateTime getLocalDateTimeReq(int index, String... patterns) throws DateFormatException {
    String value = getStringReq(index);
    return (patterns.length > 0) ? ParseUtils.parseLocalDateTime(value, patterns)
        : ParseUtils.parseLocalDateTime(value, CsvUtils.PATTERNS_DATETIME);
  }

  public LocalDateTime getLocalDateTime(int index, String... patterns) throws DateFormatException {
    String value = getString(index);
    if (value != null) {
      return (patterns.length > 0) ? ParseUtils.parseLocalDateTime(value, patterns)
          : ParseUtils.parseLocalDateTime(value, CsvUtils.PATTERNS_DATETIME);
    }
    return null;
  }

  public OffsetTime getOffsetTimeReq(int index, String... patterns) throws DateFormatException {
    String value = getStringReq(index);
    return (patterns.length > 0) ? ParseUtils.parseOffsetTime(value, patterns)
        : ParseUtils.parseOffsetTime(value, CsvUtils.PATTERNS_TIMEZ);
  }

  public OffsetTime getOffsetTime(int index, String... patterns) throws DateFormatException {
    String value = getString(index);
    if (value != null) {
      return (patterns.length > 0) ? ParseUtils.parseOffsetTime(value, patterns)
          : ParseUtils.parseOffsetTime(value, CsvUtils.PATTERNS_TIMEZ);
    }
    return null;
  }

  public OffsetDateTime getOffsetDateTimeReq(int index, String... patterns) throws DateFormatException {
    String value = getStringReq(index);
    return (patterns.length > 0) ? ParseUtils.parseOffsetDateTime(value, patterns)
        : ParseUtils.parseOffsetDateTime(value, CsvUtils.PATTERNS_DATETIMEZ);
  }

  public OffsetDateTime getOffsetDateTime(int index, String... patterns) throws DateFormatException {
    String value = getString(index);
    if (value != null) {
      return (patterns.length > 0) ? ParseUtils.parseOffsetDateTime(value, patterns)
          : ParseUtils.parseOffsetDateTime(value, CsvUtils.PATTERNS_DATETIMEZ);
    }
    return null;
  }

  public LocalDate getLocalDateReq(int index, Collection<String> patterns) throws DateFormatException {
    Asserts.hasElements(patterns);

    String value = getStringReq(index);
    return ParseUtils.parseLocalDate(value, patterns);
  }

  public LocalDate getLocalDate(int index, Collection<String> patterns) throws DateFormatException {
    Asserts.hasElements(patterns);

    String value = getString(index);
    return (value != null) ? ParseUtils.parseLocalDate(value, patterns) : null;
  }

  public LocalTime getLocalTimeReq(int index, Collection<String> patterns) throws DateFormatException {
    Asserts.hasElements(patterns);

    String value = getStringReq(index);
    return ParseUtils.parseLocalTime(value, patterns);
  }

  public LocalTime getLocalTime(int index, Collection<String> patterns) throws DateFormatException {
    Asserts.hasElements(patterns);

    String value = getString(index);
    return (value != null) ? ParseUtils.parseLocalTime(value, patterns) : null;
  }

  public LocalDateTime getLocalDateTimeReq(int index, Collection<String> patterns) throws DateFormatException {
    Asserts.hasElements(patterns);

    String value = getStringReq(index);
    return ParseUtils.parseLocalDateTime(value, patterns);
  }

  public LocalDateTime getLocalDateTime(int index, Collection<String> patterns) throws DateFormatException {
    Asserts.hasElements(patterns);

    String value = getString(index);
    return (value != null) ? ParseUtils.parseLocalDateTime(value, patterns) : null;
  }

  public OffsetTime getOffsetTimeReq(int index, Collection<String> patterns) throws DateFormatException {
    Asserts.hasElements(patterns);

    String value = getStringReq(index);
    return ParseUtils.parseOffsetTime(value, patterns);
  }

  public OffsetTime getOffsetTime(int index, Collection<String> patterns) throws DateFormatException {
    Asserts.hasElements(patterns);

    String value = getString(index);
    return (value != null) ? ParseUtils.parseOffsetTime(value, patterns) : null;
  }

  public OffsetDateTime getOffsetDateTimeReq(int index, Collection<String> patterns) throws DateFormatException {
    Asserts.hasElements(patterns);

    String value = getStringReq(index);
    return ParseUtils.parseOffsetDateTime(value, patterns);
  }

  public OffsetDateTime getOffsetDateTime(int index, Collection<String> patterns) throws DateFormatException {
    Asserts.hasElements(patterns);

    String value = getString(index);
    return (value != null) ? ParseUtils.parseOffsetDateTime(value, patterns) : null;
  }

  // Setters

  public CsvRecord set(int index, String value) {
    Objects.checkIndex(index, this.values.length);
    this.values[index] = (value != null) ? value : null;
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
