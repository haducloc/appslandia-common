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

    algorithm = cipherOps[0];
    mode = (cipherOps.length >= 2) ? cipherOps[1] : null;
    padding = (cipherOps.length == 3) ? cipherOps[2] : null;
  }

  public boolean isPadding(String padding) {
    if (this.padding == null) {
      return padding == null || "NoPadding".equalsIgnoreCase(padding);
    }
    return this.padding.equalsIgnoreCase(padding);
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public String getMode() {
    return mode;
  }

  public String getPadding() {
    return padding;
  }

  @Override
  public String toString() {
    if (padding != null) {
      return STR.fmt("{}/{}/{}", algorithm, mode, padding);
    }
    if (mode != null) {
      return STR.fmt("{}/{}", algorithm, mode);
    }
    return algorithm;
  }
}
