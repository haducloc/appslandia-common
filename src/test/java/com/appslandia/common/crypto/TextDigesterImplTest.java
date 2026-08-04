// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class TextDigesterImplTest {

  @Test
  public void test() {
    var impl = new TextDigesterImpl();
    impl.setDigester(new DigesterImpl("MD5"));
    try {

      var message = "data";
      var hash = impl.digest(message);

      Assertions.assertTrue(impl.verify(message, hash));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invalid() {
    var impl = new TextDigesterImpl();
    impl.setDigester(new DigesterImpl("MD5"));
    try {

      var message = "data";
      var hash = impl.digest(message);

      Assertions.assertFalse(impl.verify("invalid", hash));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
