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
 * @author Loc Ha
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

  public static Boolean parseBoolOpt(String value) throws BoolFormatException {
    return (value != null) ? parseBool(value) : null;
  }

  public static Boolean parseBool(String value, Boolean ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseBool(val)) : ifNullOrInvalid;
  }

  public static byte parseByte(String value) throws NumberFormatException {
    Arguments.notNull(value);
    return Byte.parseByte(value);
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

  public static Byte parseByteOpt(String value) throws NumberFormatException {
    return (value != null) ? parseByte(value) : null;
  }

  public static Byte parseByte(String value, Byte ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseByte(val)) : ifNullOrInvalid;
  }

  public static short parseShort(String value) throws NumberFormatException {
    Arguments.notNull(value);
    return Short.parseShort(value);
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

  public static Short parseShortOpt(String value) throws NumberFormatException {
    return (value != null) ? parseShort(value) : null;
  }

  public static Short parseShort(String value, Short ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseShort(val)) : ifNullOrInvalid;
  }

  public static int parseInt(String value) throws NumberFormatException {
    Arguments.notNull(value);
    return Integer.parseInt(value);
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

  public static Integer parseIntOpt(String value) throws NumberFormatException {
    return (value != null) ? parseInt(value) : null;
  }

  public static Integer parseInt(String value, Integer ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseInt(val)) : ifNullOrInvalid;
  }

  public static long parseLong(String value) throws NumberFormatException {
    Arguments.notNull(value);
    return Long.parseLong(value);
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

  public static Long parseLongOpt(String value) throws NumberFormatException {
    return (value != null) ? parseLong(value) : null;
  }

  public static Long parseLong(String value, Long ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseLong(val)) : ifNullOrInvalid;
  }

  public static float parseFloat(String value) throws NumberFormatException, NaNInfinityException {
    Arguments.notNull(value);
    var val = Float.parseFloat(value);
    if (!Float.isFinite(val)) {
      throw new NaNInfinityException(
          STR.fmt("Failed to convert '{}' into a finite float. Value is NaN or Infinity.", value));
    }
    return val;
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

  public static Float parseFloatOpt(String value) throws NumberFormatException, NaNInfinityException {
    return (value != null) ? parseFloat(value) : null;
  }

  public static Float parseFloat(String value, Float ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseFloat(val)) : ifNullOrInvalid;
  }

  public static double parseDouble(String value) throws NumberFormatException, NaNInfinityException {
    Arguments.notNull(value);
    var val = Double.parseDouble(value);
    if (!Double.isFinite(val)) {
      throw new NaNInfinityException(
          STR.fmt("Failed to convert '{}' into a finite double. Value is NaN or Infinity.", value));
    }
    return val;
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

  public static Double parseDoubleOpt(String value) throws NumberFormatException, NaNInfinityException {
    return (value != null) ? parseDouble(value) : null;
  }

  public static Double parseDouble(String value, Double ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> parseDouble(val)) : ifNullOrInvalid;
  }

  public static BigDecimal parseDecimal(String value) throws NumberFormatException {
    return (value != null) ? new BigDecimal(value) : null;
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

  public static BigDecimal parseDecimal(String value, BigDecimal ifNullOrInvalid) {
    return (value != null) ? parseValue(value, ifNullOrInvalid, val -> new BigDecimal(val)) : ifNullOrInvalid;
  }

  public static <T extends Enum<T>> T parseEnum(String enumValue, Class<T> enumType) throws IllegalArgumentException {
    Arguments.notNull(enumType);
    return (enumValue != null) ? Enum.valueOf(enumType, enumValue) : null;
  }

  public static <T extends Enum<T>> T parseEnum(String enumValue, Class<T> enumType, T ifNullOrInvalid) {
    return (enumValue != null) ? parseValue(enumValue, ifNullOrInvalid, val -> Enum.valueOf(enumType, val))
        : ifNullOrInvalid;
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
        || "y".equalsIgnoreCase(value) || "1".equals(value);
  }

  public static boolean isFalseValue(String value) {
    return "false".equalsIgnoreCase(value) || "f".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value)
        || "n".equalsIgnoreCase(value) || "0".equals(value);
  }

  public static LocalDate parseLocalDate(String value, String... patterns) throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, LocalDate.class, patterns, (v, p) -> LocalDate.parse(v, DateUtils.getFormatter(p)));
  }

  public static LocalDate parseLocalDate(String value, Collection<String> patterns) throws TemporalFormatException {
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

  public static LocalTime parseLocalTime(String value, Collection<String> patterns) throws TemporalFormatException {
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

  public static LocalDateTime parseLocalDateTime(String value, Collection<String> patterns)
      throws TemporalFormatException {
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

  public static YearMonth parseYearMonth(String value, Collection<String> patterns) throws TemporalFormatException {
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

  public static OffsetTime parseOffsetTime(String value, Collection<String> patterns) throws TemporalFormatException {
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

  public static OffsetDateTime parseOffsetDateTime(String value, Collection<String> patterns)
      throws TemporalFormatException {
    if (value == null) {
      return null;
    }
    return doParseTemporal(value, OffsetDateTime.class, patterns,
        (v, p) -> OffsetDateTime.parse(v, DateUtils.getFormatter(p)));
  }

  private static <T> T doParseTemporal(String value, Class<T> targetClass, String[] patterns,
      BiFunction<String, String, T> exceptionalConverter) throws TemporalFormatException {

    return doParseTemporal(value, targetClass, new ArrayUtils.ArrayIterable<>(patterns), exceptionalConverter);
  }

  private static <T> T doParseTemporal(String value, Class<T> targetClass, Iterable<String> patterns,
      BiFunction<String, String, T> exceptionalConverter) throws TemporalFormatException {

    var valueHasT = value.indexOf('T') >= 0;
    for (var pattern : patterns) {

      var patternHasT = pattern.indexOf('T') >= 0;
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
