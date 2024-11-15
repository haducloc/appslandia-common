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

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.SecureRand;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PasswordDigester extends TextDigester {

  protected String algorithm, provider;

  protected Integer saltSize;
  protected Integer iterationCount;
  protected Integer keySize;

  @Override
  protected void init() throws Exception {
    this.algorithm = ValueUtils.valueOrAlt(this.algorithm, CryptoUtils.DEFAULT_PBE_KEY_DERIVATION_ALGORITHM);
    this.baseEncoder = ValueUtils.valueOrAlt(this.baseEncoder, BaseEncoder.BASE64);

    this.saltSize = ValueUtils.valueOrAlt(this.saltSize, CryptoUtils.DEFAULT_PBE_SALT_SIZE);
    this.iterationCount = ValueUtils.valueOrAlt(this.iterationCount, CryptoUtils.DEFAULT_PBE_ITERATIONS);
    this.keySize = ValueUtils.valueOrAlt(this.keySize, CryptoUtils.DEFAULT_PBE_KEY_SIZE);
  }

  protected SecretKeyFactory getImpl() throws GeneralSecurityException {
    SecretKeyFactory impl = null;
    if (this.provider == null) {
      impl = SecretKeyFactory.getInstance(this.algorithm);
    } else {
      impl = SecretKeyFactory.getInstance(this.algorithm, this.provider);
    }
    return impl;
  }

  @Override
  public String digest(String password) throws CryptoException {
    this.initialize();
    Asserts.notNull(password, "password is required.");

    byte[] salt = RandomUtils.nextBytes(this.saltSize, SecureRand.getInstance());
    char[] pwdChars = password.toCharArray();

    try {
      PBEKeySpec keySpec = new PBEKeySpec(pwdChars, salt, this.iterationCount, this.keySize * 8);
      SecretKey key = this.getImpl().generateSecret(keySpec);
      keySpec.clearPassword();

      byte[] storedHash = key.getEncoded();
      CryptoUtils.destroy(key);
      return this.baseEncoder.encode(ArrayUtils.append(salt, storedHash));

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex.getMessage(), ex);
    } finally {
      CryptoUtils.clear(pwdChars);
    }
  }

  @Override
  public boolean verify(String password, String digested) throws CryptoException {
    this.initialize();
    Asserts.notNull(password, "password is required.");
    Asserts.notNull(digested, "digested is required.");

    byte[] digestedBytes = this.baseEncoder.decode(digested);
    Asserts.isTrue(digestedBytes.length > this.saltSize, "digested is invalid.");

    byte[] salt = new byte[this.saltSize];
    byte[] storedHash = new byte[digestedBytes.length - this.saltSize];
    ArrayUtils.copy(digestedBytes, salt, storedHash);

    char[] pwdChars = password.toCharArray();
    try {
      PBEKeySpec keySpec = new PBEKeySpec(pwdChars, salt, this.iterationCount, this.keySize * 8);
      SecretKey key = this.getImpl().generateSecret(keySpec);
      keySpec.clearPassword();

      byte[] computedHash = key.getEncoded();
      CryptoUtils.destroy(key);
      return MessageDigest.isEqual(storedHash, computedHash);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex.getMessage(), ex);
    } finally {
      CryptoUtils.clear(pwdChars);
    }
  }

  public PasswordDigester setSaltSize(Integer saltSize) {
    this.assertNotInitialized();
    this.saltSize = saltSize;
    return this;
  }

  public PasswordDigester setIterationCount(Integer iterationCount) {
    this.assertNotInitialized();
    this.iterationCount = iterationCount;
    return this;
  }

  public PasswordDigester setKeySize(Integer keySize) {
    this.assertNotInitialized();
    this.keySize = keySize;
    return this;
  }

  @Override
  public PasswordDigester setDigester(Digester digester) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PasswordDigester setTextCharset(Charset charset) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PasswordDigester setTextCharset(String textCharset) {
    throw new UnsupportedOperationException();
  }
}
