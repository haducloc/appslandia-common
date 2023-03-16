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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JsonObjectParser extends InitializeObject {

    static final String ROOT_PATH = "";

    final Map<String, Function<Object, Object>> valueConverters = new LinkedHashMap<>();
    final Map<String, Pattern> pathPatterns = new ConcurrentHashMap<>();

    private JsonValueConverter jsonValueConverter;

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.jsonValueConverter);
    }

    public <F, V> JsonObjectParser setValueConverter(String[] pathOrPatterns, Function<F, V> converter) {
	this.assertNotInitialized();

	for (String pathOrPattern : pathOrPatterns) {
	    this.valueConverters.put(pathOrPattern, ObjectUtils.cast(converter));
	}
	return this;
    }

    public <V> JsonObjectParser setRootConverter(Function<Map<String, Object>, V> converter) {
	this.assertNotInitialized();

	this.valueConverters.put(ROOT_PATH, ObjectUtils.cast(converter));
	return this;
    }

    public JsonObjectParser setJsonValueConverter(JsonValueConverter jsonValueConverter) {
	this.assertNotInitialized();
	this.jsonValueConverter = jsonValueConverter;
	return this;
    }

    public Object parse(Object rootElement, boolean unmodifiable) {
	this.initialize();
	Asserts.notNull(rootElement);

	Out<Boolean> asResult = new Out<>();
	Iterator<Map.Entry<String, Object>> elementEntries = this.jsonValueConverter.asJsonObject(rootElement, asResult);
	Asserts.isTrue(Boolean.TRUE.equals(asResult.value));

	Map<String, Object> rootMap = new LinkedHashMap<>();
	StringBuilder path = new StringBuilder();

	while (elementEntries.hasNext()) {
	    Map.Entry<String, Object> elementEntry = elementEntries.next();

	    int len = path.length();
	    path.append(elementEntry.getKey());

	    Object parsedVal = parseValue(elementEntry.getValue(), unmodifiable, path, asResult.set(false));
	    parsedVal = convertValue(parsedVal, path.toString(), unmodifiable);

	    rootMap.put(elementEntry.getKey(), parsedVal);
	    path.delete(len, path.length());
	}

	Map<String, Object> rMap = unmodifiable ? Collections.unmodifiableMap(rootMap) : rootMap;
	Function<Object, Object> rootConverter = this.valueConverters.get(ROOT_PATH);
	return (rootConverter != null) ? rootConverter.apply(rMap) : rMap;
    }

    protected Object convertValue(Object value, String path, boolean unmodifiable) {
	Function<Object, Object> converter = this.valueConverters.get(path);
	if (converter != null) {
	    return converter.apply(value);
	}
	for (Map.Entry<String, Function<Object, Object>> converterEntry : this.valueConverters.entrySet()) {

	    Pattern pattern = this.pathPatterns.computeIfAbsent(converterEntry.getKey(), (p) -> Pattern.compile(p, Pattern.CASE_INSENSITIVE));
	    if (pattern.matcher(path).matches()) {

		return converterEntry.getValue().apply(value);
	    }
	}
	return value;
    }

    protected Object parseValue(Object element, boolean unmodifiable, StringBuilder path, Out<Boolean> asResult) {
	// NULL
	if (this.jsonValueConverter.isJsonNull(element)) {
	    return null;
	}

	// String
	String strValue = this.jsonValueConverter.asString(element, asResult.set(false));
	if (Boolean.TRUE.equals(asResult.value)) {
	    return strValue;
	}

	// Boolean
	boolean boolValue = this.jsonValueConverter.asBoolean(element, asResult.set(false));
	if (Boolean.TRUE.equals(asResult.value)) {
	    return boolValue;
	}

	// Double/Long
	String numberVal = this.jsonValueConverter.asNumber(element, asResult.set(false));
	if (Boolean.TRUE.equals(asResult.value)) {
	    try {
		return Long.valueOf(numberVal);

	    } catch (NumberFormatException ex) {
		return Double.valueOf(numberVal);
	    }
	}

	// Array
	Iterator<?> childElements = this.jsonValueConverter.asJsonArray(element, asResult.set(false));
	if (Boolean.TRUE.equals(asResult.value)) {

	    List<Object> list = new LinkedList<>();
	    int idx = 0;

	    while (childElements.hasNext()) {
		Object childElement = childElements.next();

		int len = path.length();
		path.append("[" + (idx++) + "]");

		Object parsedVal = parseValue(childElement, unmodifiable, path, asResult.set(false));
		parsedVal = convertValue(parsedVal, path.toString(), unmodifiable);

		list.add(parsedVal);
		path.delete(len, path.length());
	    }
	    return unmodifiable ? Collections.unmodifiableList(list) : list;
	}

	// MAP
	Iterator<Map.Entry<String, Object>> childElementEntries = this.jsonValueConverter.asJsonObject(element, asResult.set(false));
	if (Boolean.TRUE.equals(asResult.value)) {

	    Map<String, Object> map = new LinkedHashMap<>();

	    while (childElementEntries.hasNext()) {
		Map.Entry<String, Object> childElementEntry = childElementEntries.next();

		int len = path.length();
		path.append(".").append(childElementEntry.getKey());

		Object parsedVal = parseValue(childElementEntry.getValue(), unmodifiable, path, asResult.set(false));
		parsedVal = convertValue(parsedVal, path.toString(), unmodifiable);

		map.put(childElementEntry.getKey(), parsedVal);
		path.delete(len, path.length());
	    }
	    return unmodifiable ? Collections.unmodifiableMap(map) : map;
	}

	throw new Error();
    }
}
