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
public class IntegerConverterTest {

	@Test
	public void test_targetType() {
		IntegerConverter converter = new IntegerConverter();
		Assertions.assertEquals(Integer.class, converter.getTargetType());
	}

	@Test
	public void test() {
		IntegerConverter converter = new IntegerConverter();
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
		try {
			Integer v = converter.parse("12345", formatProvider);
			Assertions.assertEquals(12345, v.intValue());

		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		IntegerConverter converter = new IntegerConverter();
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
		try {
			Integer val = converter.parse(null, formatProvider);
			Assertions.assertNull(val);

			val = converter.parse("", formatProvider);
			Assertions.assertNull(val);
		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalid() {
		IntegerConverter converter = new IntegerConverter();
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
		try {
			converter.parse("12,345", formatProvider);
			Assertions.fail();
		} catch (Exception ex) {
			Assertions.assertTrue(ex instanceof ConverterException);
		}
		try {
			converter.parse("12345.67", formatProvider);
			Assertions.fail();
		} catch (Exception ex) {
			Assertions.assertTrue(ex instanceof ConverterException);
		}
	}

	@Test
	public void test_maxMin() {
		IntegerConverter converter = new IntegerConverter();
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
		try {
			Integer v = converter.parse(Integer.toString(Integer.MAX_VALUE), formatProvider);
			Assertions.assertEquals(Integer.MAX_VALUE, v.intValue());
		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
		try {
			Integer v = converter.parse(Integer.toString(Integer.MIN_VALUE), formatProvider);
			Assertions.assertEquals(Integer.MIN_VALUE, v.intValue());
		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}

	@Test
	public void test_overflow() {
		IntegerConverter converter = new IntegerConverter();
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
		try {
			// Max: 2147483647
			converter.parse("2147483648", formatProvider);
			Assertions.fail();
		} catch (Exception ex) {
			Assertions.assertTrue(ex instanceof ConverterException);
		}
		try {
			// MIN: -2147483648
			converter.parse("-2147483649", formatProvider);
			Assertions.fail();
		} catch (Exception ex) {
			Assertions.assertTrue(ex instanceof ConverterException);
		}
	}
}
