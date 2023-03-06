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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	if (value instanceof List) {
	    return validateList((List<?>) value);
	}
	if (value instanceof Map) {
	    return validateMap((Map<?, ?>) value);
	}
	Class<?> type = value.getClass();
	if (type.isArray()) {
	    return validateArray(value);
	}
	return isBasicType(type);
    }

    protected boolean isBasicType(Class<?> c) {
	return (c == String.class) || (c == Byte.class) || (c == Short.class) || (c == Integer.class) || (c == Long.class) || (c == Float.class) || (c == Double.class)
		|| (c == BigDecimal.class) || (c == Boolean.class) || (c == Character.class) || Date.class.isAssignableFrom(c) || Temporal.class.isAssignableFrom(c);
    }

    private boolean validateMap(Map<?, ?> map) {
	for (Entry<?, ?> entry : map.entrySet()) {
	    // Key
	    if ((entry.getKey() == null) || (entry.getKey().getClass() != String.class)) {
		return false;
	    }

	    // Value
	    if ((entry.getValue() != null) && !isValueSupported(entry.getValue())) {
		return false;
	    }
	}
	return true;
    }

    private boolean validateList(List<?> list) {
	for (Object value : list) {

	    // Value
	    if ((value != null) && !isValueSupported(value)) {
		return false;
	    }
	}
	return true;
    }

    private boolean validateArray(Object array) {
	int len = Array.getLength(array);
	for (int i = 0; i < len; i++) {
	    Object value = Array.get(array, i);

	    // Value
	    if ((value != null) && !isValueSupported(value)) {
		return false;
	    }
	}
	return true;
    }
}
