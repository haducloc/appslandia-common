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
public class DateAdapter implements JsonSerializer<java.util.Date>, JsonDeserializer<java.util.Date> {

    @Override
    public JsonElement serialize(java.util.Date src, Type typeOfSrc, JsonSerializationContext context) {
	return new JsonPrimitive(DateUtils.newDateFormat(DateUtils.ISO8601_DATETIME).format(src));
    }

    @Override
    public java.util.Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	String value = json.getAsString();
	try {
	    if (DateUtils.ISO8601_DATE.length() == value.length()) {
		return DateUtils.newDateFormat(DateUtils.ISO8601_DATE).parse(value);

	    } else if (DateUtils.ISO8601_DATETIME_M.length() - 2 == value.length()) {
		return DateUtils.newDateFormat(DateUtils.ISO8601_DATETIME_M).parse(value);

	    } else if (DateUtils.ISO8601_DATETIME_S.length() - 2 == value.length()) {
		return DateUtils.newDateFormat(DateUtils.ISO8601_DATETIME_S).parse(value);

	    } else if (DateUtils.ISO8601_DATETIME.length() - 2 == value.length()) {
		return DateUtils.newDateFormat(DateUtils.ISO8601_DATETIME).parse(value);
	    }
	} catch (ParseException ex) {
	}
	throw new IllegalArgumentException(STR.fmt("Couldn't parse '{}' to java.util.Date.", value));
    }
}
