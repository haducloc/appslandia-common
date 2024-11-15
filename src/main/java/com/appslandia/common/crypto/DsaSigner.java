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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DsaSigner extends InitializeObject implements Digester {
  protected String algorithm, provider;

  protected PrivateKey privateKey;
  protected PublicKey publicKey;

  protected AlgorithmParameterSpec algParamSpec;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.algorithm, "algorithm is required.");
    Asserts.isTrue((this.privateKey != null) || (this.publicKey != null), "No key is provided.");
  }

  protected Signature getImpl() throws GeneralSecurityException {
    Signature impl = null;
    if (this.provider == null) {
      impl = Signature.getInstance(this.algorithm);
    } else {
      impl = Signature.getInstance(this.algorithm, this.provider);
    }
    return impl;
  }

  @Override
  public void destroy() throws DestroyException {
    CryptoUtils.destroy(this.privateKey);
  }

  @Override
  public byte[] digest(byte[] message) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");
    Asserts.notNull(this.privateKey, "privateKey is required.");

    try {
      Signature impl = getImpl();
      if (this.algParamSpec != null) {
        impl.setParameter(this.algParamSpec);
      }
      impl.initSign(this.privateKey);
      impl.update(message);
      return impl.sign();

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  @Override
  public boolean verify(byte[] message, byte[] signature) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");
    Asserts.notNull(signature, "signature is required.");
    Asserts.notNull(this.publicKey, "publicKey is required.");

    try {
      Signature impl = getImpl();
      if (this.algParamSpec != null) {
        impl.setParameter(this.algParamSpec);
      }
      impl.initVerify(this.publicKey);
      impl.update(message);
      return impl.verify(signature);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public String getAlgorithm() {
    this.initialize();
    return this.algorithm;
  }

  public DsaSigner setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public DsaSigner setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public DsaSigner setPrivateKey(PrivateKey privateKey) {
    assertNotInitialized();
    this.privateKey = privateKey;
    return this;
  }

  public DsaSigner setPublicKey(PublicKey publicKey) {
    assertNotInitialized();
    this.publicKey = publicKey;
    return this;
  }

  public DsaSigner setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
