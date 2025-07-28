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
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ArrayUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class PbeMacSigner extends InitializeObject implements Digester {

  protected String algorithm, provider;
  protected AlgorithmParameterSpec algParamSpec;
  protected PbeSecretGen pbeSecretGen;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.algorithm, "algorithm is required.");
  }

  @Override
  public void destroy() throws DestroyException {
    if (this.pbeSecretGen != null) {
      this.pbeSecretGen.destroy();
    }
  }

  protected Mac getImpl() throws GeneralSecurityException {
    Mac impl = null;
    if (this.provider == null) {
      impl = Mac.getInstance(this.algorithm);
    } else {
      impl = Mac.getInstance(this.algorithm, this.provider);
    }
    return impl;
  }

  protected void release(Mac impl) {
  }

  @Override
  public byte[] digest(byte[] message) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");

    Mac impl = null;
    SecretKey key = null;
    var salt = new Out<byte[]>();
    try {
      impl = this.getImpl();
      key = this.pbeSecretGen.generate(this.algorithm, salt);
      if (this.algParamSpec == null) {
        impl.init(key);
      } else {
        impl.init(key, this.algParamSpec);
      }

      var storedMac = impl.doFinal(message);
      return ArrayUtils.append(salt.value, storedMac);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(key);
      if (impl != null) {
        release(impl);
      }
    }
  }

  @Override
  public boolean verify(byte[] message, byte[] digested) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(digested, "digested is required.");

    var saltSize = this.pbeSecretGen.getSaltSize();
    Arguments.isTrue(digested.length >= saltSize, "digested is invalid.");

    var salt = new byte[saltSize];
    var storedHash = new byte[digested.length - saltSize];
    ArrayUtils.copy(digested, salt, storedHash);

    Mac impl = null;
    SecretKey key = null;
    try {
      impl = this.getImpl();
      key = this.pbeSecretGen.generate(this.algorithm, salt);
      if (this.algParamSpec == null) {
        impl.init(key);
      } else {
        impl.init(key, this.algParamSpec);
      }

      var computedMac = impl.doFinal(message);
      return MessageDigest.isEqual(storedHash, computedMac);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(key);
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getAlgorithm() {
    this.initialize();
    return this.algorithm;
  }

  public PbeMacSigner setAlgorithm(String algorithm) {
    this.assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public PbeMacSigner setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public PbeMacSigner setPbeSecretGen(PbeSecretGen pbeSecretGen) {
    this.assertNotInitialized();
    this.pbeSecretGen = pbeSecretGen;
    return this;
  }

  public PbeMacSigner setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
