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

package com.appslandia.common.json;

import java.lang.reflect.Array;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
public abstract class JsonMapObject extends MapWrapper<String, Object> {
    private static final long serialVersionUID = 1L;

    public JsonMapObject() {
	super(new LinkedHashMap<>());
    }

    public JsonMapObject(Map<String, Object> map) {
	super(map);
    }

    @Override
    public Object put(String key, Object value) {
	Asserts.notNull(key);
	Asserts.isTrue(isValueSupported(value), "The value is unsuppored.");

	return super.put(key, value);
    }

    public JsonMapObject set(String key, Object value) {
	Asserts.notNull(key);
	Asserts.isTrue(isValueSupported(value), "The value is unsuppored.");

	super.put(key, value);
	return this;
    }

    public Object getRequired(String key) {
	Object value = super.get(key);
	return Asserts.notNull(value);
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

    protected boolean isValueSupported(Object value) {
	if (value == null) {
	    return true;
	}

	// Basic Types
	if (isBasicType(value.getClass())) {
	    return true;
	}

	// Collection
	if (value instanceof Collection) {
	    return validateCollection((Collection<?>) value);
	}

	// Map
	if (value instanceof Map) {
	    return validateMap((Map<?, ?>) value);
	}

	// Array
	if (value.getClass().isArray()) {
	    return validateArray(value);
	}
	return false;
    }

    protected boolean isBasicType(Class<?> c) {
	return (c == String.class) || Number.class.isAssignableFrom(c) || (c == Boolean.class) || Date.class.isAssignableFrom(c) || Temporal.class.isAssignableFrom(c);
    }

    private boolean validateMap(Map<?, ?> map) {
	return map.entrySet().stream().allMatch(entry -> {
	    return (entry.getKey() instanceof String) && ((entry.getValue() == null) || isValueSupported(entry.getValue()));
	});
    }

    private boolean validateCollection(Collection<?> col) {
	return col.stream().allMatch(value -> (value == null) || isValueSupported(value));
    }

    private boolean validateArray(Object array) {
	int len = Array.getLength(array);

	return IntStream.range(0, len).allMatch(i -> {
	    Object value = Array.get(array, i);
	    return (value == null) || isValueSupported(value);
	});
    }
}
