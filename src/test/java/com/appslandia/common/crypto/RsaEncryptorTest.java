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
 * @author Loc Ha
 *
 */
public class RsaEncryptorTest {

  private KeyPair keyPair;

  @BeforeEach
  public void initialize() {
    try {
      var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048, new SecureRandom());
      keyPair = keyPairGenerator.generateKeyPair();
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test() {
    var impl = new RsaEncryptor();
    impl.setTransformation("RSA/ECB/PKCS1Padding");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var enc = impl.encrypt(data);

      Assertions.assertArrayEquals(data, impl.decrypt(enc));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_OAEPPadding() {
    var impl = new RsaEncryptor();
    impl.setTransformation("RSA/ECB/OAEPPadding");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var enc = impl.encrypt(data);

      Assertions.assertArrayEquals(data, impl.decrypt(enc));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_OAEPPadding_MD5() {
    var impl = new RsaEncryptor();
    impl.setTransformation("RSA/ECB/OAEPWithMD5AndMGF1Padding");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var enc = impl.encrypt(data);

      Assertions.assertArrayEquals(data, impl.decrypt(enc));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_OAEPWithSHA1AndMGF1Padding() {
    var impl = new RsaEncryptor();
    impl.setTransformation("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var enc = impl.encrypt(data);

      Assertions.assertArrayEquals(data, impl.decrypt(enc));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_OAEPWithSHA224AndMGF1Padding() {
    var impl = new RsaEncryptor();
    impl.setTransformation("RSA/ECB/OAEPWithSHA-224AndMGF1Padding");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var enc = impl.encrypt(data);

      Assertions.assertArrayEquals(data, impl.decrypt(enc));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_OAEPWithSHA256AndMGF1Padding() {
    var impl = new RsaEncryptor();
    impl.setTransformation("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var enc = impl.encrypt(data);

      Assertions.assertArrayEquals(data, impl.decrypt(enc));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_OAEPWithSHA384AndMGF1Padding() {
    var impl = new RsaEncryptor();
    impl.setTransformation("RSA/ECB/OAEPWithSHA-384AndMGF1Padding");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var enc = impl.encrypt(data);

      Assertions.assertArrayEquals(data, impl.decrypt(enc));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_OAEPWithSHA512AndMGF1Padding() {
    var impl = new RsaEncryptor();
    impl.setTransformation("RSA/ECB/OAEPWithSHA-512AndMGF1Padding");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var enc = impl.encrypt(data);

      Assertions.assertArrayEquals(data, impl.decrypt(enc));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_threadSafe() {
    final var impl = new RsaEncryptor();
    impl.setTransformation("RSA/ECB/PKCS1Padding");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

    new ThreadSafeTester() {

      @Override
      protected Runnable newTask() {
        return new Runnable() {

          @Override
          public void run() {
            try {
              var data = "data".getBytes(StandardCharsets.UTF_8);
              var enc = impl.encrypt(data);

              Assertions.assertArrayEquals(data, impl.decrypt(enc));

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
