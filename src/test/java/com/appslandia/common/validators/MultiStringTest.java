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
public class MultiStringTest {

  @Test
  public void test() {
    var m = new TestModel();
    Set<?> errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.roles = "admin, user";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());
  }

  @Test
  public void test_invalid() {
    var m = new TestModel();
    m.roles = "admin, operator";

    Set<?> errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(!errors.isEmpty());
  }

  private static class TestModel {

    @MultiString({ "admin", "manager", "user" })
    public String roles;
  }
}
