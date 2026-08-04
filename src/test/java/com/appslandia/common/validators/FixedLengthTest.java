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
public class FixedLengthTest {

  @Test
  public void test() {
    var m = new TestModel();
    Set<?> errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());

    m.prop = "123";
    errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(errors.isEmpty());
  }

  @Test
  public void test_invalid() {
    var m = new TestModel();

    m.prop = "12";
    Set<?> errors = ValidatorUtils.getValidator().validate(m);
    Assertions.assertTrue(!errors.isEmpty());
  }

  private static class TestModel {

    @FixedLength(3)
    public String prop;
  }
}
