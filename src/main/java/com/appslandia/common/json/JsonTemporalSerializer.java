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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeParseException;

import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class JsonTemporalSerializer {

	protected final String serializeIsoPattern;

	public JsonTemporalSerializer(String serializeIsoPattern) {
		this.serializeIsoPattern = serializeIsoPattern;
	}

	// @formatter:off
	private static final String[] TIME_PATTERNS = new String[]{
			DateUtils.ISO8601_TIME_M, DateUtils.ISO8601_TIME_S,
			DateUtils.ISO8601_TIME_N1, DateUtils.ISO8601_TIME_N2,
			DateUtils.ISO8601_TIME_N3, DateUtils.ISO8601_TIME_N4,
			DateUtils.ISO8601_TIME_N5, DateUtils.ISO8601_TIME_N6,
			DateUtils.ISO8601_TIME_N7};
	// @formatter:on

	// @formatter:off
	private static final String[] TIMEZ_PATTERNS = new String[]{
			DateUtils.ISO8601_TIMEZ_M, DateUtils.ISO8601_TIMEZ_S,
			DateUtils.ISO8601_TIMEZ_N1, DateUtils.ISO8601_TIMEZ_N2,
			DateUtils.ISO8601_TIMEZ_N3, DateUtils.ISO8601_TIMEZ_N4,
			DateUtils.ISO8601_TIMEZ_N5, DateUtils.ISO8601_TIMEZ_N6,
			DateUtils.ISO8601_TIMEZ_N7};
	// @formatter:on

	// @formatter:off
	private static final String[] DATETIME_PATTERNS = new String[]{
			DateUtils.ISO8601_DATETIME_M, DateUtils.ISO8601_DATETIME_S,
			DateUtils.ISO8601_DATETIME_N1, DateUtils.ISO8601_DATETIME_N2,
			DateUtils.ISO8601_DATETIME_N3, DateUtils.ISO8601_DATETIME_N4,
			DateUtils.ISO8601_DATETIME_N5, DateUtils.ISO8601_DATETIME_N6,
			DateUtils.ISO8601_DATETIME_N7};
	// @formatter:on

	// @formatter:off
	private static final String[] DATETIMEZ_PATTERNS = new String[]{
			DateUtils.ISO8601_DATETIMEZ_M, DateUtils.ISO8601_DATETIMEZ_S,
			DateUtils.ISO8601_DATETIMEZ_N1, DateUtils.ISO8601_DATETIMEZ_N2,
			DateUtils.ISO8601_DATETIMEZ_N3, DateUtils.ISO8601_DATETIMEZ_N4,
			DateUtils.ISO8601_DATETIMEZ_N5, DateUtils.ISO8601_DATETIMEZ_N6,
			DateUtils.ISO8601_DATETIMEZ_N7};
	// @formatter:on

	protected LocalTime parseLocalTime(String value) {
		for (String pattern : TIME_PATTERNS) {
			if (pattern.length() == value.length()) {
				try {
					return LocalTime.parse(value, DateUtils.getFormatter(pattern));
				} catch (DateTimeParseException ex) {
				}
				break;
			}
		}
		throw new IllegalArgumentException(STR.fmt("Couldn't parse '{}' to LocalTime.", value));
	}

	protected OffsetTime parseOffsetTime(String value) {
		for (String pattern : TIMEZ_PATTERNS) {
			try {
				return OffsetTime.parse(value, DateUtils.getFormatter(pattern));
			} catch (DateTimeParseException ex) {
			}
		}
		throw new IllegalArgumentException(STR.fmt("Couldn't parse '{}' to OffsetTime.", value));
	}

	protected LocalDateTime parseLocalDateTime(String value) {
		for (String pattern : DATETIME_PATTERNS) {
			if (pattern.length() == value.length() + 2) {
				try {
					return LocalDateTime.parse(value, DateUtils.getFormatter(pattern));
				} catch (DateTimeParseException ex) {
				}
				break;
			}
		}
		throw new IllegalArgumentException(STR.fmt("Couldn't parse '{}' to LocalDateTime.", value));
	}

	protected OffsetDateTime parseOffsetDateTime(String value) {
		for (String pattern : DATETIMEZ_PATTERNS) {
			try {
				return OffsetDateTime.parse(value, DateUtils.getFormatter(pattern));
			} catch (DateTimeParseException ex) {
			}
		}
		throw new IllegalArgumentException(STR.fmt("Couldn't parse '{}' to OffsetDateTime.", value));
	}
}
