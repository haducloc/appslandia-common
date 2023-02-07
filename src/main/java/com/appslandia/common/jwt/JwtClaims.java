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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.base.AssertException;
import com.appslandia.common.base.MapWrapper;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class JwtClaims extends MapWrapper<String, Object> {
    private static final long serialVersionUID = 1L;

    public JwtClaims() {
	super(new LinkedHashMap<>());
    }

    public JwtClaims(Map<String, Object> map) {
	super(map);
    }

    @Override
    public Object put(String key, Object value) {
	Asserts.notNull(key);

	if (value == null)
	    return this.remove(key);

	Asserts.isTrue(JwtUtils.isSupportedValue(value), "value is unsupported.");
	return super.put(key, value);
    }

    public JwtClaims set(String key, Object value) {
	put(key, value);
	return this;
    }

    public Date getNumericDate(String key) {
	Number nd = (Number) this.get(key);
	return (nd != null) ? JwtUtils.toDate(nd.longValue()) : null;
    }

    public JwtClaims setNumericDate(String key, Date value) {
	return this.set(key, JwtUtils.toNumericDate(value));
    }

    public JwtClaims setNumericDate(String key, long timeInMs) {
	return this.set(key, JwtUtils.toNumericDate(timeInMs));
    }

    public Object getRequired(String key) {
	Object obj = this.get(key);
	return Asserts.notNull(obj, () -> STR.fmt("No value associated with key '{}'.", key));
    }

    public Date getDate(String key) {
	Object d = this.get(key);
	if (d == null)
	    return null;

	if (d instanceof Date)
	    return (Date) d;

	if (d.getClass() == Long.class)
	    return new Date((Long) d);

	throw new AssertException("Date conversion failed.");
    }
}
