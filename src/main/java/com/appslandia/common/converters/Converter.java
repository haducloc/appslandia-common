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

package com.appslandia.common.converters;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public interface Converter<T> {
    public static final String BYTE = "Byte";
    public static final String SHORT = "Short";
    public static final String INTEGER = "Integer";
    public static final String LONG = "Long";

    public static final String FLOAT = "Float";
    public static final String DOUBLE = "Double";
    public static final String BIGDECIMAL = "BigDecimal";

    public static final String FLOAT3 = "Float3";
    public static final String DOUBLE3 = "Double3";
    public static final String BIGDECIMAL3 = "BigDecimal3";

    public static final String FLOAT6 = "Float6";
    public static final String DOUBLE6 = "Double6";
    public static final String BIGDECIMAL6 = "BigDecimal6";

    public static final String DATE = "Date";

    public static final String TIME = "Time";
    public static final String TIME_M = "TimeM";
    public static final String TIME_S = "TimeS";

    public static final String TIMESTAMP = "Timestamp";
    public static final String TIMESTAMP_M = "TimestampM";
    public static final String TIMESTAMP_S = "TimestampS";

    public static final String LOCAL_DATE = "LocalDate";
    public static final String YEAR_MONTH = "YearMonth";

    public static final String LOCAL_TIME = "LocalTime";
    public static final String LOCAL_TIME_M = "LocalTimeM";
    public static final String LOCAL_TIME_S = "LocalTimeS";

    public static final String LOCAL_DATETIME = "LocalDateTime";
    public static final String LOCAL_DATETIME_M = "LocalDateTimeM";
    public static final String LOCAL_DATETIME_S = "LocalDateTimeS";

    public static final String OFFSET_TIME = "OffsetTime";
    public static final String OFFSET_TIME_M = "OffsetTimeM";
    public static final String OFFSET_TIME_S = "OffsetTimeS";

    public static final String OFFSET_DATETIME = "OffsetDateTime";
    public static final String OFFSET_DATETIME_M = "OffsetDateTimeM";
    public static final String OFFSET_DATETIME_S = "OffsetDateTimeS";

    public static final String BOOLEAN = "Boolean";

    public static final String STRING = "String";
    public static final String STRING_UC = "StringUC";
    public static final String STRING_LC = "StringLC";

    public static final String TAG = "Tag";
    public static final String TAGS = "Tags";
    public static final String DB_TAGS = "DbTags";

    public static final String KEYWORDS = "Keywords";
    public static final String TEXT = "Text";

    default String getErrorMsgKey() {
	return getClass().getName() + ".message";
    }

    Class<T> getTargetType();

    String format(T obj, FormatProvider formatProvider, boolean localize);

    T parse(String str, FormatProvider formatProvider) throws ConverterException;

    default ConverterException toParsingError(String str, String targetName) {
	return new ConverterException(STR.fmt("An error occurred while parsing '{}' to {}.", str, targetName), getErrorMsgKey());
    }

    default ConverterException toNumberOverflowError(String str) {
	return new ConverterException(STR.fmt("A number overflow occurred while parsing '{}' to {}.", str, getTargetType().getName()), getErrorMsgKey());
    }
}
