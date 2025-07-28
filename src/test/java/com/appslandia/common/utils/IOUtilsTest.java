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

package com.appslandia.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class IOUtilsTest {

  @Test
  public void test_copyFromIsToOs() {
    var src = RandomUtils.nextBytes(100, new Random());
    var is = new ByteArrayInputStream(src);
    var os = new ByteArrayOutputStream();
    try {
      IOUtils.copy(is, os);
      Assertions.assertArrayEquals(src, os.toByteArray());
    } catch (IOException ex) {
    }
  }

  @Test
  public void test_toByteArray() {
    var src = RandomUtils.nextBytes(100, new Random());
    var is = new ByteArrayInputStream(src);
    try {
      var ba = IOUtils.toByteArray(is);
      Assertions.assertArrayEquals(src, ba);
    } catch (IOException ex) {
    }
  }

  @Test
  public void test_copyWithLength() {
    var src = MathUtils.toByteArray(1, 100);
    var os = new ByteArrayOutputStream();
    try {
      IOUtils.copy(src, 50, os);
      Assertions.assertArrayEquals(MathUtils.toByteArray(1, 50), os.toByteArray());
    } catch (IOException ex) {
    }
  }
}
