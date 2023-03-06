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

import java.util.HashMap;
import java.util.Map;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ValidatableMap extends MapWrapper<String, Object> {
    private static final long serialVersionUID = 1L;

    public ValidatableMap() {
	super(new HashMap<String, Object>());
    }

    public ValidatableMap(Map<String, Object> newMap) {
	super(newMap);
    }

    public Object getRequired(String key) {
	Object value = this.get(key);
	return Asserts.notNull(value, () -> STR.fmt("No value associated with key '{}'.", key));
    }

    @Override
    public Object put(String key, Object value) {
	Asserts.isTrue(isValueSupported(value), "value is unsupported.");
	return this.map.put(key, value);
    }

    public ValidatableMap set(String key, Object value) {
	Asserts.isTrue(isValueSupported(value), "value is unsupported.");

	this.map.put(key, value);
	return this;
    }

    protected abstract boolean isValueSupported(Object value);
}
