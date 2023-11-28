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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GeoPolygon implements Serializable {
  private static final long serialVersionUID = 1L;

  final List<List<GeoLocation>> polygons;

  public GeoPolygon(List<List<GeoLocation>> polygons) {
    Asserts.hasElements(polygons);

    for (List<GeoLocation> polygon : polygons) {
      Asserts.isTrue(polygon.size() >= 3, "Each polygon must consist of at least three points.");
    }
    this.polygons = polygons;
  }

  public boolean contains(GeoLocation point) {
    return contains(point, 0.00001);
  }

  public boolean contains(GeoLocation point, double tolerance) {
    int intersects = 0;

    // Iterate through all polygons, including the most outer polygon and inner
    // holes.
    for (List<GeoLocation> polygon : this.polygons) {

      // Iterate through each edge of the polygon
      for (int i = 0; i < polygon.size(); i++) {
        GeoLocation A = polygon.get(i);
        GeoLocation B = polygon.get((i + 1) % polygon.size());

        // Ensure A.y is less than or equal to B.y
        if (A.y > B.y) {
          GeoLocation temp = A;
          A = B;
          B = temp;
        }

        // If the point has the same y as either A or B, adjust the point's y
        if (Double.compare(point.y, A.y) == 0 || Double.compare(point.y, B.y) == 0) {
          point = new GeoLocation(point.x, point.y + tolerance);
        }

        // Check if the point is outside the vertical bounds of the current edge
        // or to the right of the rightmost point of the edge
        if (point.y < A.y || point.y > B.y || point.x > Math.max(A.x, B.x)) {
          continue;
        }

        // Check if the point is to the left of the leftmost point of the edge
        if (point.x < Math.min(A.x, B.x)) {
          intersects++;
          continue;
        }

        // Calculate slopes
        double abSlope = (Double.compare(B.x, A.x) != 0) ? (B.y - A.y) / (B.x - A.x) : Double.POSITIVE_INFINITY;
        double apSlope = (Double.compare(point.x, A.x) != 0) ? (point.y - A.y) / (point.x - A.x)
            : Double.POSITIVE_INFINITY;

        if (Double.compare(apSlope, abSlope) >= 0) {
          intersects++;
        }
      }
    }

    // Return true if the number of intersections is odd,
    // indicating the point is inside the polygon
    return intersects % 2 == 1;
  }

  public String toStringWKT() {
    StringBuilder sb = new StringBuilder();
    sb.append("POLYGON(");

    for (int i = 0; i < this.polygons.size(); i++) {
      List<GeoLocation> polygon = this.polygons.get(i);

      if (i > 0)
        sb.append(", ");
      sb.append("(");

      for (int j = 0; j < polygon.size(); j++) {
        GeoLocation point = polygon.get(j);

        if (j > 0)
          sb.append(", ");
        sb.append(point.x).append(" ").append(point.y);
      }

      // Add the first point
      sb.append(", ").append(polygon.getFirst().x).append(" ").append(polygon.getFirst().y);
      sb.append(")");
    }
    sb.append(")");
    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");

    for (int i = 0; i < this.polygons.size(); i++) {
      List<GeoLocation> polygon = this.polygons.get(i);

      if (i > 0)
        sb.append(", ");
      sb.append("[");

      for (int j = 0; j < polygon.size(); j++) {
        GeoLocation point = polygon.get(j);

        if (j > 0)
          sb.append(", ");
        sb.append("(").append(point.y).append(", ").append(point.x).append(")");
      }

      sb.append("]");
    }
    sb.append("]");
    return sb.toString();
  }

  static final Pattern POINT_PATTERN = Pattern.compile("(-|\\+)?\\d+(\\.\\d+)?\\s+(-|\\+)?\\d+(\\.\\d+)?");

  static final Pattern POLYGON_PATTERN = Pattern
      .compile("\\(\\s*" + POINT_PATTERN.pattern() + "(\\s*,\\s*" + POINT_PATTERN.pattern() + "\\s*){3,}" + "\\s*\\)");

  static final Pattern POLYGON_PATTERN_WKT = Pattern.compile("^\\s*POLYGON\\s*\\(\\s*" + POLYGON_PATTERN.pattern()
      + "(\\s*,\\s*" + POLYGON_PATTERN.pattern() + "\\s*){0,}" + "\\s*\\)\\s*$", Pattern.CASE_INSENSITIVE);

  // Well-Known Text (WKT) Format
  static GeoPolygon parseWKT(String polygonAsWKT) {
    if (!POLYGON_PATTERN_WKT.matcher(polygonAsWKT).matches()) {
      throw new IllegalArgumentException("The given polygonAsWKT is invalid.");
    }

    List<List<GeoLocation>> polygons = new ArrayList<>();
    Matcher polygonMatcher = POLYGON_PATTERN.matcher(polygonAsWKT);
    while (polygonMatcher.find()) {

      String polygonStr = polygonMatcher.group();
      List<GeoLocation> polygon = new ArrayList<>();

      Matcher pointMatcher = POINT_PATTERN.matcher(polygonStr);
      while (pointMatcher.find()) {
        String[] point = pointMatcher.group().split("\\s+");

        double x = Double.parseDouble(point[0]);
        double y = Double.parseDouble(point[1]);

        polygon.add(new GeoLocation(x, y));
      }

      if (!polygon.getLast().equals(polygon.getFirst())) {
        throw new IllegalArgumentException(
            "The given polygonString is invalid. The last point of each polygon must be the same as its first point.");
      }

      polygon.removeLast();
      polygons.add(polygon);
    }
    return new GeoPolygon(polygons);
  }
}
