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
import java.nio.charset.StandardCharsets;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.DestroyException;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class TextDigester extends TextBasedCrypto {
  protected Digester digester;

  public TextDigester() {
  }

  public TextDigester(Digester digester) {
    this.digester = digester;
  }

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.digester, "digester is required.");

    this.textCharset = ValueUtils.valueOrAlt(this.textCharset, StandardCharsets.UTF_8);
    this.baseEncoder = ValueUtils.valueOrAlt(this.baseEncoder, BaseEncoder.BASE64);
  }

  @Override
  public void destroy() throws DestroyException {
    if (this.digester != null) {
      this.digester.destroy();
    }
  }

  public String digest(String message) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");

    return this.baseEncoder.encode(this.digester.digest(message.getBytes(this.textCharset)));
  }

  public boolean verify(String message, String digested) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(digested, "digested is required.");

    return this.digester.verify(message.getBytes(this.textCharset), this.baseEncoder.decode(digested));
  }

  public TextDigester setDigester(Digester digester) {
    this.assertNotInitialized();
    this.digester = digester;
    return this;
  }

  @Override
  public TextDigester setTextCharset(Charset charset) {
    super.setTextCharset(charset);
    return this;
  }

  @Override
  public TextDigester setTextCharset(String textCharset) {
    super.setTextCharset(textCharset);
    return this;
  }

  @Override
  public TextDigester setBaseEncoder(BaseEncoder baseEncoder) {
    super.setBaseEncoder(baseEncoder);
    return this;
  }
}
