// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.threading;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.utils.MathUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class BlockingQueuePoolTest {

  @Test
  public void test() {
    var pool = new BlockingQueuePool<>(() -> new SimpleDateFormat("MM/dd/yyyy"), 16);

    var impl = pool.obtain();
    pool.release(impl);

    var impl2 = pool.obtain();
    Assertions.assertTrue(impl == impl2);
  }

  @Test
  public void test_reset() {
    var pool = new BlockingQueuePool<>(() -> MathUtils.toByteArray(1, 16), 16, b -> Arrays.fill(b, (byte) 0));

    var impl = pool.obtain();
    pool.release(impl);

    var impl2 = pool.obtain();
    Assertions.assertTrue(Arrays.equals(impl2, new byte[16]));
  }
}
