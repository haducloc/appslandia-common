// The MIT License (MIT)
// Copyright © 2015 Loc Ha

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

package com.appslandia.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class ValueUtilsTest {

  @Test
  public void test_valueOrMin() {

    var val = ValueUtils.valueOrMin((Integer) null, 10);
    Assertions.assertEquals(10, val);

    val = ValueUtils.valueOrMin(15, 10);
    Assertions.assertEquals(15, val);
  }

  @Test
  public void test_valueOrMax() {
    var val = ValueUtils.valueOrMax((Integer) null, 10);
    Assertions.assertEquals(10, val);

    val = ValueUtils.valueOrMax(15, 10);
    Assertions.assertEquals(10, val);
  }

  @Test
  public void test_inRange() {
    var val = ValueUtils.inRange((Integer) null, 5, 10);
    Assertions.assertEquals(5, val);

    val = ValueUtils.inRange(1, 5, 10);
    Assertions.assertEquals(5, val);

    val = ValueUtils.inRange(15, 5, 10);
    Assertions.assertEquals(10, val);
  }

  @Test
  public void test_valueOrNull() {
    var val = ValueUtils.valueOrNull((Integer) null, new int[] { 1, 2, 3 });
    Assertions.assertNull(val);

    val = ValueUtils.valueOrNull(4, new int[] { 1, 2, 3 });
    Assertions.assertNull(val);

    val = ValueUtils.valueOrNull(3, new int[] { 1, 2, 3 });
    Assertions.assertNotNull(val);
    Assertions.assertEquals(3, val.intValue());
  }
}
