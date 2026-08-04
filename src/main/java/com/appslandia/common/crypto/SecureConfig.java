// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.DestroyingSupport;
import com.appslandia.common.base.SimpleConfig;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class SecureConfig extends SimpleConfig implements DestroyingSupport {

  protected final TextEncryptor textEncryptor;

  public SecureConfig(TextEncryptor textEncryptor) {
    this(textEncryptor, new LinkedHashMap<>());
  }

  public SecureConfig(TextEncryptor textEncryptor, Map<String, String> newCfg) {
    super(newCfg);
    this.textEncryptor = textEncryptor;
  }

  @Override
  public void destroy() throws DestroyingException {
    textEncryptor.destroy();
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
    return textEncryptor.decrypt(CryptoUtils.parseEncValue(value));
  }

  public SecureConfig sets(String key, String value) throws CryptoException {
    Arguments.notNull(key);
    Arguments.notNull(value);
    value = value.strip();

    cfg.put(key, CryptoUtils.markEncValue(textEncryptor.encrypt(value)));
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
