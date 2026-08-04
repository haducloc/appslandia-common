// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.Params;

/**
 *
 * @author Loc Ha
 *
 */
public class STRTest {

  @Test
  public void test_map() {
    try {
      var msg = STR.format("this is ${p1} and ${p2}", new Params().set("p1", "v1").set("p2", "v2"));
      Assertions.assertEquals("this is v1 and v2", msg);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_map_missing() {
    try {
      STR.format("this is ${p1} and ${p2}", new Params().set("p1", "v1"));
      Assertions.fail();

    } catch (Exception ex) {
    }
  }

  @Test
  public void test_map_null() {
    try {
      var msg = STR.format("this is ${p1} and ${p2}", new Params().set("p1", "v1").set("p2", null));
      Assertions.assertEquals("this is v1 and null", msg);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_map_prop() {
    var propName = "prop" + System.currentTimeMillis();
    try {
      System.setProperty(propName, "12345");

      var msg = STR.format("this is ${p1} and ${" + propName + "}", new Params().set("p1", "v1"));
      Assertions.assertEquals("this is v1 and 12345", msg);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());

    } finally {
      System.getProperties().remove(propName);
    }
  }

  @Test
  public void test_params() {
    try {
      var msg = STR.format("this is ${0} and ${1}", "v1", "v2");
      Assertions.assertEquals("this is v1 and v2", msg);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_params_missing() {
    try {
      STR.format("this is ${0} and ${1}", "v1");
      Assertions.fail();

    } catch (Exception ex) {
    }
  }

  @Test
  public void test_params_null() {
    try {
      var msg = STR.format("this is ${0} and ${1}", "v1", null);
      Assertions.assertEquals("this is v1 and null", msg);

    } catch (Exception ex) {
    }
  }

  @Test
  public void test_fmt() {
    try {
      var msg = STR.fmt("this is {} and {}", "v1", "v2");
      Assertions.assertEquals("this is v1 and v2", msg);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_fmt_missing() {
    try {
      STR.fmt("this is {} and {}", "v1");
      Assertions.fail();

    } catch (Exception ex) {
    }
  }

  @Test
  public void test_fmt_null() {
    try {
      var msg = STR.fmt("this is {} and {}", "v1", null);
      Assertions.assertEquals("this is v1 and null", msg);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
