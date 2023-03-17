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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.appslandia.common.base.MapWrapper;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class JoseMapObject extends MapWrapper<String, Object> {
    private static final long serialVersionUID = 1L;

    public JoseMapObject() {
	super(new LinkedHashMap<>());
    }

    public JoseMapObject(Map<String, Object> map) {
	super(map);
    }

    public JoseMapObject set(String key, Object value) {
	this.map.put(key, value);
	return this;
    }

    public Object getRequired(String key) {
	Object value = super.get(key);
	return Asserts.notNull(value);
    }

    public Date getNumericDate(String key) {
	Number nd = (Number) this.get(key);
	return (nd != null) ? JoseUtils.toDate(nd.longValue()) : null;
    }

    public JoseMapObject setNumericDate(String key, Date value) {
	return set(key, JoseUtils.toNumericDate(value));
    }

    public JoseMapObject setNumericDate(String key, long timeInMs) {
	return set(key, JoseUtils.toNumericDate(timeInMs));
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
	throw new IllegalArgumentException(STR.fmt("Failed to getDate('{}')", key));
    }

    public <E> List<E> getList(String key) {
	Object value = this.get(key);
	if (value == null) {
	    return null;
	}

	// List
	if (value instanceof List) {
	    return ObjectUtils.cast(value);
	}

	// Collection
	if (value instanceof Collection) {
	    Collection<E> col = ObjectUtils.cast(value);
	    return new ArrayList<>(col);
	}

	// Array
	if (value.getClass().isArray()) {
	    List<Object> list = ArrayUtils.toList(value);
	    return ObjectUtils.cast(list);
	}
	throw new IllegalArgumentException(STR.fmt("Failed to getList('{}')", key));
    }
}
