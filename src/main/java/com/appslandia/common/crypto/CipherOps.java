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

import java.util.Arrays;
import java.util.regex.Pattern;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class CipherOps {

  final String algorithm;
  final String mode;
  final String padding;

  public CipherOps(String transformation) {
    Arguments.notNull(transformation);

    var cipherOps = transformation.split("/");
    Arguments.isTrue(cipherOps.length >= 1 && cipherOps.length <= 3, "transformation is invalid.");

    this.algorithm = cipherOps[0];
    this.mode = (cipherOps.length >= 2) ? cipherOps[1] : null;
    this.padding = (cipherOps.length == 3) ? cipherOps[2] : null;
  }

  public boolean isMode(String... modes) {
    return Arrays.stream(modes).anyMatch(m -> m.equalsIgnoreCase(this.mode)
        || Pattern.compile(m, Pattern.CASE_INSENSITIVE).matcher(this.mode).matches());
  }

  public boolean isAlgorithm(String algorithm) {
    return this.algorithm.equalsIgnoreCase(algorithm);
  }

  public String getAlgorithm() {
    return this.algorithm;
  }

  public String getMode() {
    return this.mode;
  }

  public String getPadding() {
    return this.padding;
  }

  @Override
  public String toString() {
    if (this.padding != null) {
      return STR.fmt("{}/{}/{}", this.algorithm, this.mode, this.padding);
    }
    if (this.mode != null) {
      return STR.fmt("{}/{}", this.algorithm, this.mode);
    }
    return this.algorithm;
  }
}
