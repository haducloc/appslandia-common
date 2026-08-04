// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class DistanceUnitTest {

  @Test
  public void test() {
    Assertions.assertEquals(1, (int) DistanceUnit.METER.unitInMeter());
    Assertions.assertEquals(1000, (int) DistanceUnit.KILOMETER.unitInMeter());
    Assertions.assertEquals(1609, (int) DistanceUnit.MILE.unitInMeter());
  }

  @Test
  public void test_symbol() {
    Assertions.assertEquals("m", DistanceUnit.METER.symbol());
    Assertions.assertEquals("km", DistanceUnit.KILOMETER.symbol());
    Assertions.assertEquals("mi", DistanceUnit.MILE.symbol());
  }

  @Test
  public void test_convert() {
    Assertions.assertEquals(1, (int) DistanceUnit.METER.convert(1, DistanceUnit.METER));
    Assertions.assertEquals(1000_00, (int) DistanceUnit.METER.convert(100, DistanceUnit.KILOMETER));
    Assertions.assertEquals(1609_344, (int) DistanceUnit.METER.convert(1000, DistanceUnit.MILE));
  }
}
