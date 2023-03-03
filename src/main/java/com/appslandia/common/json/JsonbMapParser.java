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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.appslandia.common.utils.STR;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JsonbMapParser {

    public Map<String, Object> parseMap(JsonObject jsonObject, Supplier<Map<String, Object>> newMap, boolean makeReadonly) {
	Map<String, Object> map = newMap.get();
	for (String key : jsonObject.keySet()) {

	    map.put(key, parseValue(jsonObject.get(key), makeReadonly));
	}
	return makeReadonly ? Collections.unmodifiableMap(map) : map;
    }

    public Object parseValue(JsonValue element, boolean makeReadonly) {
	// NULL
	if (element.getValueType() == ValueType.NULL) {
	    return null;
	}

	// STRING
	if (element.getValueType() == ValueType.STRING) {
	    return element.toString();
	}

	// Boolean
	if (element.getValueType() == ValueType.TRUE) {
	    return true;
	}
	if (element.getValueType() == ValueType.FALSE) {
	    return false;
	}

	// Number
	if (element.getValueType() == ValueType.NUMBER) {
	    String str = element.toString();
	    try {
		return Long.valueOf(str);

	    } catch (NumberFormatException ex) {
		Double d = Double.valueOf(str);
		if (d.isInfinite() || d.isNaN()) {
		}
		return d;
	    }
	}

	// Array
	if (element.getValueType() == ValueType.ARRAY) {
	    JsonArray jsonArray = element.asJsonArray();
	    List<Object> list = new ArrayList<>(jsonArray.size());

	    for (JsonValue jsonElement : jsonArray) {
		list.add(parseValue(jsonElement, makeReadonly));
	    }
	    return makeReadonly ? Collections.unmodifiableList(list) : list;
	}

	// MAP
	if (element.getValueType() == ValueType.OBJECT) {
	    JsonObject jsonObject = element.asJsonObject();
	    Map<String, Object> map = new LinkedHashMap<>(jsonObject.size());

	    for (String key : jsonObject.keySet()) {
		map.put(key, parseValue(jsonObject.get(key), makeReadonly));
	    }
	    return makeReadonly ? Collections.unmodifiableMap(map) : map;
	}

	throw new IllegalArgumentException(STR.fmt("invalid JsonValue {}", element.toString()));
    }
}
