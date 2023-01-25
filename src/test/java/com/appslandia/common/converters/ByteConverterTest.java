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
public class ByteConverterTest {

    @Test
    public void test_targetType() {
	ByteConverter converter = new ByteConverter();
	Assertions.assertEquals(Byte.class, converter.getTargetType());
    }

    @Test
    public void test() {
	ByteConverter converter = new ByteConverter();
	FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
	try {
	    Byte v = converter.parse("123", formatProvider);
	    Assertions.assertEquals(123, v.byteValue());

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_null() {
	ByteConverter converter = new ByteConverter();
	FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
	try {
	    Byte val = converter.parse(null, formatProvider);
	    Assertions.assertNull(val);

	    val = converter.parse("", formatProvider);
	    Assertions.assertNull(val);
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_invalid() {
	ByteConverter converter = new ByteConverter();
	FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
	try {
	    converter.parse("1,23", formatProvider);
	    Assertions.fail();
	} catch (Exception ex) {
	    Assertions.assertTrue(ex instanceof ConverterException);
	}

	try {
	    converter.parse("12.345", formatProvider);
	    Assertions.fail();
	} catch (Exception ex) {
	    Assertions.assertTrue(ex instanceof ConverterException);
	}
    }

    @Test
    public void test_maxMin() {
	ByteConverter converter = new ByteConverter();
	FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
	try {
	    Byte v = converter.parse(Byte.toString(Byte.MAX_VALUE), formatProvider);
	    Assertions.assertEquals(Byte.MAX_VALUE, v.byteValue());
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}

	try {
	    Byte v = converter.parse(Byte.toString(Byte.MIN_VALUE), formatProvider);
	    Assertions.assertEquals(Byte.MIN_VALUE, v.byteValue());
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_overflow() {
	ByteConverter converter = new ByteConverter();
	FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
	try {
	    // Max: 127
	    converter.parse("128", formatProvider);
	    Assertions.fail();
	} catch (Exception ex) {
	    Assertions.assertTrue(ex instanceof ConverterException);
	}

	try {
	    // MIN: -128
	    converter.parse("-129", formatProvider);
	    Assertions.fail();
	} catch (Exception ex) {
	    Assertions.assertTrue(ex instanceof ConverterException);
	}
    }
}
