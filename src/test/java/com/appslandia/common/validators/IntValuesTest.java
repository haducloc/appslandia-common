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
public class IntValuesTest {

  @Test
  public void test() {
    var m = new TestModel();
    Set<?> errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.gender = 1;
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.gender = 2;
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());
  }

  @Test
  public void test_invalid() {
    var m = new TestModel();

    m.gender = 3;
    Set<?> errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(!errors.isEmpty());
  }

  private static class TestModel {

    @IntValues({ 1, 2 })
    public Integer gender;
  }
}
