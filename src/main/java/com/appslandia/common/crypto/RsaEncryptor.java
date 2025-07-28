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
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class RsaEncryptor extends InitializeObject implements Encryptor {
  protected String transformation, provider;

  protected PublicKey publicKey;
  protected PrivateKey privateKey;

  protected AlgorithmParameterSpec algParamSpec;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.transformation, "transformation is required.");

    var cipherOps = new CipherOps(this.transformation);
    Arguments.isTrue(cipherOps.isAlgorithm("RSA"), "RSA algorithm is required.");

    if (this.privateKey != null) {
      Arguments.isTrue("RSA".equalsIgnoreCase(this.privateKey.getAlgorithm()), "The privateKey.algorithm must be RSA.");
    }
    if (this.publicKey != null) {
      Arguments.isTrue("RSA".equalsIgnoreCase(this.publicKey.getAlgorithm()), "The publicKey.algorithm must be RSA.");
    }
  }

  protected Cipher getImpl() throws GeneralSecurityException {
    Cipher impl = null;
    if (this.provider == null) {
      impl = Cipher.getInstance(this.transformation);
    } else {
      impl = Cipher.getInstance(this.transformation, this.provider);
    }
    return impl;
  }

  protected void release(Cipher impl) {
  }

  @Override
  public void destroy() throws DestroyException {
    CryptoUtils.destroy(this.privateKey);
  }

  @Override
  public byte[] encrypt(byte[] message) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(this.publicKey, "publicKey is required.");

    Cipher impl = null;
    try {
      impl = getImpl();
      if (this.algParamSpec == null) {
        impl.init(Cipher.ENCRYPT_MODE, this.publicKey);
      } else {
        impl.init(Cipher.ENCRYPT_MODE, this.publicKey, this.algParamSpec);
      }
      return impl.doFinal(message);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  @Override
  public byte[] decrypt(byte[] message) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(this.privateKey, "privateKey is required.");

    Cipher impl = null;
    try {
      impl = getImpl();
      if (this.algParamSpec == null) {
        impl.init(Cipher.DECRYPT_MODE, this.privateKey);
      } else {
        impl.init(Cipher.DECRYPT_MODE, this.privateKey, this.algParamSpec);
      }
      return impl.doFinal(message);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getTransformation() {
    this.initialize();
    return this.transformation;
  }

  public RsaEncryptor setTransformation(String transformation) {
    this.assertNotInitialized();
    this.transformation = transformation;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public RsaEncryptor setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public RsaEncryptor setPrivateKey(PrivateKey privateKey) {
    assertNotInitialized();
    if (privateKey != null) {
      this.privateKey = new KeyFactoryUtil(privateKey.getAlgorithm()).copy(privateKey);
    }
    return this;
  }

  public RsaEncryptor setPublicKey(PublicKey publicKey) {
    assertNotInitialized();
    if (publicKey != null) {
      this.publicKey = new KeyFactoryUtil(publicKey.getAlgorithm()).copy(publicKey);
    }
    return this;
  }

  public RsaEncryptor setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
