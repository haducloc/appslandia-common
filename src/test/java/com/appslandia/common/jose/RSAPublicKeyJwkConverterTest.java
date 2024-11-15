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
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RSAPublicKeyJwkConverterTest {

  private static KeyPair generateRsaKeyPair() throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048, new SecureRandom());
    return keyPairGenerator.generateKeyPair();
  }

  @Test
  public void test() {
    RSAPublicKeyJwkConverter converter = new RSAPublicKeyJwkConverter();

    try {
      KeyPair keyPair = generateRsaKeyPair();
      JsonWebKey key = converter.toJsonWebKey((RSAPublicKey) keyPair.getPublic());

      Assertions.assertNotNull(key);
      Assertions.assertEquals("RSA", key.getString("kty"));

      Assertions.assertNotNull(key.getString("n"));
      Assertions.assertNotNull(key.getString("e"));

      RSAPublicKey decoded = converter.fromJsonWebKey(key);
      Assertions.assertNotNull(decoded);

      Assertions.assertEquals(keyPair.getPublic(), decoded);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
