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

package com.appslandia.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DecimalUtils {

    public static double round(double value, int scale, RoundingMode roundingMode) {
	try {
	    double roundedValue = (new BigDecimal(Double.toString(value)).setScale(scale, roundingMode)).doubleValue();
	    return fixSign(roundedValue, value);

	} catch (NumberFormatException ex) {
	    if (Double.isInfinite(value))
		return value;
	    else
		return Double.NaN;
	}
    }

    public static float round(float value, int scale, RoundingMode roundingMode) {
	try {
	    float roundedValue = (new BigDecimal(Float.toString(value)).setScale(scale, roundingMode)).floatValue();
	    return fixSign(roundedValue, value);

	} catch (NumberFormatException ex) {
	    if (Float.isInfinite(value))
		return value;
	    else
		return Float.NaN;
	}
    }

    public static double fixSign(double roundedValue, double value) {
	return roundedValue == 0.0d ? 0.0d * value : roundedValue;
    }

    public static float fixSign(float roundedValue, float value) {
	return roundedValue == 0.0f ? 0.0f * value : roundedValue;
    }

    public static boolean isNegativeZero(double value) {
	return (value == 0.0d) && (Double.compare(value, 0.0d) < 0);
    }

    public static boolean isNegativeZero(float value) {
	return (value == 0.0f) && (Float.compare(value, 0.0f) < 0);
    }

    public static boolean equals(double d1, double d2, double delta) {
	if (Double.compare(d1, d2) == 0)
	    return true;

	if ((Math.abs(d1 - d2) <= delta))
	    return true;

	return false;
    }

    public static boolean equals(float f1, float f2, float delta) {
	if (Float.compare(f1, f2) == 0)
	    return true;

	if ((Math.abs(f1 - f2) <= delta))
	    return true;

	return false;
    }
}
