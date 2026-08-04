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
