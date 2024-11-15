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
import javax.crypto.spec.GCMParameterSpec;
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
public class AesEncryptor extends InitializeObject implements Encryptor {

  protected String transformation, provider;
  protected CipherOps cipherOps;
  protected GcmSpec gcmSpec;

  protected byte[] secret;
  protected SecretKey key;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.secret, "secret is required.");
    Asserts.notNull(this.transformation, "transformation is required.");

    CipherOps cipherOps = new CipherOps(this.transformation);

    Asserts.isTrue(cipherOps.isAlgorithm("AES"), "AES algorithm is required.");
    Asserts.isTrue(cipherOps.isMode("CBC", "^CFB\\d*$", "CTR", "^OFB\\d*$", "ECB", "GCM"),
        "CBC|CFB|CTR|OFB|ECB|GCM mode is required.");

    if (cipherOps.isMode("GCM")) {
      this.gcmSpec = new GcmSpec();
    }

    this.key = new SecretKeySpec(this.secret, cipherOps.getAlgorithm());
    CryptoUtils.clear(this.secret);
    this.cipherOps = cipherOps;
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

  protected int getIvSize(Cipher cipher) {
    if (this.cipherOps.isMode("ECB")) {
      return -1;
    }
    if (this.cipherOps.isMode("GCM")) {
      return this.gcmSpec.getIvSize();
    }
    return cipher.getBlockSize();
  }

  @Override
  public byte[] encrypt(byte[] message) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");

    try {
      Cipher impl = getImpl();
      int ivSize = getIvSize(impl);
      byte[] iv = null;

      if (ivSize <= 0) {
        impl.init(Cipher.ENCRYPT_MODE, this.key);
      } else {
        iv = RandomUtils.nextBytes(ivSize, SecureRand.getInstance());

        if (this.cipherOps.isMode("GCM")) {
          impl.init(Cipher.ENCRYPT_MODE, this.key, new GCMParameterSpec(this.gcmSpec.getTagSize() * 8, iv));
        } else {
          impl.init(Cipher.ENCRYPT_MODE, this.key, new IvParameterSpec(iv));
        }
      }

      byte[] storedMsg = impl.doFinal(message);
      if (iv == null) {
        return storedMsg;
      } else {
        return ArrayUtils.append(iv, storedMsg);
      }
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  @Override
  public byte[] decrypt(byte[] message) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");

    try {
      Cipher impl = getImpl();
      int ivSize = getIvSize(impl);
      byte[] iv = null;

      if (ivSize > 0) {
        Asserts.isTrue(message.length >= ivSize, "message is invalid.");
        iv = new byte[ivSize];
        ArrayUtils.copy(message, iv);
      }

      if (iv == null) {
        impl.init(Cipher.DECRYPT_MODE, this.key);
      } else if (this.cipherOps.isMode("GCM")) {
        impl.init(Cipher.DECRYPT_MODE, this.key, new GCMParameterSpec(this.gcmSpec.getTagSize() * 8, iv));
      } else {
        impl.init(Cipher.DECRYPT_MODE, this.key, new IvParameterSpec(iv));
      }

      if (iv == null) {
        return impl.doFinal(message);
      } else {
        return impl.doFinal(message, ivSize, message.length - ivSize);
      }
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public String getTransformation() {
    this.initialize();
    return this.transformation;
  }

  public AesEncryptor setTransformation(String transformation) {
    this.assertNotInitialized();
    this.transformation = transformation;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public AesEncryptor setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public AesEncryptor setSecret(byte[] secret) {
    this.assertNotInitialized();
    if (secret != null) {
      this.secret = ArrayUtils.copy(secret);
    }
    return this;
  }
}