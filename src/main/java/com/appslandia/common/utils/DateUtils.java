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
import java.time.temporal.ChronoUnit;
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
 * @author Loc Ha
 *
 */
public class DateUtils {

  // ISO8601
  public static final String ISO8601_DATE = "yyyy-MM-dd";
  public static final String ISO8601_YEAR_MONTH = "yyyy-MM";

  public static final String ISO8601_TIME_M = "HH:mm";
  public static final String ISO8601_TIME_S = "HH:mm:ss";
  public static final String ISO8601_TIME_F1 = "HH:mm:ss.S";
  public static final String ISO8601_TIME_F2 = "HH:mm:ss.SS";
  public static final String ISO8601_TIME_F3 = "HH:mm:ss.SSS";
  public static final String ISO8601_TIME_F4 = "HH:mm:ss.SSSS";
  public static final String ISO8601_TIME_F5 = "HH:mm:ss.SSSSS";
  public static final String ISO8601_TIME_F6 = "HH:mm:ss.SSSSSS";
  public static final String ISO8601_TIME_F7 = "HH:mm:ss.SSSSSSS";

  public static final String ISO8601_TIMEZ_M = "HH:mmXXX";
  public static final String ISO8601_TIMEZ_S = "HH:mm:ssXXX";
  public static final String ISO8601_TIMEZ_F1 = "HH:mm:ss.SXXX";
  public static final String ISO8601_TIMEZ_F2 = "HH:mm:ss.SSXXX";
  public static final String ISO8601_TIMEZ_F3 = "HH:mm:ss.SSSXXX";
  public static final String ISO8601_TIMEZ_F4 = "HH:mm:ss.SSSSXXX";
  public static final String ISO8601_TIMEZ_F5 = "HH:mm:ss.SSSSSXXX";
  public static final String ISO8601_TIMEZ_F6 = "HH:mm:ss.SSSSSSXXX";
  public static final String ISO8601_TIMEZ_F7 = "HH:mm:ss.SSSSSSSXXX";

  public static final String ISO8601_DATETIME_M = "yyyy-MM-dd'T'HH:mm";
  public static final String ISO8601_DATETIME_S = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String ISO8601_DATETIME_F1 = "yyyy-MM-dd'T'HH:mm:ss.S";
  public static final String ISO8601_DATETIME_F2 = "yyyy-MM-dd'T'HH:mm:ss.SS";
  public static final String ISO8601_DATETIME_F3 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  public static final String ISO8601_DATETIME_F4 = "yyyy-MM-dd'T'HH:mm:ss.SSSS";
  public static final String ISO8601_DATETIME_F5 = "yyyy-MM-dd'T'HH:mm:ss.SSSSS";
  public static final String ISO8601_DATETIME_F6 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
  public static final String ISO8601_DATETIME_F7 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS";

  public static final String ISO8601_DATETIMEZ_M = "yyyy-MM-dd'T'HH:mmXXX";
  public static final String ISO8601_DATETIMEZ_S = "yyyy-MM-dd'T'HH:mm:ssXXX";
  public static final String ISO8601_DATETIMEZ_F1 = "yyyy-MM-dd'T'HH:mm:ss.SXXX";
  public static final String ISO8601_DATETIMEZ_F2 = "yyyy-MM-dd'T'HH:mm:ss.SSXXX";
  public static final String ISO8601_DATETIMEZ_F3 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  public static final String ISO8601_DATETIMEZ_F4 = "yyyy-MM-dd'T'HH:mm:ss.SSSSXXX";
  public static final String ISO8601_DATETIMEZ_F5 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSXXX";
  public static final String ISO8601_DATETIMEZ_F6 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX";
  public static final String ISO8601_DATETIMEZ_F7 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX";

  public static long clearMs(long timeInMs) {
    return (timeInMs / 1000) * 1000;
  }

  public static boolean isExpired(long timeInMs, long leewayMs) {
    return System.currentTimeMillis() - timeInMs > leewayMs;
  }

