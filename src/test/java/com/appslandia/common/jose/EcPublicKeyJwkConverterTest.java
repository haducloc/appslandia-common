// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.jose;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class EcPublicKeyJwkConverterTest {

  private static KeyPair generateECKeyPair() throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
    ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256r1");
    keyPairGenerator.initialize(ecGenParameterSpec);
    return keyPairGenerator.generateKeyPair();
  }

  @Test
  public void test() {
    EcPublicKeyJwkConverter converter = new EcPublicKeyJwkConverter();

    try {
      KeyPair keyPair = generateECKeyPair();
      JsonWebKey key = converter.toJsonWebKey((ECPublicKey) keyPair.getPublic());
      Assertions.assertNotNull(key);
      Assertions.assertEquals("EC", key.getString("kty"));
      Assertions.assertEquals("P-256", key.getString("crv"));

      Assertions.assertNotNull(key.getString("x"));
      Assertions.assertNotNull(key.getString("y"));

      ECPublicKey decoded = converter.fromJsonWebKey(key);
      Assertions.assertNotNull(decoded);

      Assertions.assertEquals(keyPair.getPublic(), decoded);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
