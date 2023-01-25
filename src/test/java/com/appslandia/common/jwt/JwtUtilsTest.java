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

package com.appslandia.common.jwt;

import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtUtilsTest {

    @Test
    public void test_toNumericDate() {
	Date dt = DateUtils.iso8601DateTime("2010-10-10T10:10:10.999");

	Long numericDate = JwtUtils.toNumericDate(dt.getTime());
	Date restoredDate = JwtUtils.toDate(numericDate);

	Assertions.assertEquals("2010-10-10T10:10:10.000", DateUtils.iso8601DateTime(restoredDate));
	Assertions.assertEquals((dt.getTime() / 1000) * 1000, restoredDate.getTime());
    }

    @Test
    public void test_isSupportedValue() {
	// Basic Types
	Assertions.assertTrue(JwtUtils.isSupportedValue(1));
	Assertions.assertTrue(JwtUtils.isSupportedValue(1L));
	Assertions.assertTrue(JwtUtils.isSupportedValue(1d));
	Assertions.assertTrue(JwtUtils.isSupportedValue(true));
	Assertions.assertTrue(JwtUtils.isSupportedValue("admin"));
	Assertions.assertTrue(JwtUtils.isSupportedValue(new Date()));

	// Array
	Assertions.assertTrue(JwtUtils.isSupportedValue(new Object[] { 1, 1L, 1d, true, "admin", new Date() }));

	// List
	Assertions.assertTrue(JwtUtils.isSupportedValue(Arrays.asList(1, 1L, 1d, true, "admin", new Date())));

	// Map
	Assertions.assertTrue(JwtUtils.isSupportedValue(CollectionUtils.toMap("1", 1, "1L", 1L, "1d", 1d, "true", true, "admin", "admin", "date", new Date())));
    }

    @Test
    public void test_isSupportedValue_invalidMap() {
	// Key must be String
	Assertions.assertFalse(JwtUtils.isSupportedValue(CollectionUtils.toMap(1, 1)));
    }
}
