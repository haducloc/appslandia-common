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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.ThreadSafeTester;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DsaSignerTest {

  private KeyPair keyPair;

  @BeforeEach
  public void initialize() {
    try {
      KeyPairGenerator generator = KeyPairGenerator.getInstance("DSA");
      generator.initialize(2048, new SecureRandom());
      keyPair = generator.generateKeyPair();
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test() {
    DsaSigner impl = new DsaSigner();
    impl.setAlgorithm("SHA256withDSA");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());
    try {
      byte[] data = "data".getBytes(StandardCharsets.UTF_8);
      byte[] sign = impl.digest(data);

      Assertions.assertTrue(impl.verify(data, sign));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invalid() {
    DsaSigner impl = new DsaSigner();
    impl.setAlgorithm("SHA256withDSA");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());
    try {
      byte[] data = "data".getBytes(StandardCharsets.UTF_8);
      byte[] sign = impl.digest(data);

      byte[] modifiedData = "invalid".getBytes(StandardCharsets.UTF_8);
      Assertions.assertFalse(impl.verify(modifiedData, sign));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_threadSafe() {
    final DsaSigner impl = new DsaSigner();
    impl.setAlgorithm("SHA256withDSA");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

    new ThreadSafeTester() {

      @Override
      protected Runnable newTask() {
        return new Runnable() {

          @Override
          public void run() {
            try {
              byte[] data = "data".getBytes(StandardCharsets.UTF_8);
              byte[] sign = impl.digest(data);

              Assertions.assertTrue(impl.verify(data, sign));

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
