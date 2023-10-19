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
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.STR;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class LocalDateTimeAdapter extends Java8DateAdapter<LocalDateTime> {

    public LocalDateTimeAdapter() {
	this(DateUtils.ISO8601_DATETIME_N3);
    }

    public LocalDateTimeAdapter(String serializeIsoPattern) {
	super(serializeIsoPattern);
    }

    // @formatter:off
    static final String[] PATTERNS = new String[] {
        DateUtils.ISO8601_DATETIME_M,
        DateUtils.ISO8601_DATETIME_S,
        DateUtils.ISO8601_DATETIME_N1,
        DateUtils.ISO8601_DATETIME_N2,
        DateUtils.ISO8601_DATETIME_N3,
        DateUtils.ISO8601_DATETIME_N4,
        DateUtils.ISO8601_DATETIME_N5,
        DateUtils.ISO8601_DATETIME_N6,
        DateUtils.ISO8601_DATETIME_N7
    };
    // @formatter:on

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	String value = json.getAsString();

	for (String pattern : PATTERNS) {
	    if (pattern.length() == value.length() + 2) {
		try {
		    return LocalDateTime.parse(value, getFormatter(pattern));
		} catch (DateTimeParseException ex) {
		}
		break;
	    }
	}
	throw new IllegalArgumentException(STR.fmt("Couldn't parse '{}' to LocalDateTime.", value));
    }
}
