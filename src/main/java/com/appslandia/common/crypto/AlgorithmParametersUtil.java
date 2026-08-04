// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class AlgorithmParametersUtil extends InitializingObject {

  // AES, ChaCha20-Poly1305, DSA, EC, GCM, OAEP
  // PBEWith<prf>And<encryption>
  // PBEWith<digest>And<encryption>
  protected String algorithm, provider;
  protected AlgorithmParameterSpec algParamSpec;

  public AlgorithmParametersUtil() {
  }

  public AlgorithmParametersUtil(String algorithm) {
    this.algorithm = algorithm;
  }

  public AlgorithmParametersUtil(String algorithm, String provider) {
    this.algorithm = algorithm;
    this.provider = provider;
  }

  @Override
  protected void init() throws Exception {
    Arguments.notNull(algorithm, "algorithm is required.");
  }

  protected AlgorithmParameters getImpl() throws GeneralSecurityException {
    AlgorithmParameters impl = null;
    if (provider == null) {
      impl = AlgorithmParameters.getInstance(algorithm);
    } else {
      impl = AlgorithmParameters.getInstance(algorithm, provider);
    }
    return impl;
  }

  protected void release(AlgorithmParameters impl) {
  }

  public <T extends AlgorithmParameterSpec> T getParameterSpec(Class<T> paramSpec) throws CryptoException {
    initialize();
    AlgorithmParameters impl = null;
    try {
      impl = getImpl();
      if (algParamSpec != null) {
        impl.init(algParamSpec);
      }
      return impl.getParameterSpec(paramSpec);

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

  public AlgorithmParametersUtil setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public AlgorithmParametersUtil setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public AlgorithmParametersUtil setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
