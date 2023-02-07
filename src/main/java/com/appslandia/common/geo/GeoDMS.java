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

import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GeoDMS {

    final int degrees;
    final int minutes;
    final double seconds;
    final Direction direction;

    GeoDMS(double decimalDegrees, Direction direction) {
	Asserts.isTrue(decimalDegrees >= 0.0d);
	Asserts.notNull(direction);

	int d = (int) decimalDegrees;
	int m = (int) ((decimalDegrees - d) * 60);
	double s = decimalDegrees * 3600 - d * 3600 - m * 60;

	this.degrees = d;
	this.minutes = m;
	this.seconds = s;

	this.direction = direction;
    }

    GeoDMS(int degrees, int minutes, double seconds, Direction direction) {
	this(GeoUtils.toDecimalDegrees(degrees, minutes, seconds), direction);
    }

    public int getDegrees() {
	return this.degrees;
    }

    public int getMinutes() {
	return this.minutes;
    }

    public double getSeconds() {
	return this.seconds;
    }

    public Direction getDirection() {
	return this.direction;
    }

    public double toDecimalDegrees() {
	double deg = GeoUtils.toDecimalDegrees(this.degrees, this.minutes, this.seconds);

	if (this.direction == Direction.NORTH || this.direction == Direction.EAST)
	    return deg;

	return -deg;
    }

    public String toStringDMS(int secondsDecimals) {
	String fmtSec = GeoUtils.format(this.seconds, secondsDecimals);
	return String.format("%d°%02d'%s\"%s", this.degrees, this.minutes, fmtSec, this.direction.symbol());
    }

    @Override
    public String toString() {
	return toStringDMS(1);
    }

    public static GeoDMS toLatDMS(double latitude) {
	Asserts.isTrue((latitude >= -90.0) && (latitude <= 90.0), "latitude is invalid.");

	return new GeoDMS(Math.abs(latitude), Double.compare(latitude, 0.0) >= 0 ? Direction.NORTH : Direction.SOUTH);
    }

    public static GeoDMS toLongDMS(double longitude) {
	Asserts.isTrue((longitude >= -180.0) && (longitude <= 180.0), "longitude is invalid.");

	return new GeoDMS(Math.abs(longitude), Double.compare(longitude, 0.0) >= 0 ? Direction.EAST : Direction.WEST);
    }

    public static GeoDMS toLatDMS(int degrees, int minutes, double seconds) {
	double decimalDegrees = GeoUtils.toDecimalDegrees(degrees, minutes, seconds);
	return toLatDMS(decimalDegrees);
    }

    public static GeoDMS toLongDMS(int degrees, int minutes, double seconds) {
	double decimalDegrees = GeoUtils.toDecimalDegrees(degrees, minutes, seconds);
	return toLongDMS(decimalDegrees);
    }
}
