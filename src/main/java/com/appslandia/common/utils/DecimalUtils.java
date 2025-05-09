// The MIT License (MIT)
// Copyright © 2015 Loc Ha

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
 * @author Loc Ha
 *
 */
public class DecimalUtils {

  public static double round(double value, int scale, RoundingMode roundingMode) {
    if (Double.isNaN(value) || Double.isInfinite(value)) {
      return value;
    }
    var bd = new BigDecimal(Double.toString(value));
    var roundedValue = bd.setScale(scale, roundingMode).doubleValue();
    return fixSign(roundedValue, value);
  }

  public static float round(float value, int scale, RoundingMode roundingMode) {
    if (Float.isNaN(value) || Float.isInfinite(value)) {
      return value;
    }
    var bd = new BigDecimal(Float.toString(value));
    var roundedValue = bd.setScale(scale, roundingMode).floatValue();
    return fixSign(roundedValue, value);
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
    if ((Double.compare(d1, d2) == 0) || (Math.abs(d1 - d2) <= delta)) {
      return true;
    }
    return false;
  }

  public static boolean equals(float f1, float f2, float delta) {
    if ((Float.compare(f1, f2) == 0) || (Math.abs(f1 - f2) <= delta)) {
      return true;
    }
    return false;
  }

  public static boolean isFloatRange(double value) {
    var posDouble = (value >= 0d) ? value : (value * -1d);

    if (posDouble != 0d && (posDouble < Float.MIN_VALUE || posDouble > Float.MAX_VALUE)) {
      return false;
    }
    return true;
  }
}
