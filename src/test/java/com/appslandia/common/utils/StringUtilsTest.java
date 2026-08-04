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
public class StringUtilsTest {

  @Test
  public void test_constants() {
    Assertions.assertTrue(StringUtils.EMPTY_STRING.length() == 0);
    Assertions.assertTrue(StringUtils.EMPTY_ARRAY.length == 0);
  }
}
