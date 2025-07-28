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

package com.appslandia.common.converters;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.TypeUtils;

/**
 *
 * @author Loc Ha
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
  public static final String LOCAL_TIME_F1 = "LocalTimeF1";
  public static final String LOCAL_TIME_F2 = "LocalTimeF2";
  public static final String LOCAL_TIME_F3 = "LocalTimeF3";
  public static final String LOCAL_TIME_F4 = "LocalTimeF4";
  public static final String LOCAL_TIME_F5 = "LocalTimeF5";
  public static final String LOCAL_TIME_F6 = "LocalTimeF6";
  public static final String LOCAL_TIME_F7 = "LocalTimeF7";

  public static final String LOCAL_DATETIME = "LocalDateTime";
  public static final String LOCAL_DATETIME_M = "LocalDateTimeM";
  public static final String LOCAL_DATETIME_S = "LocalDateTimeS";
  public static final String LOCAL_DATETIME_F1 = "LocalDateTimeF1";
  public static final String LOCAL_DATETIME_F2 = "LocalDateTimeF2";
  public static final String LOCAL_DATETIME_F3 = "LocalDateTimeF3";
  public static final String LOCAL_DATETIME_F4 = "LocalDateTimeF4";
  public static final String LOCAL_DATETIME_F5 = "LocalDateTimeF5";
  public static final String LOCAL_DATETIME_F6 = "LocalDateTimeF6";
  public static final String LOCAL_DATETIME_F7 = "LocalDateTimeF7";

  public static final String OFFSET_TIME = "OffsetTime";
  public static final String OFFSET_TIME_M = "OffsetTimeM";
  public static final String OFFSET_TIME_S = "OffsetTimeS";
  public static final String OFFSET_TIME_F1 = "OffsetTimeF1";
  public static final String OFFSET_TIME_F2 = "OffsetTimeF2";
  public static final String OFFSET_TIME_F3 = "OffsetTimeF3";
  public static final String OFFSET_TIME_F4 = "OffsetTimeF4";
  public static final String OFFSET_TIME_F5 = "OffsetTimeF5";
  public static final String OFFSET_TIME_F6 = "OffsetTimeF6";
  public static final String OFFSET_TIME_F7 = "OffsetTimeF7";

  public static final String OFFSET_DATETIME = "OffsetDateTime";
  public static final String OFFSET_DATETIME_M = "OffsetDateTimeM";
  public static final String OFFSET_DATETIME_S = "OffsetDateTimeS";
  public static final String OFFSET_DATETIME_F1 = "OffsetDateTimeF1";
  public static final String OFFSET_DATETIME_F2 = "OffsetDateTimeF2";
  public static final String OFFSET_DATETIME_F3 = "OffsetDateTimeF3";
  public static final String OFFSET_DATETIME_F4 = "OffsetDateTimeF4";
  public static final String OFFSET_DATETIME_F5 = "OffsetDateTimeF5";
  public static final String OFFSET_DATETIME_F6 = "OffsetDateTimeF6";
  public static final String OFFSET_DATETIME_F7 = "OffsetDateTimeF7";

  public static final String BOOLEAN = "Boolean";
  public static final String UUID = "UUID";

  public static final String STRING = "String";
  public static final String STRING_UPPER = "StringUpper";
  public static final String STRING_LOWER = "StringLower";

  public static final String TAG = "Tag";
  public static final String TAGS = "Tags";
  public static final String DB_TAGS = "DbTags";

  public static final String KEYWORDS = "Keywords";
  public static final String TEXT = "Text";

  public static final String USER_ROLES = "UserRoles";
  public static final String PHONE_NUMBER = "PhoneNumber";

  default String getErrorMsgKey() {
    return getClass().getName() + ".message";
  }

  Class<T> getTargetType();

  String format(T obj, FormatProvider formatProvider, boolean localize);

  T parse(String str, FormatProvider formatProvider) throws ConverterException;

  default ConverterException toParsingError(String str, String targetName) {
    return new ConverterException(STR.fmt("An error occurred while parsing '{}' to {}.", str, targetName),
        getErrorMsgKey());
  }

  public static String toConverterId(Class<?> targetType) {
    return TypeUtils.wrap(targetType).getSimpleName();
  }
}
