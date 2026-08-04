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
public class TokenGeneratorTest {

  @Test
  public void test() {
    for (var i = 1; i <= 1000; i++) {

      var impl = new TokenGenerator().setLength(i);
      var str = impl.generate();
      Assertions.assertEquals(i, str.length());

      Assertions.assertTrue(impl.verify(str));
    }
  }
}
