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

import java.util.Map;
import java.util.function.Function;

import com.appslandia.common.utils.ObjectUtils;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.bind.adapter.JsonbAdapter;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class JsonbMapAdapter<T extends Map<String, Object>> implements JsonbAdapter<T, JsonObject> {

    final boolean unmodifiable;
    final JsonObjectParser jsonObjectParser = new JsonObjectParser().setJsonValueConverter(JsonbJsonValueConverter.INSTANCE);

    public JsonbMapAdapter(boolean unmodifiable, Function<Map<String, Object>, T> rootConverter) {
	this.unmodifiable = unmodifiable;
	this.jsonObjectParser.setRootConverter(ObjectUtils.cast(rootConverter));
    }

    public <F, V> JsonbMapAdapter<T> setValueConverter(String[] pathOrPatterns, Function<F, V> converter) {
	this.jsonObjectParser.setValueConverter(pathOrPatterns, converter);
	return this;
    }

    @Override
    public JsonObject adaptToJson(T obj) throws Exception {
	return Json.createObjectBuilder(obj).build();
    }

    @Override
    public T adaptFromJson(JsonObject obj) throws Exception {
	Object value = this.jsonObjectParser.parse(obj, this.unmodifiable);
	return ObjectUtils.cast(value);
    }

    public boolean isUnmodifiable() {
	return this.unmodifiable;
    }
}
