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
import java.security.MessageDigest;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
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

  @Override
  public String digest(String password) throws CryptoException {
    this.initialize();
    Asserts.notNull(password, "password is required.");

    Out<byte[]> salt = new Out<>();
    char[] pwdChars = password.toCharArray();
    try {
      byte[] storedHash = new PbeSecretKeyGenerator(this.algorithm, this.provider).setPassword(pwdChars)
          .setSaltSize(this.saltSize).setIterationCount(this.iterationCount).setKeySize(this.keySize).generate(salt);

      return this.baseEncoder.encode(ArrayUtils.append(salt.value, storedHash));

    } finally {
      CryptoUtils.clear(pwdChars);
    }
  }

  @Override
  public boolean verify(String password, String digested) throws CryptoException {
    this.initialize();
    Asserts.notNull(password, "password is required.");
    Asserts.notNull(digested, "digested is required.");

    byte[] dBytes = this.baseEncoder.decode(digested);
    Asserts.isTrue(dBytes.length > this.saltSize, "digested is invalid.");

    byte[] salt = new byte[this.saltSize];
    byte[] storedHash = new byte[dBytes.length - this.saltSize];
    ArrayUtils.copy(dBytes, salt, storedHash);

    char[] pwdChars = password.toCharArray();
    try {
      byte[] computedHash = new PbeSecretKeyGenerator(this.algorithm, this.provider).setPassword(pwdChars)
          .setSaltSize(this.saltSize).setIterationCount(this.iterationCount).setKeySize(this.keySize).generate(salt);

      return MessageDigest.isEqual(computedHash, storedHash);
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
