// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.json.JsonMap;
import com.appslandia.common.json.JsonProcessor;

/**
 *
 * @author Loc Ha
 *
 */
public class JoseJsonbTest {

  @Test
  public void test() {
    try {
      JsonProcessor json = JoseJsonb.newJsonProcessor();

      var user = new JoseMap();
      user.set("username", "user1");

      var address = new JoseMap();
      user.set("address", address);
      address.set("city", "city1");

      var jsonStr = json.toString(user);
      var readUser = json.read(jsonStr, JoseMap.class);

      Assertions.assertTrue(readUser.get("username") instanceof String);
      Assertions.assertTrue(readUser.get("address") instanceof JoseMap);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_JsonMapObject() {
    try {
      JsonProcessor json = JoseJsonb.newJsonProcessor();

      var user = new JsonMap();
      user.set("username", "user1");

      var address = new JsonMap();
      user.set("address", address);
      address.set("city", "city1");

      var jsonStr = json.toString(user);
      var readUser = json.read(jsonStr, JsonMap.class);

      Assertions.assertTrue(readUser.get("username") instanceof String);
      Assertions.assertTrue(readUser.get("address") instanceof JsonMap);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
