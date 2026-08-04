// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.ThreadSafeTester;

/**
 *
 * @author Loc Ha
 *
 */
public class PasswordDigesterImplTest {

  @Test
  public void test() {
    var impl = new PasswordDigesterImpl();
    try {

      var password = "password";
      var digested = impl.digest(password);

      Assertions.assertTrue(impl.verify(password, digested));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invalid() {
    var impl = new PasswordDigesterImpl();
    try {

      var password = "password";
      var digested = impl.digest(password);

      Assertions.assertFalse(impl.verify("invalidPassword", digested));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_threadSafe() {
    var impl = new PasswordDigesterImpl();

    new ThreadSafeTester() {

      @Override
      protected Runnable newTask() {
        return new Runnable() {

          @Override
          public void run() {
            try {

              var password = "password";
              var digested = impl.digest(password);

              Assertions.assertTrue(impl.verify(password, digested));

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
