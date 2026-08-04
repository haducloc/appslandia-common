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
public class TextDigesterImpl extends TextBasedCrypto implements TextDigester {
  protected Digester digester;

  public TextDigesterImpl() {
  }

  public TextDigesterImpl(Digester digester) {
    this.digester = digester;
  }

  public TextDigesterImpl(String algorithm) {
    this(new DigesterImpl(algorithm));
  }

  public TextDigesterImpl(String algorithm, String provider) {
    this(new DigesterImpl(algorithm, provider));
  }

  @Override
  protected void init() throws Exception {
    super.init();

    Arguments.notNull(digester, "digester is required.");
  }

  @Override
  public void destroy() throws DestroyingException {
    if (digester != null) {
      digester.destroy();
    }
  }

  @Override
  public String digest(String message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");

    return baseEncoder.encode(digester.digest(message.getBytes(textCharset)));
  }

  @Override
  public boolean verify(String message, String digested) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(digested, "digested is required.");

    return digester.verify(message.getBytes(textCharset), baseEncoder.decode(digested));
  }

  public TextDigesterImpl setDigester(Digester digester) {
    assertNotInitialized();
    this.digester = digester;
    return this;
  }

  @Override
  public TextDigesterImpl setTextCharset(Charset charset) {
    super.setTextCharset(charset);
    return this;
  }

  @Override
  public TextDigesterImpl setTextCharset(String textCharset) {
    super.setTextCharset(textCharset);
    return this;
  }

  @Override
  public TextDigesterImpl setBaseEncoder(BaseEncoder baseEncoder) {
    super.setBaseEncoder(baseEncoder);
    return this;
  }
}
