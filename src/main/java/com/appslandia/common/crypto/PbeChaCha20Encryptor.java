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
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeChaCha20Encryptor extends InitializeObject implements Encryptor {

  protected static final int IV_SIZE = 12;

  protected String transformation, provider;
  protected CipherOps cipherOps;

  protected PbeSecretGen pbeSecretGen;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.transformation, "transformation is required.");
    CipherOps cipherOps = new CipherOps(this.transformation);

    Asserts.isTrue(cipherOps.isAlgorithm("ChaCha20") || cipherOps.isAlgorithm("ChaCha20-Poly1305"),
        "ChaCha20|ChaCha20-Poly1305 algorithm is required.");

    Asserts.notNull(this.pbeSecretGen, "pbeSecretGen is required.");
    Asserts.isTrue(this.pbeSecretGen.getKeySize() == 32,
        "pbeSecretGen.keySize must be 32 bytes when using ChaCha20 or ChaCha20-Poly1305.");

    this.cipherOps = cipherOps;
  }

  @Override
  public void destroy() throws DestroyException {
    if (this.pbeSecretGen != null) {
      this.pbeSecretGen.destroy();
    }
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
      Out<byte[]> salt = new Out<>();
      key = this.pbeSecretGen.generate(this.cipherOps.getAlgorithm(), salt);

      Cipher impl = getImpl();
      byte[] iv = CryptoUtils.randomBytes(IV_SIZE);

      if (this.cipherOps.isAlgorithm("ChaCha20")) {
        impl.init(Cipher.ENCRYPT_MODE, key, new ChaCha20ParameterSpec(iv, 1));
      } else {
        impl.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
      }

      byte[] storedMsg = impl.doFinal(message);
      return ArrayUtils.append(iv, salt.value, storedMsg);

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
      int saltSize = this.pbeSecretGen.getSaltSize();
      Asserts.isTrue(message.length >= IV_SIZE + saltSize, "message is invalid.");

      byte[] iv = new byte[IV_SIZE];
      byte[] salt = new byte[saltSize];
      ArrayUtils.copy(message, iv, salt);

      key = this.pbeSecretGen.generate(this.cipherOps.getAlgorithm(), salt);
      Cipher impl = getImpl();

      if (this.cipherOps.isAlgorithm("ChaCha20")) {
        impl.init(Cipher.DECRYPT_MODE, key, new ChaCha20ParameterSpec(iv, 1));
      } else {
        impl.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
      }
      return impl.doFinal(message, IV_SIZE + saltSize, message.length - IV_SIZE - saltSize);

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

  public PbeChaCha20Encryptor setPbeSecretGen(PbeSecretGen pbeSecretGen) {
    this.assertNotInitialized();
    this.pbeSecretGen = pbeSecretGen;
    return this;
  }
}
