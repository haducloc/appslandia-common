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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.Params;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class STRTest {

    @Test
    public void test_map() {
	String msg = STR.format("this is ${p1} and ${p2}", new Params().set("p1", "v1").set("p2", null));
	Assertions.assertEquals("this is v1 and null", msg);

	msg = STR.format("this is ${p1} and ${p2}", new Params().set("p1", "v1").set("p2", "v2"));
	Assertions.assertEquals("this is v1 and v2", msg);
    }

    @Test
    public void test_map_missing() {
	try {
	    String msg = STR.format("this is ${p1} and ${p2}", new Params().set("p1", "v1"));
	    Assertions.assertEquals("this is v1 and ${p2}", msg);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_array() {
	String msg = STR.format("this is ${0} and ${1}", "v1", null);
	Assertions.assertEquals("this is v1 and null", msg);

	msg = STR.format("this is ${0} and ${1}", "v1", "v2");
	Assertions.assertEquals("this is v1 and v2", msg);
    }

    @Test
    public void test_array_missing() {
	try {
	    String msg = STR.format("this is ${0} and ${1}", "v1");
	    Assertions.assertEquals("this is v1 and ${1}", msg);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_fmt() {
	String msg = STR.fmt("this is {} and {}", "v1", null);
	Assertions.assertEquals("this is v1 and null", msg);

	msg = STR.fmt("this is {} and {}", "v1", "v2");
	Assertions.assertEquals("this is v1 and v2", msg);
    }

    @Test
    public void test_fmt_missing() {
	try {
	    String msg = STR.fmt("this is {} and {}", "v1");
	    Assertions.assertEquals("this is v1 and {}", msg);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @SuppressWarnings("el-syntax")
    @Test
    public void test_format_pattern() {
	try {
	    String msg = STR.format("this is ${0:#,##0} and ${1:MM/dd/yyyy}", 12345, DateUtils.iso8601Date("2023-01-01"));
	    Assertions.assertEquals("this is 12,345 and 01/01/2023", msg);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_fmt_pattern() {
	try {
	    String msg = STR.fmt("this is {#,###} and {MM/dd/yyyy}", 12345, DateUtils.iso8601Date("2023-01-01"));
	    Assertions.assertEquals("this is 12,345 and 01/01/2023", msg);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }
}
