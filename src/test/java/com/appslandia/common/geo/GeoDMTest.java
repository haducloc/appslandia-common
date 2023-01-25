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
public class GeoDMTest {

    @Test
    public void test_toLatDM() {

	GeoDM dm = GeoDM.toLatDM(GeoUtils.toDecimalDegrees(10, 20.15));

	Assertions.assertEquals(10, dm.getDegrees());
	Assertions.assertEquals(20.15, dm.getMinutes(), 0.001d);

	Assertions.assertEquals(Direction.NORTH, dm.getDirection());
    }

    @Test
    public void test_toLatDM_Zero() {

	GeoDM dm = GeoDM.toLatDM(0.0);

	Assertions.assertEquals(0, dm.getDegrees());
	Assertions.assertEquals(0.0, dm.getMinutes(), 0.01d);

	Assertions.assertEquals(Direction.NORTH, dm.getDirection());
    }

    @Test
    public void test_toLongDM() {

	GeoDM dm = GeoDM.toLongDM(GeoUtils.toDecimalDegrees(10, 20.15));

	Assertions.assertEquals(10, dm.getDegrees());
	Assertions.assertEquals(20.15, dm.getMinutes(), 0.001d);

	Assertions.assertEquals(Direction.EAST, dm.getDirection());
    }

    @Test
    public void test_toLongDM_Zero() {

	GeoDM dm = GeoDM.toLongDM(-0.0);

	Assertions.assertEquals(0, dm.getDegrees());
	Assertions.assertEquals(0.0, dm.getMinutes(), 0.01d);

	// Direction.WEST because -0.0
	Assertions.assertEquals(Direction.WEST, dm.getDirection());
    }
}
