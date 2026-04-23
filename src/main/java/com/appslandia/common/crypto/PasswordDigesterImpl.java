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
import java.security.MessageDigest;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class PasswordDigesterImpl extends InitializingObject implements PasswordDigester {

  protected String algorithm, provider;

  protected Integer saltSize;
  protected Integer iterationCount;
  protected Integer keySize;

  @Override
  protected void init() throws Exception {
    algorithm = ValueUtils.valueOrAlt(algorithm, "PBKDF2WithHmacSHA256");
    saltSize = ValueUtils.valueOrAlt(saltSize, 16);
    iterationCount = ValueUtils.valueOrAlt(iterationCount, 100_000);
    keySize = ValueUtils.valueOrAlt(keySize, 32);
  }

  @Override
  public void destroy() throws DestroyingException {
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

  @Override
  public String digest(String password) throws CryptoException {
    initialize();
    Arguments.notNull(password, "password is required.");

    var salt = CryptoUtils.randomBytes(saltSize);
    var pwdChars = password.toCharArray();
    var keySpec = new PBEKeySpec(pwdChars, salt, iterationCount, keySize * 8);

    SecretKeyFactory impl = null;
    try {
      impl = getImpl();
      var secret = impl.generateSecret(keySpec);
      var storedHash = secret.getEncoded();
      CryptoUtils.destroy(secret);

      return BaseEncoder.BASE64.encode(ArrayUtils.append(salt, storedHash));

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex.getMessage(), ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
      keySpec.clearPassword();
      CryptoUtils.clear(pwdChars);
    }
  }

  @Override
  public boolean verify(String password, String digested) throws CryptoException {
    initialize();
    Arguments.notNull(password, "password is required.");
    Arguments.notNull(digested, "digested is required.");

    var dBytes = BaseEncoder.BASE64.decode(digested);
    Arguments.isTrue(dBytes.length > saltSize, "digested is invalid.");

    var salt = new byte[saltSize];
    var storedHash = new byte[dBytes.length - saltSize];
    ArrayUtils.copy(dBytes, salt, storedHash);

    var pwdChars = password.toCharArray();
    var keySpec = new PBEKeySpec(pwdChars, salt, iterationCount, keySize * 8);

    SecretKeyFactory impl = null;
    try {
      impl = getImpl();
      var key = impl.generateSecret(keySpec);
      keySpec.clearPassword();

      var computedHash = key.getEncoded();
      CryptoUtils.destroy(key);
      return MessageDigest.isEqual(storedHash, computedHash);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex.getMessage(), ex);

    } finally {
      if (impl != null) {
        release(impl);
      }
      keySpec.clearPassword();
      CryptoUtils.clear(pwdChars);
    }
  }

  public PasswordDigesterImpl setSaltSize(Integer saltSize) {
    assertNotInitialized();
    this.saltSize = saltSize;
    return this;
  }

  public PasswordDigesterImpl setIterationCount(Integer iterationCount) {
    assertNotInitialized();
    this.iterationCount = iterationCount;
    return this;
  }

  public PasswordDigesterImpl setKeySize(Integer keySize) {
    assertNotInitialized();
    this.keySize = keySize;
    return this;
  }
}
