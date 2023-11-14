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
import com.appslandia.common.utils.TypeUtils;

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
  public static final String FLOAT1 = "Float1";
  public static final String FLOAT2 = "Float2";
  public static final String FLOAT3 = "Float3";
  public static final String FLOAT4 = "Float4";
  public static final String FLOAT5 = "Float5";
  public static final String FLOAT6 = "Float6";
  public static final String FLOAT7 = "Float7";
  public static final String FLOAT8 = "Float8";

  public static final String DOUBLE = "Double";
  public static final String DOUBLE1 = "Double1";
  public static final String DOUBLE2 = "Double2";
  public static final String DOUBLE3 = "Double3";
  public static final String DOUBLE4 = "Double4";
  public static final String DOUBLE5 = "Double5";
  public static final String DOUBLE6 = "Double6";
  public static final String DOUBLE7 = "Double7";
  public static final String DOUBLE8 = "Double8";

  public static final String BIGDECIMAL = "BigDecimal";
  public static final String BIGDECIMAL1 = "BigDecimal1";
  public static final String BIGDECIMAL2 = "BigDecimal2";
  public static final String BIGDECIMAL3 = "BigDecimal3";
  public static final String BIGDECIMAL4 = "BigDecimal4";
  public static final String BIGDECIMAL5 = "BigDecimal5";
  public static final String BIGDECIMAL6 = "BigDecimal6";
  public static final String BIGDECIMAL7 = "BigDecimal7";
  public static final String BIGDECIMAL8 = "BigDecimal8";

  public static final String LOCAL_DATE = "LocalDate";
  public static final String YEAR_MONTH = "YearMonth";

  public static final String LOCAL_TIME = "LocalTime";
  public static final String LOCAL_TIME_M = "LocalTimeM";
  public static final String LOCAL_TIME_S = "LocalTimeS";
  public static final String LOCAL_TIME_N1 = "LocalTimeN1";
  public static final String LOCAL_TIME_N2 = "LocalTimeN2";
  public static final String LOCAL_TIME_N3 = "LocalTimeN3";
  public static final String LOCAL_TIME_N4 = "LocalTimeN4";
  public static final String LOCAL_TIME_N5 = "LocalTimeN5";
  public static final String LOCAL_TIME_N6 = "LocalTimeN6";
  public static final String LOCAL_TIME_N7 = "LocalTimeN7";

  public static final String LOCAL_DATETIME = "LocalDateTime";
  public static final String LOCAL_DATETIME_M = "LocalDateTimeM";
  public static final String LOCAL_DATETIME_S = "LocalDateTimeS";
  public static final String LOCAL_DATETIME_N1 = "LocalDateTimeN1";
  public static final String LOCAL_DATETIME_N2 = "LocalDateTimeN2";
  public static final String LOCAL_DATETIME_N3 = "LocalDateTimeN3";
  public static final String LOCAL_DATETIME_N4 = "LocalDateTimeN4";
  public static final String LOCAL_DATETIME_N5 = "LocalDateTimeN5";
  public static final String LOCAL_DATETIME_N6 = "LocalDateTimeN6";
  public static final String LOCAL_DATETIME_N7 = "LocalDateTimeN7";

  public static final String OFFSET_TIME = "OffsetTime";
  public static final String OFFSET_TIME_M = "OffsetTimeM";
  public static final String OFFSET_TIME_S = "OffsetTimeS";
  public static final String OFFSET_TIME_N1 = "OffsetTimeN1";
  public static final String OFFSET_TIME_N2 = "OffsetTimeN2";
  public static final String OFFSET_TIME_N3 = "OffsetTimeN3";
  public static final String OFFSET_TIME_N4 = "OffsetTimeN4";
  public static final String OFFSET_TIME_N5 = "OffsetTimeN5";
  public static final String OFFSET_TIME_N6 = "OffsetTimeN6";
  public static final String OFFSET_TIME_N7 = "OffsetTimeN7";

  public static final String OFFSET_DATETIME = "OffsetDateTime";
  public static final String OFFSET_DATETIME_M = "OffsetDateTimeM";
  public static final String OFFSET_DATETIME_S = "OffsetDateTimeS";
  public static final String OFFSET_DATETIME_N1 = "OffsetDateTimeN1";
  public static final String OFFSET_DATETIME_N2 = "OffsetDateTimeN2";
  public static final String OFFSET_DATETIME_N3 = "OffsetDateTimeN3";
  public static final String OFFSET_DATETIME_N4 = "OffsetDateTimeN4";
  public static final String OFFSET_DATETIME_N5 = "OffsetDateTimeN5";
  public static final String OFFSET_DATETIME_N6 = "OffsetDateTimeN6";
  public static final String OFFSET_DATETIME_N7 = "OffsetDateTimeN7";

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

  public static String toConverterId(Class<?> targetType) {
    return TypeUtils.wrap(targetType).getSimpleName();
  }
}
