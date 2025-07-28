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

package com.appslandia.common.geo;

import java.text.DecimalFormat;

import com.appslandia.common.utils.Arguments;

public class GeoUtils {

  // https://en.wikipedia.org/wiki/Earth_radius: 3958.7613 Miles
  public static final double EARTH_RADIUS_METER = 6371008.74559;

  // https://en.wikipedia.org/wiki/Earth%27s_circumference
  public static final double POLAR_CIRCUMFERENCE_MILES = 24_859.734;

  public static final double EQUATOR_CIRCUMFERENCE_MILES = 24_901.461;

  public static double toDecimalDegrees(int degrees, int minutes, double seconds) {
    Arguments.isTrue(degrees >= 0);
    Arguments.isTrue(minutes >= 0);
    Arguments.isTrue(seconds >= 0.0d);

    return degrees + minutes / 60.0 + seconds / 3600;
  }

  public static String formatMinOrSec(double minOrSec, int fractionDigits) {
    if (fractionDigits < 0) {
      return Double.toString(minOrSec);
    }

    var format = (fractionDigits > 0) ? "00." + "0".repeat(fractionDigits) : "00";
    return new DecimalFormat(format).format(minOrSec);
  }
}
