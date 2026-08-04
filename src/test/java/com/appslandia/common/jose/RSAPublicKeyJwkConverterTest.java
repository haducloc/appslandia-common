// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class RSAPublicKeyJwkConverterTest {

  private static KeyPair generateRsaKeyPair() throws Exception {
    var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048, new SecureRandom());
    return keyPairGenerator.generateKeyPair();
  }

  @Test
  public void test() {
    var converter = new RSAPublicKeyJwkConverter();

    try {
      var keyPair = generateRsaKeyPair();
      var key = converter.toJsonWebKey((RSAPublicKey) keyPair.getPublic());

      Assertions.assertNotNull(key);
      Assertions.assertEquals("RSA", key.getString("kty"));

      Assertions.assertNotNull(key.getString("n"));
      Assertions.assertNotNull(key.getString("e"));

      var decoded = converter.fromJsonWebKey(key);
      Assertions.assertNotNull(decoded);

      Assertions.assertEquals(keyPair.getPublic(), decoded);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
