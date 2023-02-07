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
public class GeoDM {

    final int degrees;
    final double minutes;
    final Direction direction;

    GeoDM(double decimalDegrees, Direction direction) {
	Asserts.isTrue(decimalDegrees >= 0.0d);
	Asserts.notNull(direction);

	int d = (int) decimalDegrees;
	double m = decimalDegrees * 60 - d * 60;

	this.degrees = d;
	this.minutes = m;

	this.direction = direction;
    }

    GeoDM(int degrees, double minutes, Direction direction) {
	Asserts.isTrue(degrees >= 0);
	Asserts.isTrue(minutes >= 0.0d);
	Asserts.notNull(direction);

	this.degrees = degrees;
	this.minutes = minutes;
	this.direction = direction;
    }

    public int getDegrees() {
	return this.degrees;
    }

    public double getMinutes() {
	return this.minutes;
    }

    public Direction getDirection() {
	return this.direction;
    }

    public double toDecimalDegrees() {
	double deg = GeoUtils.toDecimalDegrees(this.degrees, this.minutes);

	if (this.direction == Direction.NORTH || this.direction == Direction.EAST) {
	    return deg;
	}
	return -deg;
    }

    public String toString(int minutesDecimals) {
	Asserts.isTrue(minutesDecimals >= 0 && minutesDecimals <= 7);

	String minfmt = GeoUtils.format(this.minutes, minutesDecimals);
	return String.format("%d°%s'%s", this.degrees, minfmt, this.direction.symbol());
    }

    @Override
    public String toString() {
	return toString(2);
    }

    public static GeoDM toLatDM(double latitude) {
	Asserts.isTrue((latitude >= -90.0) && (latitude <= 90.0), "latitude is invalid.");

	return new GeoDM(Math.abs(latitude), Double.compare(latitude, 0.0) >= 0 ? Direction.NORTH : Direction.SOUTH);
    }

    public static GeoDM toLongDM(double longitude) {
	Asserts.isTrue((longitude >= -180.0) && (longitude <= 180.0), "longitude is invalid.");

	return new GeoDM(Math.abs(longitude), Double.compare(longitude, 0.0) >= 0 ? Direction.EAST : Direction.WEST);
    }
}
