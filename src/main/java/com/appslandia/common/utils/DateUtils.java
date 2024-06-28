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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.appslandia.common.base.TemporalFormatException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DateUtils {

  // ISO8601
  public static final String ISO8601_DATE = "yyyy-MM-dd";
  public static final String ISO8601_YEAR_MONTH = "yyyy-MM";

  public static final String ISO8601_TIME_M = "HH:mm";
  public static final String ISO8601_TIME_S = "HH:mm:ss";
  public static final String ISO8601_TIME_N1 = "HH:mm:ss.S";
  public static final String ISO8601_TIME_N2 = "HH:mm:ss.SS";
  public static final String ISO8601_TIME_N3 = "HH:mm:ss.SSS";
  public static final String ISO8601_TIME_N4 = "HH:mm:ss.SSSS";
  public static final String ISO8601_TIME_N5 = "HH:mm:ss.SSSSS";
  public static final String ISO8601_TIME_N6 = "HH:mm:ss.SSSSSS";
  public static final String ISO8601_TIME_N7 = "HH:mm:ss.SSSSSSS";

  public static final String ISO8601_TIMEZ_M = "HH:mmXXX";
  public static final String ISO8601_TIMEZ_S = "HH:mm:ssXXX";
  public static final String ISO8601_TIMEZ_N1 = "HH:mm:ss.SXXX";
  public static final String ISO8601_TIMEZ_N2 = "HH:mm:ss.SSXXX";
  public static final String ISO8601_TIMEZ_N3 = "HH:mm:ss.SSSXXX";
  public static final String ISO8601_TIMEZ_N4 = "HH:mm:ss.SSSSXXX";
  public static final String ISO8601_TIMEZ_N5 = "HH:mm:ss.SSSSSXXX";
  public static final String ISO8601_TIMEZ_N6 = "HH:mm:ss.SSSSSSXXX";
  public static final String ISO8601_TIMEZ_N7 = "HH:mm:ss.SSSSSSSXXX";

  public static final String ISO8601_DATETIME_M = "yyyy-MM-dd'T'HH:mm";
  public static final String ISO8601_DATETIME_S = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String ISO8601_DATETIME_N1 = "yyyy-MM-dd'T'HH:mm:ss.S";
  public static final String ISO8601_DATETIME_N2 = "yyyy-MM-dd'T'HH:mm:ss.SS";
  public static final String ISO8601_DATETIME_N3 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  public static final String ISO8601_DATETIME_N4 = "yyyy-MM-dd'T'HH:mm:ss.SSSS";
  public static final String ISO8601_DATETIME_N5 = "yyyy-MM-dd'T'HH:mm:ss.SSSSS";
  public static final String ISO8601_DATETIME_N6 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
  public static final String ISO8601_DATETIME_N7 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS";

  public static final String ISO8601_DATETIMEZ_M = "yyyy-MM-dd'T'HH:mmXXX";
  public static final String ISO8601_DATETIMEZ_S = "yyyy-MM-dd'T'HH:mm:ssXXX";
  public static final String ISO8601_DATETIMEZ_N1 = "yyyy-MM-dd'T'HH:mm:ss.SXXX";
  public static final String ISO8601_DATETIMEZ_N2 = "yyyy-MM-dd'T'HH:mm:ss.SSXXX";
  public static final String ISO8601_DATETIMEZ_N3 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  public static final String ISO8601_DATETIMEZ_N4 = "yyyy-MM-dd'T'HH:mm:ss.SSSSXXX";
  public static final String ISO8601_DATETIMEZ_N5 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSXXX";
  public static final String ISO8601_DATETIMEZ_N6 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX";
  public static final String ISO8601_DATETIMEZ_N7 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX";

  public static long clearMs(long timeInMs) {
    return (timeInMs / 1000) * 1000;
  }

  public static boolean isFutureTime(long timeMillis, int leewayMs) {
    return System.currentTimeMillis() - leewayMs < timeMillis;
  }

  public static boolean isPastTime(long timeMillis, int leewayMs) {
    return System.currentTimeMillis() + leewayMs > timeMillis;
  }

  // Java8 Date/Time

  public static ZoneOffset toCurrentOffset(ZoneId zoneId) {
    return (zoneId != null) ? zoneId.getRules().getOffset(Instant.now()) : null;
  }

  // yyyyMMdd
  public static Integer toDateID(LocalDate ld) {
    if (ld == null) {
      return null;
    }
    return ld.getYear() * 1_00_00 + ld.getMonthValue() * 1_00 + ld.getDayOfMonth();
  }

  public static LocalDate fromDateID(Integer dateID) throws DateTimeException {
    if (dateID == null) {
      return null;
    }

    int day = dateID % 100;
    dateID /= 100;

    int month = dateID % 100;
    int year = dateID / 100;

    return LocalDate.of(year, month, day);
  }

  // yyyyMMdd
  public static Integer toWeekID(LocalDate ld, Locale locale) {
    Asserts.notNull(locale);
    if (ld == null) {
      return null;
    }
    return toDateID(firstDayOfWeek(ld, locale));
  }

  // yyyyMM
  public static Integer toMonthID(LocalDate ld) {
    if (ld == null) {
      return null;
    }
    return ld.getYear() * 100 + ld.getMonthValue();
  }

  public static Integer toMonthID(YearMonth ym) {
    if (ym == null) {
      return null;
    }
    return ym.getYear() * 100 + ym.getMonthValue();
  }

  public static YearMonth fromMonthID(Integer monthID) throws DateTimeException {
    if (monthID == null) {
      return null;
    }

    int month = monthID % 100;
    int year = monthID / 100;

    return YearMonth.of(year, month);
  }

  // yyyyMMddHHmm
  public static Long toDateTimeID(LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return dateTime.getYear() * 1_00_00_00_00L + dateTime.getMonthValue() * 1_00_00_00L
        + dateTime.getDayOfMonth() * 1_00_00L + dateTime.getHour() * 1_00L + dateTime.getMinute();
  }

  public static LocalDateTime fromDateTimeID(Long dateTimeID) throws DateTimeException {
    if (dateTimeID == null) {
      return null;
    }

    int minute = (int) (dateTimeID % 100);
    dateTimeID /= 100;

    int hour = (int) (dateTimeID % 100);
    dateTimeID /= 100;

    int day = (int) (dateTimeID % 100);
    dateTimeID /= 100;

    int month = (int) (dateTimeID % 100);
    int year = (int) (dateTimeID / 100);

    return LocalDateTime.of(year, month, day, hour, minute);
  }

  public static LocalDateTime toLocalDateTime(Long timeMillis, ZoneOffset offset) {
    return (timeMillis != null) ? Instant.ofEpochMilli(timeMillis).atOffset(offset).toLocalDateTime() : null;
  }

  private static final class DateTimeFormattersHolder {
    private static final ConcurrentMap<String, DateTimeFormatter> FORMATTERS = new ConcurrentHashMap<>();
  }

  public static DateTimeFormatter getFormatter(String pattern) {
    return DateTimeFormattersHolder.FORMATTERS.computeIfAbsent(pattern, p -> DateTimeFormatter.ofPattern(p));
  }

  public static String format(Temporal temporal, String pattern) {
    return (temporal != null) ? getFormatter(pattern).format(temporal) : null;
  }

  public static LocalDateTime atStartOfDay(LocalDate ld) {
    if (ld == null) {
      return null;
    }
    return LocalDateTime.of(ld, LocalTime.MIN);
  }

  public static LocalDateTime atEndOfDay(LocalDate ld) {
    if (ld == null) {
      return null;
    }
    return LocalDateTime.of(ld, LocalTime.MAX);
  }

  public static LocalDateTime atStartOfDay(LocalDateTime ldt) {
    if (ldt == null) {
      return null;
    }
    return LocalDateTime.of(ldt.toLocalDate(), LocalTime.MIN);
  }

  public static LocalDateTime atEndOfDay(LocalDateTime ldt) {
    if (ldt == null) {
      return null;
    }
    return LocalDateTime.of(ldt.toLocalDate(), LocalTime.MAX);
  }

  public static OffsetDateTime atStartOfDay(OffsetDateTime odt) {
    if (odt == null) {
      return null;
    }
    return OffsetDateTime.of(odt.toLocalDate(), LocalTime.MIN, odt.getOffset());
  }

  public static OffsetDateTime atEndOfDay(OffsetDateTime odt) {
    if (odt == null) {
      return null;
    }
    return OffsetDateTime.of(odt.toLocalDate(), LocalTime.MAX, odt.getOffset());
  }

  public static OffsetDateTime nowAtUTC() {
    return nowAt(ZoneOffset.UTC);
  }

  public static OffsetDateTime nowAt(String zoneId) {
    return (zoneId != null) ? nowAt(ZoneId.of(zoneId)) : nowAt((ZoneId) null);
  }

  public static OffsetDateTime nowAt(ZoneId zoneId) {
    return (zoneId != null) ? OffsetDateTime.now(zoneId) : OffsetDateTime.now();
  }

  public static LocalDate todayAt(String zoneId) {
    return (zoneId != null) ? todayAt(ZoneId.of(zoneId)) : todayAt((ZoneId) null);
  }

  public static LocalDate todayAt(ZoneId zoneId) {
    return (zoneId != null) ? LocalDate.now(zoneId) : LocalDate.now();
  }

  public static OffsetDateTime toSameInstantUTC(OffsetDateTime odt) {
    return toSameInstant(odt, ZoneOffset.UTC);
  }

  public static OffsetDateTime toSameInstant(OffsetDateTime odt, String offsetId) {
    return toSameInstant(odt, ZoneOffset.of(offsetId));
  }

  public static OffsetDateTime toSameInstant(OffsetDateTime odt, ZoneOffset offset) {
    if (odt == null) {
      return null;
    }
    return odt.withOffsetSameInstant(offset);
  }

  public static LocalDate firstDayOfMonth(LocalDate ld) {
    if (ld == null) {
      return null;
    }
    return ld.with(TemporalAdjusters.firstDayOfMonth());
  }

  public static LocalDate lastDayOfMonth(LocalDate ld) {
    if (ld == null) {
      return null;
    }
    return ld.with(TemporalAdjusters.lastDayOfMonth());
  }

  public static LocalDate firstDayOfWeek(LocalDate ld, Locale locale) {
    Asserts.notNull(locale);
    if (ld == null) {
      return null;
    }
    WeekFields weekFields = WeekFields.of(locale);
    return ld.with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));
  }

  public static LocalDate lastDayOfWeek(LocalDate ld, Locale locale) {
    Asserts.notNull(locale);
    if (ld == null) {
      return null;
    }
    return firstDayOfWeek(ld, locale).plusDays(6);
  }

  // 1w 2d 3h 4m 50s 500ms
  private static final Pattern TEMPORAL_AMT_PATTERN = Pattern.compile("((\\d+.\\d+|\\d+)(w|d|h|m|s|ms)\\s*)+",
      Pattern.CASE_INSENSITIVE);

  public static long translateToMs(String temporalAmt) {
    Asserts.notNull(temporalAmt, "temporalAmt is required.");
    Asserts.isTrue(TEMPORAL_AMT_PATTERN.matcher(temporalAmt).matches(),
        () -> STR.fmt("temporalAmt '{}' is invalid.", temporalAmt));

    double result = 0l;
    int i = 0;

    while (i < temporalAmt.length()) {
      int j = i;
      while (Character.isDigit(temporalAmt.charAt(j)) || (temporalAmt.charAt(j) == '.')) {
        j++;
      }
      int k = j;
      while ((k <= temporalAmt.length() - 1)
          && (Character.isLetter(temporalAmt.charAt(k)) || (temporalAmt.charAt(k) == (' ')))) {
        k++;
      }

      double amt = Double.parseDouble(temporalAmt.substring(i, j));
      String unit = temporalAmt.substring(j, k).strip().toLowerCase(Locale.ENGLISH);

      switch (unit) {
      case "w":
        result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) * 7 * amt;
        break;
      case "d":
        result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) * amt;
        break;
      case "h":
        result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS) * amt;
        break;
      case "m":
        result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES) * amt;
        break;
      case "s":
        result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS) * amt;
        break;
      default:
        result += amt;
        break;
      }
      i = k;
    }
    return (long) Math.ceil(result);
  }

  public static Map<TimeUnit, Long> parseUnits(long duration, TimeUnit unit) {
    return parseUnits(duration, unit, TimeUnit.DAYS, TimeUnit.NANOSECONDS);
  }

  public static Map<TimeUnit, Long> parseUnits(long duration, TimeUnit unit, TimeUnit highUnit, TimeUnit lowUnit) {
    Asserts.isTrue(highUnit.compareTo(lowUnit) >= 0, "highUnit must be gte lowUnit.");
    Map<TimeUnit, Long> res = new EnumMap<>(TimeUnit.class);

    duration = lowUnit.convert(duration, unit);
    TimeUnit u = highUnit;

    while (true) {
      long v = u.convert(duration, lowUnit);
      res.put(u, v);

      if (u == lowUnit) {
        break;
      }
      duration -= lowUnit.convert(v, u);
      u = nextLowerUnit(u);
    }
    return res;
  }

  private static TimeUnit nextLowerUnit(TimeUnit unit) {
    switch (unit) {
    case DAYS:
      return TimeUnit.HOURS;
    case HOURS:
      return TimeUnit.MINUTES;
    case MINUTES:
      return TimeUnit.SECONDS;
    case SECONDS:
      return TimeUnit.MILLISECONDS;
    case MILLISECONDS:
      return TimeUnit.MICROSECONDS;
    case MICROSECONDS:
      return TimeUnit.NANOSECONDS;
    default:
      throw new Error();
    }
  }

  public static String toDatePattern(Locale locale) {
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    String isoDate = null;
    try {
      isoDate = df.format(new SimpleDateFormat(ISO8601_DATE).parse("3333-11-22"));
    } catch (ParseException ex) {
      throw new Error(ex);
    }

    Set<Character> letters = new LinkedHashSet<>();
    Character separator = null;

    for (int i = 0; i < isoDate.length(); i++) {
      char ch = isoDate.charAt(i);

      if (ch == '1') {
        letters.add('M');
      } else if (ch == '2') {
        letters.add('d');
      } else if (ch == '3') {
        letters.add('y');
      } else {
        if (separator == null) {
          if (ch == '-' || ch == '/' || ch == '.') {
            separator = ch;
          }
        }
      }
    }

    // If separator known & letters are yMd
    if ((separator != null) && (letters.size() == 3)) {

      StringBuilder datePt = new StringBuilder(10);

      for (Character ch : letters) {
        if (datePt.length() > 0) {
          datePt.append(separator);
        }
        datePt.append(ch);
        datePt.append(ch);

        if (ch.equals('y')) {
          datePt.append(ch);
          datePt.append(ch);
        }
      }
      return datePt.toString();
    }
    return null;
  }

  public static boolean isInputDatePatternValid(String datePattern) {
    Asserts.notNull(datePattern);

    if (datePattern.length() != 10) {
      return false;
    }
    if (!datePattern.contains("dd") || !datePattern.contains("MM") || !datePattern.contains("yyyy")) {
      return false;
    }

    // separators
    HashMap<Character, Integer> separators = new HashMap<>(2);
    char sep = 0;

    for (int i = 0; i < datePattern.length(); i++) {
      char c = datePattern.charAt(i);

      if (c != 'd' && c != 'M' && c != 'y') {
        separators.compute(c, (k, v) -> v != null ? v + 1 : 1);

        sep = c;
      }
    }

    if (separators.size() != 1) {
      return false;
    }
    if (separators.get(sep) != 2) {
      return false;
    }
    if (sep != '-' && sep != '/' && sep != '.') {
      return false;
    }
    return true;
  }

  public static final Collection<String> ISO8601_PATTERNS_TIME = CollectionUtils.unmodifiableSet(new TreeSet<>(),
      ISO8601_TIME_M, ISO8601_TIME_S, ISO8601_TIME_N1, ISO8601_TIME_N2, ISO8601_TIME_N3, ISO8601_TIME_N4,
      ISO8601_TIME_N5, ISO8601_TIME_N6, ISO8601_TIME_N7);

  public static final Collection<String> ISO8601_PATTERNS_TIMEZ = CollectionUtils.unmodifiableSet(new TreeSet<>(),
      ISO8601_TIMEZ_M, ISO8601_TIMEZ_S, ISO8601_TIMEZ_N1, ISO8601_TIMEZ_N2, ISO8601_TIMEZ_N3, ISO8601_TIMEZ_N4,
      ISO8601_TIMEZ_N5, ISO8601_TIMEZ_N6, ISO8601_TIMEZ_N7);

  public static final Collection<String> ISO8601_PATTERNS_DATETIME = CollectionUtils.unmodifiableSet(new TreeSet<>(),
      ISO8601_DATETIME_M, ISO8601_DATETIME_S, ISO8601_DATETIME_N1, ISO8601_DATETIME_N2, ISO8601_DATETIME_N3,
      ISO8601_DATETIME_N4, ISO8601_DATETIME_N5, ISO8601_DATETIME_N6, ISO8601_DATETIME_N7);

  public static final Collection<String> ISO8601_PATTERNS_DATETIMEZ = CollectionUtils.unmodifiableSet(new TreeSet<>(),
      ISO8601_DATETIMEZ_M, ISO8601_DATETIMEZ_S, ISO8601_DATETIMEZ_N1, ISO8601_DATETIMEZ_N2, ISO8601_DATETIMEZ_N3,
      ISO8601_DATETIMEZ_N4, ISO8601_DATETIMEZ_N5, ISO8601_DATETIMEZ_N6, ISO8601_DATETIMEZ_N7);

  public static LocalDate parseLocalDate(String isoValue) throws TemporalFormatException {
    return (isoValue != null) ? ParseUtils.parseLocalDate(isoValue, ISO8601_DATE) : null;
  }

  public static LocalTime parseLocalTime(String isoValue) throws TemporalFormatException {
    return (isoValue != null) ? ParseUtils.parseLocalTime(isoValue, ISO8601_PATTERNS_TIME) : null;
  }

  public static OffsetTime parseOffsetTime(String isoValue) throws TemporalFormatException {
    return (isoValue != null) ? ParseUtils.parseOffsetTime(isoValue, ISO8601_PATTERNS_TIMEZ) : null;
  }

  public static LocalDateTime parseLocalDateTime(String isoValue) throws TemporalFormatException {
    return (isoValue != null) ? ParseUtils.parseLocalDateTime(isoValue, ISO8601_PATTERNS_DATETIME) : null;
  }

  public static OffsetDateTime parseOffsetDateTime(String isoValue) throws TemporalFormatException {
    return (isoValue != null) ? ParseUtils.parseOffsetDateTime(isoValue, ISO8601_PATTERNS_DATETIMEZ) : null;
  }

  public static YearMonth parseYearMonth(String isoValue) throws TemporalFormatException {
    return (isoValue != null) ? ParseUtils.parseYearMonth(isoValue, ISO8601_YEAR_MONTH) : null;
  }
}
