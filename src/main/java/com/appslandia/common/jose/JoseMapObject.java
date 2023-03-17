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

package com.appslandia.common.jose;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import com.appslandia.common.base.AssertException;
import com.appslandia.common.base.BasicMap;
import com.appslandia.common.utils.CollectionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class JoseMapObject extends BasicMap {
    private static final long serialVersionUID = 1L;

    public JoseMapObject() {
	super(new LinkedHashMap<>());
    }

    public JoseMapObject(Map<String, Object> map) {
	super(map);
    }

    public Date getNumericDate(String key) {
	Number nd = (Number) this.get(key);
	return (nd != null) ? JoseUtils.toDate(nd.longValue()) : null;
    }

    public JoseMapObject setNumericDate(String key, Date value) {
	set(key, JoseUtils.toNumericDate(value));
	return this;
    }

    public JoseMapObject setNumericDate(String key, long timeInMs) {
	set(key, JoseUtils.toNumericDate(timeInMs));
	return this;
    }

    public JoseMapObject setList(String key, Object[] values) {
	set(key, (values != null) ? CollectionUtils.toList(new LinkedList<>(), values) : null);
	return this;
    }

    public Date getDate(String key) {
	Object d = this.get(key);
	if (d == null) {
	    return null;
	}
	if (d instanceof Date) {
	    return (Date) d;
	}
	if (d.getClass() == Long.class) {
	    return new Date((Long) d);
	}
	throw new AssertException("Date conversion failed.");
    }
}
