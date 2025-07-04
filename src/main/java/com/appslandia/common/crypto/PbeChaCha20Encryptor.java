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
import javax.crypto.spec.ChaCha20ParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ArrayUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class PbeChaCha20Encryptor extends InitializeObject implements Encryptor {

  protected static final int IV_SIZE = 12;

  protected String transformation, provider;
  protected CipherOps cipherOps;

  protected PbeSecretGen pbeSecretGen;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.transformation, "transformation is required.");
    var cipherOps = new CipherOps(this.transformation);
    Arguments.isTrue(cipherOps.isAlgorithm("ChaCha20") || cipherOps.isAlgorithm("ChaCha20-Poly1305"),
        "ChaCha20|ChaCha20-Poly1305 algorithm is required.");
    Arguments.notNull(this.pbeSecretGen, "pbeSecretGen is required.");
    Arguments.isTrue(this.pbeSecretGen.getKeySize() == 32,
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

  protected void release(Cipher impl) {
  }

  @Override
  public byte[] encrypt(byte[] message) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");

    Cipher impl = null;
    SecretKey key = null;
    try {
      impl = getImpl();

      var salt = new Out<byte[]>();
      key = this.pbeSecretGen.generate(this.cipherOps.getAlgorithm(), salt);
      var iv = CryptoUtils.randomBytes(IV_SIZE);

      if (this.cipherOps.isAlgorithm("ChaCha20")) {
        impl.init(Cipher.ENCRYPT_MODE, key, new ChaCha20ParameterSpec(iv, 1));
      } else {
        impl.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
      }

      var storedMsg = impl.doFinal(message);
      return ArrayUtils.append(iv, salt.value, storedMsg);

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
    this.initialize();
    Arguments.notNull(message, "message is required.");

    Cipher impl = null;
    SecretKey key = null;
    try {
      impl = getImpl();

      var saltSize = this.pbeSecretGen.getSaltSize();
      Arguments.isTrue(message.length >= IV_SIZE + saltSize, "message is invalid.");

      var iv = new byte[IV_SIZE];
      var salt = new byte[saltSize];
      ArrayUtils.copy(message, iv, salt);

      key = this.pbeSecretGen.generate(this.cipherOps.getAlgorithm(), salt);

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
      if (impl != null) {
        release(impl);
      }
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
