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
public class BitMapTest {

  @Test
  public void test() {

    var bits = new BitMap();
    bits.on(1, 3, 5);
    bits.off(0, 2, 4);

    Assertions.assertTrue(bits.length() == 6);
    Assertions.assertTrue(bits.cardinality() == 3);

    Assertions.assertTrue(bits.get(1));
    Assertions.assertTrue(bits.get(3));
    Assertions.assertTrue(bits.get(5));

    Assertions.assertFalse(bits.get(0));
    Assertions.assertFalse(bits.get(2));
    Assertions.assertFalse(bits.get(4));
  }

  @Test
  public void test_toggle() {

    var bits = new BitMap();
    bits.on(1, 3, 5);

    bits.toggle(1, 3, 5);

    Assertions.assertFalse(bits.get(1));
    Assertions.assertFalse(bits.get(3));
    Assertions.assertFalse(bits.get(5));
  }

  @Test
  public void test_ctor() {

    var bits = new BitMap();
    bits.on(1, 3, 5);

    var copy = new BitMap(bits);
    Assertions.assertEquals(bits, copy);
  }

  @Test
  public void test_clone() {

    var bits = new BitMap();
    bits.on(1, 3, 5);

    var copy = bits.clone();
    Assertions.assertEquals(bits, copy);
  }
}
