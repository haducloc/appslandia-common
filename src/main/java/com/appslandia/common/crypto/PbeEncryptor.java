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
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.RandomUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeEncryptor extends PbeObject implements Encryptor {

  protected static final int GCM_IV_SIZE = 12;
  protected static final int GCM_TAG_SIZE = 16;

  protected String transformation, provider;
  private CipherOps cipherOps;

  protected static final class RandomHolder {
    static final Random instance = new SecureRandom();
  }

  @Override
  protected void init() throws Exception {
    super.init();
    Asserts.notNull(this.transformation, "transformation is required.");

    this.cipherOps = new CipherOps(this.transformation);
    Asserts.isTrue(this.cipherOps.isMode("CBC", "CFB", "OFB", "CTR", "ECB", "GCM"),
        "CBC|CFB|OFB|CTR|ECB|GCM mode is required.");
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

  protected int getIvSize(Cipher cipher) {
    if (this.cipherOps.isMode("ECB")) {
      return -1;
    }
    if (this.cipherOps.isMode("GCM")) {
      return getIvSize();
    }
    return cipher.getBlockSize();
  }

  protected int getIvSize() {
    return GCM_IV_SIZE;
  }

  protected int getTagSize() {
    return GCM_TAG_SIZE;
  }

  @Override
  public byte[] encrypt(byte[] message) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");

    SecretKey key = null;
    try {
      Cipher impl = getImpl();
      int ivSize = getIvSize(impl);
      byte[] iv = null;

      byte[] salt = RandomUtils.nextBytes(this.saltSize, RandomHolder.instance);
      key = toSecretKey(salt, this.cipherOps.getAlgorithm());

      if (ivSize <= 0) {
        impl.init(Cipher.ENCRYPT_MODE, key);
      } else {
        iv = RandomUtils.nextBytes(ivSize, RandomHolder.instance);

        if (this.cipherOps.isMode("GCM")) {
          impl.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(getTagSize() * 8, iv));
        } else {
          impl.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        }
      }

      byte[] encMsg = impl.doFinal(message);
      if (iv == null) {
        return ArrayUtils.append(salt, encMsg);
      } else {
        return ArrayUtils.append(iv, salt, encMsg);
      }

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
      int ivSize = getIvSize(impl);
      byte[] iv = null;
      byte[] salt = new byte[this.saltSize];

      if (ivSize <= 0) {
        Asserts.isTrue(message.length >= this.saltSize, "message is invalid.");
        ArrayUtils.copy(message, salt);
      } else {
        Asserts.isTrue(message.length >= ivSize + this.saltSize, "message is invalid.");
        iv = new byte[ivSize];
        ArrayUtils.copy(message, iv, salt);
      }
      key = toSecretKey(salt, this.cipherOps.getAlgorithm());

      if (iv == null) {
        impl.init(Cipher.DECRYPT_MODE, key);
      } else if (this.cipherOps.isMode("GCM")) {
        impl.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(getTagSize() * 8, iv));
      } else {
        impl.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
      }

      if (iv == null) {
        return impl.doFinal(message, this.saltSize, message.length - this.saltSize);
      } else {
        return impl.doFinal(message, ivSize + this.saltSize, message.length - ivSize - this.saltSize);
      }

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

  public PbeEncryptor setTransformation(String transformation) {
    this.assertNotInitialized();
    this.transformation = transformation;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public PbeEncryptor setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }

  @Override
  public PbeEncryptor setSaltSize(int saltSize) {
    super.setSaltSize(saltSize);
    return this;
  }

  @Override
  public PbeEncryptor setIterationCount(int iterationCount) {
    super.setIterationCount(iterationCount);
    return this;
  }

  @Override
  public PbeEncryptor setKeySize(int keySize) {
    super.setKeySize(keySize);
    return this;
  }

  @Override
  public PbeEncryptor setPassword(char[] password) {
    super.setPassword(password);
    return this;
  }

  @Override
  public PbeEncryptor setPassword(String passwordExpr) {
    super.setPassword(passwordExpr);
    return this;
  }

  @Override
  public PbeEncryptor setPbeSecretKeyGenerator(PbeSecretKeyGenerator pbeSecretKeyGenerator) {
    super.setPbeSecretKeyGenerator(pbeSecretKeyGenerator);
    return this;
  }
}
