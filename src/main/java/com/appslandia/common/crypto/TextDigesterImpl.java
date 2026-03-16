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
