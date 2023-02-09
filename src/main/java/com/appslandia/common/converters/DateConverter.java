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

package com.appslandia.common.converters;

import java.text.DateFormat;
import java.text.ParsePosition;

import com.appslandia.common.base.FormatProvider;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class DateConverter<T extends java.util.Date> implements Converter<T> {

    final String isoPattern;

    public DateConverter(String isoPattern) {
	this.isoPattern = isoPattern;
    }

    @Override
    public String format(T obj, FormatProvider formatProvider, boolean localize) {
	if (obj == null) {
	    return null;
	}
	if (localize) {
	    String pattern = formatProvider.getLanguage().getTemporalPattern(this.isoPattern);
	    return formatProvider.getDateFormat(pattern).format(obj);
	}
	return formatProvider.getDateFormat(this.isoPattern).format(obj);
    }

    protected java.util.Date doParse(String str, FormatProvider formatProvider) throws ConverterException {
	java.util.Date parsedValue = parseDate(str, formatProvider.getDateFormat(this.isoPattern));
	if (parsedValue != null) {
	    return parsedValue;
	}
	String pattern = formatProvider.getLanguage().getTemporalPattern(this.isoPattern);

	parsedValue = parseDate(str, formatProvider.getDateFormat(pattern));
	if (parsedValue != null) {
	    return parsedValue;
	}
	throw toParsingError(str, getTargetType().getName());
    }

    private static java.util.Date parseDate(String str, DateFormat dateFormat) {
	ParsePosition pos = new ParsePosition(0);
	java.util.Date parsedValue = dateFormat.parse(str, pos);

	if ((pos.getErrorIndex() < 0) && (pos.getIndex() == str.length()) && (parsedValue != null)) {
	    return parsedValue;
	}
	return null;
    }
}
