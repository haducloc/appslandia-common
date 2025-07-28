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
public class TokenGenerator extends InitializeObject implements TextGenerator {

  private static final Pattern BASE64_URL_NP_PATTERN = Pattern.compile("[a-zA-Z\\d-_]+");

  private int length = 32;

  public TokenGenerator() {
  }

  public TokenGenerator(int length) {
    this.length = length;
  }

  @Override
  protected void init() throws Exception {
    Arguments.isTrue(this.length > 0, "length is required.");
  }

  @Override
  public String generate() {
    initialize();
    var len = MathUtils.toNearestMultipleOf(4, this.length);
    var rBytes = CryptoUtils.randomBytes((len * 3) / 4);

    var base64 = BaseEncoder.BASE64_URL_NP.encode(rBytes);
    return (base64.length() == this.length) ? base64 : base64.substring(0, this.length);
  }

  @Override
  public boolean verify(String value) {
    initialize();
    Arguments.notNull(value);
    if (value.length() != this.length) {
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
