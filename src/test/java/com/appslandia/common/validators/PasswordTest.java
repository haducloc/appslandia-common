// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.validators;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.crypto.PasswordUtil;

/**
 *
 * @author Loc Ha
 *
 */
public class PasswordTest {

  @Test
  public void test() {
    var m = new TestModel();

    Set<?> errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    for (var i = 1; i < 1000; i++) {
      m.password = new String(PasswordUtil.generatePassword(8, 32));

      errors = ValidatorUtils.getValidator().validate(m);
      Assertions.assertTrue(errors.isEmpty());
    }
  }

  @Test
  public void test_invalid() {
    var m = new TestModel();

    Set<?> errors = null;

    m.password = "password";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.password = "passW!";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.password = "passw123";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.password = "passw12#";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.password = "pasSw123";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());
  }

  private static class TestModel {

    @Password
    public String password;
  }
}
