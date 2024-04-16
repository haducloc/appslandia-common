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
import java.util.function.Supplier;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.SYS;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Language extends InitializeObject {

  public static final Language EN_US = new Language().setLocale(Locale.US).initialize();
  public static final Language VI_VN = new Language().setLocale(Locale.of("vi", "VN")).initialize();

  private Locale locale;
  private String languageId;

  private Map<String, String> temporalPatterns = new HashMap<>();
  private Map<String, String> attributes = new HashMap<>();

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.locale, "locale is required.");

    if (this.languageId == null) {
      this.languageId = this.locale.getLanguage();
    }

    String datePattern = this.temporalPatterns.get(DateUtils.ISO8601_DATE);
    if (datePattern != null) {
      Asserts.isTrue(DateUtils.isInputDatePatternValid(datePattern),
          STR.fmt("The datePattern is invalid: '{}'.", datePattern));
    }

    if (datePattern == null) {
      datePattern = DateUtils.toDatePattern(this.locale);
      Asserts.notNull(datePattern, STR.fmt("Couldn't determine datePattern for the locale '{}'.", this.locale));

      this.temporalPatterns.put(DateUtils.ISO8601_DATE, datePattern);
    }

    // Other patterns
    this.temporalPatterns.put(DateUtils.ISO8601_YEAR_MONTH, parseYearMonthPattern(datePattern));

    this.temporalPatterns.put(DateUtils.ISO8601_TIME_M, DateUtils.ISO8601_TIME_M);
    this.temporalPatterns.put(DateUtils.ISO8601_TIME_S, DateUtils.ISO8601_TIME_S);
    this.temporalPatterns.put(DateUtils.ISO8601_TIME_N1, DateUtils.ISO8601_TIME_N1);
    this.temporalPatterns.put(DateUtils.ISO8601_TIME_N2, DateUtils.ISO8601_TIME_N2);
    this.temporalPatterns.put(DateUtils.ISO8601_TIME_N3, DateUtils.ISO8601_TIME_N3);
    this.temporalPatterns.put(DateUtils.ISO8601_TIME_N4, DateUtils.ISO8601_TIME_N4);
    this.temporalPatterns.put(DateUtils.ISO8601_TIME_N5, DateUtils.ISO8601_TIME_N5);
    this.temporalPatterns.put(DateUtils.ISO8601_TIME_N6, DateUtils.ISO8601_TIME_N6);
    this.temporalPatterns.put(DateUtils.ISO8601_TIME_N7, DateUtils.ISO8601_TIME_N7);

    this.temporalPatterns.put(DateUtils.ISO8601_TIMEZ_M, DateUtils.ISO8601_TIMEZ_M);
    this.temporalPatterns.put(DateUtils.ISO8601_TIMEZ_S, DateUtils.ISO8601_TIMEZ_S);
    this.temporalPatterns.put(DateUtils.ISO8601_TIMEZ_N1, DateUtils.ISO8601_TIMEZ_N1);
    this.temporalPatterns.put(DateUtils.ISO8601_TIMEZ_N2, DateUtils.ISO8601_TIMEZ_N2);
    this.temporalPatterns.put(DateUtils.ISO8601_TIMEZ_N3, DateUtils.ISO8601_TIMEZ_N3);
    this.temporalPatterns.put(DateUtils.ISO8601_TIMEZ_N4, DateUtils.ISO8601_TIMEZ_N4);
    this.temporalPatterns.put(DateUtils.ISO8601_TIMEZ_N5, DateUtils.ISO8601_TIMEZ_N5);
    this.temporalPatterns.put(DateUtils.ISO8601_TIMEZ_N6, DateUtils.ISO8601_TIMEZ_N6);
    this.temporalPatterns.put(DateUtils.ISO8601_TIMEZ_N7, DateUtils.ISO8601_TIMEZ_N7);

    this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_M, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_M));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_S, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_S));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_N1, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_N1));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_N2, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_N2));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_N3, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_N3));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_N4, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_N4));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_N5, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_N5));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_N6, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_N6));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_N7, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_N7));

    this.temporalPatterns.put(DateUtils.ISO8601_DATETIMEZ_M, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_M));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIMEZ_S, STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_S));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIMEZ_N1,
        STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_N1));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIMEZ_N2,
        STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_N2));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIMEZ_N3,
        STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_N3));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIMEZ_N4,
        STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_N4));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIMEZ_N5,
        STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_N5));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIMEZ_N6,
        STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_N6));
    this.temporalPatterns.put(DateUtils.ISO8601_DATETIMEZ_N7,
        STR.fmt("{} {}", datePattern, DateUtils.ISO8601_TIMEZ_N7));

    this.temporalPatterns = Collections.unmodifiableMap(this.temporalPatterns);

    if (this.attributes != null) {
      this.attributes = Collections.unmodifiableMap(this.attributes);
    }
  }

  @Override
  public Language initialize() throws InitializeException {
    super.initialize();
    return this;
  }

  public String getLanguageId() {
    this.initialize();
    return this.languageId;
  }

  public Language setLanguageId(String languageId) {
    this.assertNotInitialized();
    this.languageId = languageId;
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

  public Language setDatePattern(String datePattern) {
    this.assertNotInitialized();
    this.temporalPatterns.put(DateUtils.ISO8601_DATE, datePattern);
    return this;
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
    return this.getLanguageId().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Language)) {
      return false;
    }
    Language other = (Language) obj;
    return this.getLanguageId().equals(other.getLanguageId());
  }

  private static volatile Language __default;
  private static final Object MUTEX = new Object();

  public static Language getDefault() {
    Language obj = __default;
    if (obj == null) {
      synchronized (MUTEX) {
        if ((obj = __default) == null) {
          __default = obj = initLanguage();
        }
      }
    }
    return obj;
  }

  public static void setDefault(Language impl) {
    Asserts.isNull(__default, "Language.__default must be null.");

    if (__default == null) {
      synchronized (MUTEX) {
        if (__default == null) {
          __default = impl;
          return;
        }
      }
    }
  }

  private static Supplier<Language> __provider;

  public static void setProvider(Supplier<Language> provider) {
    Asserts.isNull(__default);
    __provider = provider;
  }

  @SuppressWarnings("el-syntax")
  private static Language initLanguage() {
    if (__provider != null) {
      return __provider.get();
    }
    try {
      String implName = SYS.resolve("${language_impl,env.LANGUAGE_IMPL}");
      if (implName == null) {
        return EN_US;
      }
      Class<? extends Language> implClass = ReflectionUtils.loadClass(implName, null);
      return ReflectionUtils.newInstance(implClass);

    } catch (Exception ex) {
      throw new InitializeException(ex);
    }
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
