// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.geo;

/**
 *
 * @author Loc Ha
 *
 */
public enum DistanceUnit {

  METER("m", 1.0), KILOMETER("km", 1000.0), MILE("mi", 1609.344), NAUTICAL_MILE("nm", 1851.85185185);

  final String symbol;
  final double unitInMeter;

  private DistanceUnit(String symbol, double unitInMeter) {
    this.symbol = symbol;
    this.unitInMeter = unitInMeter;
  }

  public double convert(double distance, DistanceUnit distUnit) {
    if (this == distUnit) {
      return distance;
    }
    if (Double.compare(unitInMeter, 0.0d) == 0) {
      return 0.0d;
    }
    return distance * distUnit.unitInMeter / unitInMeter;
  }

  public String symbol() {
    return symbol;
  }

  public double unitInMeter() {
    return unitInMeter;
  }
}
