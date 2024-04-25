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
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PasswordDigester extends TextDigester {

  private int saltSize;
  private int iterationCount;
  private int keySize;

  private PbeSecretKeyFactory secretKeyFactoryUtil;

  final Random random = new SecureRandom();

  @Override
  protected void init() throws Exception {
    // baseEncoder
    this.baseEncoder = ValueUtils.valueOrAlt(this.baseEncoder, BaseEncoder.BASE64);

    this.keySize = ValueUtils.valueOrMin(this.keySize, 32);
    this.saltSize = ValueUtils.valueOrMin(this.saltSize, this.keySize);
    this.iterationCount = ValueUtils.valueOrMin(this.iterationCount, CryptoUtils.DEFAULT_ITERATION_COUNT);

    if (this.secretKeyFactoryUtil == null) {
      this.secretKeyFactoryUtil = new PbeSecretKeyFactory();
    }
  }

  @Override
  public String digest(String password) throws CryptoException {
    this.initialize();
    Asserts.notNull(password, "password is required.");

    byte[] salt = RandomUtils.nextBytes(this.saltSize, this.random);
    char[] pwdChars = password.toCharArray();
    try {
      byte[] secKey = this.secretKeyFactoryUtil.generate(pwdChars, salt, this.iterationCount, this.keySize);
      String digested = this.baseEncoder.encode(ArrayUtils.append(salt, secKey));

      CryptoUtils.clear(secKey);
      return digested;
    } finally {
      CryptoUtils.clear(pwdChars);
    }
  }

  @Override
  public boolean verify(String password, String digested) throws CryptoException {
    this.initialize();
    Asserts.notNull(password, "password is required.");
    Asserts.notNull(digested, "digested is required.");

    byte[] dg = this.baseEncoder.decode(digested);
    Asserts.isTrue(dg.length > this.saltSize, "digested is invalid.");

    byte[] salt = new byte[this.saltSize];
    byte[] secKey = new byte[dg.length - this.saltSize];
    ArrayUtils.copy(dg, salt, secKey);

    char[] pwdChars = password.toCharArray();
    try {
      byte[] computedSecKey = this.secretKeyFactoryUtil.generate(pwdChars, salt, this.iterationCount, this.keySize);
      boolean verified = Arrays.equals(computedSecKey, secKey);

      CryptoUtils.clear(secKey);
      CryptoUtils.clear(computedSecKey);

      return verified;
    } finally {
      CryptoUtils.clear(pwdChars);
    }
  }

  public PasswordDigester setSaltSize(int saltSize) {
    this.assertNotInitialized();
    this.saltSize = saltSize;
    return this;
  }

  public PasswordDigester setIterationCount(int iterationCount) {
    this.assertNotInitialized();
    this.iterationCount = iterationCount;
    return this;
  }

  public PasswordDigester setKeySize(int keySize) {
    this.assertNotInitialized();
    this.keySize = keySize;
    return this;
  }

  public PasswordDigester setPbeSecretKeyFactory(PbeSecretKeyFactory secretKeyFactoryUtil) {
    this.assertNotInitialized();
    this.secretKeyFactoryUtil = secretKeyFactoryUtil;
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
