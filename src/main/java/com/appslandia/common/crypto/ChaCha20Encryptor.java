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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.ChaCha20ParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.SecureRand;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ChaCha20Encryptor extends InitializeObject implements Encryptor {

  protected static final int IV_SIZE = 12;

  protected String transformation, provider;
  protected CipherOps cipherOps;

  protected byte[] secret;
  protected SecretKey key;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.secret, "secret is required.");
    Asserts.notNull(this.transformation, "transformation is required.");

    this.cipherOps = new CipherOps(this.transformation);

    Asserts.isTrue(this.cipherOps.isAlgorithm("ChaCha20") || this.cipherOps.isAlgorithm("ChaCha20-Poly1305"),
        "ChaCha20|ChaCha20-Poly1305 algorithm is required.");
    Asserts.isTrue(this.secret.length == 32, "secret must be 32 bytes when using ChaCha20 or ChaCha20-Poly1305.");

    this.key = new SecretKeySpec(this.secret, this.cipherOps.getAlgorithm());
    CryptoUtils.clear(this.secret);
  }

  @Override
  public void destroy() throws DestroyException {
    CryptoUtils.destroy(this.key);
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

    try {
      Cipher impl = getImpl();
      byte[] iv = RandomUtils.nextBytes(IV_SIZE, SecureRand.getInstance());

      if (this.cipherOps.isAlgorithm("ChaCha20")) {
        impl.init(Cipher.ENCRYPT_MODE, this.key, new ChaCha20ParameterSpec(iv, 1));
      } else {
        impl.init(Cipher.ENCRYPT_MODE, this.key, new IvParameterSpec(iv));
      }

      byte[] storedMsg = impl.doFinal(message);
      return ArrayUtils.append(iv, storedMsg);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  @Override
  public byte[] decrypt(byte[] message) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");

    try {
      Asserts.isTrue(message.length >= IV_SIZE, "message is invalid.");

      byte[] iv = new byte[IV_SIZE];
      ArrayUtils.copy(message, iv);
      Cipher impl = getImpl();

      if (this.cipherOps.isAlgorithm("ChaCha20")) {
        impl.init(Cipher.DECRYPT_MODE, this.key, new ChaCha20ParameterSpec(iv, 1));
      } else {
        impl.init(Cipher.DECRYPT_MODE, this.key, new IvParameterSpec(iv));
      }
      return impl.doFinal(message, IV_SIZE, message.length - IV_SIZE);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public String getTransformation() {
    this.initialize();
    return this.transformation;
  }

  public ChaCha20Encryptor setTransformation(String transformation) {
    this.assertNotInitialized();
    this.transformation = transformation;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public ChaCha20Encryptor setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public ChaCha20Encryptor setSecret(byte[] secret) {
    this.assertNotInitialized();
    if (secret != null) {
      this.secret = ArrayUtils.copy(secret);
    }
    return this;
  }
}