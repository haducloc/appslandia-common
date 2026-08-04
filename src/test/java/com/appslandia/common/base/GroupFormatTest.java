// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class GroupFormatTest {

  @Test
  public void test() {
    try {
      var format = new GroupFormat("({3}) {3}-{4}");
      var result = format.format("4024130224");

      Assertions.assertEquals(10, format.getInputLength());
      Assertions.assertEquals("(402) 413-0224", result);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_spaces() {
    try {
      var format = new GroupFormat("({ 3 }) {  3}-{4}");
      var result = format.format("4024130224");

      Assertions.assertEquals(10, format.getInputLength());
      Assertions.assertEquals("(402) 413-0224", result);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_noGroup() {
    try {
      var format = new GroupFormat("ABC-123");
      Assertions.assertEquals(0, format.getInputLength());

      var result = format.format("XYZ");
      Assertions.assertEquals("XYZ", result);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_skippedFormat() {
    try {
      var format = new GroupFormat("({3}) {3}-{4}");
      var result = format.format("402413022");
      Assertions.assertEquals("402413022", result);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_validate() {
    try {
      var format = new GroupFormat("({3}) {3}-{4}", true);
      format.format("402413022");
      Assertions.fail();

    } catch (Exception ex) {
    }
  }
}
