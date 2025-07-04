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
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.SYS;

/**
 *
 * @author Loc Ha
 *
 */
public class AesEncryptor extends InitializeObject implements Encryptor {

  protected String transformation, provider;
  protected SecretKey secretKey;

  protected GcmSpec gcmSpec;
  protected CipherOps cipherOps;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.transformation, "transformation is required.");
    Arguments.notNull(this.secretKey, "secretKey is required.");

    var cipherOps = this.cipherOps;
    Arguments.isTrue(cipherOps.isAlgorithm("AES"), "AES algorithm is required.");
    Arguments.isTrue(cipherOps.isMode("CBC", "^CFB\\d*$", "CTR", "^OFB\\d*$", "ECB", "GCM"),
        "CBC|CFB|CTR|OFB|ECB|GCM mode is required.");

    if (cipherOps.isMode("GCM")) {
      this.gcmSpec = new GcmSpec();
    }
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

  protected void release(Cipher impl) {
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
    Arguments.notNull(message, "message is required.");

    Cipher impl = null;
    try {
      impl = getImpl();
      var ivSize = getIvSize(impl);
      byte[] iv = null;

      if (ivSize <= 0) {
        impl.init(Cipher.ENCRYPT_MODE, this.secretKey);
      } else {
        iv = CryptoUtils.randomBytes(ivSize);

        if (this.cipherOps.isMode("GCM")) {
          impl.init(Cipher.ENCRYPT_MODE, this.secretKey, new GCMParameterSpec(this.gcmSpec.getTagSize() * 8, iv));
        } else {
          impl.init(Cipher.ENCRYPT_MODE, this.secretKey, new IvParameterSpec(iv));
        }
      }

      var storedMsg = impl.doFinal(message);
      if (iv == null) {
        return storedMsg;
      } else {
        return ArrayUtils.append(iv, storedMsg);
      }
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
    this.initialize();
    Arguments.notNull(message, "message is required.");

    Cipher impl = null;
    try {
      impl = getImpl();
      var ivSize = getIvSize(impl);
      byte[] iv = null;

      if (ivSize > 0) {
        Arguments.isTrue(message.length >= ivSize, "message is invalid.");
        iv = new byte[ivSize];
        ArrayUtils.copy(message, iv);
      }

      if (iv == null) {
        impl.init(Cipher.DECRYPT_MODE, this.secretKey);
      } else if (this.cipherOps.isMode("GCM")) {
        impl.init(Cipher.DECRYPT_MODE, this.secretKey, new GCMParameterSpec(this.gcmSpec.getTagSize() * 8, iv));
      } else {
        impl.init(Cipher.DECRYPT_MODE, this.secretKey, new IvParameterSpec(iv));
      }

      if (iv == null) {
        return impl.doFinal(message);
      } else {
        return impl.doFinal(message, ivSize, message.length - ivSize);
      }
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getTransformation() {
    this.initialize();
    return this.transformation;
  }

  public AesEncryptor setTransformation(String transformation) {
    this.assertNotInitialized();

    if (transformation != null) {
      this.cipherOps = new CipherOps(transformation);
      this.transformation = transformation;
    }
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

  /**
   * Please make sure the transformation is set before setting the secret.
   *
   * @param secret
   * @return
   */
  public AesEncryptor setSecret(byte[] secret) {
    this.assertNotInitialized();
    if (secret != null) {
      Asserts.notNull(this.cipherOps);
      this.secretKey = new DSecretKeySpec(secret, this.cipherOps.getAlgorithm());
    }
    return this;
  }

  /**
   * Please make sure the transformation is set before setting the secret expression.
   *
   * @param secretExpr
   * @return
   */
  public AesEncryptor setSecret(String secretExpr) {
    this.assertNotInitialized();

    if (secretExpr != null) {
      var resolvedValue = SYS.resolve(secretExpr);

      if (resolvedValue == null) {
        throw new IllegalArgumentException("Failed to resolve expression: " + secretExpr);
      }
      setSecret(resolvedValue.getBytes(StandardCharsets.UTF_8));
    }
    return this;
  }

  /**
   * Please make sure the transformation is set before setting the secret key.
   *
   * @param secretKey
   * @return
   */
  public AesEncryptor setSecretKey(SecretKey secretKey) {
    this.assertNotInitialized();
    if (secretKey != null) {
      Asserts.notNull(this.cipherOps);
      Arguments.isTrue(this.cipherOps.isAlgorithm(secretKey.getAlgorithm()));

      this.secretKey = CryptoUtils.copy(secretKey);
    }
    return this;
  }
}
