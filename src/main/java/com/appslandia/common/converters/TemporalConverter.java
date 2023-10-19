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

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class TemporalConverter<T extends Temporal> implements Converter<T> {

    final String isoPattern;

    public TemporalConverter(String isoPattern) {
	this.isoPattern = isoPattern;
    }

    protected abstract T parse(String str, DateTimeFormatter converter) throws DateTimeParseException;

    @Override
    public String format(T obj, FormatProvider formatProvider, boolean localize) {
	if (obj == null) {
	    return null;
	}
	if (localize) {
	    String pattern = formatProvider.getLanguage().getTemporalPattern(this.isoPattern);
	    return getFormatter(pattern).format(obj);
	}
	return getFormatter(this.isoPattern).format(obj);
    }

    @Override
    public T parse(String str, FormatProvider formatProvider) throws ConverterException {
	str = StringUtils.trimToNull(str);
	if (str == null) {
	    return null;
	}
	try {
	    return parse(str, getFormatter(this.isoPattern));
	} catch (DateTimeParseException ex) {
	}

	try {
	    String pattern = formatProvider.getLanguage().getTemporalPattern(this.isoPattern);
	    return parse(str, getFormatter(pattern));

	} catch (DateTimeParseException ex) {
	}
	throw toParsingError(str, getTargetType().getName());
    }

    protected static DateTimeFormatter getFormatter(String pattern) {
	return DateUtils.getFormatter(pattern);
    }
}