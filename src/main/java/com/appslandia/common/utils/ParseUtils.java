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
import java.time.YearMonth;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.appslandia.common.base.BoolFormatException;
import com.appslandia.common.base.TemporalFormatException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ParseUtils {

  public static boolean parseBool(String value) throws BoolFormatException {
    Arguments.notNull(value);
    if (isTrueValue(value)) {
      return true;
    }
    if (isFalseValue(value)) {
      return false;
    }
    throw new BoolFormatException(value);
  }

  public static byte parseByte(String value) throws NumberFormatException {
    Arguments.notNull(value);
    return Byte.parseByte(value);
  }

  public static short parseShort(String value) throws NumberFormatException {
    Arguments.notNull(value);
    return Short.parseShort(value);
  }

  public static int parseInt(String value) throws NumberFormatException {
    Arguments.notNull(value);
    return Integer.parseInt(value);
  }

  public static long parseLong(String value) throws NumberFormatException {
    Arguments.notNull(value);
    return Long.parseLong(value);
  }

  public static float parseFloat(String value) throws NumberFormatException, NaNInfinityException {
    Arguments.notNull(value);
    float val = Float.parseFloat(value);
    if (!Float.isFinite(val)) {

      throw new NaNInfinityException(
          STR.fmt("Failed to convert '{}' into a finite float. Value is NaN or Infinity.", value));
    }
    return val;
  }

  public static double parseDouble(String value) throws NumberFormatException, NaNInfinityException {
    Arguments.notNull(value);
    double val = Double.parseDouble(value);
    if (!Double.isFinite(val)) {

      throw new NaNInfinityException(
          STR.fmt("Failed to convert '{}' into a finite double. Value is NaN or Infinity.", value));
    }
    return val;
  }

  public static boolean parseBool(String value, boolean ifNullOrInvalid) {
    if (value == null) {
      return ifNullOrInvalid;
    }
    try {
      return parseBool(value);
    } catch (BoolFormatException ex) {
      return ifNullOrInvalid;
    }
  }

  public static byte parseByte(String value, byte ifNullOrInvalid) {
    if (value == null) {
      return ifNullOrInvalid;
    }
    try {
      return Byte.parseByte(value);
    } catch (NumberFormatException ex) {
      return ifNullOrInvalid;
    }
  }

  public static short parseShort(String value, short ifNullOrInvalid) {
    if (value == null) {
      return ifNullOrInvalid;
    }
    try {
      return Short.parseShort(value);
    } catch (NumberFormatException ex) {
      return ifNullOrInvalid;
    }
  }

  public static int parseInt(String value, int ifNullOrInvalid) {
    if (value == null) {
      return ifNullOrInvalid;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException ex) {
      return ifNullOrInvalid;
    }
  }

  public static long parseLong(String value, long ifNullOrInvalid) {
    if (value == null) {
      return ifNullOrInvalid;
    }
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException ex) {
      return ifNullOrInvalid;
    }
  }

  public static float parseFloat(String value, float ifNullOrInvalid) {
    if (value == null) {
      return ifNullOrInvalid;
    }
    try {
      return Float.parseFloat(value);
    } catch (NumberFormatException ex) {
      return ifNullOrInvalid;
    }
  }

  public static double parseDouble(String value, double ifNullOrInvalid) {
    if (value == null) {
      return ifNullOrInvalid;
    }
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException ex) {
      return ifNullOrInvalid;
    }
  }

  public static BigDecimal parseDecimal(String value, double ifNullOrInvalid) {
    if (value == null) {
      return new BigDecimal(Double.toString(ifNullOrInvalid));
    }
    try {
      return new BigDecimal(value);
    } catch (NumberFormatException ex) {
      return new BigDecimal(Double.toString(ifNullOrInvalid));
    }
  }

  public static Boolean parseBoolOpt(String value, Boolean ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseBool(val)) : ifNullOrInvalid;
  }

  public static Byte parseByteOpt(String value, Byte ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseByte(val)) : ifNullOrInvalid;
  }

  public static Short parseShortOpt(String value, Short ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseShort(val)) : ifNullOrInvalid;
  }

  public static Integer parseIntOpt(String value, Integer ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseInt(val)) : ifNullOrInvalid;
  }

  public static Long parseLongOpt(String value, Long ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseLong(val)) : ifNullOrInvalid;
  }

  public static Float parseFloatOpt(String value, Float ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseFloat(val)) : ifNullOrInvalid;
  }

  public static Double parseDoubleOpt(String value, Double ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseDouble(val)) : ifNullOrInvalid;
  }

  public static BigDecimal parseDecimalOpt(String value, BigDecimal ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> new BigDecimal(val)) : ifNullOrInvalid;
  }

  public static <T> T parseValue(String value, Function<String, T> exceptionalConverter) {
    return (value != null) ? exceptionalConverter.apply(value) : null;
  }

  public static <T> T parseValue(String value, T ifNullOrInvalid, Function<String, T> exceptionalConverter) {
    if (value == null) {
      return ifNullOrInvalid;
    }
    try {
      return exceptionalConverter.apply(value);
    } catch (Exception ex) {
      return ifNullOrInvalid;
    }
  }

  public static boolean isTrueValue(String value) {
    return "true".equalsIgnoreCase(value) || "t".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value)
        || "y".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value);
  }

  public static boolean isFalseValue(String value) {
    return "false".equalsIgnoreCase(value) || "f".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value)
        || "n".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value);
  }

  public static LocalDate parseLocalDate(String value, String... patterns) throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, LocalDate.class, patterns, (v, p) -> LocalDate.parse(v, DateUtils.getFormatter(p)));
  }

  public static LocalTime parseLocalTime(String value, String... patterns) throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, LocalTime.class, patterns, (v, p) -> LocalTime.parse(v, DateUtils.getFormatter(p)));
  }

  public static LocalDateTime parseLocalDateTime(String value, String... patterns) throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, LocalDateTime.class, patterns,
        (v, p) -> LocalDateTime.parse(v, DateUtils.getFormatter(p)));
  }

  public static YearMonth parseYearMonth(String value, String... patterns) throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, YearMonth.class, patterns, (v, p) -> YearMonth.parse(v, DateUtils.getFormatter(p)));
  }

  public static OffsetTime parseOffsetTime(String value, String... patterns) throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, OffsetTime.class, patterns, (v, p) -> OffsetTime.parse(v, DateUtils.getFormatter(p)));
  }

  public static OffsetDateTime parseOffsetDateTime(String value, String... patterns) throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, OffsetDateTime.class, patterns,
        (v, p) -> OffsetDateTime.parse(v, DateUtils.getFormatter(p)));
  }

  private static <T> T doParseTemporal(String value, Class<T> targetClass, String[] patterns,
      BiFunction<String, String, T> exceptionalConverter) throws TemporalFormatException {
    Arguments.hasElements(patterns);

    for (String pattern : patterns) {
      try {
        return exceptionalConverter.apply(value, pattern);
      } catch (Exception ex) {
        // ignore
      }
    }
    throw new TemporalFormatException(STR.fmt("Failed to parse {} from '{}'.", targetClass, value));
  }

  public static LocalDate parseLocalDate(String value, Collection<String> patterns) throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, LocalDate.class, patterns, (v, p) -> LocalDate.parse(v, DateUtils.getFormatter(p)));
  }

  public static LocalTime parseLocalTime(String value, Collection<String> patterns) throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, LocalTime.class, patterns, (v, p) -> LocalTime.parse(v, DateUtils.getFormatter(p)));
  }

  public static LocalDateTime parseLocalDateTime(String value, Collection<String> patterns)
      throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, LocalDateTime.class, patterns,
        (v, p) -> LocalDateTime.parse(v, DateUtils.getFormatter(p)));
  }

  public static YearMonth parseYearMonth(String value, Collection<String> patterns) throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, YearMonth.class, patterns, (v, p) -> YearMonth.parse(v, DateUtils.getFormatter(p)));
  }

  public static OffsetTime parseOffsetTime(String value, Collection<String> patterns) throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, OffsetTime.class, patterns, (v, p) -> OffsetTime.parse(v, DateUtils.getFormatter(p)));
  }

  public static OffsetDateTime parseOffsetDateTime(String value, Collection<String> patterns)
      throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, OffsetDateTime.class, patterns,
        (v, p) -> OffsetDateTime.parse(v, DateUtils.getFormatter(p)));
  }

  private static <T> T doParseTemporal(String value, Class<T> targetClass, Collection<String> patterns,
      BiFunction<String, String, T> exceptionalConverter) throws TemporalFormatException {
    Arguments.hasElements(patterns);

    boolean valueHasT = value.indexOf('T') >= 0;
    for (String pattern : patterns) {

      boolean patternHasT = pattern.indexOf('T') >= 0;
      if (valueHasT != patternHasT) {
        continue;
      }
      try {
        return exceptionalConverter.apply(value, pattern);
      } catch (Exception ex) {
        // ignore
      }
    }
    throw new TemporalFormatException(STR.fmt("Failed to parse {} from '{}'.", targetClass, value));
  }
}
