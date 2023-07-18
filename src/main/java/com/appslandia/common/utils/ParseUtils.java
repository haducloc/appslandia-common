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
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.appslandia.common.base.BoolFormatException;
import com.appslandia.common.base.DateFormatException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ParseUtils {

    public static boolean parseBool(String value) throws BoolFormatException {
	Asserts.notNull(value);

	if (isTrueValue(value)) {
	    return true;
	}
	if (isFalseValue(value)) {
	    return false;
	}
	throw new BoolFormatException(value);
    }

    public static boolean parseBool(String value, boolean defaultValue) {
	if (value == null) {
	    return defaultValue;
	}
	try {
	    return parseBool(value);

	} catch (BoolFormatException ex) {
	    return defaultValue;
	}
    }

    public static int parseInt(String value) throws NumberFormatException {
	Asserts.notNull(value);
	return Integer.parseInt(value);
    }

    public static int parseInt(String value, int defaultValue) {
	if (value == null) {
	    return defaultValue;
	}

	try {
	    return Integer.parseInt(value);
	} catch (NumberFormatException ex) {
	    return defaultValue;
	}
    }

    public static long parseLong(String value) throws NumberFormatException {
	Asserts.notNull(value);
	return Long.parseLong(value);
    }

    public static long parseLong(String value, long defaultValue) {
	if (value == null) {
	    return defaultValue;
	}

	try {
	    return Long.parseLong(value);
	} catch (NumberFormatException ex) {
	    return defaultValue;
	}
    }

    public static double parseDouble(String value) throws NumberFormatException {
	Asserts.notNull(value);
	return Double.parseDouble(value);
    }

    public static double parseDouble(String value, double defaultValue) {
	if (value == null) {
	    return defaultValue;
	}

	try {
	    return Double.parseDouble(value);
	} catch (NumberFormatException ex) {
	    return defaultValue;
	}
    }

    public static BigDecimal parseDecimal(String value) throws NumberFormatException {
	Asserts.notNull(value);
	return new BigDecimal(value);
    }

    public static BigDecimal parseDecimal(String value, BigDecimal defaultValue) {
	if (value == null) {
	    return defaultValue;
	}

	try {
	    return new BigDecimal(value);
	} catch (NumberFormatException ex) {
	    return defaultValue;
	}
    }

    public static <T> T parseValue(String value, Function<String, T> converter) {
	if (value == null) {
	    return null;
	}
	return converter.apply(value);
    }

    public static Boolean parseBoolObj(String value) throws BoolFormatException {
	if (value == null) {
	    return null;
	}
	return parseBool(value);
    }

    public static Integer parseIntObj(String value) throws NumberFormatException {
	return (value != null) ? Integer.parseInt(value) : null;
    }

    public static Long parseLongObj(String value) throws NumberFormatException {
	return (value != null) ? Long.parseLong(value) : null;
    }

    public static Double parseDoubleObj(String value) throws NumberFormatException {
	return (value != null) ? Double.parseDouble(value) : null;
    }

    public static boolean isTrueValue(String value) {
	return "true".equalsIgnoreCase(value) || "t".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value);
    }

    public static boolean isFalseValue(String value) {
	return "false".equalsIgnoreCase(value) || "f".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value) || "n".equalsIgnoreCase(value);
    }

    public static Date parseDate(String value, String... patterns) throws DateFormatException {
	return parseDateObject(value, Date.class, patterns, (v, p) -> DateUtils.parse(v, p));
    }

    public static LocalDate parseLocalDate(String value, String... patterns) throws DateFormatException {
	return parseDateObject(value, LocalDate.class, patterns, (v, p) -> LocalDate.parse(v, DateUtils.getFormatter(p)));
    }

    public static LocalTime parseLocalTime(String value, String... patterns) throws DateFormatException {
	return parseDateObject(value, LocalTime.class, patterns, (v, p) -> LocalTime.parse(v, DateUtils.getFormatter(p)));
    }

    public static LocalDateTime parseLocalDateTime(String value, String... patterns) throws DateFormatException {
	return parseDateObject(value, LocalDateTime.class, patterns, (v, p) -> LocalDateTime.parse(v, DateUtils.getFormatter(p)));
    }

    public static OffsetTime parseOffsetTime(String value, String... patterns) throws DateFormatException {
	return parseDateObject(value, OffsetTime.class, patterns, (v, p) -> OffsetTime.parse(v, DateUtils.getFormatter(p)));
    }

    public static OffsetDateTime parseOffsetDateTime(String value, String... patterns) throws DateFormatException {
	return parseDateObject(value, OffsetDateTime.class, patterns, (v, p) -> OffsetDateTime.parse(v, DateUtils.getFormatter(p)));
    }

    private static <T> T parseDateObject(String value, Class<T> targetClass, String[] patterns, BiFunction<String, String, T> converter) throws DateFormatException {
	if (value == null) {
	    return null;
	}
	for (String pattern : patterns) {
	    try {
		return converter.apply(value, pattern);
	    } catch (Exception ex) {
	    }
	}
	throw new DateFormatException(STR.fmt("Failed to parse {} from '{}'.", targetClass, value));
    }
}
