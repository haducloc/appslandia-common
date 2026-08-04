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
public class GeoDMSTest {

  @Test
  public void test_toLatDMS() {

    var dms = GeoDMS.toLatDMS(GeoUtils.toDecimalDegrees(10, 20, 30.05));

    Assertions.assertEquals(10, dms.getDegrees());
    Assertions.assertEquals(20, dms.getMinutes());
    Assertions.assertEquals(30.05, dms.getSeconds(), 0.001d);

    Assertions.assertEquals(Direction.NORTH, dms.getDirection());
  }

  @Test
  public void test_toLatDMS_Zero() {

    var dms = GeoDMS.toLatDMS(0.0);

    Assertions.assertEquals(0, dms.getDegrees());
    Assertions.assertEquals(0, dms.getMinutes());
    Assertions.assertEquals(0.0, dms.getSeconds(), 0.01d);

    Assertions.assertEquals(Direction.NORTH, dms.getDirection());
  }

  @Test
  public void test_toLongDMS() {

    var dms = GeoDMS.toLongDMS(GeoUtils.toDecimalDegrees(10, 20, 30.05));

    Assertions.assertEquals(10, dms.getDegrees());
    Assertions.assertEquals(20, dms.getMinutes());
    Assertions.assertEquals(30.05, dms.getSeconds(), 0.001d);

    Assertions.assertEquals(Direction.EAST, dms.getDirection());
  }

  @Test
  public void test_toLongDMS_Zero() {

    var dms = GeoDMS.toLongDMS(-0.0);

    Assertions.assertEquals(0, dms.getDegrees());
    Assertions.assertEquals(0, dms.getMinutes());
    Assertions.assertEquals(0.0, dms.getSeconds(), 0.01d);

    // Direction.WEST because -0.0
    Assertions.assertEquals(Direction.WEST, dms.getDirection());
  }

  @Test
  public void test_toGeoDMS_lat() {

    var dms = GeoDMS.toGeoDMS("10°20'30.05\"N");

    Assertions.assertEquals(10, dms.getDegrees());
    Assertions.assertEquals(20, dms.getMinutes());
    Assertions.assertEquals(30.05, dms.getSeconds(), 0.001d);

    Assertions.assertEquals(Direction.NORTH, dms.getDirection());
  }

  @Test
  public void test_toGeoDMS_long() {

    var dms = GeoDMS.toGeoDMS("10°20'30.05\"E");

    Assertions.assertEquals(10, dms.getDegrees());
    Assertions.assertEquals(20, dms.getMinutes());
    Assertions.assertEquals(30.05, dms.getSeconds(), 0.001d);

    Assertions.assertEquals(Direction.EAST, dms.getDirection());
  }
}
