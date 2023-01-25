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
public class StringFormatTest {

    @Test
    public void test_map() {
	String msg = StringFormat.format("this is ${p1} and ${p2}", new Params().set("p1", "v1").set("p2", null));
	Assertions.assertEquals("this is v1 and null", msg);

	msg = StringFormat.format("this is ${p1} and ${p2}", new Params().set("p1", "v1").set("p2", "v2"));
	Assertions.assertEquals("this is v1 and v2", msg);
    }

    @Test
    public void test_map_missing() {
	try {
	    StringFormat.format("this is ${p1} and ${p2}", new Params().set("p1", "v1"));
	    Assertions.fail();
	} catch (Exception ex) {
	}
    }

    @Test
    public void test_array() {
	String msg = StringFormat.format("this is ${0} and ${1}", "v1", null);
	Assertions.assertEquals("this is v1 and null", msg);

	msg = StringFormat.format("this is ${0} and ${1}", "v1", "v2");
	Assertions.assertEquals("this is v1 and v2", msg);
    }

    @Test
    public void test_array_missing() {
	try {
	    StringFormat.format("this is ${0} and ${1}", "v1");
	    Assertions.fail();
	} catch (Exception ex) {
	}
    }

    @Test
    public void test_env_missing() {
	try {
	    StringFormat.format("Temp Dir=${env.__SYS__TEST__TMP__}");
	    Assertions.fail();
	} catch (Exception ex) {
	}
    }

    @Test
    public void test_prop_missing() {
	try {
	    StringFormat.format("Temp Dir=${env.__sys__test__tmp__}");
	    Assertions.fail();
	} catch (Exception ex) {
	}
    }

    @Test
    public void test_fmt() {
	String msg = StringFormat.fmt("this is {} and {}", "v1", null);
	Assertions.assertEquals("this is v1 and null", msg);

	msg = StringFormat.fmt("this is {} and {}", "v1", "v2");
	Assertions.assertEquals("this is v1 and v2", msg);
    }

    @Test
    public void test_fmt_missing() {
	try {
	    StringFormat.fmt("this is {} and {}", "v1");
	    Assertions.fail();
	} catch (Exception ex) {
	}
    }
}
