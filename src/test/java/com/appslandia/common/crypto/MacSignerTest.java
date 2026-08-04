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
public class MacSignerTest {

  @Test
  public void test() {
    var impl = new MacSigner().setAlgorithm("HmacMD5");
    impl.setSecret("secret".getBytes(StandardCharsets.UTF_8));

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var digest = impl.digest(data);

      Assertions.assertTrue(impl.verify(data, digest));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invalid() {
    var impl = new MacSigner().setAlgorithm("HmacMD5");
    impl.setSecret("secret".getBytes(StandardCharsets.UTF_8));

    try {
      var data = "data".getBytes(StandardCharsets.UTF_8);
      var digest = impl.digest(data);

      Assertions.assertFalse(impl.verify("invalid".getBytes(StandardCharsets.UTF_8), digest));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_threadSafe() {
    final var impl = new MacSigner().setAlgorithm("HmacMD5");
    impl.setSecret("secret".getBytes(StandardCharsets.UTF_8));

    new ThreadSafeTester() {

      @Override
      protected Runnable newTask() {
        return new Runnable() {

          @Override
          public void run() {
            try {
              var data = "data".getBytes(StandardCharsets.UTF_8);
              var digest = impl.digest(data);

              Assertions.assertTrue(impl.verify(data, digest));

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
