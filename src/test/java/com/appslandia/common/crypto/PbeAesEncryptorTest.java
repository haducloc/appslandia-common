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

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.ThreadSafeTester;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeAesEncryptorTest {

  @Test
  public void test_CBC() {
    PbeAesEncryptor impl = new PbeAesEncryptor();
    impl.setTransformation("AES/CBC/PKCS5Padding");
    impl.setPbeSecretKeyGenerator(new PbeSecretKeyGenerator().setKeySize(16).setPassword("password".toCharArray()));

    try {
      byte[] data = "data".getBytes(StandardCharsets.UTF_8);
      byte[] encrypted = impl.encrypt(data);

      byte[] decrypted = impl.decrypt(encrypted);
      Assertions.assertArrayEquals(data, decrypted);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_CFB() {
    PbeAesEncryptor impl = new PbeAesEncryptor();
    impl.setTransformation("AES/CFB/PKCS5Padding");
    impl.setPbeSecretKeyGenerator(new PbeSecretKeyGenerator().setKeySize(16).setPassword("password".toCharArray()));

    try {
      byte[] data = "data".getBytes(StandardCharsets.UTF_8);
      byte[] encrypted = impl.encrypt(data);

      byte[] decrypted = impl.decrypt(encrypted);
      Assertions.assertArrayEquals(data, decrypted);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_CTR() {
    PbeAesEncryptor impl = new PbeAesEncryptor();
    impl.setTransformation("AES/CTR/NoPadding");
    impl.setPbeSecretKeyGenerator(new PbeSecretKeyGenerator().setKeySize(16).setPassword("password".toCharArray()));

    try {
      byte[] data = "data".getBytes(StandardCharsets.UTF_8);
      byte[] encrypted = impl.encrypt(data);

      byte[] decrypted = impl.decrypt(encrypted);
      Assertions.assertArrayEquals(data, decrypted);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_OFB() {
    PbeAesEncryptor impl = new PbeAesEncryptor();
    impl.setTransformation("AES/OFB/PKCS5Padding");
    impl.setPbeSecretKeyGenerator(new PbeSecretKeyGenerator().setKeySize(16).setPassword("password".toCharArray()));

    try {
      byte[] data = "data".getBytes(StandardCharsets.UTF_8);
      byte[] encrypted = impl.encrypt(data);

      byte[] decrypted = impl.decrypt(encrypted);
      Assertions.assertArrayEquals(data, decrypted);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_ECB() {
    PbeAesEncryptor impl = new PbeAesEncryptor();
    impl.setTransformation("AES/ECB/PKCS5Padding");
    impl.setPbeSecretKeyGenerator(new PbeSecretKeyGenerator().setKeySize(16).setPassword("password".toCharArray()));

    try {
      byte[] data = "data".getBytes(StandardCharsets.UTF_8);
      byte[] encrypted = impl.encrypt(data);

      byte[] decrypted = impl.decrypt(encrypted);
      Assertions.assertArrayEquals(data, decrypted);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_GCM() {
    PbeAesEncryptor impl = new PbeAesEncryptor();
    impl.setTransformation("AES/GCM/NoPadding");
    impl.setPbeSecretKeyGenerator(new PbeSecretKeyGenerator().setKeySize(16).setPassword("password".toCharArray()));

    try {
      byte[] data = "data".getBytes(StandardCharsets.UTF_8);
      byte[] encrypted = impl.encrypt(data);

      byte[] decrypted = impl.decrypt(encrypted);
      Assertions.assertArrayEquals(data, decrypted);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_threadSafe() {
    final PbeAesEncryptor impl = new PbeAesEncryptor();
    impl.setTransformation("AES/CBC/PKCS5Padding");
    impl.setPbeSecretKeyGenerator(new PbeSecretKeyGenerator().setKeySize(16).setPassword("password".toCharArray()));

    new ThreadSafeTester() {

      @Override
      protected Runnable newTask() {
        return new Runnable() {

          @Override
          public void run() {
            try {
              byte[] data = "data".getBytes(StandardCharsets.UTF_8);
              byte[] encrypted = impl.encrypt(data);

              byte[] decrypted = impl.decrypt(encrypted);
              Assertions.assertArrayEquals(data, decrypted);

            } catch (Exception ex) {
              Assertions.fail(ex.getMessage());
            } finally {
              doneTask();
            }
          }
        };
      }
    }.execute();
  }
}
