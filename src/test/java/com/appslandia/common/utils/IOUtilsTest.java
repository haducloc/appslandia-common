// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class IOUtilsTest {

  @Test
  public void test_copyFromIsToOs() {
    var src = RandomUtils.nextBytes(100, new Random());
    var is = new ByteArrayInputStream(src);
    var os = new ByteArrayOutputStream();
    try {
      IOUtils.copy(is, os);
      Assertions.assertArrayEquals(src, os.toByteArray());
    } catch (IOException ex) {
    }
  }

  @Test
  public void test_toByteArray() {
    var src = RandomUtils.nextBytes(100, new Random());
    var is = new ByteArrayInputStream(src);
    try {
      var ba = IOUtils.toByteArray(is);
      Assertions.assertArrayEquals(src, ba);
    } catch (IOException ex) {
    }
  }

  @Test
  public void test_copyWithLength() {
    var src = MathUtils.toByteArray(1, 100);
    var os = new ByteArrayOutputStream();
    try {
      IOUtils.copy(src, 50, os);
      Assertions.assertArrayEquals(MathUtils.toByteArray(1, 50), os.toByteArray());
    } catch (IOException ex) {
    }
  }
}
