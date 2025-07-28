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
    var pool = new BlockingQueuePool<>(() -> MathUtils.toByteArray(1, 16), b -> Arrays.fill(b, (byte) 0), 16);

    var impl = pool.obtain();
    pool.release(impl);

    var impl2 = pool.obtain();
    Assertions.assertTrue(Arrays.equals(impl2, new byte[16]));
  }
}
