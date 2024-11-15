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
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.SYS;
import com.appslandia.common.utils.SecureRand;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeSecretKeyGenerator extends InitializeObject {
  protected String algorithm, provider;

  protected char[] password;
  protected Integer saltSize;
  protected Integer iterationCount;
  protected Integer keySize;

  public PbeSecretKeyGenerator() {
  }

  public PbeSecretKeyGenerator(String algorithm) {
    this.algorithm = algorithm;
  }

  public PbeSecretKeyGenerator(String algorithm, String provider) {
    this.algorithm = algorithm;
    this.provider = provider;
  }

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.password, "password is required.");

    this.algorithm = ValueUtils.valueOrAlt(this.algorithm, CryptoUtils.DEFAULT_PBE_KEY_DERIVATION_ALGORITHM);
    this.saltSize = ValueUtils.valueOrAlt(this.saltSize, CryptoUtils.DEFAULT_PBE_SALT_SIZE);
    this.iterationCount = ValueUtils.valueOrAlt(this.iterationCount, CryptoUtils.DEFAULT_PBE_ITERATIONS);
    this.keySize = ValueUtils.valueOrAlt(this.keySize, CryptoUtils.DEFAULT_PBE_KEY_SIZE);
  }

  @Override
  public void destroy() throws DestroyException {
    CryptoUtils.clear(this.password);
  }

  protected SecretKeyFactory getImpl() throws GeneralSecurityException {
    SecretKeyFactory impl = null;
    if (this.provider == null) {
      impl = SecretKeyFactory.getInstance(this.algorithm);
    } else {
      impl = SecretKeyFactory.getInstance(this.algorithm, this.provider);
    }
    return impl;
  }

  public SecretKey generate(String algorithm, Out<byte[]> salt) throws CryptoException {
    this.initialize();
    salt.value = RandomUtils.nextBytes(this.saltSize, SecureRand.getInstance());
    return generate(algorithm, salt.value);
  }

  public SecretKey generate(String algorithm, byte[] salt) throws CryptoException {
    this.initialize();
    PBEKeySpec keySpec = new PBEKeySpec(this.password, salt, this.iterationCount, this.keySize * 8);
    try {
      SecretKey secret = this.getImpl().generateSecret(keySpec);
      byte[] kBytes = secret.getEncoded();
      CryptoUtils.destroy(secret);

      SecretKey key = new SecretKeySpec(kBytes, algorithm);
      CryptoUtils.clear(kBytes);
      return key;

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      keySpec.clearPassword();
    }
  }

  public String getAlgorithm() {
    this.initialize();
    return this.algorithm;
  }

  public PbeSecretKeyGenerator setAlgorithm(String algorithm) {
    this.assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public PbeSecretKeyGenerator setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public int getSaltSize() {
    this.initialize();
    return this.saltSize;
  }

  public PbeSecretKeyGenerator setSaltSize(Integer saltSize) {
    this.assertNotInitialized();
    this.saltSize = saltSize;
    return this;
  }

  public int getIterationCount() {
    this.initialize();
    return this.iterationCount;
  }

  public PbeSecretKeyGenerator setIterationCount(Integer iterationCount) {
    this.assertNotInitialized();
    this.iterationCount = iterationCount;
    return this;
  }

  public int getKeySize() {
    this.initialize();
    return this.keySize;
  }

  public PbeSecretKeyGenerator setKeySize(Integer keySize) {
    this.assertNotInitialized();
    this.keySize = keySize;
    return this;
  }

  public PbeSecretKeyGenerator setPassword(char[] password) {
    this.assertNotInitialized();
    if (password != null) {
      this.password = Arrays.copyOf(password, password.length);
    }
    return this;
  }

  public PbeSecretKeyGenerator setPassword(String passwordExpr) {
    this.assertNotInitialized();

    if (passwordExpr != null) {
      String resolvedValue = SYS.resolve(passwordExpr);

      if (resolvedValue == null) {
        throw new IllegalArgumentException("Failed to resolve expression: " + passwordExpr);
      }
      this.password = resolvedValue.toCharArray();
    }
    return this;
  }
}
