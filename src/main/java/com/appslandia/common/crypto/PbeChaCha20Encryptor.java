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
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.ChaCha20ParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.RandomUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeChaCha20Encryptor extends PbeObject implements Encryptor {

  protected static final int IV_SIZE = 12;
  protected static final int KEY_SIZE = 32;

  protected String transformation, provider;
  private CipherOps cipherOps;

  protected static final class RandomHolder {
    static final Random instance = new SecureRandom();
  }

  @Override
  protected void init() throws Exception {
    this.keySize = KEY_SIZE;
    super.init();

    Asserts.notNull(this.transformation, "transformation is required.");
    this.cipherOps = new CipherOps(this.transformation);

    Asserts.isTrue(this.cipherOps.isAlgorithm("ChaCha20") || this.cipherOps.isAlgorithm("ChaCha20-Poly1305"),
        "ChaCha20|ChaCha20-Poly1305 algorithm is required.");
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
      key = toSecretKey(salt, this.cipherOps.getAlgorithm());
      Cipher impl = getImpl();

      byte[] iv = RandomUtils.nextBytes(IV_SIZE, RandomHolder.instance);
      if (this.cipherOps.isAlgorithm("ChaCha20")) {
        impl.init(Cipher.ENCRYPT_MODE, key, new ChaCha20ParameterSpec(iv, 1));
      } else {
        impl.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
      }

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
      Asserts.isTrue(message.length >= IV_SIZE + this.saltSize, "message is invalid.");

      byte[] iv = new byte[IV_SIZE];
      byte[] salt = new byte[this.saltSize];
      ArrayUtils.copy(message, iv, salt);

      key = toSecretKey(salt, this.cipherOps.getAlgorithm());
      Cipher impl = getImpl();

      if (this.cipherOps.isAlgorithm("ChaCha20")) {
        impl.init(Cipher.DECRYPT_MODE, key, new ChaCha20ParameterSpec(iv, 1));
      } else {
        impl.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
      }
      return impl.doFinal(message, IV_SIZE + this.saltSize, message.length - IV_SIZE - this.saltSize);

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

  public PbeChaCha20Encryptor setTransformation(String transformation) {
    this.assertNotInitialized();
    this.transformation = transformation;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public PbeChaCha20Encryptor setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }

  @Override
  public PbeChaCha20Encryptor setSaltSize(int saltSize) {
    super.setSaltSize(saltSize);
    return this;
  }

  @Override
  public PbeChaCha20Encryptor setIterationCount(int iterationCount) {
    super.setIterationCount(iterationCount);
    return this;
  }

  @Override
  public PbeChaCha20Encryptor setKeySize(int keySize) {
    throw new UnsupportedOperationException("ChaCha20Encryptor.setKeySize(int keySize)");
  }

  @Override
  public PbeChaCha20Encryptor setPassword(char[] password) {
    super.setPassword(password);
    return this;
  }

  @Override
  public PbeChaCha20Encryptor setPassword(String passwordExpr) {
    super.setPassword(passwordExpr);
    return this;
  }

  @Override
  public PbeChaCha20Encryptor setPbeSecretKeyGenerator(PbeSecretKeyGenerator pbeSecretKeyGenerator) {
    super.setPbeSecretKeyGenerator(pbeSecretKeyGenerator);
    return this;
  }
}
