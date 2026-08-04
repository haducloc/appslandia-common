// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
