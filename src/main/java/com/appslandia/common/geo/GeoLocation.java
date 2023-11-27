// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GeoLocation implements Serializable {
  private static final long serialVersionUID = 1L;

  public final double x;
  public final double y;

  public GeoLocation(double longitudeX, double latitudeY) {
    Asserts.isTrue(!((longitudeX < -180.0) || (longitudeX > 180.0)), "longitudeX is invalid.");
    Asserts.isTrue(!((latitudeY < -90.0) || (latitudeY > 90.0)), "latitudeY is invalid.");

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
    Asserts.notNull(direction);
    Asserts.notNull(unit);

    double perdegLong = 360.0 / GeoUtils.POLAR_CIRCUMFERENCE_MILES;
    double perdegLat = 360.0 / (Math.cos(Math.toRadians(this.y)) * GeoUtils.EQUATOR_CIRCUMFERENCE_MILES);

    switch (direction) {
    case NORTH:
      return new GeoLocation(this.x, this.y + DistanceUnit.MILE.convert(distance, unit) * perdegLong);

    case SOUTH:
      return new GeoLocation(this.x, this.y - DistanceUnit.MILE.convert(distance, unit) * perdegLong);

    case EAST:
      return new GeoLocation(this.x + DistanceUnit.MILE.convert(distance, unit) * perdegLat, this.y);

    case WEST:
      return new GeoLocation(this.x - DistanceUnit.MILE.convert(distance, unit) * perdegLat, this.y);
    default:
      throw new Error();
    }
  }

  public double distanceTo(GeoLocation to, DistanceUnit unit) {
    // Ensure 'to' and 'unit' are not null
    Asserts.notNull(to);
    Asserts.notNull(unit);

    // Convert latitude and longitude to radians
    double radLat1 = Math.toRadians(this.y);
    double radLat2 = Math.toRadians(to.y);
    double radLon1 = Math.toRadians(this.x);
    double radLon2 = Math.toRadians(to.x);

    // HaversineFormula: https://en.wikipedia.org/wiki/Haversine_formula
    double h = Math.sin((radLat2 - radLat1) / 2) * Math.sin((radLat2 - radLat1) / 2)
        + Math.cos(radLat1) * Math.cos(radLat2) * Math.sin((radLon2 - radLon1) / 2) * Math.sin((radLon2 - radLon1) / 2);

    // Calculate the great-circle distance in meters
    double distanceInMeters = 2 * GeoUtils.EARTH_RADIUS_METER * Math.asin(Math.sqrt(h));

    // Convert distance to the desired unit
    return unit.convert(distanceInMeters, DistanceUnit.METER);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    GeoLocation that = (GeoLocation) o;
    return Double.compare(that.x, this.x) == 0 && Double.compare(that.y, this.y) == 0;
  }

  public String toStringWKT() {
    return "POINT(" + this.x + " " + this.y + ")";
  }

  @Override
  public String toString() {
    return this.y + ", " + this.x;
  }

  static final Pattern POINT_PATTERN = Pattern.compile("(-|\\+)?\\d+(\\.\\d+)?\\s+(-|\\+)?\\d+(\\.\\d+)?");

  static final Pattern POINT_PATTERN_WKT = Pattern
      .compile("^\\s*POINT\\s*\\(\\s*" + POINT_PATTERN.pattern() + "\\s*\\)\\s*$", Pattern.CASE_INSENSITIVE);

  public static GeoLocation parseWKT(String pointAsWKT) {
    if (!POINT_PATTERN_WKT.matcher(pointAsWKT).matches()) {
      throw new IllegalArgumentException("The given pointAsWKT is invalid.");
    }

    Matcher matcher = POINT_PATTERN.matcher(pointAsWKT);
    GeoLocation loc = null;

    while (matcher.find()) {
      String[] point = matcher.group().split("\\s+");

      double longitude = Double.parseDouble(point[0]);
      double latitude = Double.parseDouble(point[1]);

      loc = new GeoLocation(longitude, latitude);
    }
    return loc;
  }
}
