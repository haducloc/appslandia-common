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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class ConverterProvider extends InitializeObject {

  private Map<String, Converter<?>> converters = new HashMap<>();

  @Override
  protected void init() throws Exception {
    this.converters.putIfAbsent(Converter.BYTE, new ByteConverter());
    this.converters.putIfAbsent(Converter.SHORT, new ShortConverter());
    this.converters.putIfAbsent(Converter.INTEGER, new IntegerConverter());
    this.converters.putIfAbsent(Converter.LONG, new LongConverter());

    this.converters.putIfAbsent(Converter.FLOAT, new FloatConverter());
    this.converters.putIfAbsent(Converter.FLOAT1, new FloatConverter(1));
    this.converters.putIfAbsent(Converter.FLOAT2, new FloatConverter(2));
    this.converters.putIfAbsent(Converter.FLOAT3, new FloatConverter(3));
    this.converters.putIfAbsent(Converter.FLOAT4, new FloatConverter(4));
    this.converters.putIfAbsent(Converter.FLOAT5, new FloatConverter(5));
    this.converters.putIfAbsent(Converter.FLOAT6, new FloatConverter(6));
    this.converters.putIfAbsent(Converter.FLOAT7, new FloatConverter(7));
    this.converters.putIfAbsent(Converter.FLOAT8, new FloatConverter(8));

    this.converters.putIfAbsent(Converter.DOUBLE, new DoubleConverter());
    this.converters.putIfAbsent(Converter.DOUBLE1, new DoubleConverter(1));
    this.converters.putIfAbsent(Converter.DOUBLE2, new DoubleConverter(2));
    this.converters.putIfAbsent(Converter.DOUBLE3, new DoubleConverter(3));
    this.converters.putIfAbsent(Converter.DOUBLE4, new DoubleConverter(4));
    this.converters.putIfAbsent(Converter.DOUBLE5, new DoubleConverter(5));
    this.converters.putIfAbsent(Converter.DOUBLE6, new DoubleConverter(6));
    this.converters.putIfAbsent(Converter.DOUBLE7, new DoubleConverter(7));
    this.converters.putIfAbsent(Converter.DOUBLE8, new DoubleConverter(8));

    this.converters.putIfAbsent(Converter.BIGDECIMAL, new BigDecimalConverter());
    this.converters.putIfAbsent(Converter.BIGDECIMAL1, new BigDecimalConverter(1));
    this.converters.putIfAbsent(Converter.BIGDECIMAL2, new BigDecimalConverter(2));
    this.converters.putIfAbsent(Converter.BIGDECIMAL3, new BigDecimalConverter(3));
    this.converters.putIfAbsent(Converter.BIGDECIMAL4, new BigDecimalConverter(4));
    this.converters.putIfAbsent(Converter.BIGDECIMAL5, new BigDecimalConverter(5));
    this.converters.putIfAbsent(Converter.BIGDECIMAL6, new BigDecimalConverter(6));
    this.converters.putIfAbsent(Converter.BIGDECIMAL7, new BigDecimalConverter(7));
    this.converters.putIfAbsent(Converter.BIGDECIMAL8, new BigDecimalConverter(8));

    this.converters.putIfAbsent(Converter.BOOLEAN, new BooleanConverter());
    this.converters.putIfAbsent(Converter.UUID, new UUIDConverter());

    this.converters.putIfAbsent(Converter.STRING, new StringConverter());
    this.converters.putIfAbsent(Converter.STRING_UPPER, new ULStringConverter(true, Locale.ROOT));
    this.converters.putIfAbsent(Converter.STRING_LOWER, new ULStringConverter(false, Locale.ROOT));

    this.converters.putIfAbsent(Converter.KEYWORDS, new KeywordsConverter());
    this.converters.putIfAbsent(Converter.TEXT, new TextConverter());
    this.converters.putIfAbsent(Converter.USER_ROLES, new UserRolesConverter());
    this.converters.putIfAbsent(Converter.PHONE_NUMBER, new PhoneNumberConverter());

    // Java8 Date/Time
    this.converters.putIfAbsent(Converter.LOCAL_DATE, new LocalDateConverter());
    this.converters.putIfAbsent(Converter.YEAR_MONTH, new YearMonthConverter());

    this.converters.putIfAbsent(Converter.LOCAL_TIME, new LocalTimeConverter());
    this.converters.putIfAbsent(Converter.LOCAL_TIME_M, new LocalTimeConverter(DateUtils.ISO8601_TIME_M));
    this.converters.putIfAbsent(Converter.LOCAL_TIME_S, new LocalTimeConverter(DateUtils.ISO8601_TIME_S));
    this.converters.putIfAbsent(Converter.LOCAL_TIME_F1, new LocalTimeConverter(DateUtils.ISO8601_TIME_F1));
    this.converters.putIfAbsent(Converter.LOCAL_TIME_F2, new LocalTimeConverter(DateUtils.ISO8601_TIME_F2));
    this.converters.putIfAbsent(Converter.LOCAL_TIME_F3, new LocalTimeConverter(DateUtils.ISO8601_TIME_F3));
    this.converters.putIfAbsent(Converter.LOCAL_TIME_F4, new LocalTimeConverter(DateUtils.ISO8601_TIME_F4));
    this.converters.putIfAbsent(Converter.LOCAL_TIME_F5, new LocalTimeConverter(DateUtils.ISO8601_TIME_F5));
    this.converters.putIfAbsent(Converter.LOCAL_TIME_F6, new LocalTimeConverter(DateUtils.ISO8601_TIME_F6));
    this.converters.putIfAbsent(Converter.LOCAL_TIME_F7, new LocalTimeConverter(DateUtils.ISO8601_TIME_F7));

    this.converters.putIfAbsent(Converter.LOCAL_DATETIME, new LocalDateTimeConverter());
    this.converters.putIfAbsent(Converter.LOCAL_DATETIME_M, new LocalDateTimeConverter(DateUtils.ISO8601_DATETIME_M));
    this.converters.putIfAbsent(Converter.LOCAL_DATETIME_S, new LocalDateTimeConverter(DateUtils.ISO8601_DATETIME_S));
    this.converters.putIfAbsent(Converter.LOCAL_DATETIME_F1, new LocalDateTimeConverter(DateUtils.ISO8601_DATETIME_F1));
    this.converters.putIfAbsent(Converter.LOCAL_DATETIME_F2, new LocalDateTimeConverter(DateUtils.ISO8601_DATETIME_F2));
    this.converters.putIfAbsent(Converter.LOCAL_DATETIME_F3, new LocalDateTimeConverter(DateUtils.ISO8601_DATETIME_F3));
    this.converters.putIfAbsent(Converter.LOCAL_DATETIME_F4, new LocalDateTimeConverter(DateUtils.ISO8601_DATETIME_F4));
    this.converters.putIfAbsent(Converter.LOCAL_DATETIME_F5, new LocalDateTimeConverter(DateUtils.ISO8601_DATETIME_F5));
    this.converters.putIfAbsent(Converter.LOCAL_DATETIME_F6, new LocalDateTimeConverter(DateUtils.ISO8601_DATETIME_F6));
    this.converters.putIfAbsent(Converter.LOCAL_DATETIME_F7, new LocalDateTimeConverter(DateUtils.ISO8601_DATETIME_F7));

    this.converters.putIfAbsent(Converter.OFFSET_TIME, new OffsetTimeConverter());
    this.converters.putIfAbsent(Converter.OFFSET_TIME_M, new OffsetTimeConverter(DateUtils.ISO8601_TIMEZ_M));
    this.converters.putIfAbsent(Converter.OFFSET_TIME_S, new OffsetTimeConverter(DateUtils.ISO8601_TIMEZ_S));
    this.converters.putIfAbsent(Converter.OFFSET_TIME_F1, new OffsetTimeConverter(DateUtils.ISO8601_TIMEZ_F1));
    this.converters.putIfAbsent(Converter.OFFSET_TIME_F2, new OffsetTimeConverter(DateUtils.ISO8601_TIMEZ_F2));
    this.converters.putIfAbsent(Converter.OFFSET_TIME_F3, new OffsetTimeConverter(DateUtils.ISO8601_TIMEZ_F3));
    this.converters.putIfAbsent(Converter.OFFSET_TIME_F4, new OffsetTimeConverter(DateUtils.ISO8601_TIMEZ_F4));
    this.converters.putIfAbsent(Converter.OFFSET_TIME_F5, new OffsetTimeConverter(DateUtils.ISO8601_TIMEZ_F5));
    this.converters.putIfAbsent(Converter.OFFSET_TIME_F6, new OffsetTimeConverter(DateUtils.ISO8601_TIMEZ_F6));
    this.converters.putIfAbsent(Converter.OFFSET_TIME_F7, new OffsetTimeConverter(DateUtils.ISO8601_TIMEZ_F7));

    this.converters.putIfAbsent(Converter.OFFSET_DATETIME, new OffsetDateTimeConverter());
    this.converters.putIfAbsent(Converter.OFFSET_DATETIME_M,
        new OffsetDateTimeConverter(DateUtils.ISO8601_DATETIMEZ_M));
    this.converters.putIfAbsent(Converter.OFFSET_DATETIME_S,
        new OffsetDateTimeConverter(DateUtils.ISO8601_DATETIMEZ_S));
    this.converters.putIfAbsent(Converter.OFFSET_DATETIME_F1,
        new OffsetDateTimeConverter(DateUtils.ISO8601_DATETIMEZ_F1));
    this.converters.putIfAbsent(Converter.OFFSET_DATETIME_F2,
        new OffsetDateTimeConverter(DateUtils.ISO8601_DATETIMEZ_F2));
    this.converters.putIfAbsent(Converter.OFFSET_DATETIME_F3,
        new OffsetDateTimeConverter(DateUtils.ISO8601_DATETIMEZ_F3));
    this.converters.putIfAbsent(Converter.OFFSET_DATETIME_F4,
        new OffsetDateTimeConverter(DateUtils.ISO8601_DATETIMEZ_F4));
    this.converters.putIfAbsent(Converter.OFFSET_DATETIME_F5,
        new OffsetDateTimeConverter(DateUtils.ISO8601_DATETIMEZ_F5));
    this.converters.putIfAbsent(Converter.OFFSET_DATETIME_F6,
        new OffsetDateTimeConverter(DateUtils.ISO8601_DATETIMEZ_F6));
    this.converters.putIfAbsent(Converter.OFFSET_DATETIME_F7,
        new OffsetDateTimeConverter(DateUtils.ISO8601_DATETIMEZ_F7));

    this.converters = Collections.unmodifiableMap(this.converters);
  }

  public void registerConverter(String converterId, Converter<?> converter) {
    this.assertNotInitialized();
    this.converters.put(converterId, converter);
  }

  @SuppressWarnings("unchecked")
  public <T> Converter<T> getConverter(Class<?> targetType) throws IllegalArgumentException {
    this.initialize();
    var converterId = Converter.toConverterId(targetType);
    var impl = (Converter<T>) this.converters.get(converterId);
    return Arguments.notNull(impl, "No Converter is registered for type '{}'.", targetType);
  }

  @SuppressWarnings("unchecked")
  public <T> Converter<T> getConverter(String converterId) throws IllegalArgumentException {
    this.initialize();
    var impl = (Converter<T>) this.converters.get(converterId);
    return Arguments.notNull(impl, "No Converter is registered for id '{}'.", converterId);
  }

  public boolean hasConverter(Class<?> targetType) {
    this.initialize();
    var converterId = Converter.toConverterId(targetType);
    return hasConverter(converterId);
  }

  public boolean hasConverter(String converterId) {
    this.initialize();
    return this.converters.containsKey(converterId);
  }
}
