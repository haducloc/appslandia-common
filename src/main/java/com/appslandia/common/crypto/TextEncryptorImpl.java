// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.nio.charset.Charset;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class TextEncryptorImpl extends TextBasedCrypto implements TextEncryptor {
  protected Encryptor encryptor;

  public TextEncryptorImpl() {
  }

  public TextEncryptorImpl(Encryptor encryptor) {
    this.encryptor = encryptor;
  }

  @Override
  protected void init() throws Exception {
    super.init();

    Arguments.notNull(encryptor, "encryptor is required.");
  }

  @Override
  public void destroy() throws DestroyingException {
    if (encryptor != null) {
      encryptor.destroy();
    }
  }

  @Override
  public String encrypt(String message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");

    return baseEncoder.encode(encryptor.encrypt(message.getBytes(textCharset)));
  }

  @Override
  public String decrypt(String message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");

    return new String(encryptor.decrypt(baseEncoder.decode(message)), textCharset);
  }

  public TextEncryptorImpl setEncryptor(Encryptor encryptor) {
    assertNotInitialized();
    this.encryptor = encryptor;
    return this;
  }

  @Override
  public TextEncryptorImpl setTextCharset(Charset charset) {
    super.setTextCharset(charset);
    return this;
  }

  @Override
  public TextEncryptorImpl setTextCharset(String textCharset) {
    super.setTextCharset(textCharset);
    return this;
  }

  @Override
  public TextEncryptorImpl setBaseEncoder(BaseEncoder baseEncoder) {
    super.setBaseEncoder(baseEncoder);
    return this;
  }
}
