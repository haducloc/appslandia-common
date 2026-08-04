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
