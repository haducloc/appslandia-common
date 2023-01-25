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

package com.appslandia.common.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PropertyConfigTest {

    @Test
    public void test() {
	PropertyConfig config = new PropertyConfig();

	config.set("stringConfig", "data");
	config.set("boolConfig", true);
	config.set("intConfig", Integer.MAX_VALUE);
	config.set("longConfig", Long.MAX_VALUE);

	config.set("floatConfig", 12.345f);
	config.set("doubleConfig", 12.345);

	Assertions.assertNotNull(config.getString("stringConfig"));
	Assertions.assertNotNull(config.getString("boolConfig"));
	Assertions.assertNotNull(config.getString("intConfig"));
	Assertions.assertNotNull(config.getString("longConfig"));

	Assertions.assertNotNull(config.getString("floatConfig"));
	Assertions.assertNotNull(config.getString("doubleConfig"));
    }

    @Test
    public void test_assertEquals() {
	PropertyConfig config = new PropertyConfig();

	config.set("stringConfig", "data");
	config.set("boolConfig", true);
	config.set("intConfig", Integer.MAX_VALUE);
	config.set("longConfig", Long.MAX_VALUE);

	config.set("floatConfig", 12.345f);
	config.set("doubleConfig", 12.345);

	try {
	    Assertions.assertEquals("data", config.getRequiredString("stringConfig"));
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}

	try {
	    Assertions.assertEquals(true, config.getRequiredBool("boolConfig"));
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}

	try {
	    Assertions.assertEquals(Integer.MAX_VALUE, config.getRequiredInt("intConfig"));
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}

	try {
	    Assertions.assertEquals(Long.MAX_VALUE, config.getRequiredLong("longConfig"));
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}

	try {
	    Assertions.assertTrue(config.getRequiredDouble("doubleConfig") == 12.345);
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_assertNumbers() {
	PropertyConfig config = new PropertyConfig();

	config.set("intConfig", Integer.MAX_VALUE);
	config.set("longConfig", Long.MAX_VALUE);

	config.set("floatConfig", 12.345f);
	config.set("doubleConfig", 12.345);

	try {
	    Assertions.assertEquals(Integer.MAX_VALUE, config.getRequiredInt("intConfig"));
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}

	try {
	    Assertions.assertEquals(Long.MAX_VALUE, config.getRequiredLong("longConfig"));
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}

	try {
	    Assertions.assertTrue(config.getRequiredDouble("doubleConfig") == 12.345);
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_getFormatted() {
	PropertyConfig config = new PropertyConfig();
	config.set("contextPath", "/app");
	config.set("authUrl", "${contextPath}/auth.html");

	String authUrl = config.getFormatted("authUrl");
	Assertions.assertEquals("/app/auth.html", authUrl);
    }

    @Test
    public void test_getFormatted_Map() {
	PropertyConfig config = new PropertyConfig();
	config.set("authUrl", "${contextPath}/auth.html");

	String authUrl = config.getFormatted("authUrl", new Params().set("contextPath", "/app"));
	Assertions.assertEquals("/app/auth.html", authUrl);
    }

    @Test
    public void test_getFormatted_PROP() {
	PropertyConfig config = new PropertyConfig();
	config.set("connStr", "db=${db}&user=${user}&password=${password}");

	config.set("db", "db1");
	config.set("user", "user1");
	System.setProperty("password", "123456");

	try {
	    String connStr = config.getFormatted("connStr");
	    Assertions.assertEquals("db=db1&user=user1&password=123456", connStr);

	} finally {
	    System.getProperties().remove("password");
	}
    }

    @Test
    public void test_getFormatted_ENV() {
	if (System.getenv("TMP") == null) {
	    return;
	}

	PropertyConfig config = new PropertyConfig();
	config.set("uploadDir", "${env.TMP}/upload");

	String dir = config.getFormatted("uploadDir");
	Assertions.assertNotNull(dir);
    }

    @Test
    public void test_getFormatted_Exception() {
	PropertyConfig config = new PropertyConfig();
	config.set("connStr", "db=${db}&user=${user}&password=${password}");

	config.set("db", "db1");
	config.set("user", "user1");

	try {
	    // Missing ${password}
	    config.getFormatted("connStr");
	    Assertions.fail();

	} catch (Exception ex) {
	}
    }

    @Test
    public void test_getFormatted_Map_Exception() {
	PropertyConfig config = new PropertyConfig();
	config.set("authUrl", "${contextPath}/auth.html");

	try {
	    // Missing ${contextPath}
	    config.getFormatted("authUrl", new Params().set("ctx", "/app"));
	    Assertions.fail();

	} catch (Exception ex) {
	}
    }

    @Test
    public void test_getFormatted_Array() {
	PropertyConfig config = new PropertyConfig();
	config.set("authUrl", "${0}/auth.html");

	String authUrl = config.getFormatted("authUrl", "/app");
	Assertions.assertEquals("/app/auth.html", authUrl);
    }

    @Test
    public void test_getFormatted_Array_PROP() {
	PropertyConfig config = new PropertyConfig();
	config.set("connStr", "db=${0}&user=${1}&password=${password}");

	System.setProperty("password", "123456");

	try {
	    String connStr = config.getFormatted("connStr", "db1", "user1");
	    Assertions.assertEquals("db=db1&user=user1&password=123456", connStr);

	} finally {
	    System.getProperties().remove("password");
	}
    }

    @Test
    public void test_getFormatted_Array_Exception() {
	PropertyConfig config = new PropertyConfig();
	config.set("authUrl", "${0}/${1}/auth.html");

	try {
	    // Missing ${1}
	    config.getFormatted("authUrl", "/app");
	    Assertions.fail();

	} catch (Exception ex) {
	}
    }
}
