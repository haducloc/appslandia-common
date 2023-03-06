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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class BasicMap extends ValidatableMap {
    private static final long serialVersionUID = 1L;

    public BasicMap() {
	super(new LinkedHashMap<String, Object>());
    }

    public BasicMap(Map<String, Object> newMap) {
	super(newMap);
    }

    @Override
    public BasicMap set(String key, Object value) {
	super.set(key, value);
	return this;
    }

    @Override
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
	return (c == String.class) || (c == Byte.class) || (c == Short.class) || (c == Integer.class) || (c == Long.class) || (c == Float.class) || (c == Double.class)
		|| (c == BigDecimal.class) || (c == Boolean.class) || (c == Character.class) || Date.class.isAssignableFrom(c) || Temporal.class.isAssignableFrom(c);
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

    public Map<String, Object> copyInnerMap() {
	Map<String, Object> cm = new LinkedHashMap<>();

	for (Entry<String, Object> entry : this.map.entrySet()) {
	    cm.put(entry.getKey(), copyValue(entry.getValue()));
	}
	return cm;
    }

    protected Object copyValue(Object value) {
	if (value == null) {
	    return null;
	}

	// Basic Types
	if (isBasicType(value.getClass())) {
	    return value;
	}

	// Collection
	if (value instanceof Collection) {
	    Collection<Object> valueAsCol = ObjectUtils.cast(value);
	    Collection<Object> cc = (valueAsCol instanceof Set) ? new LinkedHashSet<>() : new LinkedList<>();

	    for (Object element : valueAsCol) {
		cc.add(copyValue(element));
	    }
	    return cc;
	}

	// Map
	if (value instanceof Map) {
	    Map<String, Object> valueAsMap = ObjectUtils.cast(value);
	    Map<String, Object> cm = new LinkedHashMap<>(valueAsMap.size());

	    for (Entry<String, Object> entry : valueAsMap.entrySet()) {
		cm.put(entry.getKey(), copyValue(entry.getValue()));
	    }
	    return cm;
	}

	// Array
	if (value.getClass().isArray()) {
	    int len = Array.getLength(value);
	    Object[] cv = new Object[len];

	    for (int i = 0; i < len; i++) {
		cv[i] = copyValue(Array.get(value, i));
	    }
	    return cv;
	}

	throw new Error();
    }
}
