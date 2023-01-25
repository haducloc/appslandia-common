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

import java.sql.Time;

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
public class SqlTimeConverterTest {

    @Test
    public void test_targetType() {
	SqlTimeConverter converter = new SqlTimeConverter();
	Assertions.assertEquals(java.sql.Time.class, converter.getTargetType());
    }

    @Test
    public void test() {
	SqlTimeConverter converter = new SqlTimeConverter();
	FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
	try {
	    Time v = converter.parse("09:30:00.999", formatProvider);
	    Assertions.assertNotNull(v);
	    Assertions.assertEquals("09:30:00.999", converter.format(v, formatProvider, true));
	    Assertions.assertEquals("09:30:00.999", converter.format(v, formatProvider, false));

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_null() {
	SqlTimeConverter converter = new SqlTimeConverter();
	FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
	try {
	    Time v = converter.parse(null, formatProvider);
	    Assertions.assertNull(v);

	    v = converter.parse("", formatProvider);
	    Assertions.assertNull(v);
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_invalid() {
	SqlTimeConverter converter = new SqlTimeConverter();
	FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
	try {
	    converter.parse("09:30:00", formatProvider);
	    Assertions.fail();
	} catch (Exception ex) {
	    Assertions.assertTrue(ex instanceof ConverterException);
	}
    }
}
