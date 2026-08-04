// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class EcPublicKeyJwkConverterTest {

  private static KeyPair generateECKeyPair() throws Exception {
    var keyPairGenerator = KeyPairGenerator.getInstance("EC");
    var ecGenParameterSpec = new ECGenParameterSpec("secp256r1");
    keyPairGenerator.initialize(ecGenParameterSpec);
    return keyPairGenerator.generateKeyPair();
  }

  @Test
  public void test() {
    var converter = new EcPublicKeyJwkConverter();

    try {
      var keyPair = generateECKeyPair();
      var key = converter.toJsonWebKey((ECPublicKey) keyPair.getPublic());
      Assertions.assertNotNull(key);
      Assertions.assertEquals("EC", key.getString("kty"));
      Assertions.assertEquals("P-256", key.getString("crv"));

      Assertions.assertNotNull(key.getString("x"));
      Assertions.assertNotNull(key.getString("y"));

      var decoded = converter.fromJsonWebKey(key);
      Assertions.assertNotNull(decoded);

      Assertions.assertEquals(keyPair.getPublic(), decoded);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
