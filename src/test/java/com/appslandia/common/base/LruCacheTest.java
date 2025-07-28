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
