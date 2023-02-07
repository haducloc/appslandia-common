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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.DecimalUtils;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FloatConverter extends NumberConverter<Float> {

    public static final String ERROR_MSG_KEY = FloatConverter.class.getName() + ".message";

    final int fractionDigits;
    final RoundingMode roundingMode;

    public FloatConverter(int fractionDigits, RoundingMode roundingMode) {
	this.fractionDigits = fractionDigits;
	this.roundingMode = roundingMode;
    }

    @Override
    public String getErrorMsgKey() {
	return ERROR_MSG_KEY;
    }

    @Override
    public Class<Float> getTargetType() {
	return Float.class;
    }

    @Override
    public String format(Float obj, FormatProvider formatProvider, boolean localize) {
	if (obj == null) {
	    return null;
	}

	if (localize) {
	    NumberFormat nf = formatProvider.getNumberFormat(this.fractionDigits, this.roundingMode, false);
	    return nf.format(obj);
	}

	float value = DecimalUtils.round(obj.floatValue(), this.fractionDigits, this.roundingMode);
	String str = new BigDecimal(Float.toString(value)).setScale(this.fractionDigits, this.roundingMode).toString();

	return DecimalUtils.isNegativeZero(value) ? ('-' + str) : str;
    }

    @Override
    public Float parse(String str, FormatProvider formatProvider) throws ConverterException {
	str = StringUtils.trimToNull(str);
	if (str == null) {
	    return null;
	}

	try {
	    double value = Double.parseDouble(str);

	    if (!ValueUtils.isFloatRange(value))
		throw toNumberOverflowError(str);

	    return (float) value;

	} catch (NumberFormatException ex) {
	}

	Number number = this.parseNumber(str, formatProvider.getNumberParser());
	if (number != null) {
	    double value = number.doubleValue();

	    if (!ValueUtils.isFloatRange(value))
		throw toNumberOverflowError(str);

	    return (float) value;
	}

	throw toParsingError(str, getTargetType().getName());
    }
}
