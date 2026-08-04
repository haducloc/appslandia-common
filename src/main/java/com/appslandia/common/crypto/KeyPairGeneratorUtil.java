// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.AlgorithmParameterSpec;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class KeyPairGeneratorUtil extends InitializingObject {

  // DiffieHellman, DSA, EC, EdDSA, Ed25519, Ed448,
  // RSA, RSASSA-PSS, XDH, X25519, X448, etc.
  protected String algorithm, provider;

  protected Integer keySize;
  protected AlgorithmParameterSpec algParamSpec;

  public KeyPairGeneratorUtil() {
  }

  public KeyPairGeneratorUtil(String algorithm) {
    this.algorithm = algorithm;
  }

  public KeyPairGeneratorUtil(String algorithm, String provider) {
    this.algorithm = algorithm;
    this.provider = provider;
  }

  @Override
  protected void init() throws Exception {
    Arguments.notNull(algorithm);
  }

  protected KeyPairGenerator getImpl() throws GeneralSecurityException {
    KeyPairGenerator impl = null;
    if (provider == null) {
      impl = KeyPairGenerator.getInstance(algorithm);
    } else {
      impl = KeyPairGenerator.getInstance(algorithm, provider);
    }
    return impl;
  }

  public KeyPair generate() throws CryptoException {
    initialize();
    KeyPairGenerator impl = null;
    try {
      impl = getImpl();

      if (keySize != null) {
        impl.initialize(keySize * 8);
      } else if (algParamSpec != null) {
        impl.initialize(algParamSpec);
      }
      return impl.generateKeyPair();

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public String getAlgorithm() {
    initialize();
    return algorithm;
  }

  public KeyPairGeneratorUtil setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public KeyPairGeneratorUtil setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public KeyPairGeneratorUtil setKeySize(Integer keySize) {
    assertNotInitialized();
    this.keySize = keySize;
    return this;
  }

  public KeyPairGeneratorUtil setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
