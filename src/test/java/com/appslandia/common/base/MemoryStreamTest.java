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

package com.appslandia.common.base;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.JavaSerUtils;
import com.appslandia.common.utils.MathUtils;
import com.appslandia.common.utils.RandomUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MemoryStreamTest {

  @Test
  public void test() {
    try {
      MemoryStream out = new MemoryStream(4);

      out.write('a');
      out.write("csde".getBytes());
      out.write("g_hk".getBytes(), 1, 3);
      out.flush();

      Assertions.assertEquals(8, out.size());
      Assertions.assertEquals("acsde_hk", out.toString(StandardCharsets.UTF_8));

      out.close();
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_equals() {
    try {
      MemoryStream out = new MemoryStream(4);

      out.write(1);
      Assertions.assertArrayEquals(MathUtils.toByteArray(1, 1), out.toByteArray());
      out.write(2);
      Assertions.assertArrayEquals(MathUtils.toByteArray(1, 2), out.toByteArray());
      out.write(3);
      Assertions.assertArrayEquals(MathUtils.toByteArray(1, 3), out.toByteArray());
      out.write(4);
      Assertions.assertArrayEquals(MathUtils.toByteArray(1, 4), out.toByteArray());

      out.write(new byte[] { 5, 6 });
      Assertions.assertArrayEquals(MathUtils.toByteArray(1, 6), out.toByteArray());

      out.write(new byte[] { 7, 8, 9, 10, 11, 12 });
      Assertions.assertArrayEquals(MathUtils.toByteArray(1, 12), out.toByteArray());

      out.write(new byte[] { 13, 14, 15, 16, 17 });
      Assertions.assertArrayEquals(MathUtils.toByteArray(1, 17), out.toByteArray());

      out.close();
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_randomBytes() {
    try {
      MemoryStream out = new MemoryStream(32);

      Random rd = new Random();
      byte[] merged = new byte[0];
      int len = 0;

      for (int i = 0; i < 500; i++) {
        int rdLen = RandomUtils.nextInt(1, 100, rd);
        byte[] rdBytes = RandomUtils.nextBytes(rdLen, rd);

        merged = ArrayUtils.append(merged, rdBytes);
        out.write(rdBytes);
        len += rdLen;
      }
      Assertions.assertEquals(len, out.size());
      Assertions.assertArrayEquals(merged, out.toByteArray());

      out.close();
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_reset() {
    try {
      MemoryStream out = new MemoryStream(4);
      out.write("acsdef".getBytes());
      out.reset();

      Assertions.assertEquals(0, out.size());

      out.write("acs".getBytes());
      Assertions.assertEquals("acs", out.toString(StandardCharsets.UTF_8));

      out.close();
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_ser() {
    try {
      MemoryStream out = new MemoryStream(4);
      out.write(MathUtils.toByteArray(1, 10));

      byte[] ser = JavaSerUtils.serialize(out);
      MemoryStream des = JavaSerUtils.deserialize(ser);
      Assertions.assertArrayEquals(MathUtils.toByteArray(1, 10), des.toByteArray());

      out.close();
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
