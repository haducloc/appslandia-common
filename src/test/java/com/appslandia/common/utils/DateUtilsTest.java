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

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Calendar;
import java.util.Date;
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
    public void test_todaySqlDate() {
	java.sql.Date today = DateUtils.todaySqlDate();
	Assertions.assertEquals(DateUtils.iso8601DateTime(DateUtils.clearTime(today)), DateUtils.iso8601DateTime(today));
    }

    @Test
    public void test_todayAsLong() {
	long today = DateUtils.todayAsLong();
	Assertions.assertEquals(DateUtils.iso8601DateTime(new Date(DateUtils.clearTime(today))), DateUtils.iso8601DateTime(new Date(today)));
    }

    @Test
    public void test_getCalendar() {
	Calendar cal = DateUtils.getCalendar(3, 9, 30);

	Assertions.assertEquals(3, cal.get(Calendar.DAY_OF_WEEK));
	Assertions.assertEquals(9, cal.get(Calendar.HOUR_OF_DAY));
	Assertions.assertEquals(30, cal.get(Calendar.MINUTE));

	Assertions.assertEquals(0, cal.get(Calendar.SECOND));
	Assertions.assertEquals(0, cal.get(Calendar.MILLISECOND));
    }

    @Test
    public void test_copyTime() {
	Date d1 = DateUtils.iso8601DateTime("2017-11-02T09:45:00.000");
	Date d2 = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999");
	d2 = DateUtils.copyTime(d2, d1);
	Assertions.assertEquals("09:45:00.000", DateUtils.iso8601Time(d2));
    }

    @Test
    public void test_clearTime_dt() {
	Date time = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999");
	Date noTime = DateUtils.clearTime(time);
	Assertions.assertEquals("2017-11-02T00:00:00.000", DateUtils.iso8601DateTime(noTime));
    }

    @Test
    public void test_clearTime_long() {
	long time = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999").getTime();
	long noTime = DateUtils.clearTime(time);
	Assertions.assertEquals("2017-11-02T00:00:00.000", DateUtils.iso8601DateTime(new Date(noTime)));
    }

    @Test
    public void test_clearMs_dt() {
	Date time = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999");
	Date noMs = DateUtils.clearMs(time);
	Assertions.assertEquals("2017-11-02T09:45:00.000", DateUtils.iso8601DateTime(noMs));
    }

    @Test
    public void test_clearMs_long() {
	long time = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999").getTime();
	long noMs = DateUtils.clearMs(time);
	Assertions.assertEquals("2017-11-02T09:45:00.000", DateUtils.iso8601DateTime(new Date(noMs)));
    }

    @Test
    public void test_translateToMs() {
	long ms = DateUtils.translateToMs("1d 4h 8m");
	Assertions.assertEquals(
		TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) + TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS) + TimeUnit.MILLISECONDS.convert(8, TimeUnit.MINUTES), ms);

	ms = DateUtils.translateToMs("1D 4H 8M");
	Assertions.assertEquals(
		TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) + TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS) + TimeUnit.MILLISECONDS.convert(8, TimeUnit.MINUTES), ms);
    }

    @Test
    public void test_translateToMs_failed() {
	try {
	    DateUtils.translateToMs("1d 4hr 8min");
	    Assertions.fail();
	} catch (Exception ex) {
	    Assertions.assertTrue(ex instanceof IllegalArgumentException);
	}
    }

    @Test
    public void test_parse_date() {
	try {
	    Date t = DateUtils.iso8601Date("2017-11-02");
	    String s = DateUtils.iso8601Date(t);

	    Assertions.assertEquals("2017-11-02", s);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_parse_time() {
	try {
	    Time t = DateUtils.iso8601Time("09:45:00.999");
	    String s = DateUtils.iso8601Time(t);

	    Assertions.assertEquals("09:45:00.999", s);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_parse_dateTime() {
	try {
	    Timestamp t = DateUtils.iso8601DateTime("2017-11-02T09:45:00.999");
	    String s = DateUtils.iso8601DateTime(t);

	    Assertions.assertEquals("2017-11-02T09:45:00.999", s);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
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
