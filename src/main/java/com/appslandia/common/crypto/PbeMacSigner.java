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

import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ArrayUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class PbeMacSigner extends InitializingObject implements Digester {

  protected String algorithm, provider;
  protected AlgorithmParameterSpec algParamSpec;
  protected PbeSecretGen pbeSecretGen;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(algorithm, "algorithm is required.");
  }

  @Override
  public void destroy() throws DestroyingException {
    if (pbeSecretGen != null) {
      pbeSecretGen.destroy();
    }
  }

  protected Mac getImpl() throws GeneralSecurityException {
    Mac impl = null;
    if (provider == null) {
      impl = Mac.getInstance(algorithm);
    } else {
      impl = Mac.getInstance(algorithm, provider);
    }
    return impl;
  }

  protected void release(Mac impl) {
  }

  @Override
  public byte[] digest(byte[] message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");

    Mac impl = null;
    SecretKey key = null;
    var salt = new Out<byte[]>();
    try {
      impl = getImpl();
      key = pbeSecretGen.generate(algorithm, salt);
      if (algParamSpec == null) {
        impl.init(key);
      } else {
        impl.init(key, algParamSpec);
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
    initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(digested, "digested is required.");

    var saltSize = pbeSecretGen.getSaltSize();
    Arguments.isTrue(digested.length >= saltSize, "digested is invalid.");

    var salt = new byte[saltSize];
    var storedHash = new byte[digested.length - saltSize];
    ArrayUtils.copy(digested, salt, storedHash);

    Mac impl = null;
    SecretKey key = null;
    try {
      impl = getImpl();
      key = pbeSecretGen.generate(algorithm, salt);
      if (algParamSpec == null) {
        impl.init(key);
      } else {
        impl.init(key, algParamSpec);
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
    initialize();
    return algorithm;
  }

  public PbeMacSigner setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public PbeMacSigner setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public PbeMacSigner setPbeSecretGen(PbeSecretGen pbeSecretGen) {
    assertNotInitialized();
    this.pbeSecretGen = pbeSecretGen;
    return this;
  }

  public PbeMacSigner setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
