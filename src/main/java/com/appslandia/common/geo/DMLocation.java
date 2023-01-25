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

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringFormat;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DMLocation {

    final GeoDM latitude;
    final GeoDM longitude;

    public DMLocation(double latitude, double longitude) {
	AssertUtils.assertTrue((latitude >= -90.0) && (latitude <= 90.0), "latitude is invalid.");
	AssertUtils.assertTrue((longitude >= -180.0) && (longitude <= 180.0), "longitude is invalid.");

	this.latitude = GeoDM.toLatDM(latitude);
	this.longitude = GeoDM.toLongDM(longitude);
    }

    public GeoDM getLatitude() {
	return this.latitude;
    }

    public GeoDM getLongitude() {
	return this.longitude;
    }

    public GeoLocation toGeoLocation() {
	return new GeoLocation(this.latitude.toDecimalDegrees(), this.longitude.toDecimalDegrees());
    }

    public String toString(int minutesDecimals) {
	return StringFormat.fmt("{} {}", this.latitude.toString(minutesDecimals), this.longitude.toString(minutesDecimals));
    }

    @Override
    public String toString() {
	return toString(2);
    }
}
