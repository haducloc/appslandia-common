// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SimpleConfigTest {

  @Test
  public void test() {
    SimpleConfig config = new SimpleConfig();
    config.set("config", "value");

    Assertions.assertEquals("value", config.getString("config"));
  }

  @Test
  public void test_empty() {
    SimpleConfig config = new SimpleConfig();
    config.set("config", "");

    Assertions.assertEquals("", config.getString("config"));
  }

  @Test
  public void test_resolve() {
    SimpleConfig config = new SimpleConfig();
    config.set("user", "user1");
    config.set("db", "db1");
    config.set("conn", "db={db}&user={user}");

    Assertions.assertEquals("db=db1&user=user1", config.resolve("conn"));
  }

  @Test
  public void test_resolve_map() {
    SimpleConfig config = new SimpleConfig();
    config.set("user", "user1");
    config.set("db", "db1");
    config.set("conn", "db={db}&user={user}");

    Assertions.assertEquals("db=db1&user=user2", config.resolve("conn", new Params().set("user", "user2")));
  }
}
