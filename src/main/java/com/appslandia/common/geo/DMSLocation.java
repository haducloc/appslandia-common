// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.geo;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.SplitUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class DMSLocation implements Serializable {
  private static final long serialVersionUID = 1L;

  public final GeoDMS x;
  public final GeoDMS y;

  public DMSLocation(double longitudeX, double latitudeY) {
    Arguments.isTrue(longitudeX >= -180.0 && longitudeX <= 180.0, "longitudeX is invalid.");
    Arguments.isTrue(latitudeY >= -90.0 && latitudeY <= 90.0, "latitudeY is invalid.");

    x = GeoDMS.toLongDMS(longitudeX);
    y = GeoDMS.toLatDMS(latitudeY);
  }

  DMSLocation(GeoDMS longitudeX, GeoDMS latitudeY) {
    x = longitudeX;
    y = latitudeY;
  }

  public GeoDMS getLongitude() {
    return x;
  }

  public GeoDMS getLatitude() {
    return y;
  }

  public GeoLocation toGeoLocation() {
    return new GeoLocation(x.toDecimalDegrees(), y.toDecimalDegrees());
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    var that = (DMSLocation) o;
    return Objects.equals(x, that.x) && Objects.equals(y, that.y);
  }

  @Override
  public String toString() {
    return y.toString() + ", " + x.toString();
  }

  static final Pattern DMS_LOCATION_PATTERN = Pattern.compile(
      STR.fmt("{}\\s*,\\s*{}", GeoDMS.DMS_PATTERN.pattern(), GeoDMS.DMS_PATTERN.pattern()), Pattern.CASE_INSENSITIVE);

  public static DMSLocation toDMSLocation(String dmsLocation) {
    Arguments.notNull(dmsLocation);
    Arguments.isTrue(DMS_LOCATION_PATTERN.matcher(dmsLocation).matches(), "dmsLocation {} is invalid.", dmsLocation);

    var geoDmss = SplitUtils.splitByComma(dmsLocation);

    var dms1 = GeoDMS.toGeoDMS(geoDmss[0]);
    var dms2 = GeoDMS.toGeoDMS(geoDmss[1]);

    Arguments.isTrue(!(dms1.isLatitude() && dms2.isLatitude()) && !(dms1.isLongitude() && dms2.isLongitude()),
        "dmsLocation {} is invalid.", dmsLocation);

    return dms1.isLatitude() ? new DMSLocation(dms2, dms1) : new DMSLocation(dms1, dms2);
  }
}
