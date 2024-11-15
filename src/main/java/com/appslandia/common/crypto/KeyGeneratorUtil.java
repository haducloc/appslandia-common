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

package com.appslandia.common.crypto;

import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class KeyGeneratorUtil extends InitializeObject {

  protected String algorithm, provider;
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
    Asserts.notNull(this.algorithm);
  }

  protected KeyGenerator getImpl() throws GeneralSecurityException {
    KeyGenerator impl = null;
    if (this.provider == null) {
      impl = KeyGenerator.getInstance(this.algorithm);
    } else {
      impl = KeyGenerator.getInstance(this.algorithm, this.provider);
    }
    return impl;
  }

  public SecretKey generate() throws CryptoException {
    this.initialize();
    try {
      KeyGenerator impl = getImpl();
      if (this.algParamSpec != null) {
        impl.init(this.algParamSpec);
      }
      return impl.generateKey();

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public String getAlgorithm() {
    this.initialize();
    return this.algorithm;
  }

  public KeyGeneratorUtil setAlgorithm(String algorithm) {
    this.assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public KeyGeneratorUtil setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public KeyGeneratorUtil setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
