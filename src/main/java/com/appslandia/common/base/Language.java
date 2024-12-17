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

package com.appslandia.common.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Language extends InitializeObject {

  public static final Language EN_US = new Language().setLocale(Locale.US).initialize();
  public static final Language VI_VN = new Language().setLocale(Locale.of("vi", "VN")).initialize();

  private Locale locale;
  private String id;

  private Map<String, String> temporalPatterns = new HashMap<>();
  private Map<String, String> attributes = new HashMap<>();

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.locale, "locale is required.");

    if (this.id == null) {
      this.id = this.locale.getLanguage();
    }
    Map<String, String> patterns = new HashMap<>();
    String datePattern = InputDatePattern.getDefault().parse(this.locale);

    patterns.put(DateUtils.ISO8601_DATE, datePattern);
    patterns.put(DateUtils.ISO8601_YEAR_MONTH, parseYearMonthPattern(datePattern));

    patterns.put(DateUtils.ISO8601_TIME_M, DateUtils.ISO8601_TIME_M);
    patterns.put(DateUtils.ISO8601_TIME_S, DateUtils.ISO8601_TIME_S);
    patterns.put(DateUtils.ISO8601_TIME_F1, DateUtils.ISO8601_TIME_F1);
    patterns.put(DateUtils.ISO8601_TIME_F2, DateUtils.ISO8601_TIME_F2);
    patterns.put(DateUtils.ISO8601_TIME_F3, DateUtils.ISO8601_TIME_F3);
    patterns.put(DateUtils.ISO8601_TIME_F4, DateUtils.ISO8601_TIME_F4);
    patterns.put(DateUtils.ISO8601_TIME_F5, DateUtils.ISO8601_TIME_F5);
    patterns.put(DateUtils.ISO8601_TIME_F6, DateUtils.ISO8601_TIME_F6);
    patterns.put(DateUtils.ISO8601_TIME_F7, DateUtils.ISO8601_TIME_F7);

    patterns.put(DateUtils.ISO8601_TIMEZ_M, DateUtils.ISO8601_TIMEZ_M);
    patterns.put(DateUtils.ISO8601_TIMEZ_S, DateUtils.ISO8601_TIMEZ_S);
    patterns.put(DateUtils.ISO8601_TIMEZ_F1, DateUtils.ISO8601_TIMEZ_F1);
    patterns.put(DateUtils.ISO8601_TIMEZ_F2, DateUtils.ISO8601_TIMEZ_F2);
    patterns.put(DateUtils.ISO8601_TIMEZ_F3, DateUtils.ISO8601_TIMEZ_F3);
    patterns.put(DateUtils.ISO8601_TIMEZ_F4, DateUtils.ISO8601_TIMEZ_F4);
    patterns.put(DateUtils.ISO8601_TIMEZ_F5, DateUtils.ISO8601_TIMEZ_F5);
    patterns.put(DateUtils.ISO8601_TIMEZ_F6, DateUtils.ISO8601_TIMEZ_F6);
    patterns.put(DateUtils.ISO8601_TIMEZ_F7, DateUtils.ISO8601_TIMEZ_F7);

    patterns.put(DateUtils.ISO8601_DATETIME_M, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_M));
    patterns.put(DateUtils.ISO8601_DATETIME_S, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_S));
    patterns.put(DateUtils.ISO8601_DATETIME_F1, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F1));
    patterns.put(DateUtils.ISO8601_DATETIME_F2, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F2));
    patterns.put(DateUtils.ISO8601_DATETIME_F3, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F3));
    patterns.put(DateUtils.ISO8601_DATETIME_F4, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F4));
    patterns.put(DateUtils.ISO8601_DATETIME_F5, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F5));
    patterns.put(DateUtils.ISO8601_DATETIME_F6, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F6));
    patterns.put(DateUtils.ISO8601_DATETIME_F7, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_F7));

    patterns.put(DateUtils.ISO8601_DATETIMEZ_M, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_M));
    patterns.put(DateUtils.ISO8601_DATETIMEZ_S, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_S));
    patterns.put(DateUtils.ISO8601_DATETIMEZ_F1, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F1));
    patterns.put(DateUtils.ISO8601_DATETIMEZ_F2, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F2));
    patterns.put(DateUtils.ISO8601_DATETIMEZ_F3, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F3));
    patterns.put(DateUtils.ISO8601_DATETIMEZ_F4, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F4));
    patterns.put(DateUtils.ISO8601_DATETIMEZ_F5, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F5));
    patterns.put(DateUtils.ISO8601_DATETIMEZ_F6, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F6));
    patterns.put(DateUtils.ISO8601_DATETIMEZ_F7, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_F7));

    this.temporalPatterns = Collections.unmodifiableMap(patterns);

    if (this.attributes != null) {
      this.attributes = Collections.unmodifiableMap(this.attributes);
    }
  }

  @Override
  public Language initialize() throws InitializeException {
    super.initialize();
    return this;
  }

  public String getId() {
    this.initialize();
    return this.id;
  }

  public Language setId(String id) {
    this.assertNotInitialized();
    this.id = id;
    return this;
  }

  public String getDisplayName() {
    this.initialize();
    return this.locale.getDisplayLanguage(this.locale);
  }

  public Locale getLocale() {
    this.initialize();
    return this.locale;
  }

  public Language setLocale(Locale locale) {
    this.assertNotInitialized();
    this.locale = locale;
    return this;
  }

  public String getTemporalPattern(String isoPattern) {
    this.initialize();
    return Asserts.notNull(this.temporalPatterns.get(isoPattern));
  }

  public String getAttribute(String name) {
    this.initialize();
    return (this.attributes != null) ? this.attributes.get(name) : null;
  }

  public Language setAttribute(String name, String value) {
    this.assertNotInitialized();
    if (this.attributes == null) {
      this.attributes = new HashMap<>();
    }
    this.attributes.put(name, value);
    return this;
  }

  @Override
  public int hashCode() {
    return this.getId().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Language)) {
      return false;
    }
    Language that = (Language) obj;
    return this.getId().equals(that.getId());
  }

  private static volatile Language __default;
  private static final Object MUTEX = new Object();

  public static Language getDefault() {
    Language obj = __default;
    if (obj == null) {
      synchronized (MUTEX) {
        if ((obj = __default) == null) {
          __default = obj = initDefault();
        }
      }
    }
    return obj;
  }

  public static void setDefault(Language impl) {
    Asserts.isNull(__default, "Language.__default must be null.");
    __default = impl;
  }

  private static Language initDefault() {
    if (Locale.US.equals(Locale.getDefault())) {
      return EN_US;
    }
    return new Language().setLocale(Locale.getDefault()).initialize();
  }

  // The datePattern includes "dd" "MM" and "yyyy" in any order.

  static String parseYearMonthPattern(String datePattern) {
    int idx = datePattern.indexOf("dd");
    if (idx == 0) {
      return datePattern.substring(3);
    }
    return datePattern.substring(0, idx - 1) + datePattern.substring(idx + 2);
  }
}
