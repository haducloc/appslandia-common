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

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ChaCha20Encryptor extends InitializeObject implements Encryptor {

  protected static final int IV_SIZE = 12;

  protected String transformation, provider;
  protected byte[] secret;
  protected SecretKey secretKey;

  protected CipherOps cipherOps;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.transformation, "transformation is required.");
    Asserts.isTrue(this.secret != null || this.secretKey != null, "secret|secretKey is required.");

    CipherOps cipherOps = new CipherOps(this.transformation);

    Asserts.isTrue(cipherOps.isAlgorithm("ChaCha20") || cipherOps.isAlgorithm("ChaCha20-Poly1305"),
        "ChaCha20|ChaCha20-Poly1305 algorithm is required.");

    if (this.secret != null) {
      this.secretKey = new DSecretKeySpec(this.secret, cipherOps.getAlgorithm());
      CryptoUtils.clear(this.secret);
      this.secret = null;

    } else {
      Asserts.isTrue(cipherOps.getAlgorithm().equalsIgnoreCase(this.secretKey.getAlgorithm()));
    }
    this.cipherOps = cipherOps;
  }

  @Override
  public void destroy() throws DestroyException {
    CryptoUtils.destroy(this.secretKey);
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
      byte[] iv = CryptoUtils.randomBytes(IV_SIZE);

      if (this.cipherOps.isAlgorithm("ChaCha20")) {
        impl.init(Cipher.ENCRYPT_MODE, this.secretKey, new ChaCha20ParameterSpec(iv, 1));
      } else {
        impl.init(Cipher.ENCRYPT_MODE, this.secretKey, new IvParameterSpec(iv));
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
    Asserts.isTrue(message.length >= IV_SIZE, "message is invalid.");

    try {
      byte[] iv = new byte[IV_SIZE];
      ArrayUtils.copy(message, iv);
      Cipher impl = getImpl();

      if (this.cipherOps.isAlgorithm("ChaCha20")) {
        impl.init(Cipher.DECRYPT_MODE, this.secretKey, new ChaCha20ParameterSpec(iv, 1));
      } else {
        impl.init(Cipher.DECRYPT_MODE, this.secretKey, new IvParameterSpec(iv));
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

  public ChaCha20Encryptor setSecretKey(SecretKey secretKey) {
    this.assertNotInitialized();
    if (secretKey != null) {
      this.secretKey = CryptoUtils.copy(secretKey);
    }
    return this;
  }
}
