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

import java.text.DateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.SYS;
import com.appslandia.common.utils.StringFormat;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Language extends InitializeObject {

    public static final Language EN_US = new Language().setLocale(Locale.US).initialize();
    public static final Language VI_VN = new Language().setLocale(new Locale("vi", "VN")).initialize();

    private Locale locale;
    private String languageId;

    private Map<String, String> temporalPatterns = new HashMap<>();
    private Map<String, String> attributes = new HashMap<>();

    @Override
    protected void init() throws Exception {
	AssertUtils.assertNotNull(this.locale, "locale is required.");

	if (this.languageId == null)
	    this.languageId = this.locale.getLanguage();

	String datePattern = this.temporalPatterns.get(DateUtils.ISO8601_DATE);
	if (datePattern != null) {

	    AssertUtils.assertNotNull(datePattern.length() == 10 && datePattern.contains("yyyy") && datePattern.contains("MM") && datePattern.contains("dd"),
		    StringFormat.fmt("datePattern '{}' is invalid (10 length, use yyyy, MM, and dd).", datePattern));
	}

	if (datePattern == null) {
	    datePattern = parseDatePattern(locale);

	    if (datePattern == null) {
		throw new InitializeException("Couldn't determine datePattern for the locale: " + locale);
	    }
	    this.temporalPatterns.put(DateUtils.ISO8601_DATE, datePattern);
	}

	// Other patterns
	this.temporalPatterns.put(DateUtils.ISO8601_YEAR_MONTH, parseYearMonthPattern(datePattern));

	this.temporalPatterns.put(DateUtils.ISO8601_TIME_M, DateUtils.ISO8601_TIME_M);
	this.temporalPatterns.put(DateUtils.ISO8601_TIME_MZ, DateUtils.ISO8601_TIME_MZ);
	this.temporalPatterns.put(DateUtils.ISO8601_TIME_S, DateUtils.ISO8601_TIME_S);
	this.temporalPatterns.put(DateUtils.ISO8601_TIME_SZ, DateUtils.ISO8601_TIME_SZ);
	this.temporalPatterns.put(DateUtils.ISO8601_TIME, DateUtils.ISO8601_TIME);
	this.temporalPatterns.put(DateUtils.ISO8601_TIME_Z, DateUtils.ISO8601_TIME_Z);

	this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_M, StringFormat.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_M));
	this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_MZ, StringFormat.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_MZ));
	this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_S, StringFormat.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_S));
	this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_SZ, StringFormat.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_SZ));
	this.temporalPatterns.put(DateUtils.ISO8601_DATETIME, StringFormat.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME));
	this.temporalPatterns.put(DateUtils.ISO8601_DATETIME_Z, StringFormat.fmt("{} {}", datePattern, DateUtils.ISO8601_TIME_Z));

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
	return AssertUtils.assertNotNull(this.temporalPatterns.get(isoPattern));
    }

    public Language setTemporalPattern(String isoPattern, String localizedPattern) {
	this.assertNotInitialized();
	this.temporalPatterns.put(isoPattern, localizedPattern);
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
	Language another = (Language) obj;
	return this.getLanguageId().equals(another.getLanguageId());
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
	if (__default == null) {
	    synchronized (MUTEX) {
		if (__default == null) {
		    __default = impl;
		    return;
		}
	    }
	}
	throw new IllegalStateException("Language.__default must be null.");
    }

    private static Supplier<Language> __provider;

    public static void setProvider(Supplier<Language> provider) {
	AssertUtils.assertNull(__default);
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

    static String parseDatePattern(Locale locale) {
	DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
	String isoDate = df.format(DateUtils.iso8601Date("3333-11-22"));

	Set<Character> letters = new LinkedHashSet<>();
	Character separator = null;

	for (int i = 0; i < isoDate.length(); i++) {
	    char ch = isoDate.charAt(i);

	    if (ch == '1')
		letters.add('M');
	    else if (ch == '2')
		letters.add('d');
	    else if (ch == '3')
		letters.add('y');
	    else {
		if (separator == null) {
		    if (ch == '-' || ch == '/' || ch == '.')
			separator = ch;
		}
	    }
	}

	// If separator known & letters are yMd
	if ((separator != null) && (letters.size() == 3)) {

	    StringBuilder datePt = new StringBuilder(10);

	    for (Character ch : letters) {
		if (datePt.length() > 0)
		    datePt.append(separator);

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

    static String parseYearMonthPattern(String datePattern) {
	int idx = datePattern.indexOf("dd");
	if (idx == 0)
	    return datePattern.substring(3);

	return datePattern.substring(0, idx - 1) + datePattern.substring(idx + 2);
    }
}
