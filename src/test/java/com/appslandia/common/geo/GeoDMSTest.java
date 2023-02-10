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
public class GeoDMSTest {

    @Test
    public void test_toLatDMS() {

	GeoDMS dms = GeoDMS.toLatDMS(GeoUtils.toDecimalDegrees(10, 20, 30.05));

	Assertions.assertEquals(10, dms.getDegrees());
	Assertions.assertEquals(20, dms.getMinutes());
	Assertions.assertEquals(30.05, dms.getSeconds(), 0.001d);

	Assertions.assertEquals(Direction.NORTH, dms.getDirection());
    }

    @Test
    public void test_toLatDMS_Zero() {

	GeoDMS dms = GeoDMS.toLatDMS(0.0);

	Assertions.assertEquals(0, dms.getDegrees());
	Assertions.assertEquals(0, dms.getMinutes());
	Assertions.assertEquals(0.0, dms.getSeconds(), 0.01d);

	Assertions.assertEquals(Direction.NORTH, dms.getDirection());
    }

    @Test
    public void test_toLongDMS() {

	GeoDMS dms = GeoDMS.toLongDMS(GeoUtils.toDecimalDegrees(10, 20, 30.05));

	Assertions.assertEquals(10, dms.getDegrees());
	Assertions.assertEquals(20, dms.getMinutes());
	Assertions.assertEquals(30.05, dms.getSeconds(), 0.001d);

	Assertions.assertEquals(Direction.EAST, dms.getDirection());
    }

    @Test
    public void test_toLongDMS_Zero() {

	GeoDMS dms = GeoDMS.toLongDMS(-0.0);

	Assertions.assertEquals(0, dms.getDegrees());
	Assertions.assertEquals(0, dms.getMinutes());
	Assertions.assertEquals(0.0, dms.getSeconds(), 0.01d);

	// Direction.WEST because -0.0
	Assertions.assertEquals(Direction.WEST, dms.getDirection());
    }

    @Test
    public void test_toGeoDMS_lat() {

	GeoDMS dms = GeoDMS.toGeoDMS("10°20'30.05\"N");

	Assertions.assertEquals(10, dms.getDegrees());
	Assertions.assertEquals(20, dms.getMinutes());
	Assertions.assertEquals(30.05, dms.getSeconds(), 0.001d);

	Assertions.assertEquals(Direction.NORTH, dms.getDirection());
    }

    @Test
    public void test_toGeoDMS_long() {

	GeoDMS dms = GeoDMS.toGeoDMS("10°20'30.05\"E");

	Assertions.assertEquals(10, dms.getDegrees());
	Assertions.assertEquals(20, dms.getMinutes());
	Assertions.assertEquals(30.05, dms.getSeconds(), 0.001d);

	Assertions.assertEquals(Direction.EAST, dms.getDirection());
    }
}
