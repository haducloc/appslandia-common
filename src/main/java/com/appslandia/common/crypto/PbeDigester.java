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
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeDigester extends InitializeObject implements Digester {

  protected String algorithm, provider;
  protected PbeSecretKeyGenerator pbeSecretKeyGenerator;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.algorithm, "algorithm is required.");
  }

  @Override
  public void destroy() throws DestroyException {
    if (this.pbeSecretKeyGenerator != null) {
      this.pbeSecretKeyGenerator.destroy();
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

  @Override
  public byte[] digest(byte[] message) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");

    Out<byte[]> salt = new Out<>();
    SecretKey key = null;
    try {
      Mac impl = this.getImpl();
      key = this.pbeSecretKeyGenerator.generate(this.algorithm, salt);
      impl.init(key);

      byte[] storedMac = impl.doFinal(message);
      return ArrayUtils.append(salt.value, storedMac);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(key);
    }
  }

  @Override
  public boolean verify(byte[] message, byte[] digested) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");
    Asserts.notNull(digested, "digested is required.");

    int saltSize = this.pbeSecretKeyGenerator.getSaltSize();
    Asserts.isTrue(digested.length >= saltSize, "digested is invalid.");

    byte[] salt = new byte[saltSize];
    byte[] storedHash = new byte[digested.length - saltSize];
    ArrayUtils.copy(digested, salt, storedHash);

    SecretKey key = null;
    try {
      Mac impl = this.getImpl();
      key = this.pbeSecretKeyGenerator.generate(this.algorithm, salt);
      impl.init(key);

      byte[] computedMac = impl.doFinal(message);
      return MessageDigest.isEqual(storedHash, computedMac);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(key);
    }
  }

  public String getAlgorithm() {
    this.initialize();
    return this.algorithm;
  }

  public PbeDigester setAlgorithm(String algorithm) {
    this.assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public PbeDigester setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public PbeDigester setPbeSecretKeyGenerator(PbeSecretKeyGenerator pbeSecretKeyGenerator) {
    this.assertNotInitialized();
    this.pbeSecretKeyGenerator = pbeSecretKeyGenerator;
    return this;
  }
}
