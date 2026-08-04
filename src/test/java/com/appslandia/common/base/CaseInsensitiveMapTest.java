// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class CaseInsensitiveMapTest {

  @Test
  public void test() {
    var m = new CaseInsensitiveMap<String>(new HashMap<>());
    m.put("k1", "v1");

    Assertions.assertTrue(m.containsKey("K1"));
    Assertions.assertTrue(m.containsKey("k1"));

    Assertions.assertEquals("v1", m.get("K1"));
    Assertions.assertEquals("v1", m.get("k1"));

    m.put("K2", "v1");

    Assertions.assertTrue(m.containsKey("k2"));
    Assertions.assertTrue(m.containsKey("K2"));

    Assertions.assertEquals("v1", m.get("k2"));
    Assertions.assertEquals("v1", m.get("K2"));
  }
}
