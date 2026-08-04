// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.validators;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class UsernameTest {

  @Test
  public void test() {
    var m = new TestModel();

    Set<?> errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.username = "user.name";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.username = "user.name.m";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.username = "user.name2";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.username = "user2.name";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.username = "user2.user2.user2.user2";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());
  }

  @Test
  public void test_invalid() {
    var m = new TestModel();

    Set<?> errors = null;

    // >= 6 length
    m.username = "usern";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.username = "user name";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.username = "user-name";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.username = "user.name.";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.username = ".user.name";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.username = "user_name";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.username = "1user.name";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.username = "user..name";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.username = "username1.username1.username1.username1.username1.username1.username1.username1.username1.username1.username1.username1.username1";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());
  }

  private static class TestModel {

    @Username
    public String username;
  }
}
