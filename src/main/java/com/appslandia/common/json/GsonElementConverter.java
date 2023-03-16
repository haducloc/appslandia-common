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

import java.util.Iterator;
import java.util.Map.Entry;

import com.appslandia.common.base.Out;
import com.appslandia.common.utils.ObjectUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GsonElementConverter implements JsonElementConverter {

    public static final GsonElementConverter INSTANCE = new GsonElementConverter();

    @Override
    public boolean isJsonNull(Object element) {
	return ((JsonElement) element).isJsonNull();
    }

    @Override
    public String asNumber(Object element, Out<Boolean> asResult) {
	asResult.value = Boolean.FALSE;
	JsonElement jsonElement = (JsonElement) element;

	if (jsonElement.isJsonPrimitive() && ((JsonPrimitive) jsonElement).isNumber()) {
	    asResult.value = Boolean.TRUE;
	    return jsonElement.getAsString();
	}
	return null;
    }

    @Override
    public String asString(Object element, Out<Boolean> asResult) {
	asResult.value = Boolean.FALSE;
	JsonElement jsonElement = (JsonElement) element;

	if (jsonElement.isJsonPrimitive() && ((JsonPrimitive) jsonElement).isString()) {
	    asResult.value = Boolean.TRUE;
	    return jsonElement.getAsString();
	}
	return null;
    }

    @Override
    public boolean asBoolean(Object element, Out<Boolean> asResult) {
	asResult.value = Boolean.FALSE;
	JsonElement jsonElement = (JsonElement) element;

	if (jsonElement.isJsonPrimitive() && ((JsonPrimitive) jsonElement).isBoolean()) {
	    asResult.value = Boolean.TRUE;
	    return jsonElement.getAsBoolean();
	}
	return false;
    }

    @Override
    public Iterator<Object> asJsonArray(Object element, Out<Boolean> asResult) {
	asResult.value = Boolean.FALSE;
	JsonElement jsonElement = (JsonElement) element;

	if (jsonElement.isJsonArray()) {
	    asResult.value = Boolean.TRUE;
	    return ObjectUtils.cast(jsonElement.getAsJsonArray().iterator());
	}
	return null;
    }

    @Override
    public Iterator<Entry<String, Object>> asJsonObject(Object element, Out<Boolean> asResult) {
	asResult.value = Boolean.FALSE;
	JsonElement jsonElement = (JsonElement) element;

	if (jsonElement.isJsonObject()) {
	    asResult.value = Boolean.TRUE;
	    return ObjectUtils.cast(jsonElement.getAsJsonObject().entrySet().iterator());
	}
	return null;
    }
}
