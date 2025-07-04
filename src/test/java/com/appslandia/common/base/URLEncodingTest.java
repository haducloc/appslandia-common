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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.crypto.PasswordUtil;
import com.appslandia.common.utils.URLEncoding;

/**
 *
 * @author Loc Ha
 *
 */
public class URLEncodingTest {

  @Test
  public void test_encodeParam() {
    var s = new String(PasswordUtil.generatePassword(512, 1024));

    var enc = URLEncoding.encodeParam(s);
    var dec = URLEncoding.decodeParam(enc);

    Assertions.assertEquals(s, dec);
  }

  @Test
  public void test_encodeParam_spaceToPlus() {
    var s = " ";

    var enc = URLEncoding.encodeParam(s);
    var dec = URLEncoding.decodeParam(enc);

    Assertions.assertEquals("+", enc);
    Assertions.assertEquals(" ", dec);
  }

  @Test
  public void test_encodeParam_escSpace() {
    var s = " ";

    var enc = URLEncoding.encodeParam(s, false);
    var dec = URLEncoding.decodeParam(enc);

    Assertions.assertEquals("%20", enc);
    Assertions.assertEquals(" ", dec);
  }

  @Test
  public void test_encodePath() {
    var s = new String(PasswordUtil.generatePassword(512, 1024));

    var enc = URLEncoding.encodePath(s);
    var dec = URLEncoding.decodePath(enc);

    Assertions.assertEquals(s, dec);
  }
}
