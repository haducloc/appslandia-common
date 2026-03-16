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
import javax.crypto.spec.IvParameterSpec;

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
public class PbeChaCha20Poly1305Encryptor extends InitializingObject implements Encryptor {

  protected static final int IV_SIZE = 12;
  protected static final int TAG_SIZE = 16;

  protected String provider;
  protected PbeSecretGen pbeSecretGen;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(pbeSecretGen, "pbeSecretGen is required.");
    Arguments.isTrue(pbeSecretGen.getKeySize() == 32,
        "pbeSecretGen.keySize must be 32 bytes when using ChaCha20-Poly1305.");
  }

  @Override
  public void destroy() throws DestroyingException {
    if (pbeSecretGen != null) {
      pbeSecretGen.destroy();
      pbeSecretGen = null;
    }
  }

  protected Cipher getImpl() throws GeneralSecurityException {
    if (provider == null) {
      return Cipher.getInstance("ChaCha20-Poly1305");
    }
    return Cipher.getInstance("ChaCha20-Poly1305", provider);
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

      var salt = new Out<byte[]>();
      key = pbeSecretGen.generate("ChaCha20-Poly1305", salt);

      var iv = CryptoUtils.randomBytes(IV_SIZE);
      impl.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

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

      Arguments.isTrue(message.length >= saltSize + IV_SIZE + TAG_SIZE, "message is invalid.");

      var iv = new byte[IV_SIZE];
      var salt = new byte[saltSize];
      ArrayUtils.copy(message, salt, iv);

      key = pbeSecretGen.generate("ChaCha20-Poly1305", salt);
      impl.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

      return impl.doFinal(message, saltSize + IV_SIZE, message.length - IV_SIZE - saltSize);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(key);
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public PbeChaCha20Poly1305Encryptor setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public PbeChaCha20Poly1305Encryptor setPbeSecretGen(PbeSecretGen pbeSecretGen) {
    assertNotInitialized();
    this.pbeSecretGen = pbeSecretGen;
    return this;
  }
}
