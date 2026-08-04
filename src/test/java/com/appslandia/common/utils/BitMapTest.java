// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
