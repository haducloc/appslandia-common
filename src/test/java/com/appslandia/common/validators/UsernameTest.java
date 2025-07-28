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
