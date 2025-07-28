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

package com.appslandia.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class SYSTest {

  @Test
  public void test_resolve() {
    var propName = "db.password." + System.currentTimeMillis();

    try {
      System.setProperty(propName, "12345");
      var resolvedValue = SYS.resolve("{" + propName + "}");

      Assertions.assertNotNull(resolvedValue);
      Assertions.assertEquals("12345", resolvedValue);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());

    } finally {
      System.getProperties().remove(propName);
    }
  }

  @Test
  public void test_resolve_failed() {
    var propName = "db.password." + System.currentTimeMillis();

    try {
      var resolvedValue = SYS.resolve("{" + propName + "}");
      Assertions.assertNull(resolvedValue);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_resolve_env() {
    if (System.getenv("TMP") == null) {
      return;
    }
    try {
      var resolvedValue = SYS.resolve("{env.TMP}");
      Assertions.assertNotNull(resolvedValue);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_resolve_env_failed() {
    var varName = "env.TMP." + System.currentTimeMillis();

    try {
      var resolvedValue = SYS.resolve("{" + varName + "}");
      Assertions.assertNull(resolvedValue);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @SuppressWarnings("el-syntax")
  @Test
  public void test_resolve_vars() {
    if (System.getenv("TMP") == null) {
      return;
    }
    var varName = "temp.dir." + System.currentTimeMillis();

    try {
      var resolvedValue = SYS.resolve("{" + varName + ",env.TMP}");
      Assertions.assertNotNull(resolvedValue);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
