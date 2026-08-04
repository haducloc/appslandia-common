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
public class DsaSignerTest {

  private KeyPair keyPair;

  @BeforeEach
  public void initialize() {
    try {
      var generator = KeyPairGenerator.getInstance("DSA");
      generator.initialize(2048, new SecureRandom());
      keyPair = generator.generateKeyPair();
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test() {
    var impl = new DsaSigner();
    impl.setAlgorithm("SHA256withDSA");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());
    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var sign = impl.digest(data);

      Assertions.assertTrue(impl.verify(data, sign));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invalid() {
    var impl = new DsaSigner();
    impl.setAlgorithm("SHA256withDSA");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());
    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var sign = impl.digest(data);

      var modifiedData = "invalid".getBytes(StandardCharsets.UTF_8);
      Assertions.assertFalse(impl.verify(modifiedData, sign));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_threadSafe() {
    final var impl = new DsaSigner();
    impl.setAlgorithm("SHA256withDSA");
    impl.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

    new ThreadSafeTester() {

      @Override
      protected Runnable newTask() {
        return new Runnable() {

          @Override
          public void run() {
            try {
              var data = "data".getBytes(StandardCharsets.UTF_8);
              var sign = impl.digest(data);

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
