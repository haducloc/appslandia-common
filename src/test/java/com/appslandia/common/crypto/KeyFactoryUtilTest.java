// The MIT License (MIT)
// Copyright © 2015 Loc Ha

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
