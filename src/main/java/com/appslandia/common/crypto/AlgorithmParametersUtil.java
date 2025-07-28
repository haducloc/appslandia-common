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

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class AlgorithmParametersUtil extends InitializeObject {

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
    Arguments.notNull(this.algorithm, "algorithm is required.");
  }

  protected AlgorithmParameters getImpl() throws GeneralSecurityException {
    AlgorithmParameters impl = null;
    if (this.provider == null) {
      impl = AlgorithmParameters.getInstance(this.algorithm);
    } else {
      impl = AlgorithmParameters.getInstance(this.algorithm, this.provider);
    }
    return impl;
  }

  protected void release(AlgorithmParameters impl) {
  }

  public <T extends AlgorithmParameterSpec> T getParameterSpec(Class<T> paramSpec) throws CryptoException {
    this.initialize();
    AlgorithmParameters impl = null;
    try {
      impl = getImpl();
      if (this.algParamSpec != null) {
        impl.init(this.algParamSpec);
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
    return this.algorithm;
  }

  public AlgorithmParametersUtil setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return this.provider;
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
