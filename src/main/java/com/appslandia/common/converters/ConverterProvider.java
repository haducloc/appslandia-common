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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.TypeUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
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
	this.converters.putIfAbsent(Converter.DOUBLE, new DoubleConverter());
	this.converters.putIfAbsent(Converter.BIGDECIMAL, new BigDecimalConverter());

	for (int fractionDigits = 1; fractionDigits <= 8; fractionDigits++) {
	    this.converters.putIfAbsent(Converter.FLOAT + fractionDigits, new FloatConverter(fractionDigits));
	    this.converters.putIfAbsent(Converter.DOUBLE + fractionDigits, new DoubleConverter(fractionDigits));
	    this.converters.putIfAbsent(Converter.BIGDECIMAL + fractionDigits, new BigDecimalConverter(fractionDigits));
	}
	this.converters.putIfAbsent(Converter.BOOLEAN, new BooleanConverter());

	this.converters.putIfAbsent(Converter.STRING, new StringConverter());
	this.converters.putIfAbsent(Converter.STRING_UC, new ULCStringConverter(true, Locale.ROOT));
	this.converters.putIfAbsent(Converter.STRING_LC, new ULCStringConverter(false, Locale.ROOT));

	this.converters.putIfAbsent(Converter.TAG, new TagConverter());
	this.converters.putIfAbsent(Converter.TAGS, new TagsConverter());
	this.converters.putIfAbsent(Converter.DB_TAGS, new DbTagsConverter());

	this.converters.putIfAbsent(Converter.KEYWORDS, new KeywordsConverter());
	this.converters.putIfAbsent(Converter.TEXT, new TextConverter());

	// Date/Time
	this.converters.putIfAbsent(Converter.DATE, new SqlDateConverter());

	this.converters.putIfAbsent(Converter.TIME, new SqlTimeConverter());
	this.converters.putIfAbsent(Converter.TIME_M, new SqlTimeConverter(DateUtils.ISO8601_TIME_M));
	this.converters.putIfAbsent(Converter.TIME_S, new SqlTimeConverter(DateUtils.ISO8601_TIME_S));

	this.converters.putIfAbsent(Converter.TIMESTAMP, new SqlTimestampConverter());
	this.converters.putIfAbsent(Converter.TIMESTAMP_M, new SqlTimestampConverter(DateUtils.ISO8601_DATETIME_M));
	this.converters.putIfAbsent(Converter.TIMESTAMP_S, new SqlTimestampConverter(DateUtils.ISO8601_DATETIME_S));

	// Java8 Date/Time
	this.converters.putIfAbsent(Converter.LOCAL_DATE, new LocalDateConverter());
	this.converters.putIfAbsent(Converter.YEAR_MONTH, new YearMonthConverter());

	this.converters.putIfAbsent(Converter.LOCAL_TIME, new LocalTimeConverter());
	this.converters.putIfAbsent(Converter.LOCAL_TIME_M, new LocalTimeConverter(DateUtils.ISO8601_TIME_M));
	this.converters.putIfAbsent(Converter.LOCAL_TIME_S, new LocalTimeConverter(DateUtils.ISO8601_TIME_S));

	this.converters.putIfAbsent(Converter.LOCAL_DATETIME, new LocalDateTimeConverter());
	this.converters.putIfAbsent(Converter.LOCAL_DATETIME_M, new LocalDateTimeConverter(DateUtils.ISO8601_DATETIME_M));
	this.converters.putIfAbsent(Converter.LOCAL_DATETIME_S, new LocalDateTimeConverter(DateUtils.ISO8601_DATETIME_S));

	this.converters.putIfAbsent(Converter.OFFSET_TIME, new OffsetTimeConverter());
	this.converters.putIfAbsent(Converter.OFFSET_TIME_M, new OffsetTimeConverter(DateUtils.ISO8601_TIME_MZ));
	this.converters.putIfAbsent(Converter.OFFSET_TIME_S, new OffsetTimeConverter(DateUtils.ISO8601_TIME_SZ));

	this.converters.putIfAbsent(Converter.OFFSET_DATETIME, new OffsetDateTimeConverter());
	this.converters.putIfAbsent(Converter.OFFSET_DATETIME_M, new OffsetDateTimeConverter(DateUtils.ISO8601_DATETIME_MZ));
	this.converters.putIfAbsent(Converter.OFFSET_DATETIME_S, new OffsetDateTimeConverter(DateUtils.ISO8601_DATETIME_SZ));

	this.converters = Collections.unmodifiableMap(this.converters);
    }

    public void addConverter(String converterId, Converter<?> converter) {
	this.assertNotInitialized();
	this.converters.put(converterId, converter);
    }

    public <T> Converter<T> getConverter(String converterId, Class<?> targetType) {
	this.initialize();
	return (converterId != null) ? getConverter(converterId) : getConverter(targetType);
    }

    @SuppressWarnings("unchecked")
    public <T> Converter<T> getConverter(Class<?> targetType) {
	this.initialize();
	return (Converter<T>) this.converters.get(TypeUtils.wrap(targetType).getSimpleName());
    }

    @SuppressWarnings("unchecked")
    public <T> Converter<T> getConverter(String converterId) {
	this.initialize();
	return (Converter<T>) this.converters.get(converterId);
    }

    private static volatile ConverterProvider __default;
    private static final Object MUTEX = new Object();

    public static ConverterProvider getDefault() {
	ConverterProvider obj = __default;
	if (obj == null) {
	    synchronized (MUTEX) {
		if ((obj = __default) == null) {
		    __default = obj = initConverterProvider();
		}
	    }
	}
	return obj;
    }

    public static void setDefault(ConverterProvider impl) {
	Assertions.assertNull(__default, "ConverterProvider.__default must be null.");

	if (__default == null) {
	    synchronized (MUTEX) {
		if (__default == null) {
		    __default = impl;
		    return;
		}
	    }
	}
    }

    private static Supplier<ConverterProvider> __provider;

    public static void setProvider(Supplier<ConverterProvider> impl) {
	Assertions.assertNull(__default, "ConverterProvider.__default must be null.");

	if (__default == null) {
	    synchronized (MUTEX) {
		if (__default == null) {
		    __provider = impl;
		    return;
		}
	    }
	}
    }

    private static ConverterProvider initConverterProvider() {
	if (__provider != null) {
	    return __provider.get();
	}
	return new ConverterProvider();
    }
}
