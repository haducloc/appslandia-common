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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.SimpleConfig;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class SecureConfig extends SimpleConfig {

  protected final TextEncryptor textEncryptor;

  public SecureConfig(char[] password) {
    this(password, new HashMap<>());
  }

  public SecureConfig(char[] password, Map<String, String> newCfg) {
    super(newCfg);
    Arguments.notNull(password);
    this.textEncryptor = new TextEncryptor(new PbeAesEncryptor().setTransformation("AES/GCM/NoPadding")
        .setPbeSecretGen(new PbeSecretGen().setPassword(password).setKeySize(32)));
  }

  public SecureConfig(String passwordExpr) {
    this(passwordExpr, new HashMap<>());
  }

  public SecureConfig(String passwordExpr, Map<String, String> newCfg) {
    super(newCfg);
    Arguments.notNull(passwordExpr);
    this.textEncryptor = new TextEncryptor(new PbeAesEncryptor().setTransformation("AES/GCM/NoPadding")
        .setPbeSecretGen(new PbeSecretGen().setPassword(passwordExpr).setKeySize(32)));
  }

  public void destroy() throws DestroyException {
    this.textEncryptor.destroy();
  }

  @Override
  public String getString(String key) {
    var value = super.getString(key);

    if (value == null) {
      return null;
    }
    if (!CryptoUtils.isEncValue(value)) {
      return value;
    }
    return this.textEncryptor.decrypt(CryptoUtils.parseEncValue(value));
  }

  public SecureConfig sets(String key, String value) throws CryptoException {
    Arguments.notNull(key);
    Arguments.notNull(value);
    value = value.strip();

    this.cfg.put(key, CryptoUtils.markEncValue(this.textEncryptor.encrypt(value)));
    return this;
  }

  public SecureConfig sets(String key, boolean value) throws CryptoException {
    Arguments.notNull(key);
    return sets(key, Boolean.toString(value));
  }

  public SecureConfig sets(String key, int value) throws CryptoException {
    Arguments.notNull(key);
    return sets(key, Integer.toString(value));
  }

  public SecureConfig sets(String key, long value) throws CryptoException {
    Arguments.notNull(key);
    return sets(key, Long.toString(value));
  }

  public SecureConfig sets(String key, double value) throws CryptoException {
    Arguments.notNull(key);
    return sets(key, Double.toString(value));
  }

  public SecureConfig sets(String key, BigDecimal value) throws CryptoException {
    Arguments.notNull(key);
    Arguments.notNull(value);

    return sets(key, value.toPlainString());
  }
}
