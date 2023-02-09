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

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SYSTest {

    @Test
    public void test_resolve_prop() {
	try {
	    System.setProperty("__sys__test__password__", "12345");
	    String resolvedValue = SYS.resolve("${__sys__test__password__}");

	    Assertions.assertNotNull(resolvedValue);
	    Assertions.assertEquals("12345", resolvedValue);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());

	} finally {
	    System.getProperties().remove("__sys__test__password__");
	}
    }

    @Test
    public void test_resolve_prop_novalue() {
	try {
	    String resolvedValue = SYS.resolve("${__sys__test__password__}");
	    Assertions.assertNull(resolvedValue);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @SuppressWarnings("el-syntax")
    @Test
    public void test_resolve_prop_default() {
	try {
	    String resolvedValue = SYS.resolve("${__sys__test__password__:12345}");

	    Assertions.assertNotNull(resolvedValue);
	    Assertions.assertEquals("12345", resolvedValue);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_resolve_env() {
	if (System.getenv("TMP") == null) {
	    return;
	}

	try {
	    String resolvedValue = SYS.resolve("${env.TMP}");
	    Assertions.assertNotNull(resolvedValue);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_resolve_env_novalue() {
	try {
	    String resolvedValue = SYS.resolve("${env.__SYS__TEST__TMP__}");
	    Assertions.assertNull(resolvedValue);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @SuppressWarnings("el-syntax")
    @Test
    public void test_resolve_env_default() {
	try {
	    String resolvedValue = SYS.resolve("${env.__SYS__TEST__TMP__:/upload}");

	    Assertions.assertNotNull(resolvedValue);
	    Assertions.assertEquals("/upload", resolvedValue);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @SuppressWarnings("el-syntax")
    @Test
    public void test_resolve_prop_env() {
	if (System.getenv("TMP") == null) {
	    return;
	}

	try {
	    String resolvedValue = SYS.resolve("${__sys__test__tmp__,env.TMP}");
	    Assertions.assertNotNull(resolvedValue);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @SuppressWarnings("el-syntax")
    @Test
    public void test_resolve_prop_env_default() {
	try {
	    String resolvedValue = SYS.resolve("${__sys__test__tmp__,env.__SYS__TEST__TMP__:/upload}");
	    Assertions.assertNotNull(resolvedValue);
	    Assertions.assertEquals("/upload", resolvedValue);

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }
}
