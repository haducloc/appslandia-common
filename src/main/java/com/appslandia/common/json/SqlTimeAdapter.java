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
import java.text.ParseException;

import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.STR;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 */
public class SqlTimeAdapter implements JsonSerializer<java.sql.Time>, JsonDeserializer<java.sql.Time> {

    @Override
    public JsonElement serialize(java.sql.Time src, Type typeOfSrc, JsonSerializationContext context) {
	return new JsonPrimitive(DateUtils.newDateFormat(DateUtils.ISO8601_TIME).format(src));
    }

    @Override
    public java.sql.Time deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	String value = json.getAsString();

	try {
	    if (DateUtils.ISO8601_TIME_M.length() == value.length()) {
		return new java.sql.Time(DateUtils.newDateFormat(DateUtils.ISO8601_TIME_M).parse(value).getTime());

	    } else if (DateUtils.ISO8601_TIME_S.length() == value.length()) {
		return new java.sql.Time(DateUtils.newDateFormat(DateUtils.ISO8601_TIME_S).parse(value).getTime());

	    } else if (DateUtils.ISO8601_TIME.length() == value.length()) {
		return new java.sql.Time(DateUtils.newDateFormat(DateUtils.ISO8601_TIME).parse(value).getTime());
	    }
	} catch (ParseException ex) {
	}
	throw new IllegalArgumentException(STR.fmt("Couldn't parse '{}' to java.sql.Time.", value));
    }
}
