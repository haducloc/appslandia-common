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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

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
public class PbeAesGcmEncryptor extends InitializingObject implements Encryptor {

  final String transformation = "AES/GCM/NoPadding";
  protected String provider;
  protected PbeSecretGen pbeSecretGen;
  protected CipherOps cipherOps;

  protected int ivSize = 12;
  protected int tagSize = 16;

  @Override
  protected void init() throws Exception {
    cipherOps = new CipherOps(transformation);

    Arguments.isTrue(ivSize >= 12 && ivSize <= 16, "ivSize must be in range [12..16].");
    Arguments.isTrue(tagSize >= 12 && tagSize <= 16, "tagSize must be in range [12..16].");

    Arguments.notNull(pbeSecretGen, "pbeSecretGen is required.");
  }

  @Override
  public void destroy() throws DestroyingException {
    if (pbeSecretGen != null) {
      pbeSecretGen.destroy();
    }
  }

  protected Cipher getImpl() throws GeneralSecurityException {
    Cipher impl = null;
    if (provider == null) {
      impl = Cipher.getInstance(transformation);
    } else {
      impl = Cipher.getInstance(transformation, provider);
    }
    return impl;
  }

  protected void release(Cipher impl) {
  }

  @Override
  public byte[] encrypt(byte[] message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");

    Cipher impl = null;
    SecretKey key = null;
    try {
      impl = getImpl();
      var iv = CryptoUtils.randomBytes(ivSize);

      var salt = new Out<byte[]>();
      key = pbeSecretGen.generate(cipherOps.getAlgorithm(), salt);
      impl.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(this.tagSize * 8, iv));

      var storedMsg = impl.doFinal(message);
      return ArrayUtils.append(salt.value, iv, storedMsg);

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
  public byte[] decrypt(byte[] message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");

    Cipher impl = null;
    SecretKey key = null;
    try {
      impl = getImpl();
      var saltSize = pbeSecretGen.getSaltSize();
      var salt = new byte[saltSize];

      Arguments.isTrue(message.length >= saltSize + ivSize + tagSize, "message is invalid.");
      var iv = new byte[ivSize];
      ArrayUtils.copy(message, salt, iv);

      key = pbeSecretGen.generate(cipherOps.getAlgorithm(), salt);
      impl.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(this.tagSize * 8, iv));

      return impl.doFinal(message, saltSize + ivSize, message.length - ivSize - saltSize);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(key);
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getTransformation() {
    initialize();
    return transformation;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public PbeAesGcmEncryptor setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public PbeAesGcmEncryptor setPbeSecretGen(PbeSecretGen pbeSecretGen) {
    assertNotInitialized();
    this.pbeSecretGen = pbeSecretGen;
    return this;
  }

  public PbeAesGcmEncryptor setIvSize(int ivSize) {
    assertNotInitialized();
    this.ivSize = ivSize;
    return this;
  }

  public PbeAesGcmEncryptor setTagSize(int tagSize) {
    assertNotInitialized();
    this.tagSize = tagSize;
    return this;
  }
}
