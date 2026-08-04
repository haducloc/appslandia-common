// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
    keyFactoryUtil = new KeyFactoryUtil("RSA", rsaKeyFactoryProvider);
  }

  @Override
  public JsonWebKey toJsonWebKey(RSAPublicKey key) {
    initialize();
    Arguments.isTrue("RSA".equals(key.getAlgorithm()));

    // JsonWebKey
    var jwk = new JsonWebKey();
    jwk.setKty(kty);

    var nBytes = CryptoUtils.stripLeadingZeros(key.getModulus().toByteArray());
    var eBytes = CryptoUtils.stripLeadingZeros(key.getPublicExponent().toByteArray());

    jwk.put("n", JoseUtils.getJoseBase64().encode(nBytes));
    jwk.put("e", JoseUtils.getJoseBase64().encode(eBytes));

    return jwk;
  }

  @Override
  public RSAPublicKey fromJsonWebKey(JsonWebKey jwk) throws CryptoException {
    initialize();
    Arguments.isTrue(kty.equals(kty), "kty doesn't match.");

    var n = Arguments.notNull((String) jwk.get("n"), "n is required.");
    var e = Arguments.notNull((String) jwk.get("e"), "e is required.");

    var nBytes = JoseUtils.getJoseBase64().decode(n);
    var eBytes = JoseUtils.getJoseBase64().decode(e);

    var keySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes), new BigInteger(1, eBytes));
    var publicKey = keyFactoryUtil.toPublicKey(keySpec);
    return (RSAPublicKey) publicKey;
  }

  public RSAPublicKeyJwkConverter setRsaKeyFactoryProvider(String rsaKeyFactoryProvider) {
    assertNotInitialized();
    this.rsaKeyFactoryProvider = rsaKeyFactoryProvider;
    return this;
  }
}
