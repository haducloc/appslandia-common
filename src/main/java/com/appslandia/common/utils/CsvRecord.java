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
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.appslandia.common.base.AssertException;
import com.appslandia.common.base.BoolFormatException;
import com.appslandia.common.base.DateFormatException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvRecord {

    final String[] values;

    public CsvRecord(int fieldCount) {
	this.values = new String[fieldCount];
    }

    public CsvRecord(String[] fieldValues) {
	this.values = Asserts.notNull(fieldValues);
    }

    public void process(int[] indexes, Function<String, String> processor) {
	Asserts.notNull(processor);

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
	return (value != null) ? value.toUpperCase(Locale.ENGLISH) : null;
    }

    public String getStringUC(int index, String valueIfNull) {
	Asserts.notNull(valueIfNull);

	String value = getString(index);
	return (value != null) ? value.toUpperCase(Locale.ENGLISH) : valueIfNull.toUpperCase(Locale.ENGLISH);
    }

    public String getStringUCReq(int index) {
	String value = getStringReq(index);
	return value.toUpperCase(Locale.ENGLISH);
    }

    public String getStringLC(int index) {
	String value = getString(index);
	return (value != null) ? value.toLowerCase(Locale.ENGLISH) : null;
    }

    public String getStringLC(int index, String valueIfNull) {
	Asserts.notNull(valueIfNull);

	String value = getString(index);
	return (value != null) ? value.toLowerCase(Locale.ENGLISH) : valueIfNull.toLowerCase(Locale.ENGLISH);
    }

    public String getStringLCReq(int index) {
	String value = getStringReq(index);
	return value.toLowerCase(Locale.ENGLISH);
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

    public Date getDateReq(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getStringReq(index);
	return ParseUtils.parseDate(value, patterns);
    }

    public Date getDate(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getString(index);
	return (value != null) ? ParseUtils.parseDate(value, patterns) : null;
    }

    public LocalDate getLocalDateReq(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getStringReq(index);
	return ParseUtils.parseLocalDate(value, patterns);
    }

    public LocalDate getLocalDate(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getString(index);
	return (value != null) ? ParseUtils.parseLocalDate(value, patterns) : null;
    }

    public LocalDateTime getLocalDateTimeReq(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getStringReq(index);
	return ParseUtils.parseLocalDateTime(value, patterns);
    }

    public LocalDateTime getLocalDateTime(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getString(index);
	return (value != null) ? ParseUtils.parseLocalDateTime(value, patterns) : null;
    }

    public LocalTime getLocalTimeReq(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getStringReq(index);
	return ParseUtils.parseLocalTime(value, patterns);
    }

    public LocalTime getLocalTime(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getString(index);
	return (value != null) ? ParseUtils.parseLocalTime(value, patterns) : null;
    }

    public OffsetDateTime getOffsetDateTimeReq(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getStringReq(index);
	return ParseUtils.parseOffsetDateTime(value, patterns);
    }

    public OffsetDateTime getOffsetDateTime(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getString(index);
	return (value != null) ? ParseUtils.parseOffsetDateTime(value, patterns) : null;
    }

    public OffsetTime getOffsetTimeReq(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getStringReq(index);
	return ParseUtils.parseOffsetTime(value, patterns);
    }

    public OffsetTime getOffsetTime(int index, String... patterns) throws DateFormatException {
	Asserts.hasElements(patterns);

	String value = getString(index);
	return (value != null) ? ParseUtils.parseOffsetTime(value, patterns) : null;
    }

    public CsvRecord set(int index, Object value) {
	Objects.checkIndex(index, this.values.length);
	this.values[index] = (value != null) ? value.toString() : null;
	return this;
    }

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

    public CsvRecord set(int index, Date value, String pattern) {
	return set(index, (value != null) ? DateUtils.newDateFormat(pattern).format(value) : null);
    }

    public CsvRecord set(int index, Temporal value, String pattern) {
	return set(index, (value != null) ? DateUtils.getFormatter(pattern).format(value) : null);
    }

    @Override
    public String toString() {
	return Arrays.stream(this.values).map(v -> CsvProcessor.INSTANCE.escape(v)).collect(Collectors.joining(","));
    }
}
