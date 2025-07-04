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

package com.appslandia.common.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class TemporalPatterns extends InitializeObject {

  private static final Map<String, String> ISO_PATTERNS;

  static {
    Map<String, String> isoMap = new TreeMap<>();

    final var datePattern = DateUtils.ISO8601_DATE;
    isoMap.put(DateUtils.ISO8601_DATE, DateUtils.ISO8601_DATE);

    isoMap.put(DateUtils.ISO8601_DATETIME_M, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_M));
    isoMap.put(DateUtils.ISO8601_DATETIME_S, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_S));

    isoMap.put(DateUtils.ISO8601_DATETIME_F1, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F1));
    isoMap.put(DateUtils.ISO8601_DATETIME_F2, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F2));
    isoMap.put(DateUtils.ISO8601_DATETIME_F3, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F3));
    isoMap.put(DateUtils.ISO8601_DATETIME_F4, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F4));
    isoMap.put(DateUtils.ISO8601_DATETIME_F5, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F5));
    isoMap.put(DateUtils.ISO8601_DATETIME_F6, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F6));
    isoMap.put(DateUtils.ISO8601_DATETIME_F7, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F7));

    isoMap.put(DateUtils.ISO8601_DATETIMEZ_M, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_M));
    isoMap.put(DateUtils.ISO8601_DATETIMEZ_S, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_S));

    isoMap.put(DateUtils.ISO8601_DATETIMEZ_F1, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F1));
    isoMap.put(DateUtils.ISO8601_DATETIMEZ_F2, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F2));
    isoMap.put(DateUtils.ISO8601_DATETIMEZ_F3, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F3));
    isoMap.put(DateUtils.ISO8601_DATETIMEZ_F4, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F4));
    isoMap.put(DateUtils.ISO8601_DATETIMEZ_F5, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F5));
    isoMap.put(DateUtils.ISO8601_DATETIMEZ_F6, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F6));
    isoMap.put(DateUtils.ISO8601_DATETIMEZ_F7, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F7));

    isoMap.put(DateUtils.ISO8601_TIME_M, DateUtils.ISO8601_TIME_M);
    isoMap.put(DateUtils.ISO8601_TIME_S, DateUtils.ISO8601_TIME_S);

    isoMap.put(DateUtils.ISO8601_TIME_F1, DateUtils.ISO8601_TIME_F1);
    isoMap.put(DateUtils.ISO8601_TIME_F2, DateUtils.ISO8601_TIME_F2);
    isoMap.put(DateUtils.ISO8601_TIME_F3, DateUtils.ISO8601_TIME_F3);
    isoMap.put(DateUtils.ISO8601_TIME_F4, DateUtils.ISO8601_TIME_F4);
    isoMap.put(DateUtils.ISO8601_TIME_F5, DateUtils.ISO8601_TIME_F5);
    isoMap.put(DateUtils.ISO8601_TIME_F6, DateUtils.ISO8601_TIME_F6);
    isoMap.put(DateUtils.ISO8601_TIME_F7, DateUtils.ISO8601_TIME_F7);

    isoMap.put(DateUtils.ISO8601_TIMEZ_M, DateUtils.ISO8601_TIMEZ_M);
    isoMap.put(DateUtils.ISO8601_TIMEZ_S, DateUtils.ISO8601_TIMEZ_S);

    isoMap.put(DateUtils.ISO8601_TIMEZ_F1, DateUtils.ISO8601_TIMEZ_F1);
    isoMap.put(DateUtils.ISO8601_TIMEZ_F2, DateUtils.ISO8601_TIMEZ_F2);
    isoMap.put(DateUtils.ISO8601_TIMEZ_F3, DateUtils.ISO8601_TIMEZ_F3);
    isoMap.put(DateUtils.ISO8601_TIMEZ_F4, DateUtils.ISO8601_TIMEZ_F4);
    isoMap.put(DateUtils.ISO8601_TIMEZ_F5, DateUtils.ISO8601_TIMEZ_F5);
    isoMap.put(DateUtils.ISO8601_TIMEZ_F6, DateUtils.ISO8601_TIMEZ_F6);
    isoMap.put(DateUtils.ISO8601_TIMEZ_F7, DateUtils.ISO8601_TIMEZ_F7);

    ISO_PATTERNS = Collections.unmodifiableMap(isoMap);
  }

  public static final TemporalPatterns DEFAULT = new TemporalPatterns();

  private Language language;

  private Collection<String> datePatterns;
  private Collection<String> timePatterns;
  private Collection<String> dateTimePatterns;

  private Collection<String> offsetTimePatterns;
  private Collection<String> offsetDateTimePatterns;

  @Override
  protected void init() throws Exception {
    if (this.language == null) {
      this.language = new Language().setLocale(Locale.getDefault()).initialize();
    }

    this.datePatterns = toDtPatterns(new LinkedHashSet<>(), Arrays.asList(DateUtils.ISO8601_DATE));
    this.timePatterns = toDtPatterns(new LinkedHashSet<>(), DateUtils.ISO8601_PATTERNS_TIME);
    this.dateTimePatterns = toDtPatterns(new LinkedHashSet<>(), DateUtils.ISO8601_PATTERNS_DATETIME);

    this.offsetTimePatterns = toDtPatterns(new LinkedHashSet<>(), DateUtils.ISO8601_PATTERNS_TIMEZ);
    this.offsetDateTimePatterns = toDtPatterns(new LinkedHashSet<>(), DateUtils.ISO8601_PATTERNS_DATETIMEZ);
  }

  @Override
  public TemporalPatterns initialize() throws InitializeException {
    super.initialize();
    return this;
  }

  public Language getLanguage() {
    initialize();
    return this.language;
  }

  public TemporalPatterns setLanguage(Language language) {
    assertNotInitialized();
    this.language = language;
    return this;
  }

  public TemporalPatterns setLocale(Locale locale) {
    assertNotInitialized();
    if (locale != null) {
      this.language = new Language().setLocale(locale).initialize();
    }
    return this;
  }

  public Collection<String> getDatePatterns() {
    initialize();
    return this.datePatterns;
  }

  public Collection<String> getTimePatterns() {
    initialize();
    return this.timePatterns;
  }

  public Collection<String> getDateTimePatterns() {
    initialize();
    return this.dateTimePatterns;
  }

  public Collection<String> getOffsetTimePatterns() {
    initialize();
    return this.offsetTimePatterns;
  }

  public Collection<String> getOffsetDateTimePatterns() {
    initialize();
    return this.offsetDateTimePatterns;
  }

  protected Set<String> toDtPatterns(Set<String> dtPatterns, Collection<String> isoDtPatterns) {
    for (String isoDtPattern : isoDtPatterns) {
      dtPatterns.add(isoDtPattern);
      dtPatterns.add(ISO_PATTERNS.get(isoDtPattern));
      dtPatterns.add(this.language.getTemporalPattern(isoDtPattern));
    }
    return Collections.unmodifiableSet(dtPatterns);
  }

  public static String getCsvIsoPattern(String isoPattern) {
    var pattern = ISO_PATTERNS.get(isoPattern);
    if (pattern == null) {
      throw new IllegalArgumentException(STR.fmt("The given isoPattern '{}' is invalid or unsupported."));
    }
    return pattern;
  }
}
