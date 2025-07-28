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
public class SplitUtilsTest {

  @Test
  public void test_split_sep() {
    var items = SplitUtils.split("|abc|def | ghk||", '|');
    Assertions.assertTrue(items.length == 3);

    Assertions.assertEquals("abc", items[0]);
    Assertions.assertEquals("def", items[1]);
    Assertions.assertEquals("ghk", items[2]);
  }

  @Test
  public void test_splitByLine() {
    var items = SplitUtils.splitByLine("abc \r\n\t def \r\n\t\t ghk");
    Assertions.assertTrue(items.length == 3);

    Assertions.assertEquals("abc", items[0]);
    Assertions.assertEquals("def", items[1]);
    Assertions.assertEquals("ghk", items[2]);
  }

  @Test
  public void test_splitByComma() {
    var items = SplitUtils.splitByComma(",1,2,,3,");
    Assertions.assertTrue(items.length == 3);

    Assertions.assertEquals("1", items[0]);
    Assertions.assertEquals("2", items[1]);
    Assertions.assertEquals("3", items[2]);
  }

  @Test
  public void test_splitByComma_original() {
    var items = SplitUtils.splitByComma(",1,2, ,3,", SplittingBehavior.ORIGINAL);
    Assertions.assertTrue(items.length == 6);

    Assertions.assertEquals("", items[0]);
    Assertions.assertEquals("1", items[1]);
    Assertions.assertEquals("2", items[2]);
    Assertions.assertEquals(" ", items[3]);
    Assertions.assertEquals("3", items[4]);
    Assertions.assertEquals("", items[5]);
  }

  @Test
  public void test_splitByComma_trimToNull() {
    var items = SplitUtils.splitByComma(",1,2,,3,", SplittingBehavior.TRIM_TO_NULL);
    Assertions.assertTrue(items.length == 6);

    Assertions.assertNull(items[0]);
    Assertions.assertEquals("1", items[1]);
    Assertions.assertEquals("2", items[2]);
    Assertions.assertNull(items[3]);
    Assertions.assertEquals("3", items[4]);
    Assertions.assertNull(items[5]);
  }
}
