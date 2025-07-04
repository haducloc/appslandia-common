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

/**
 *
 * @author Loc Ha
 *
 */
public class BOMTest {

  @Test
  public void test_parse_UTF8() {
    var bom = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0 };
    var b = BOM.parse(bom, 3);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_8, b);
  }

  @Test
  public void test_parse_UTF_16BE() {
    var bom = new byte[] { (byte) 0xFE, (byte) 0xFF, 0, 0 };
    var b = BOM.parse(bom, 2);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_16BE, b);
  }

  @Test
  public void test_parse_UTF_16LE() {
    var bom = new byte[] { (byte) 0xFF, (byte) 0xFE, 0, 0 };
    var b = BOM.parse(bom, 2);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_16LE, b);
  }

  @Test
  public void test_parse_UTF_32BE() {
    var bom = new byte[] { 0, 0, (byte) 0xFE, (byte) 0xFF };
    var b = BOM.parse(bom, 4);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_32BE, b);
  }

  @Test
  public void test_parse_UTF_32LE() {
    var bom = new byte[] { (byte) 0xFF, (byte) 0xFE, 0, 0 };
    var b = BOM.parse(bom, 4);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_32LE, b);
  }

  @Test
  public void test_others() {
    var bom = new byte[] { (byte) 0xFF, (byte) 0xFE, 0, 0 };
    var b = BOM.parse(bom, 2);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_16LE, b);

    b = BOM.parse(bom, 4);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_32LE, b);
  }
}
