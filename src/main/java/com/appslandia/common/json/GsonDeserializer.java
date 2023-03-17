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

import java.lang.reflect.Type;
import java.util.function.Function;

import com.appslandia.common.utils.ObjectUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GsonDeserializer<T> implements JsonDeserializer<T> {

    final boolean unmodifiable;
    final JsonObjectParser jsonMapParser = new JsonObjectParser().setJsonValueConverter(GsonJsonValueConverter.INSTANCE);

    public GsonDeserializer(boolean unmodifiable, Function<Object, T> rootConverter) {
	this.unmodifiable = unmodifiable;
	this.jsonMapParser.setRootConverter(rootConverter);
    }

    public <F, V> GsonDeserializer<T> setValueConverter(String[] pathOrPattern, Function<F, V> converter) {
	this.jsonMapParser.setValueConverter(pathOrPattern, converter);
	return this;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	Object value = this.jsonMapParser.parse(json, this.unmodifiable);
	return ObjectUtils.cast(value);
    }

    public boolean isUnmodifiable() {
	return this.unmodifiable;
    }
}
