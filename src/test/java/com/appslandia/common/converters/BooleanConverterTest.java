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

package com.appslandia.common.converters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.Language;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class BooleanConverterTest {

	@Test
	public void test_targetType() {
		BooleanConverter converter = new BooleanConverter();
		Assertions.assertEquals(Boolean.class, converter.getTargetType());
	}

	@Test
	public void test() {
		BooleanConverter converter = new BooleanConverter();
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
		try {
			Boolean v = converter.parse("true", formatProvider);
			Assertions.assertTrue(v);
		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
		try {
			Boolean val = converter.parse("false", formatProvider);
			Assertions.assertFalse(val);
		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		BooleanConverter converter = new BooleanConverter();
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
		try {
			Boolean val = converter.parse(null, formatProvider);
			Assertions.assertNull(val);

			val = converter.parse("", formatProvider);
			Assertions.assertNull(val);
		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalid() {
		BooleanConverter converter = new BooleanConverter();
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
		try {
			converter.parse("yes", formatProvider);
			Assertions.fail();
		} catch (Exception ex) {
			Assertions.assertTrue(ex instanceof ConverterException);
		}
	}
}
