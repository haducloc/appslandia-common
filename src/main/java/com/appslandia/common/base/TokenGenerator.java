// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.regex.Pattern;

import com.appslandia.common.crypto.CryptoUtils;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.MathUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class TokenGenerator extends InitializingObject implements TextGenerator {

  private static final Pattern BASE64_URL_NP_PATTERN = Pattern.compile("[a-zA-Z\\d-_]+");

  private int length = 32;

  public TokenGenerator() {
  }

  public TokenGenerator(int length) {
    this.length = length;
  }

  @Override
  protected void init() throws Exception {
    Arguments.isTrue(length > 0, "length is required.");
  }

  @Override
  public String generate() {
    initialize();
    var len = MathUtils.toNearestMultipleOf(4, length);
    var rBytes = CryptoUtils.randomBytes((len * 3) / 4);

    var base64 = BaseEncoder.BASE64_URL_NP.encode(rBytes);
    return (base64.length() == length) ? base64 : base64.substring(0, length);
  }

  @Override
  public boolean verify(String value) {
    initialize();
    Arguments.notNull(value);
    if (value.length() != length) {
      return false;
    }
    return BASE64_URL_NP_PATTERN.matcher(value).matches();
  }

  public TokenGenerator setLength(int length) {
    assertNotInitialized();
    this.length = length;
    return this;
  }
}
