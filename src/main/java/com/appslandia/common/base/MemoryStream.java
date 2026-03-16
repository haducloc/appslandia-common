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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Objects;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class MemoryStream extends OutputStream implements Serializable {
  private static final long serialVersionUID = 1L;

  protected int blockSize;
  protected BlockList blockList;

  protected long length;
  protected int blockCount;

  public MemoryStream() {
    this(512);
  }

  public MemoryStream(int blockSize) {
    Arguments.isTrue(blockSize > 0);
    this.blockSize = blockSize;
    blockList = new BlockList();
    blockCount = 0;
  }

  @Override
  public void write(int b) throws IOException {
    var last = blockList.last;
    if (last == null || last.curLen == last.buf.length) {
      last = new Block(obtainBlock(blockSize), 0);
      blockList.insert(last);
      blockCount++;
    }
    last.buf[last.curLen++] = (byte) b;
    length++;
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    Objects.checkFromIndexSize(off, len, b.length);
    if (len == 0) {
      return;
    }

    while (len > 0) {
      var last = blockList.last;
      if (last == null || last.curLen == last.buf.length) {
        last = new Block(obtainBlock(blockSize), 0);
        blockList.insert(last);
        blockCount++;
      }

      var space = last.buf.length - last.curLen;
      var writable = Math.min(space, len);
      System.arraycopy(b, off, last.buf, last.curLen, writable);
      last.curLen += writable;

      off += writable;
      len -= writable;
      length += writable;
    }
  }

  public void writeTo(OutputStream out) throws IOException {
    var block = blockList.first;
    while (block != null) {
      out.write(block.buf, 0, block.curLen);
      block = block.next;
    }
  }

  public byte[] toByteArray() {
    var bytes = new byte[(int) length];
    var destPos = 0;
    var block = blockList.first;

    while (block != null) {
      System.arraycopy(block.buf, 0, bytes, destPos, block.curLen);
      destPos += block.curLen;
      block = block.next;
    }
    return bytes;
  }

  public int getBlockCount() {
    return blockCount;
  }

  public void iterate(BlockIterator iterator) throws IOException {
    var block = blockList.first;
    while (block != null) {
      iterator.iterate(block.buf, block.curLen);
      block = block.next;
    }
  }

  public String toString(Charset charset) {
    return new String(toByteArray(), 0, (int) length, charset);
  }

  public String toString(String charsetName) {
    return toString(Charset.forName(charsetName));
  }

  @Override
  public String toString() {
    return STR.fmt("{}: blockSize={}, blockCount={}, length={}", ObjectUtils.toIdHash(this), blockSize, blockCount,
        length);
  }

  public int getBlockSize() {
    return blockSize;
  }

  public long length() {
    return length;
  }

  public boolean isEmpty() {
    return length == 0;
  }

  public void reset() {
    var block = blockList.first;
    while (block != null) {
      releaseBlock(block.buf);

      var next = block.next;
      block.next = null;
      block.curLen = 0;
      block = next;
    }

    blockList.first = null;
    blockList.last = null;
    length = 0;
    blockCount = 0;
  }

  protected byte[] obtainBlock(int blockSize) {
    return new byte[blockSize];
  }

  protected void releaseBlock(byte[] block) {
  }

  // Serializable implementation

  private void writeObject(ObjectOutputStream out) throws IOException {
    out.writeInt(blockSize);
    out.writeLong(length);
    out.writeInt(blockCount);

    var block = blockList.first;
    while (block != null) {
      out.writeInt(block.curLen);
      out.write(block.buf, 0, block.curLen);
      block = block.next;
    }
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    blockSize = in.readInt();
    length = in.readLong();
    blockCount = in.readInt();

    blockList = new BlockList();
    var remaining = length;

    while (remaining > 0) {
      var len = in.readInt();
      if (len > blockSize) {
        throw new IOException("Serialized block length exceeds blockSize.");
      }

      var buf = obtainBlock(blockSize);
      in.readFully(buf, 0, len);

      var block = new Block(buf, len);
      if (blockList.first == null) {
        blockList.first = blockList.last = block;
      } else {
        blockList.last.next = block;
        blockList.last = block;
      }

      remaining -= len;
    }
  }

  public interface BlockIterator {
    void iterate(byte[] buf, int len) throws IOException;
  }

  protected static class BlockList {
    Block first;
    Block last;

    public void insert(Block block) {
      if (first == null) {
        first = last = block;
      } else {
        last.next = block;
        last = block;
      }
    }
  }

  protected static class Block {
    byte[] buf;
    Block next;
    int curLen = 0;

    public Block(byte[] buf, int curLen) {
      this.buf = buf;
      this.curLen = curLen;
    }
  }
}
