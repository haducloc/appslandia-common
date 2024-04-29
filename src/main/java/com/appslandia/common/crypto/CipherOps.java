// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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

import java.util.Locale;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CipherOps {

  final String algorithm;
  final String mode;
  final String padding;

  public CipherOps(String transformation) {
    Asserts.notNull(transformation);

    String[] cipherOps = transformation.split("/");
    Asserts.isTrue(cipherOps.length == 3,
        "The transformation is invalid. The algorithm, mode, and padding must be explicitly provided.");

    cipherOps[0] = cipherOps[0].toUpperCase(Locale.ENGLISH);
    cipherOps[1] = cipherOps[1].toUpperCase(Locale.ENGLISH);

    this.algorithm = cipherOps[0];
    this.mode = cipherOps[1];
    this.padding = cipherOps[2];
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
    return STR.fmt("{}/{}/{}", this.algorithm, this.mode, this.padding);
  }
}
