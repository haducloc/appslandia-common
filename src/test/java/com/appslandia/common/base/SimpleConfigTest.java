// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class SimpleConfigTest {

  final String NON_EXISTENT_KEY = "key-" + System.currentTimeMillis();

  @Test
  public void test_empty() {
    var config = new SimpleConfig();
    config.set("key1", "");

    Assertions.assertEquals("", config.getString("key1"));
  }

  @Test
  public void test_getString() {
    var config = new SimpleConfig();
    config.set("key1", "value1");
    Assertions.assertEquals("value1", config.getString("key1"));
    Assertions.assertEquals("default", config.getString(NON_EXISTENT_KEY, "default"));
  }

  @Test
  public void test_getStringReq() {
    var config = new SimpleConfig();
    config.set("key1", "value1");
    Assertions.assertEquals("value1", config.getStringReq("key1"));
    Assertions.assertThrows(IllegalStateException.class, () -> config.getStringReq(NON_EXISTENT_KEY));
  }

  @Test
  public void test_getStringArray() {
    var config = new SimpleConfig();
    config.set("key1", "value1,value2,value3");
    Assertions.assertArrayEquals(new String[] { "value1", "value2", "value3" }, config.getStringArray("key1"));
    Assertions.assertArrayEquals(new String[] {}, config.getStringArray(NON_EXISTENT_KEY));
  }

  @Test
  public void test_getBool() {
    var config = new SimpleConfig();
    config.set("key1", "true");
    Assertions.assertTrue(config.getBool("key1"));
    Assertions.assertFalse(config.getBool(NON_EXISTENT_KEY, false));
  }

  @Test
  public void test_getBoolReq() {
    var config = new SimpleConfig();
    config.set("key1", "true");
    Assertions.assertTrue(config.getBool("key1"));
    Assertions.assertThrows(IllegalStateException.class, () -> config.getBool(NON_EXISTENT_KEY));
  }

  @Test
  public void test_getInt() {
    var config = new SimpleConfig();
    config.set("key1", "42");
    Assertions.assertEquals(42, config.getInt("key1"));
    Assertions.assertEquals(-1, config.getInt(NON_EXISTENT_KEY, -1));
  }

  @Test
  public void test_getIntReq() {
    var config = new SimpleConfig();
    config.set("key1", "42");
    Assertions.assertEquals(42, config.getInt("key1"));
    Assertions.assertThrows(IllegalStateException.class, () -> config.getInt(NON_EXISTENT_KEY));
  }

  @Test
  public void test_getLong() {
    var config = new SimpleConfig();
    config.set("key1", "123456789012345");
    Assertions.assertEquals(123456789012345L, config.getLong("key1"));
    Assertions.assertEquals(-1L, config.getLong(NON_EXISTENT_KEY, -1L));
  }

  @Test
  public void test_getLongReq() {
    var config = new SimpleConfig();
    config.set("key1", "123456789012345");
    Assertions.assertEquals(123456789012345L, config.getLong("key1"));
    Assertions.assertThrows(IllegalStateException.class, () -> config.getLong(NON_EXISTENT_KEY));
  }

  @Test
  public void test_getDouble() {
    var config = new SimpleConfig();
    config.set("key1", "3.14");
    Assertions.assertEquals(3.14, config.getDouble("key1"));
    Assertions.assertEquals(-1.0, config.getDouble(NON_EXISTENT_KEY, -1.0));
  }

  @Test
  public void test_getDoubleReq() {
    var config = new SimpleConfig();
    config.set("key1", "3.14");
    Assertions.assertEquals(3.14, config.getDouble("key1"));
    Assertions.assertThrows(IllegalStateException.class, () -> config.getDouble(NON_EXISTENT_KEY));
  }

  @Test
  public void test_resolve() {
    var config = new SimpleConfig();
    config.set("user", "user1");
    config.set("db", "db1");
    config.set("conn", "db=${db}&user=${user}");

    Assertions.assertEquals("db=db1&user=user1", config.resolve("conn"));
  }

  @Test
  public void test_resolve_params() {
    var config = new SimpleConfig();
    config.set("user", "user1");
    config.set("db", "db1");
    config.set("conn", "db=${db}&user=${user}");

    Assertions.assertEquals("db=db1&user=user2", config.resolve("conn", new Params().set("user", "user2")));
  }

  @Test
  public void test_resolve_env() {
    var propName = "db.password";

    try {
      System.setProperty(propName, "pass1");

      var config = new SimpleConfig();
      config.set("user", "user1");
      config.set("db", "db1");
      config.set("conn", "db=${db}&user=${user}&password=${db.password}");

      Assertions.assertEquals("db=db1&user=user1&password=pass1", config.resolve("conn"));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());

    } finally {
      System.getProperties().remove(propName);
    }
  }
}
