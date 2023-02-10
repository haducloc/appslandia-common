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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DMSLocationTest {

    @Test
    public void test() {
	DMSLocation dm = new DMSLocation(40.123456, -94.123456);
	GeoLocation loc = dm.toGeoLocation();

	Assertions.assertEquals(dm.getLatitude().toDecimalDegrees(), loc.getLatitude(), 0.0000001);
	Assertions.assertEquals(dm.getLongitude().toDecimalDegrees(), loc.getLongitude(), 0.0000001);
    }

    @Test
    public void test_toDMSLocation() {
	DMSLocation loc = DMSLocation.toDMSLocation("10°20'30\"E, 11°21'31\"N");

	Assertions.assertEquals(11, loc.getLatitude().getDegrees());
	Assertions.assertEquals(21, loc.getLatitude().getMinutes());

	Assertions.assertEquals(10, loc.getLongitude().getDegrees());
	Assertions.assertEquals(20, loc.getLongitude().getMinutes());
    }
}
