// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
      var resolvedValue = SYS.resolve("${" + propName + "}");

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
      var resolvedValue = SYS.resolve("${" + propName + "}");
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
      var resolvedValue = SYS.resolve("${env.TMP}");
      Assertions.assertNotNull(resolvedValue);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_resolve_env_failed() {
    var varName = "env.TMP." + System.currentTimeMillis();

    try {
      var resolvedValue = SYS.resolve("${" + varName + "}");
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
      var resolvedValue = SYS.resolve("${" + varName + ",env.TMP}");
      Assertions.assertNotNull(resolvedValue);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
