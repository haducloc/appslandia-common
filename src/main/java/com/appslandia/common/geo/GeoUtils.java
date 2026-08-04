// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