  public static long betweenMs(LocalDateTime ldt1, LocalDateTime ldt2) {
    return ChronoUnit.MILLIS.between(ldt1, ldt2);
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

    var day = dateID % 100;
    dateID /= 100;

    var month = dateID % 100;
    var year = dateID / 100;

    return LocalDate.of(year, month, day);
  }

  // yyyyMMdd
  public static Integer toWeekID(LocalDate ld, Locale locale) {
    Arguments.notNull(locale);
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

    var month = monthID % 100;
    var year = monthID / 100;

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

    var minute = (int) (dateTimeID % 100);
    dateTimeID /= 100;

    var hour = (int) (dateTimeID % 100);
    dateTimeID /= 100;

    var day = (int) (dateTimeID % 100);
    dateTimeID /= 100;

    var month = (int) (dateTimeID % 100);
    var year = (int) (dateTimeID / 100);

    return LocalDateTime.of(year, month, day, hour, minute);
  }

  public static LocalDateTime toTimeAtUtc(Long timeMillis) {
    return toTimeAt(timeMillis, ZoneOffset.UTC);
  }

  public static LocalDateTime toTimeAt(Long timeMillis, ZoneOffset offset) {
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

  public static LocalDateTime timeAtUtcF3() {
    return timeAtUtc().truncatedTo(ChronoUnit.MILLIS);
  }

  public static OffsetDateTime nowAtUtcF3() {
    return nowAtUtc().truncatedTo(ChronoUnit.MILLIS);
  }

  public static LocalDateTime timeAtUtc() {
    return timeAt(ZoneOffset.UTC);
  }

  public static OffsetDateTime nowAtUtc() {
    return nowAt(ZoneOffset.UTC);
  }

  public static LocalDateTime timeAt(String zoneId) {
    return (zoneId != null) ? timeAt(ZoneId.of(zoneId)) : timeAt((ZoneId) null);
  }

  public static LocalDateTime timeAt(ZoneId zoneId) {
    return (zoneId != null) ? LocalDateTime.now(zoneId) : LocalDateTime.now();
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

  public static OffsetDateTime toSameInstantUtc(OffsetDateTime odt) {
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
    Arguments.notNull(locale);
    if (ld == null) {
      return null;
    }
    var weekFields = WeekFields.of(locale);
    return ld.with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));
  }

  public static LocalDate lastDayOfWeek(LocalDate ld, Locale locale) {
    Arguments.notNull(locale);
    if (ld == null) {
      return null;
    }
    return firstDayOfWeek(ld, locale).plusDays(6);
  }

  // 1w 2d 3h 4m 50s 500ms
  private static final Pattern TEMPORAL_AMT_PATTERN = Pattern.compile("((\\d+.\\d+|\\d+)(w|d|h|m|s|ms)\\s*)+",
      Pattern.CASE_INSENSITIVE);

