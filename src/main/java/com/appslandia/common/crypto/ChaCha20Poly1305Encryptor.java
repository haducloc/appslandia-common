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

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.SYS;

/**
 *
 * @author Loc Ha
 *
 */
public class ChaCha20Poly1305Encryptor extends InitializingObject implements Encryptor {

  protected static final int IV_SIZE = 12;
  protected static final int TAG_SIZE = 16;

  protected String provider;
  protected SecretKey secretKey;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(secretKey, "secretKey is required.");
  }

  @Override
  public void destroy() throws DestroyingException {
    CryptoUtils.destroy(secretKey);
  }

  protected Cipher getImpl() throws GeneralSecurityException {
    Cipher impl = null;
    if (provider == null) {
      impl = Cipher.getInstance("ChaCha20-Poly1305");
    } else {
      impl = Cipher.getInstance("ChaCha20-Poly1305", provider);
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
    try {
      impl = getImpl();
      var iv = CryptoUtils.randomBytes(IV_SIZE);

      impl.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
      var storedMsg = impl.doFinal(message);

      return ArrayUtils.append(iv, storedMsg);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  @Override
  public byte[] decrypt(byte[] message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.isTrue(message.length >= IV_SIZE + TAG_SIZE, "message is invalid.");

    Cipher impl = null;
    try {
      impl = getImpl();
      var iv = new byte[IV_SIZE];
      ArrayUtils.copy(message, iv);

      impl.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
      return impl.doFinal(message, IV_SIZE, message.length - IV_SIZE);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public ChaCha20Poly1305Encryptor setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public ChaCha20Poly1305Encryptor setSecret(byte[] secret) {
    assertNotInitialized();
    if (secret != null) {
      secretKey = new DSecretKeySpec(secret, "ChaCha20-Poly1305");
    }
    return this;
  }

  public ChaCha20Poly1305Encryptor setSecret(String secretExpr) {
    assertNotInitialized();

    if (secretExpr != null) {
      var resolvedValue = SYS.resolve(secretExpr);

      if (resolvedValue == null) {
        throw new IllegalArgumentException("Failed to resolve expression: " + secretExpr);
      }
      setSecret(resolvedValue.getBytes(StandardCharsets.UTF_8));
    }
    return this;
  }

  public ChaCha20Poly1305Encryptor setSecretKey(SecretKey secretKey) {
    assertNotInitialized();
    if (secretKey != null) {

      Arguments.isTrue("ChaCha20-Poly1305".equalsIgnoreCase(secretKey.getAlgorithm()));
      this.secretKey = CryptoUtils.copy(secretKey);
    }
    return this;
  }
}
