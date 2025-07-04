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

/**
 *
 * @author Loc Ha
 *
 */
public class GeoLocation implements Serializable {
  private static final long serialVersionUID = 1L;

  public final double x;
  public final double y;

  public GeoLocation(double longitudeX, double latitudeY) {
    Arguments.isTrue(longitudeX >= -180.0 && longitudeX <= 180.0, "longitudeX is invalid.");
    Arguments.isTrue(latitudeY >= -90.0 && latitudeY <= 90.0, "latitudeY is invalid.");

    this.x = longitudeX;
    this.y = latitudeY;
  }

  public double getLongitude() {
    return this.x;
  }

  public double getLatitude() {
    return this.y;
  }

  public DMSLocation toDMSLocation() {
    return new DMSLocation(this.x, this.y);
  }

  public GeoLocation move(Direction direction, double distance, DistanceUnit unit) {
    Arguments.notNull(direction);
    Arguments.notNull(unit);

    var perdegLat = 360.0 / GeoUtils.POLAR_CIRCUMFERENCE_MILES;
    var cosLat = Math.cos(Math.toRadians(this.y));
    if (Math.abs(cosLat) < 1e-10) {
      cosLat = 1e-10;
    }
    var perdegLong = 360.0 / (cosLat * GeoUtils.EQUATOR_CIRCUMFERENCE_MILES);

    var distanceInDegreesLat = DistanceUnit.MILE.convert(distance, unit) * perdegLat;
    var distanceInDegreesLon = DistanceUnit.MILE.convert(distance, unit) * perdegLong;

    var newLat = this.y;
    var newLon = this.x;

    switch (direction) {
    case NORTH:
      newLat += distanceInDegreesLat;
      break;
    case SOUTH:
      newLat -= distanceInDegreesLat;
      break;
    case EAST:
      newLon += distanceInDegreesLon;
      break;
    case WEST:
      newLon -= distanceInDegreesLon;
      break;
    default:
      throw new Error();
    }

    // Ensure latitude remains within valid bounds
    newLat = Math.max(-90.0, Math.min(90.0, newLat));

    // Normalize longitude to be within [-180, 180]
    if (newLon > 180.0) {
      newLon -= 360.0;
    } else if (newLon < -180.0) {
      newLon += 360.0;
    }
    return new GeoLocation(newLon, newLat);
  }

  public double distanceTo(GeoLocation to, DistanceUnit unit) {
    Arguments.notNull(to);
    Arguments.notNull(unit);

    if (this.x == to.x && this.y == to.y) {
      return 0.0;
    }

    var radLat1 = Math.toRadians(this.y);
    var radLat2 = Math.toRadians(to.y);
    var radLon1 = Math.toRadians(this.x);
    var radLon2 = Math.toRadians(to.x);

    var deltaLat = radLat2 - radLat1;
    var deltaLon = radLon2 - radLon1;

    var sinDeltaLat = Math.sin(deltaLat / 2);
    var sinDeltaLon = Math.sin(deltaLon / 2);

    // HaversineFormula: https://en.wikipedia.org/wiki/Haversine_formula
    var h = sinDeltaLat * sinDeltaLat + Math.cos(radLat1) * Math.cos(radLat2) * sinDeltaLon * sinDeltaLon;

    var distanceInMeters = 2 * GeoUtils.EARTH_RADIUS_METER * Math.asin(Math.min(1.0, Math.sqrt(h)));
    return unit.convert(distanceInMeters, DistanceUnit.METER);
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

    var that = (GeoLocation) o;
    return that.x == this.x && that.y == this.y;
  }

  public String toStringWKT() {
    return "POINT(" + this.x + " " + this.y + ")";
  }

  @Override
  public String toString() {
    return this.y + ", " + this.x;
  }

  public static String toStringWKT(GeoLocation location) {
    if (location == null) {
      return "POINT EMPTY";
    }
    return location.toStringWKT();
  }

  static final Pattern POINT_PATTERN = Pattern.compile("(-|\\+)?\\d+(\\.\\d+)?\\s+(-|\\+)?\\d+(\\.\\d+)?");

  static final Pattern POINT_PATTERN_WKT = Pattern.compile("^POINT\\s*\\(\\s*" + POINT_PATTERN.pattern() + "\\s*\\)$",
      Pattern.CASE_INSENSITIVE);

  static final Pattern POINT_PATTERN_EMPTY = Pattern.compile("^POINT\\s+EMPTY$", Pattern.CASE_INSENSITIVE);

  public static GeoLocation parseWKT(String pointAsWKT) {
    if (POINT_PATTERN_EMPTY.matcher(pointAsWKT).matches()) {
      return null;
    }
    if (!POINT_PATTERN_WKT.matcher(pointAsWKT).matches()) {
      throw new IllegalArgumentException("The given pointAsWKT is invalid.");
    }

    var matcher = POINT_PATTERN.matcher(pointAsWKT);
    GeoLocation loc = null;

    while (matcher.find()) {
      var point = matcher.group().split("\\s+");

      var longitude = Double.parseDouble(point[0]);
      var latitude = Double.parseDouble(point[1]);

      loc = new GeoLocation(longitude, latitude);
    }
    return loc;
  }
}
