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

package com.appslandia.common.crypto;

import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SecureConfigTest {

  @Test
  public void test() {
    SecureConfig config = new SecureConfig("password".toCharArray());
    config.set("config", "clearValue");

    Assertions.assertEquals("clearValue", config.getString("config"));
  }

  @Test
  public void test_password() {
    SecureConfig config = new SecureConfig("password".toCharArray());
    config.sets("config", "secret");

    Assertions.assertEquals("secret", config.getString("config"));
  }

  @Test
  public void test_toProperties() {
    SecureConfig config = new SecureConfig("password".toCharArray());
    config.sets("config", "secret");

    // Properties
    Properties props = config.toClearProperties();
    Assertions.assertEquals("secret", props.getProperty("config"));
  }
}
