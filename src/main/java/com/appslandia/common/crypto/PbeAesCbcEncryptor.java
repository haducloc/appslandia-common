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
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.RandomUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeAesCbcEncryptor extends PbeObject implements Encryptor {

  private String transformation, provider;

  private static final class RandomHolder {
    static final Random instance = new SecureRandom();
  }

  @Override
  protected void init() throws Exception {
    super.init();
    Asserts.notNull(this.transformation, "transformation is required.");

    CipherOps cipherOps = new CipherOps(this.transformation);
    Asserts.isTrue(cipherOps.isAlgorithm("AES"), "AES algorithm is required.");
    Asserts.isTrue(cipherOps.isMode("CBC"), "CBC mode is required.");
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

  @Override
  public byte[] encrypt(byte[] message) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");

    SecretKey key = null;
    try {
      byte[] salt = RandomUtils.nextBytes(this.saltSize, RandomHolder.instance);
      key = toSecretKey(salt, "AES");

      Cipher impl = getImpl();
      byte[] iv = RandomUtils.nextBytes(impl.getBlockSize(), RandomHolder.instance);
      impl.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

      byte[] encMsg = impl.doFinal(message);
      return ArrayUtils.append(iv, salt, encMsg);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(key);
    }
  }

  @Override
  public byte[] decrypt(byte[] message) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");

    SecretKey key = null;
    try {
      Cipher impl = getImpl();
      int blockSize = impl.getBlockSize();
      Asserts.isTrue(message.length >= blockSize + this.saltSize, "message is invalid.");

      byte[] iv = Arrays.copyOfRange(message, 0, blockSize);
      byte[] salt = Arrays.copyOfRange(message, blockSize, blockSize + this.saltSize);

      key = toSecretKey(salt, "AES");
      impl.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

      return impl.doFinal(message, blockSize + this.saltSize, message.length - blockSize - this.saltSize);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(key);
    }
  }

  public String getTransformation() {
    this.initialize();
    return this.transformation;
  }

  public PbeAesCbcEncryptor setTransformation(String transformation) {
    this.assertNotInitialized();
    this.transformation = transformation;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public PbeAesCbcEncryptor setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }

  @Override
  public PbeAesCbcEncryptor setSaltSize(int saltSize) {
    super.setSaltSize(saltSize);
    return this;
  }

  @Override
  public PbeAesCbcEncryptor setIterationCount(int iterationCount) {
    super.setIterationCount(iterationCount);
    return this;
  }

  @Override
  public PbeAesCbcEncryptor setKeySize(int keySize) {
    super.setKeySize(keySize);
    return this;
  }

  @Override
  public PbeAesCbcEncryptor setPassword(char[] password) {
    super.setPassword(password);
    return this;
  }

  @Override
  public PbeAesCbcEncryptor setPassword(String passwordExpr) {
    super.setPassword(passwordExpr);
    return this;
  }

  @Override
  public PbeAesCbcEncryptor setPbeSecretKeyGenerator(PbeSecretKeyGenerator pbeSecretKeyGenerator) {
    super.setPbeSecretKeyGenerator(pbeSecretKeyGenerator);
    return this;
  }
}
