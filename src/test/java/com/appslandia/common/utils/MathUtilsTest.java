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
public class MathUtilsTest {

  @Test
  public void test_toByteArray() {
    var bytes = MathUtils.toByteArray(100);
    Assertions.assertEquals(100, MathUtils.toInt(bytes));

    bytes = MathUtils.toByteArray(1000L);
    Assertions.assertEquals(1000L, MathUtils.toLong(bytes));
  }

  @Test
  public void test_toNearestMultipleOf() {
    Assertions.assertEquals(0, MathUtils.toNearestMultipleOf(15, 0));

    for (var n = 1; n < 15; n++) {
      Assertions.assertEquals(15, MathUtils.toNearestMultipleOf(15, n));
    }
    Assertions.assertEquals(30, MathUtils.toNearestMultipleOf(15, 16));
  }
}
