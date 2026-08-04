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
public class EmailTest {

  @Test
  public void test() {
    var m = new TestModel();

    Set<?> errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.email = "perSon@yahoo.com";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.email = "Person100@yahoo.com";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.email = "person-100@yahoo.com";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.email = "person+100@yahoo.COM";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.email = "person.100@yahoo.Com";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.email = "person.100@yahoo.com.VN";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.email = "person.100@yahoo-abc.com";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());
  }

  @Test
  public void test_invalid() {
    var m = new TestModel();

    Set<?> errors = null;

    m.email = "perSon@.yahoo.com";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.email = "Person100@yahoo.c";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.email = ".person-100@.com";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.email = "person*100@yahoo.COM";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.email = "person..100@yahoo.Com";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.email = "person@.100@yahoo.com";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());

    m.email = "person.100@yahoo.1c";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertFalse(errors.isEmpty());
  }

  private static class TestModel {

    @Email
    public String email;
  }
}
