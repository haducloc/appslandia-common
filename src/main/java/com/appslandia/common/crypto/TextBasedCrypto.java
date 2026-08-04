// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class TextBasedCrypto extends InitializingObject {

  protected Charset textCharset;
  protected BaseEncoder baseEncoder;

  @Override
  protected void init() throws Exception {
    textCharset = ValueUtils.valueOrAlt(textCharset, StandardCharsets.UTF_8);
    baseEncoder = ValueUtils.valueOrAlt(baseEncoder, BaseEncoder.BASE64);
  }

  public TextBasedCrypto setTextCharset(Charset charset) {
    assertNotInitialized();
    textCharset = charset;
    return this;
  }

  public TextBasedCrypto setTextCharset(String textCharset) {
    assertNotInitialized();
    if (textCharset != null) {
      this.textCharset = Charset.forName(textCharset);
    }
    return this;
  }

  public TextBasedCrypto setBaseEncoder(BaseEncoder baseEncoder) {
    assertNotInitialized();
    this.baseEncoder = baseEncoder;
    return this;
  }

  public TextBasedCrypto setBaseEncoder(String baseEncoder) {
    assertNotInitialized();
    if (baseEncoder != null) {
      this.baseEncoder = BaseEncoder.valueOf(baseEncoder);
    }
    return this;
  }
}
