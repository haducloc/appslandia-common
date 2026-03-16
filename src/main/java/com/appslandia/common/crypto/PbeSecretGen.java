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
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.DestroyingSupport;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.SYS;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class PbeSecretGen extends InitializingObject implements DestroyingSupport {
  protected String algorithm, provider;

  protected char[] password;
  protected Integer saltSize;
  protected Integer iterationCount;
  protected Integer keySize;

  public PbeSecretGen() {
  }

  public PbeSecretGen(String algorithm) {
    this.algorithm = algorithm;
  }

  public PbeSecretGen(String algorithm, String provider) {
    this.algorithm = algorithm;
    this.provider = provider;
  }

  @Override
  protected void init() throws Exception {
    Arguments.notNull(password, "password is required.");

    algorithm = ValueUtils.valueOrAlt(algorithm, "PBKDF2WithHmacSHA256");
    saltSize = ValueUtils.valueOrAlt(saltSize, 16);
    iterationCount = ValueUtils.valueOrAlt(iterationCount, 100_000);
    keySize = ValueUtils.valueOrAlt(keySize, 32);
  }

  @Override
  public void destroy() throws DestroyingException {
    CryptoUtils.clear(password);
  }

  protected SecretKeyFactory getImpl() throws GeneralSecurityException {
    SecretKeyFactory impl = null;
    if (provider == null) {
      impl = SecretKeyFactory.getInstance(algorithm);
    } else {
      impl = SecretKeyFactory.getInstance(algorithm, provider);
    }
    return impl;
  }

  protected void release(SecretKeyFactory impl) {
  }

  public SecretKey generate(String algorithm, Out<byte[]> genSalt) throws CryptoException {
    initialize();
    genSalt.value = CryptoUtils.randomBytes(saltSize);
    return generate(algorithm, genSalt.value);
  }

  public SecretKey generate(String algorithm, byte[] salt) throws CryptoException {
    initialize();

    var keySpec = new PBEKeySpec(password, salt, iterationCount, keySize * 8);
    SecretKeyFactory impl = null;
    try {
      impl = getImpl();
      var secret = impl.generateSecret(keySpec);
      var kBytes = secret.getEncoded();
      CryptoUtils.destroy(secret);

      SecretKey key = new DSecretKeySpec(kBytes, algorithm);
      CryptoUtils.clear(kBytes);
      return key;

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      keySpec.clearPassword();
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getAlgorithm() {
    initialize();
    return algorithm;
  }

  public PbeSecretGen setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public PbeSecretGen setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public int getSaltSize() {
    initialize();
    return saltSize;
  }

  public PbeSecretGen setSaltSize(Integer saltSize) {
    assertNotInitialized();
    this.saltSize = saltSize;
    return this;
  }

  public int getIterationCount() {
    initialize();
    return iterationCount;
  }

  public PbeSecretGen setIterationCount(Integer iterationCount) {
    assertNotInitialized();
    this.iterationCount = iterationCount;
    return this;
  }

  public int getKeySize() {
    initialize();
    return keySize;
  }

  public PbeSecretGen setKeySize(Integer keySize) {
    assertNotInitialized();
    this.keySize = keySize;
    return this;
  }

  public PbeSecretGen setPassword(char[] password) {
    assertNotInitialized();
    if (password != null) {
      this.password = Arrays.copyOf(password, password.length);
    }
    return this;
  }

  public PbeSecretGen setPassword(String passwordExpr) {
    assertNotInitialized();

    if (passwordExpr != null) {
      var resolvedValue = SYS.resolve(passwordExpr);

      if (resolvedValue == null) {
        throw new IllegalArgumentException("Failed to resolve expression: " + passwordExpr);
      }
      password = resolvedValue.toCharArray();
    }
    return this;
  }
}
