// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.utils.RandomUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class CryptoUtilsTest {

  @Test
  public void test_toCharArray() {
    var randomBytes = RandomUtils.nextBytes(32, new Random());

    var toChars = CryptoUtils.toCharArray(randomBytes);
    Assertions.assertNotNull(toChars);

    var toBytes = CryptoUtils.toByteArray(toChars);
    Assertions.assertNotNull(toBytes);

    Assertions.assertArrayEquals(randomBytes, toBytes);
  }
}
