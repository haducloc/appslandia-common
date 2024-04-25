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
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;
import java.util.function.BiFunction;

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
  private String transformation, provider;
  private CipherOps cipherOps;

  // -1: IV unsupported
  private int ivSize;
  private BiFunction<CipherOps, byte[], AlgorithmParameterSpec> algParamSpec;

  private Cipher cipher;
  final Object mutex = new Object();
  final Random random = new SecureRandom();

  @Override
  protected void init() throws Exception {
    super.init();

    Asserts.notNull(this.transformation, "transformation is required.");
    CipherOps cipherOps = new CipherOps(this.transformation);

    Asserts.isTrue(!"RSA".equals(cipherOps.getAlgorithm()), "Use RsaEncryptor instead.");
    this.cipherOps = cipherOps;

    // algParamSpec
    if (this.algParamSpec == null) {
      this.algParamSpec = (opers, iv) -> toAlgParamSpec(opers, iv);
    }

    // cipher
    if (this.provider == null) {
      this.cipher = Cipher.getInstance(this.transformation);
    } else {
      this.cipher = Cipher.getInstance(this.transformation, this.provider);
    }

    // ivSize
    if (this.ivSize == 0) {
      this.ivSize = this.cipher.getBlockSize();
    }
  }

  @Override
  public byte[] encrypt(byte[] message) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");

    byte[] salt = RandomUtils.nextBytes(this.saltSize, this.random);
    SecretKey secretKey = buildSecretKey(salt, this.cipherOps.getAlgorithm());
    byte[] iv = (this.ivSize > 0) ? RandomUtils.nextBytes(this.ivSize, this.random) : null;

    try {
      byte[] encMsg = null;
      synchronized (this.mutex) {
        AlgorithmParameterSpec spec = this.algParamSpec.apply(this.cipherOps, iv);

        if (spec == null) {
          this.cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
          this.cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
        }
        encMsg = this.cipher.doFinal(message);
      }
      return (iv != null) ? ArrayUtils.append(iv, salt, encMsg) : ArrayUtils.append(salt, encMsg);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(secretKey);
    }
  }

  @Override
  public byte[] decrypt(byte[] message) throws CryptoException {
    this.initialize();

    Asserts.notNull(message, "message is required.");

    byte[] salt = new byte[this.saltSize];
    byte[] iv = (this.ivSize > 0) ? new byte[this.ivSize] : null;

    if (iv == null) {
      Asserts.isTrue(message.length >= this.saltSize, "message is invalid.");
      ArrayUtils.copy(message, salt);
    } else {
      Asserts.isTrue(message.length >= this.ivSize + this.saltSize, "message is invalid.");
      ArrayUtils.copy(message, iv, salt);
    }

    SecretKey secretKey = buildSecretKey(salt, this.cipherOps.getAlgorithm());
    try {
      synchronized (this.mutex) {
        AlgorithmParameterSpec spec = this.algParamSpec.apply(this.cipherOps, iv);

        if (spec == null) {
          this.cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } else {
          this.cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        }

        if (iv == null) {
          return this.cipher.doFinal(message, salt.length, message.length - salt.length);
        } else {
          return this.cipher.doFinal(message, iv.length + salt.length, message.length - iv.length - salt.length);
        }
      }
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(secretKey);
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
  public PbeEncryptor setPassword(String passwordOrEnv) {
    super.setPassword(passwordOrEnv);
    return this;
  }

  @Override
  public PbeEncryptor setPbeSecretKeyFactory(PbeSecretKeyFactory pbeSecretKeyFactory) {
    super.setPbeSecretKeyFactory(pbeSecretKeyFactory);
    return this;
  }

  public PbeEncryptor setIvSize(int ivSize) {
    assertNotInitialized();
    this.ivSize = ivSize;
    return this;
  }

  public PbeEncryptor setAlgParamSpec(BiFunction<CipherOps, byte[], AlgorithmParameterSpec> algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }

  static AlgorithmParameterSpec toAlgParamSpec(CipherOps cipherOps, byte[] iv) {
    if (iv != null) {
      if ("GCM".equals(cipherOps.getMode())) {
        return new GCMParameterSpec(128, iv);
      }
      return new IvParameterSpec(iv);
    }
    return null;
  }
}
