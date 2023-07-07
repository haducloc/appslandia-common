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
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public int length() {
	return this.values.length;
    }

    public String getString(int index) {
	Objects.checkIndex(index, this.values.length);
	return this.values[index];
    }

    public String getRequiredString(int index) {
	Objects.checkIndex(index, this.values.length);
	return Asserts.notNull(this.values[index]);
    }

    public int getInt(int index) {
	Objects.checkIndex(index, this.values.length);
	return ParseUtils.parseInt(this.values[index]);
    }

    public Integer getIntObj(int index) {
	Objects.checkIndex(index, this.values.length);
	return ParseUtils.parseIntObj(this.values[index]);
    }

    public long getLong(int index) {
	Objects.checkIndex(index, this.values.length);
	return ParseUtils.parseLong(this.values[index]);
    }

    public Long getLongObj(int index) {
	Objects.checkIndex(index, this.values.length);
	return ParseUtils.parseLongObj(this.values[index]);
    }

    public double getDouble(int index) {
	Objects.checkIndex(index, this.values.length);
	return ParseUtils.parseDouble(this.values[index]);
    }

    public Double getDoubleObj(int index) {
	Objects.checkIndex(index, this.values.length);
	return ParseUtils.parseDoubleObj(this.values[index]);
    }

    public boolean getBool(int index) {
	Objects.checkIndex(index, this.values.length);
	return ParseUtils.parseBool(this.values[index]);
    }

    public Boolean getBoolObj(int index) {
	Objects.checkIndex(index, this.values.length);
	return ParseUtils.parseBoolObj(this.values[index]);
    }

    public BigDecimal getDecimal(int index) {
	Objects.checkIndex(index, this.values.length);
	return ParseUtils.parseDecimal(this.values[index]);
    }

    public <T> T getValue(int index, Function<String, T> converter) {
	Objects.checkIndex(index, this.values.length);
	return ParseUtils.parseValue(this.values[index], converter);
    }

    @Override
    public String toString() {
	return Arrays.stream(this.values).map(v -> new CsvProcessor().separator(',').escape(v)).collect(Collectors.joining(","));
    }
}
