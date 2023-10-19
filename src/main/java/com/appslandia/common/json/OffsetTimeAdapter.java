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
import java.time.OffsetTime;
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
public class OffsetTimeAdapter extends Java8DateAdapter<OffsetTime> {

    public OffsetTimeAdapter() {
	this(DateUtils.ISO8601_TIMEZ_N3);
    }

    public OffsetTimeAdapter(String serializeIsoPattern) {
	super(serializeIsoPattern);
    }

    // @formatter:off
    static final String[] PATTERNS = new String[] {
        DateUtils.ISO8601_TIMEZ_M,
        DateUtils.ISO8601_TIMEZ_S,
        DateUtils.ISO8601_TIMEZ_N1,
        DateUtils.ISO8601_TIMEZ_N2,
        DateUtils.ISO8601_TIMEZ_N3,
        DateUtils.ISO8601_TIMEZ_N4,
        DateUtils.ISO8601_TIMEZ_N5,
        DateUtils.ISO8601_TIMEZ_N6,
        DateUtils.ISO8601_TIMEZ_N7
    };
    // @formatter:on

    @Override
    public OffsetTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	String value = json.getAsString();

	for (String pattern : PATTERNS) {
	    try {
		return OffsetTime.parse(value, getFormatter(pattern));
	    } catch (DateTimeParseException ex) {
	    }
	}
	throw new IllegalArgumentException(STR.fmt("Couldn't parse '{}' to OffsetTime.", value));
    }
}
