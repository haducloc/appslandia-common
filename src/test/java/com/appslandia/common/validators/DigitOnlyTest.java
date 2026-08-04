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
public class DigitOnlyTest {

  @Test
  public void test() {
    var m = new TestModel();
    Set<?> errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.prop = "1234";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());
  }

  @Test
  public void test_invalid() {
    var m = new TestModel();

    m.prop = "1234abc";
    Set<?> errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(!errors.isEmpty());
  }

  private static class TestModel {

    @DigitOnly
    public String prop;
  }
}
