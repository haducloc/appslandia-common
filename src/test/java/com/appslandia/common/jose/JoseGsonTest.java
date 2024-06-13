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

package com.appslandia.common.jose;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.json.JsonProcessor;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JoseGsonTest {

  @Test
  public void test() {
    try {
      JsonProcessor json = JoseGson.newJsonProcessor();

      JoseMapObject user = new JoseMapObject();
      user.set("username", "user1");

      JoseMapObject address = new JoseMapObject();
      user.set("address", address);
      address.set("city", "city1");

      String jsonStr = json.toString(user);
      JoseMapObject readUser = json.read(jsonStr, JoseMapObject.class);

      Assertions.assertTrue(readUser.get("username") instanceof String);
      Assertions.assertTrue(readUser.get("address") instanceof JoseMapObject);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
