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
public class LruCacheTest {

  @Test
  public void test() {
    var cache = new LruCache<String, Integer>(5);

    cache.put("k1", 1);
    cache.put("k2", 2);
    cache.put("k3", 3);
    cache.put("k4", 4);
    cache.put("k5", 5);

    Assertions.assertEquals(Integer.valueOf(1), cache.get("k1"));
    Assertions.assertEquals(Integer.valueOf(2), cache.get("k2"));
    Assertions.assertEquals(Integer.valueOf(3), cache.get("k3"));
    Assertions.assertEquals(Integer.valueOf(4), cache.get("k4"));
    Assertions.assertEquals(Integer.valueOf(5), cache.get("k5"));

    cache.put("k6", 6);
    Assertions.assertEquals(Integer.valueOf(6), cache.get("k6"));
    Assertions.assertNull(cache.get("k1"));
    Assertions.assertNotNull(cache.get("k2"));
  }
}
