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

package com.appslandia.common.csv;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.appslandia.common.base.Language;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvUtils {

  // CSV Patterns: No 'T'
  private static final Map<String, String> ISO_CSV_PATTERNS;

  static {
    Map<String, String> map = new TreeMap<>();

    final String datePattern = DateUtils.ISO8601_DATE;
    map.put(DateUtils.ISO8601_DATE, DateUtils.ISO8601_DATE);

    map.put(DateUtils.ISO8601_DATETIME_M, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_M));
    map.put(DateUtils.ISO8601_DATETIME_S, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_S));

    map.put(DateUtils.ISO8601_DATETIME_F1, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F1));
    map.put(DateUtils.ISO8601_DATETIME_F2, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F2));
    map.put(DateUtils.ISO8601_DATETIME_F3, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F3));
    map.put(DateUtils.ISO8601_DATETIME_F4, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F4));
    map.put(DateUtils.ISO8601_DATETIME_F5, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F5));
    map.put(DateUtils.ISO8601_DATETIME_F6, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F6));
    map.put(DateUtils.ISO8601_DATETIME_F7, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F7));

    map.put(DateUtils.ISO8601_DATETIMEZ_M, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_M));
    map.put(DateUtils.ISO8601_DATETIMEZ_S, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_S));

    map.put(DateUtils.ISO8601_DATETIMEZ_F1, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F1));
    map.put(DateUtils.ISO8601_DATETIMEZ_F2, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F2));
    map.put(DateUtils.ISO8601_DATETIMEZ_F3, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F3));
    map.put(DateUtils.ISO8601_DATETIMEZ_F4, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F4));
    map.put(DateUtils.ISO8601_DATETIMEZ_F5, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F5));
    map.put(DateUtils.ISO8601_DATETIMEZ_F6, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F6));
    map.put(DateUtils.ISO8601_DATETIMEZ_F7, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F7));

    map.put(DateUtils.ISO8601_TIME_M, DateUtils.ISO8601_TIME_M);
    map.put(DateUtils.ISO8601_TIME_S, DateUtils.ISO8601_TIME_S);

    map.put(DateUtils.ISO8601_TIME_F1, DateUtils.ISO8601_TIME_F1);
    map.put(DateUtils.ISO8601_TIME_F2, DateUtils.ISO8601_TIME_F2);
    map.put(DateUtils.ISO8601_TIME_F3, DateUtils.ISO8601_TIME_F3);
    map.put(DateUtils.ISO8601_TIME_F4, DateUtils.ISO8601_TIME_F4);
    map.put(DateUtils.ISO8601_TIME_F5, DateUtils.ISO8601_TIME_F5);
    map.put(DateUtils.ISO8601_TIME_F6, DateUtils.ISO8601_TIME_F6);
    map.put(DateUtils.ISO8601_TIME_F7, DateUtils.ISO8601_TIME_F7);

    map.put(DateUtils.ISO8601_TIMEZ_M, DateUtils.ISO8601_TIMEZ_M);
    map.put(DateUtils.ISO8601_TIMEZ_S, DateUtils.ISO8601_TIMEZ_S);

    map.put(DateUtils.ISO8601_TIMEZ_F1, DateUtils.ISO8601_TIMEZ_F1);
    map.put(DateUtils.ISO8601_TIMEZ_F2, DateUtils.ISO8601_TIMEZ_F2);
    map.put(DateUtils.ISO8601_TIMEZ_F3, DateUtils.ISO8601_TIMEZ_F3);
    map.put(DateUtils.ISO8601_TIMEZ_F4, DateUtils.ISO8601_TIMEZ_F4);
    map.put(DateUtils.ISO8601_TIMEZ_F5, DateUtils.ISO8601_TIMEZ_F5);
    map.put(DateUtils.ISO8601_TIMEZ_F6, DateUtils.ISO8601_TIMEZ_F6);
    map.put(DateUtils.ISO8601_TIMEZ_F7, DateUtils.ISO8601_TIMEZ_F7);

    ISO_CSV_PATTERNS = Collections.unmodifiableMap(map);
  }

  public static String getCsvDtPattern(String isoDtPattern) {
    return Asserts.notNull(ISO_CSV_PATTERNS.get(isoDtPattern));
  }

  private static final Language DEFAULT_LANGUAGE;
  static {
    String defDatePattern = DateUtils.toDatePattern(Locale.getDefault());
    DEFAULT_LANGUAGE = (defDatePattern != null)
        ? new Language().setLocale(Locale.getDefault()).setDatePattern(defDatePattern)
        : null;
  }

  public static Collection<String> PATTERNS_DATE;
  static {
    PATTERNS_DATE = toCsvDtPatterns(new TreeSet<>(), Arrays.asList(DateUtils.ISO8601_DATE));
  }

  public static Collection<String> PATTERNS_TIME;
  static {
    PATTERNS_TIME = toCsvDtPatterns(new TreeSet<>(), DateUtils.ISO8601_PATTERNS_TIME);
  }

  public static Collection<String> PATTERNS_TIMEZ;
  static {
    PATTERNS_TIMEZ = toCsvDtPatterns(new TreeSet<>(), DateUtils.ISO8601_PATTERNS_TIMEZ);
  }

  public static Collection<String> PATTERNS_DATETIME;
  static {
    PATTERNS_DATETIME = toCsvDtPatterns(new TreeSet<>(), DateUtils.ISO8601_PATTERNS_DATETIME);
  }

  public static Collection<String> PATTERNS_DATETIMEZ;
  static {
    PATTERNS_DATETIMEZ = toCsvDtPatterns(new TreeSet<>(), DateUtils.ISO8601_PATTERNS_DATETIMEZ);
  }

  private static Set<String> toCsvDtPatterns(Set<String> csvDtPatterns, Collection<String> isoDtPatterns) {
    for (String isoDtPattern : isoDtPatterns) {
      var mappedPattern = getCsvDtPattern(isoDtPattern);
      csvDtPatterns.add(mappedPattern);

      if (DEFAULT_LANGUAGE != null) {
        csvDtPatterns.add(DEFAULT_LANGUAGE.getTemporalPattern(isoDtPattern));
      }
      csvDtPatterns.add(isoDtPattern);
    }
    return Collections.unmodifiableSet(csvDtPatterns);
  }
}
