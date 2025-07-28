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

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.AlgorithmParameterSpec;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class KeyPairGeneratorUtil extends InitializeObject {

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
    Arguments.notNull(this.algorithm);
  }

  protected KeyPairGenerator getImpl() throws GeneralSecurityException {
    KeyPairGenerator impl = null;
    if (this.provider == null) {
      impl = KeyPairGenerator.getInstance(this.algorithm);
    } else {
      impl = KeyPairGenerator.getInstance(this.algorithm, this.provider);
    }
    return impl;
  }

  public KeyPair generate() throws CryptoException {
    this.initialize();
    KeyPairGenerator impl = null;
    try {
      impl = getImpl();

      if (this.keySize != null) {
        impl.initialize(this.keySize * 8);
      } else if (this.algParamSpec != null) {
        impl.initialize(this.algParamSpec);
      }
      return impl.generateKeyPair();

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public String getAlgorithm() {
    this.initialize();
    return this.algorithm;
  }

  public KeyPairGeneratorUtil setAlgorithm(String algorithm) {
    this.assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public KeyPairGeneratorUtil setProvider(String provider) {
    this.assertNotInitialized();
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
