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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GeoPolygon {

  final List<GeoLocation> points;

  public GeoPolygon(GeoLocation[] points) {
    this(Arrays.asList(points));
  }

  public GeoPolygon(String polygonString) {
    this(parse(polygonString));
  }

  GeoPolygon(List<GeoLocation> points) {
    Asserts.hasElements(points);
    Asserts.isTrue(points.size() >= 3, "A GeoPolygon must have at least three points.");

    this.points = Collections.unmodifiableList(points);
  }

  public boolean contains(GeoLocation point) {
    // Ray-Casting algorithm
    for (int i = 0; i < this.points.size(); i++) {

      GeoLocation pointI = this.points.get(i);
      GeoLocation pointJ = this.points.get((i + 1) % this.points.size());

      // @formatter:off
      if ((pointI.getLatitude() > point.getLatitude()) != (pointJ.getLatitude() > point.getLatitude()) &&
          (point.getLongitude() <
                   (pointJ.getLongitude() - pointI.getLongitude()) *
                   (point.getLatitude() - pointI.getLatitude()) /
                   (pointJ.getLatitude() - pointI.getLatitude()) + pointI.getLongitude()
          )) {

        return true;
      }
      // @formatter:on
    }

    return false;
  }

  public List<GeoLocation> getPoints() {
    return this.points;
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(", ", "POLYGON((", "))");
    for (GeoLocation point : this.points) {
      joiner.add(point.getLongitude() + " " + point.getLatitude());
    }
    return joiner.toString();
  }

  static final Pattern COORDINATE_PATTERN = Pattern.compile("-?\\d+\\.\\d+\\s+-?\\d+\\.\\d+");

  static final Pattern POLYGON_PATTERN = Pattern.compile("^\\s*POLYGON\\s*\\(\\(\\s*" + COORDINATE_PATTERN.pattern()
      + "(\\s*,\\s*" + COORDINATE_PATTERN.pattern() + "\\s*)+\\)\\)$", Pattern.CASE_INSENSITIVE);

  static List<GeoLocation> parse(String polygonString) {
    if (!POLYGON_PATTERN.matcher(polygonString).matches()) {
      throw new IllegalArgumentException("The given polygonString is invalid.");
    }

    List<GeoLocation> points = new ArrayList<>();
    Matcher matcher = COORDINATE_PATTERN.matcher(polygonString);

    while (matcher.find()) {
      String[] point = matcher.group().split("\\s+");

      double longitude = Double.parseDouble(point[0]);
      double latitude = Double.parseDouble(point[1]);

      points.add(new GeoLocation(latitude, longitude));
    }
    return points;
  }
}
