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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class MemoryStreamTest {

  private MemoryStream ms;

  @BeforeEach
  void setup() {
    ms = new MemoryStream(8);
  }

  @Test
  void testWrite_singleByte() throws IOException {
    ms.write(65);
    Assertions.assertEquals(1, ms.size());
    Assertions.assertArrayEquals("A".getBytes(), ms.toByteArray());
  }

  @Test
  void testWrite_byteArray_exactFit() throws IOException {
    var data = "12345678".getBytes();
    ms.write(data);
    Assertions.assertEquals(8, ms.size());
  }

  @Test
  void testWrite_byteArray_splitAcrossBlocks() throws IOException {
    var data = "1234567890123456".getBytes();
    ms.write(data);
    Assertions.assertEquals(16, ms.size());
    Assertions.assertArrayEquals(data, ms.toByteArray());
  }

  @Test
  void testReset_clearsContent() throws IOException {
    ms.write("hello".getBytes());
    ms.reset();
    Assertions.assertEquals(0, ms.size());
    Assertions.assertTrue(ms.isEmpty());
  }

  @Test
  void testWriteTo_outputStream() throws IOException {
    ms.write("stream".getBytes());
    var out = new ByteArrayOutputStream();
    ms.writeTo(out);
    Assertions.assertEquals("stream", out.toString());
  }

  @Test
  void testIterate() throws IOException {
    ms.write("iterate".getBytes());
    var sb = new StringBuilder();
    ms.iterate((buf, len) -> sb.append(new String(buf, 0, len)));
    Assertions.assertEquals("iterate", sb.toString());
  }

  @Test
  void testToString_charset() throws IOException {
    ms.write("utf8".getBytes(StandardCharsets.UTF_8));
    Assertions.assertEquals("utf8", ms.toString(StandardCharsets.UTF_8));
  }

  @Test
  void testToString_charsetName() throws IOException {
    ms.write("test".getBytes());
    Assertions.assertEquals("test", ms.toString("UTF-8"));
  }

  @Test
  void testIsEmpty_initial() {
    Assertions.assertTrue(ms.isEmpty());
  }

  @Test
  void testIsEmpty_afterWrite() throws IOException {
    ms.write(1);
    Assertions.assertFalse(ms.isEmpty());
  }

  @Test
  void testSize_multipleWrites() throws IOException {
    ms.write("a".getBytes());
    ms.write("bc".getBytes());
    Assertions.assertEquals(3, ms.size());
  }

  @Test
  void testGetBlockSize() {
    Assertions.assertEquals(8, ms.getBlockSize());
  }

  @Test
  void testGetNodeCount_increases() throws IOException {
    var big = new byte[25];
    ms.write(big);
    Assertions.assertTrue(ms.getNodeCount() > 1);
  }

  @Test
  void testWrite_offsetLength() throws IOException {
    var data = "abcdefg12345".getBytes();
    ms.write(data, 7, 5);
    Assertions.assertArrayEquals("12345".getBytes(), ms.toByteArray());
  }

  @Test
  void testWrite_zeroLength() throws IOException {
    ms.write("abc".getBytes());
    ms.write(new byte[5], 0, 0);
    Assertions.assertEquals(3, ms.size());
  }

  @Test
  void testWrite_largeData() throws IOException {
    var data = new byte[1000];
    ms.write(data);
    Assertions.assertEquals(1000, ms.size());
  }

  @Test
  void testSerializeDeserialize() throws IOException, ClassNotFoundException {
    ms.write("serialize".getBytes());

    var bos = new ByteArrayOutputStream();
    var oos = new ObjectOutputStream(bos);
    oos.writeObject(ms);
    oos.close();

    var bis = new ByteArrayInputStream(bos.toByteArray());
    var ois = new ObjectInputStream(bis);
    var deserialized = (MemoryStream) ois.readObject();

    Assertions.assertEquals(ms.size(), deserialized.size());
    Assertions.assertArrayEquals(ms.toByteArray(), deserialized.toByteArray());
  }

  @Test
  void testMultipleResets() throws IOException {
    ms.write("reset1".getBytes());
    ms.reset();
    ms.write("reset2".getBytes());
    Assertions.assertEquals("reset2", new String(ms.toByteArray()));
  }

  @Test
  void testWrite_largeSingleByte() throws IOException {
    for (var i = 0; i < 100; i++) {
      ms.write(65);
    }
    Assertions.assertEquals(100, ms.size());
  }

  @Test
  void testWrite_blockBoundary() throws IOException {
    ms.write("12345678".getBytes());
    ms.write("9".getBytes());
    Assertions.assertEquals("123456789", new String(ms.toByteArray()));
  }

  @Test
  void testWrite_partialFits() throws IOException {
    ms.write("1234567".getBytes());
    ms.write("89".getBytes());
    Assertions.assertEquals("123456789", new String(ms.toByteArray()));
  }

  @Test
  void testNodeCountMatchesExpected() throws IOException {
    ms.write(new byte[24]);
    Assertions.assertEquals(3, ms.getNodeCount());
  }

  @Test
  void testWrite_smallChunks() throws IOException {
    for (var i = 0; i < 10; i++) {
      ms.write("a".getBytes());
    }
    Assertions.assertEquals(10, ms.size());
  }
}
