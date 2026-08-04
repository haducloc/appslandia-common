// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class KeyGeneratorUtil extends InitializingObject {

  // AES, ChaCha20, HmacXXX
  protected String algorithm, provider;
  protected Integer keySize;
  protected AlgorithmParameterSpec algParamSpec;

  public KeyGeneratorUtil() {
  }

  public KeyGeneratorUtil(String algorithm) {
    this.algorithm = algorithm;
  }

  public KeyGeneratorUtil(String algorithm, String provider) {
    this.algorithm = algorithm;
    this.provider = provider;
  }

  @Override
  protected void init() throws Exception {
    Arguments.notNull(algorithm);
  }

  protected KeyGenerator getImpl() throws GeneralSecurityException {
    KeyGenerator impl = null;
    if (provider == null) {
      impl = KeyGenerator.getInstance(algorithm);
    } else {
      impl = KeyGenerator.getInstance(algorithm, provider);
    }
    return impl;
  }

  protected void release(KeyGenerator impl) {
  }

  public SecretKey generate() throws CryptoException {
    initialize();

    KeyGenerator impl = null;
    try {
      impl = getImpl();

      if (keySize != null) {
        impl.init(keySize * 8);
      } else if (algParamSpec != null) {
        impl.init(algParamSpec);
      }
      return impl.generateKey();

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getAlgorithm() {
    initialize();
    return algorithm;
  }

  public KeyGeneratorUtil setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public KeyGeneratorUtil setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public KeyGeneratorUtil setKeySize(Integer keySize) {
    assertNotInitialized();
    this.keySize = keySize;
    return this;
  }

  public KeyGeneratorUtil setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
