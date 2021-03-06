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

package com.appslandia.common.formatters;

import java.time.OffsetDateTime;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.Language;
import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class OffsetDateTimeFormatterTest {

	@Test
	public void test_argType() {
		OffsetDateTimeFormatter formatter = new OffsetDateTimeFormatter(true);
		Assert.assertEquals(formatter.getArgType(), OffsetDateTime.class);
	}

	@Test
	public void test_pattern() {
		OffsetDateTimeFormatter formatter = new OffsetDateTimeFormatter(true);
		Assert.assertEquals(formatter.getLocalizedPattern(new FormatProviderImpl(Language.EN)), Language.EN.getPatternDateTimeZ());
		Assert.assertEquals(formatter.getIsoPattern(), DateUtils.PATTERN_DATETIME_Z);
	}

	@Test
	public void test() {
		OffsetDateTimeFormatter formatter = new OffsetDateTimeFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			OffsetDateTime v = formatter.parse("10/10/2010 09:30:00.999+07:00", formatProvider);
			Assert.assertNotNull(v);
			Assert.assertEquals(formatter.format(v, formatProvider), "10/10/2010 09:30:00.999+07:00");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_iso() {
		OffsetDateTimeFormatter formatter = new OffsetDateTimeFormatter(false);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			OffsetDateTime v = formatter.parse("2010-10-10T09:30:00.999+07:00", formatProvider);
			Assert.assertNotNull(v);
			Assert.assertEquals(formatter.format(v, formatProvider), "2010-10-10T09:30:00.999+07:00");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		OffsetDateTimeFormatter formatter = new OffsetDateTimeFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			OffsetDateTime v = formatter.parse(null, formatProvider);
			Assert.assertNull(v);

			v = formatter.parse("", formatProvider);
			Assert.assertNull(v);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalid() {
		OffsetDateTimeFormatter formatter = new OffsetDateTimeFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			formatter.parse("15/10/2010 09:30:00.999+07:00", formatProvider);
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}

		try {
			formatter.parse("10/15 /2010 09:30:00.999+07:00", formatProvider);
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}

	@Test
	public void test_invalid_format() {
		OffsetDateTimeFormatter formatter = new OffsetDateTimeFormatter(true);
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			formatter.parse("10-10-2010 09:30:00.999+07:00", formatProvider);
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}
}
