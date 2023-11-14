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

package com.appslandia.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DateUtilsTest {

	@Test
	public void test_translateToMs() {
		long ms = DateUtils.translateToMs("1d 4h 8m");
		Assertions.assertEquals(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) + TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS)
				+ TimeUnit.MILLISECONDS.convert(8, TimeUnit.MINUTES), ms);

		ms = DateUtils.translateToMs("1D 4H 8M");
		Assertions.assertEquals(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) + TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS)
				+ TimeUnit.MILLISECONDS.convert(8, TimeUnit.MINUTES), ms);
	}

	@Test
	public void test_translateToMs_failed() {
		try {
			DateUtils.translateToMs("1d 4hr 8min");
			Assertions.fail();
		} catch (Exception ex) {
		}
	}

	// Java8 Date/Time

	@Test
	public void test_parse_localDate() {
		try {
			LocalDate t = DateUtils.iso8601LocalDate("2017-11-02");
			String s = DateUtils.iso8601LocalDate(t);

			Assertions.assertEquals("2017-11-02", s);

		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_localTime() {
		try {
			LocalTime t = DateUtils.iso8601LocalTime("09:45:00.999");
			String s = DateUtils.iso8601LocalTime(t);

			Assertions.assertEquals("09:45:00.999", s);

		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_localDateTime() {
		try {
			LocalDateTime t = DateUtils.iso8601LocalDateTime("2017-11-02T09:45:00.999");
			String s = DateUtils.iso8601LocalDateTime(t);

			Assertions.assertEquals("2017-11-02T09:45:00.999", s);

		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_offsetTime() {
		try {
			OffsetTime t = DateUtils.iso8601OffsetTime("09:45:00.999-05:00");
			String s = DateUtils.iso8601OffsetTime(t);

			Assertions.assertEquals("09:45:00.999-05:00", s);

		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parse_offsetDateTime() {
		try {
			OffsetDateTime t = DateUtils.iso8601OffsetDateTime("2017-11-02T09:45:00.999-05:00");
			String s = DateUtils.iso8601OffsetDateTime(t);

			Assertions.assertEquals("2017-11-02T09:45:00.999-05:00", s);

		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}
}
