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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Maps {

    public static Builder builder() {
	return new Builder();
    }

    @SuppressWarnings("unchecked")
    public static class Builder {

	final List<MapEntry> entries = new ArrayList<>();

	public <K, V> Builder entry(K k, V v) {
	    this.entries.add(new MapEntry(k, v));
	    return this;
	}

	public <K, V> Map<K, V> toReadonlyMap() {
	    return toReadonlyMap(new HashMap<>());
	}

	public <K, V> Map<K, V> toReadonlyMap(Map<K, V> map) {
	    for (MapEntry entry : this.entries) {
		map.put((K) entry.key, (V) entry.value);
	    }
	    return Collections.unmodifiableMap(map);
	}
    }

    private static class MapEntry {

	final Object key;
	final Object value;

	MapEntry(Object key, Object value) {
	    this.key = key;
	    this.value = value;
	}
    }
}
