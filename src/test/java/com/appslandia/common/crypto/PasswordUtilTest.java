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
public class PasswordUtilTest {

  @Test
  public void test() {
    var password = new String(PasswordUtil.generatePassword(8, 32));
    Assertions.assertTrue(PasswordUtil.isValid(password));
  }
}
