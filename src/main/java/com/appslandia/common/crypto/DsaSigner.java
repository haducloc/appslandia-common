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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class DsaSigner extends InitializeObject implements Digester {
  protected String algorithm, provider;

  protected PrivateKey privateKey;
  protected PublicKey publicKey;

  protected AlgorithmParameterSpec algParamSpec;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.algorithm, "algorithm is required.");
    Arguments.isTrue((this.privateKey != null) || (this.publicKey != null), "No key is provided.");
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

  protected void release(Signature impl) {
  }

  @Override
  public void destroy() throws DestroyException {
    CryptoUtils.destroy(this.privateKey);
  }

  @Override
  public byte[] digest(byte[] message) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(this.privateKey, "privateKey is required.");

    Signature impl = null;
    try {
      impl = getImpl();
      if (this.algParamSpec != null) {
        impl.setParameter(this.algParamSpec);
      }
      impl.initSign(this.privateKey);
      impl.update(message);
      return impl.sign();

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  @Override
  public boolean verify(byte[] message, byte[] signature) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(signature, "signature is required.");
    Arguments.notNull(this.publicKey, "publicKey is required.");

    Signature impl = null;
    try {
      impl = getImpl();
      if (this.algParamSpec != null) {
        impl.setParameter(this.algParamSpec);
      }
      impl.initVerify(this.publicKey);
      impl.update(message);
      return impl.verify(signature);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
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
    if (privateKey != null) {
      this.privateKey = new KeyFactoryUtil(privateKey.getAlgorithm()).copy(privateKey);
    }
    return this;
  }

  public DsaSigner setPublicKey(PublicKey publicKey) {
    assertNotInitialized();
    if (publicKey != null) {
      this.publicKey = new KeyFactoryUtil(publicKey.getAlgorithm()).copy(publicKey);
    }
    return this;
  }

  public DsaSigner setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
