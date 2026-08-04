// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class KeyFactoryUtilTest {

  final KeyFactoryUtil keyFactoryUtil = new KeyFactoryUtil("DSA");

  @Test
  public void test() {
    try {
      var generator = KeyPairGenerator.getInstance("DSA");
      generator.initialize(1024, new SecureRandom());
      var keyPair = generator.generateKeyPair();

      var privateKeyPem = PKIUtils.toPemEncoded(keyPair.getPrivate());
      var publicKeyPem = PKIUtils.toPemEncoded(keyPair.getPublic());

      var privateKey = keyFactoryUtil.toPrivateKey(privateKeyPem);
      var publicKey = keyFactoryUtil.toPublicKey(publicKeyPem);

      Assertions.assertTrue(keyPair.getPrivate().equals(privateKey));
      Assertions.assertTrue(keyPair.getPublic().equals(publicKey));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_clone() {
    try {
      var generator = KeyPairGenerator.getInstance("DSA");
      generator.initialize(1024, new SecureRandom());
      var keyPair = generator.generateKeyPair();

      var privateKey = keyFactoryUtil.copy(keyPair.getPrivate());
      var publicKey = keyFactoryUtil.copy(keyPair.getPublic());

      Assertions.assertTrue(keyPair.getPrivate().equals(privateKey));
      Assertions.assertTrue(keyPair.getPublic().equals(publicKey));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
