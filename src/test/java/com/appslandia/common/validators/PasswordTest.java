// The MIT License (MIT)
// Copyright © 2015 Loc Ha

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

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
