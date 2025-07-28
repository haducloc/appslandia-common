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
public class ArrayUtilsTest {

  @Test
  public void test_appendArrays2() {
    var arr1 = new byte[] { 1, 2 };
    var arr2 = new byte[] { 3, 4, 5 };
    var result = ArrayUtils.append(arr1, arr2);

    Assertions.assertEquals(5, result.length);
    Assertions.assertEquals(1, result[0]);
    Assertions.assertEquals(3, result[2]);
  }

  @Test
  public void test_appendArrays3() {
    var arr1 = new byte[] { 1, 2 };
    var arr2 = new byte[] { 3, 4, 5 };
    var arr3 = new byte[] { 6, 7, 8, 9 };
    var result = ArrayUtils.append(arr1, arr2, arr3);

    Assertions.assertEquals(9, result.length);
    Assertions.assertEquals(1, result[0]);
    Assertions.assertEquals(3, result[2]);
    Assertions.assertEquals(6, result[5]);
  }

  @Test
  public void test_copyArrays2() {
    var arr = new byte[] { 1, 2, 3, 4, 5 };
    var arr1 = new byte[2];
    var arr2 = new byte[3];
    ArrayUtils.copy(arr, arr1, arr2);

    Assertions.assertEquals(1, arr1[0]);
    Assertions.assertEquals(2, arr1[1]);

    Assertions.assertEquals(3, arr2[0]);
    Assertions.assertEquals(4, arr2[1]);
    Assertions.assertEquals(5, arr2[2]);
  }

  @Test
  public void test_copyArrays3() {
    var arr = new byte[] { 1, 2, 3, 4, 5, 6 };
    var arr1 = new byte[1];
    var arr2 = new byte[2];
    var arr3 = new byte[3];
    ArrayUtils.copy(arr, arr1, arr2, arr3);

    Assertions.assertEquals(1, arr1[0]);

    Assertions.assertEquals(2, arr2[0]);
    Assertions.assertEquals(3, arr2[1]);

    Assertions.assertEquals(4, arr3[0]);
    Assertions.assertEquals(5, arr3[1]);
    Assertions.assertEquals(6, arr3[2]);
  }
}
