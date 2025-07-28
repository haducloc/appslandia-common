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

    this.x = GeoDMS.toLongDMS(longitudeX);
    this.y = GeoDMS.toLatDMS(latitudeY);
  }

  DMSLocation(GeoDMS longitudeX, GeoDMS latitudeY) {
    this.x = longitudeX;
    this.y = latitudeY;
  }

  public GeoDMS getLongitude() {
    return this.x;
  }

  public GeoDMS getLatitude() {
    return this.y;
  }

  public GeoLocation toGeoLocation() {
    return new GeoLocation(this.x.toDecimalDegrees(), this.y.toDecimalDegrees());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
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
    return Objects.equals(this.x, that.x) && Objects.equals(this.y, that.y);
  }

  @Override
  public String toString() {
    return this.y.toString() + ", " + this.x.toString();
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
