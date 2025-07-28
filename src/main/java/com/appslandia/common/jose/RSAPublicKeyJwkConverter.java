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

package com.appslandia.common.jose;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.crypto.CryptoUtils;
import com.appslandia.common.crypto.KeyFactoryUtil;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class RSAPublicKeyJwkConverter extends JwkConverter<RSAPublicKey> {

  private String rsaKeyFactoryProvider;
  private KeyFactoryUtil keyFactoryUtil;

  public RSAPublicKeyJwkConverter() {
    super("RSA");
  }

  @Override
  protected void init() throws Exception {
    this.keyFactoryUtil = new KeyFactoryUtil("RSA", this.rsaKeyFactoryProvider);
  }

  @Override
  public JsonWebKey toJsonWebKey(RSAPublicKey key) {
    this.initialize();
    Arguments.isTrue("RSA".equals(key.getAlgorithm()));

    // JsonWebKey
    var jwk = new JsonWebKey();
    jwk.setKty(this.kty);

    var nBytes = CryptoUtils.stripLeadingZeros(key.getModulus().toByteArray());
    var eBytes = CryptoUtils.stripLeadingZeros(key.getPublicExponent().toByteArray());

    jwk.put("n", JoseUtils.getJoseBase64().encode(nBytes));
    jwk.put("e", JoseUtils.getJoseBase64().encode(eBytes));

    return jwk;
  }

  @Override
  public RSAPublicKey fromJsonWebKey(JsonWebKey jwk) throws CryptoException {
    this.initialize();
    Arguments.isTrue(this.kty.equals(kty), "kty doesn't match.");

    var n = Arguments.notNull((String) jwk.get("n"), "n is required.");
    var e = Arguments.notNull((String) jwk.get("e"), "e is required.");

    var nBytes = JoseUtils.getJoseBase64().decode(n);
    var eBytes = JoseUtils.getJoseBase64().decode(e);

    var keySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes), new BigInteger(1, eBytes));
    var publicKey = this.keyFactoryUtil.toPublicKey(keySpec);
    return (RSAPublicKey) publicKey;
  }

  public RSAPublicKeyJwkConverter setRsaKeyFactoryProvider(String rsaKeyFactoryProvider) {
    assertNotInitialized();
    this.rsaKeyFactoryProvider = rsaKeyFactoryProvider;
    return this;
  }
}
