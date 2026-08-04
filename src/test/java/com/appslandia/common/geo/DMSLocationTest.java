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
public class DMSLocationTest {

  @Test
  public void test() {
    var dm = new DMSLocation(-94.123456, 40.123456);
    var loc = dm.toGeoLocation();

    Assertions.assertEquals(dm.getLatitude().toDecimalDegrees(), loc.getLatitude(), 0.0000001);
    Assertions.assertEquals(dm.getLongitude().toDecimalDegrees(), loc.getLongitude(), 0.0000001);

    Assertions.assertEquals(loc.getLatitude(), 40.123456, 0.0000001);
    Assertions.assertEquals(loc.getLongitude(), -94.123456, 0.0000001);
  }

  @Test
  public void test_toDMSLocation() {
    var loc = DMSLocation.toDMSLocation("11°21'31\"N, 10°20'30\"E");

    Assertions.assertEquals(11, loc.y.getDegrees());
    Assertions.assertEquals(21, loc.y.getMinutes());

    Assertions.assertEquals(10, loc.x.getDegrees());
    Assertions.assertEquals(20, loc.x.getMinutes());
  }
}