  public static long translateToMs(String temporalAmt) {
    Arguments.notNull(temporalAmt, "temporalAmt is required.");
    Arguments.isTrue(TEMPORAL_AMT_PATTERN.matcher(temporalAmt).matches(), "temporalAmt '{}' is invalid.", temporalAmt);

    double result = 0l;
    var i = 0;

    while (i < temporalAmt.length()) {
      var j = i;
      while (Character.isDigit(temporalAmt.charAt(j)) || (temporalAmt.charAt(j) == '.')) {
        j++;
      }
      var k = j;
      while ((k <= temporalAmt.length() - 1)
          && (Character.isLetter(temporalAmt.charAt(k)) || (temporalAmt.charAt(k) == ' '))) {
        k++;
      }

      var amt = Double.parseDouble(temporalAmt.substring(i, j));
      var unit = temporalAmt.substring(j, k).strip().toLowerCase(Locale.ENGLISH);

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
    Arguments.isTrue(highUnit.compareTo(lowUnit) >= 0);
    Map<TimeUnit, Long> res = new EnumMap<>(TimeUnit.class);

    duration = lowUnit.convert(duration, unit);
    var u = highUnit;

    while (true) {
      var v = u.convert(duration, lowUnit);
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
    return switch (unit) {
    case DAYS -> TimeUnit.HOURS;
    case HOURS -> TimeUnit.MINUTES;
    case MINUTES -> TimeUnit.SECONDS;
    case SECONDS -> TimeUnit.MILLISECONDS;
    case MILLISECONDS -> TimeUnit.MICROSECONDS;
    case MICROSECONDS -> TimeUnit.NANOSECONDS;
    default -> throw new Error();
    };
  }

  public static final Collection<String> ISO8601_PATTERNS_TIME = CollectionUtils.unmodifiableSet(new TreeSet<>(),
      ISO8601_TIME_M, ISO8601_TIME_S, ISO8601_TIME_F1, ISO8601_TIME_F2, ISO8601_TIME_F3, ISO8601_TIME_F4,
      ISO8601_TIME_F5, ISO8601_TIME_F6, ISO8601_TIME_F7);

  public static final Collection<String> ISO8601_PATTERNS_TIMEZ = CollectionUtils.unmodifiableSet(new TreeSet<>(),
      ISO8601_TIMEZ_M, ISO8601_TIMEZ_S, ISO8601_TIMEZ_F1, ISO8601_TIMEZ_F2, ISO8601_TIMEZ_F3, ISO8601_TIMEZ_F4,
      ISO8601_TIMEZ_F5, ISO8601_TIMEZ_F6, ISO8601_TIMEZ_F7);

  public static final Collection<String> ISO8601_PATTERNS_DATETIME = CollectionUtils.unmodifiableSet(new TreeSet<>(),
      ISO8601_DATETIME_M, ISO8601_DATETIME_S, ISO8601_DATETIME_F1, ISO8601_DATETIME_F2, ISO8601_DATETIME_F3,
      ISO8601_DATETIME_F4, ISO8601_DATETIME_F5, ISO8601_DATETIME_F6, ISO8601_DATETIME_F7);

  public static final Collection<String> ISO8601_PATTERNS_DATETIMEZ = CollectionUtils.unmodifiableSet(new TreeSet<>(),
      ISO8601_DATETIMEZ_M, ISO8601_DATETIMEZ_S, ISO8601_DATETIMEZ_F1, ISO8601_DATETIMEZ_F2, ISO8601_DATETIMEZ_F3,
      ISO8601_DATETIMEZ_F4, ISO8601_DATETIMEZ_F5, ISO8601_DATETIMEZ_F6, ISO8601_DATETIMEZ_F7);

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

  public static boolean isInDatePattern(String datePattern) {
    Arguments.notNull(datePattern);

    if ((datePattern.length() != 10) || !datePattern.contains("dd") || !datePattern.contains("MM")
        || !datePattern.contains("yyyy")) {
      return false;
    }

    // separators
    var separators = new HashMap<Character, Integer>(2);
    char sep = 0;

    for (var i = 0; i < datePattern.length(); i++) {
      var c = datePattern.charAt(i);

      if (c != 'd' && c != 'M' && c != 'y') {
        separators.compute(c, (k, v) -> v != null ? v + 1 : 1);

        sep = c;
      }
    }

    if ((separators.size() != 1) || (separators.get(sep) != 2)) {
      return false;
    }
    if (sep != '-' && sep != '/' && sep != '.') {
      return false;
    }
    return true;
  }

  public static String parseInDatePattern(Locale locale) {
    // isoDate
    String isoDate = null;
    try {
      var df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
      isoDate = df.format(new SimpleDateFormat(DateUtils.ISO8601_DATE).parse("3333-11-22"));
    } catch (ParseException ex) {
      throw new Error(ex);
    }

    // Parse letters and separator
    Set<Character> letters = new LinkedHashSet<>();
    Character separator = null;

    for (var i = 0; i < isoDate.length(); i++) {
      var ch = isoDate.charAt(i);
      if (ch == ' ') {
        continue;
      } else if (ch == '1') {
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
      var datePt = new StringBuilder(10);

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
}
