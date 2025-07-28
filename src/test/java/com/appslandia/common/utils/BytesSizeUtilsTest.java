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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class BytesSizeUtilsTest {

  @Test
  public void test_translateToBytes() {
    var size = BytesSizeUtils.translateToBytes("1GiB 2MiB 3KiB 4B");
    Assertions.assertEquals(1073741824 + 2 * 1_048_576 + 3 * 1024 + 4, size);

    size = BytesSizeUtils.translateToBytes("1.5GiB 4B");
    Assertions.assertEquals((long) (1.5 * 1073741824) + 4, size);

    size = BytesSizeUtils.translateToBytes("1gib 2mib");
    Assertions.assertEquals(1073741824 + 2 * 1_048_576, size);
  }

  @Test
  public void test_translateToBytes_Base10() {
    var size = BytesSizeUtils.translateToBytes("1GB 2MB 3KB 4B");
    Assertions.assertEquals(1000_000_000 + 2 * 1000_000 + 3 * 1000 + 4, size);

    size = BytesSizeUtils.translateToBytes("1.5GB 4B");
    Assertions.assertEquals((long) (1.5 * 1000_000_000L) + 4, size);

    size = BytesSizeUtils.translateToBytes("1gb 2mb");
    Assertions.assertEquals(1000_000_000 + 2 * 1000_000, size);
  }

  @Test
  public void test_translateToBytes_invalid() {
    try {
      BytesSizeUtils.translateToBytes("1GB 2M");
      Assertions.fail();
    } catch (Exception ex) {
    }
    try {
      BytesSizeUtils.translateToBytes("1GB+2M");
      Assertions.fail();
    } catch (Exception ex) {
    }
    try {
      BytesSizeUtils.translateToBytes("1 GB 2M");
      Assertions.fail();
    } catch (Exception ex) {
    }
  }
}
