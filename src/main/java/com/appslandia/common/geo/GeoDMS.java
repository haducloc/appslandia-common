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

import java.util.regex.Pattern;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

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

    final double decimalDegrees;

    GeoDMS(double decimalDegrees, Direction direction) {
	int degrees = (int) decimalDegrees;
	int minutes = (int) ((decimalDegrees - degrees) * 60);
	double seconds = decimalDegrees * 3600 - degrees * 3600 - minutes * 60;

	this.degrees = degrees;
	this.minutes = minutes;
	this.seconds = seconds;

	this.direction = direction;
	this.decimalDegrees = decimalDegrees;
    }

    GeoDMS(int degrees, int minutes, double seconds, Direction direction) {
	this.degrees = degrees;
	this.minutes = minutes;
	this.seconds = seconds;

	this.direction = direction;
	this.decimalDegrees = GeoUtils.toDecimalDegrees(degrees, minutes, seconds);
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

    public boolean isLatitude() {
	return this.direction.isY();
    }

    public boolean isLongitude() {
	return this.direction.isX();
    }

    public double toDecimalDegrees() {
	if ((this.direction == Direction.NORTH) || (this.direction == Direction.EAST)) {
	    return this.decimalDegrees;
	}
	return -this.decimalDegrees;
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

    static final Pattern DMS_PATTERN = Pattern.compile("\\d+°\\s*\\d{1,2}'\\s*\\d+(\\.\\d+)?\"\\s*(N|E|S|W)", Pattern.CASE_INSENSITIVE);
    static final Pattern DMS_SYMBOLS = Pattern.compile("(°|'|\")");

    public static GeoDMS toGeoDMS(String dms) {
	Arguments.notNull(dms);
	Arguments.isTrue(DMS_PATTERN.matcher(dms).matches(), () -> STR.fmt("dms {} is invalid.", dms));

	String[] items = DMS_SYMBOLS.split(dms);

	int degrees = Integer.parseInt(items[0].trim());
	int minutes = Integer.parseInt(items[1].trim());
	double seconds = Double.parseDouble(items[2].trim());

	double decimalDegrees = GeoUtils.toDecimalDegrees(degrees, minutes, seconds);
	Direction direction = Direction.parseValue(items[3].trim());

	if (direction.isY()) {
	    Asserts.isTrue(decimalDegrees <= 90.0, () -> STR.fmt("dms {} is invalid.", dms));
	} else {
	    Asserts.isTrue(decimalDegrees <= 180.0, () -> STR.fmt("dms {} is invalid.", dms));
	}
	return new GeoDMS(degrees, minutes, seconds, direction);
    }
}
