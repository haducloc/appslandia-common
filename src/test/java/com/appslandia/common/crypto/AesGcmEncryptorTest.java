// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.ThreadSafeTester;

/**
 *
 * @author Loc Ha
 *
 */
public class AesGcmEncryptorTest {

  @Test
  public void test() {
    var impl = new AesGcmEncryptor();
    impl.setSecret(CryptoUtils.randomBytes(32));

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var encrypted = impl.encrypt(data);

      var decrypted = impl.decrypt(encrypted);
      Assertions.assertArrayEquals(data, decrypted);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_Poly1305() {
    var impl = new AesGcmEncryptor();
    impl.setSecret(CryptoUtils.randomBytes(32));

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var encrypted = impl.encrypt(data);

      var decrypted = impl.decrypt(encrypted);
      Assertions.assertArrayEquals(data, decrypted);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_threadSafe() {
    final var impl = new AesGcmEncryptor();
    impl.setSecret(CryptoUtils.randomBytes(32));

    new ThreadSafeTester() {

      @Override
      protected Runnable newTask() {
        return new Runnable() {

          @Override
          public void run() {
            try {
              var data = "data".getBytes(StandardCharsets.UTF_8);
              var encrypted = impl.encrypt(data);

              var decrypted = impl.decrypt(encrypted);
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
