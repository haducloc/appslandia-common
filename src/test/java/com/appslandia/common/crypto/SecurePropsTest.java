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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SecurePropsTest {

  @Test
  public void test() {
    Encryptor encryptor = new PbeEncryptor().setTransformation("AES/CBC/PKCS5Padding").setKeySize(16)
        .setPassword("password".toCharArray());

    TextEncryptor textEncryptor = new TextEncryptor().setEncryptor(encryptor);
    SecureProps props = new SecureProps(textEncryptor);

    try {
      props.enc("config", "value");
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      Assertions.assertEquals("value", props.getString("config"));
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_password() {
    SecureProps props = new SecureProps("password".toCharArray());
    try {
      props.enc("config", "value");
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      Assertions.assertEquals("value", props.getString("config"));
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_resolve() {
    SecureProps props = new SecureProps("password".toCharArray());
    try {
      props.set("database", "db");
      props.set("user", "user");
      props.enc("password", "pwd");
      props.set("url", "database=${database};user=${user};password=${password}");

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      Assertions.assertEquals("database=db;user=user;password=pwd", props.resolve("url"));
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
