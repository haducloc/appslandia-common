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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.Params;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class URLUtilsTest {

	@Test
	public void test_toQueryParams() {
		Map<String, Object> params = new Params(new LinkedHashMap<>()).set("p1", "val1").set("p2", "val 2").set("p3", null);
		String queryString = URLUtils.toQueryParams(params);

		Assertions.assertEquals("p1=val1&p2=val+2&p3=", queryString);
	}

	@Test
	public void test_toQueryParams_array() {
		Map<String, Object> params = new Params(new LinkedHashMap<>()).set("p1", new String[] { "val11", null }).set("p2", "val2");
		String queryString = URLUtils.toQueryParams(params);

		Assertions.assertEquals("p1=val11&p1=&p2=val2", queryString);
	}

	@Test
	public void test_parseParams() {
		Map<String, Object> params = new Params(new LinkedHashMap<>()).set("p1", "val1").set("p2", "val 2").set("p3", null);
		String queryString = URLUtils.toQueryParams(params);

		Map<String, Object> decodedMap = URLUtils.parseParams(queryString, new HashMap<>());

		Assertions.assertEquals("val1", decodedMap.get("p1"));
		Assertions.assertEquals("val 2", decodedMap.get("p2"));

		Assertions.assertTrue(decodedMap.containsKey("p3"));
		Assertions.assertNull(decodedMap.get("p3"));
	}

	@Test
	public void test_parseParams_array() {
		Map<String, Object> params = new Params(new LinkedHashMap<>()).set("p1", new String[] { "val11", null, null }).set("p2", new String[] { null, null });
		String queryString = URLUtils.toQueryParams(params);

		Map<String, Object> decodedMap = URLUtils.parseParams(queryString, new HashMap<>());

		Assertions.assertTrue(decodedMap.containsKey("p1"));
		Object p1Val = decodedMap.get("p1");
		Assertions.assertNotNull(p1Val);

		Assertions.assertTrue(p1Val.getClass().isArray());
		Assertions.assertTrue(Arrays.equals((String[]) p1Val, new String[] { "val11", null, null }));

		Assertions.assertTrue(decodedMap.containsKey("p2"));
		Object p2Val = decodedMap.get("p2");
		Assertions.assertNotNull(p2Val);

		Assertions.assertTrue(p2Val.getClass().isArray());
		Assertions.assertTrue(Arrays.equals((String[]) p2Val, new String[] { null, null }));
	}

	@Test
	public void test_toURL() {
		String url = URLUtils.toUrl("/app/index.html", new Params().set("p1", "val1"));
		Assertions.assertTrue(url.contains("/app/index.html?p1=val1"));

		url = URLUtils.toUrl("http://server/app/index.html", new Params().set("p1", "val1"));
		Assertions.assertTrue(url.contains("http://server/app/index.html?p1=val1"));

		url = URLUtils.toUrl("/app/index.html?p0=val0", new Params().set("p1", "val1"));
		Assertions.assertTrue(url.contains("/app/index.html?p0=val0&p1=val1"));

		url = URLUtils.toUrl("http://server/app/index.html?p0=val0", new Params().set("p1", "val1"));
		Assertions.assertTrue(url.contains("http://server/app/index.html?p0=val0&p1=val1"));
	}
}
