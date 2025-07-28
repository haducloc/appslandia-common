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
    if (Double.compare(this.unitInMeter, 0.0d) == 0) {
      return 0.0d;
    }
    return distance * distUnit.unitInMeter / this.unitInMeter;
  }

  public String symbol() {
    return this.symbol;
  }

  public double unitInMeter() {
    return this.unitInMeter;
  }
}
