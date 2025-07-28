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
  protected NodeList nodeList;

  protected long count;
  protected int nodeCount;

  public MemoryStream() {
    this(512);
  }

  public MemoryStream(int blockSize) {
    Arguments.isTrue(blockSize > 0);
    this.blockSize = blockSize;
    this.nodeList = new NodeList();
    this.nodeCount = 0;
  }

  @Override
  public void write(int b) throws IOException {
    var last = this.nodeList.last;
    if (last == null || last.curLen == last.buf.length) {
      last = new Node(this.obtainBlock(this.blockSize), 0);
      this.nodeList.insert(last);
      this.nodeCount++;
    }
    last.buf[last.curLen++] = (byte) b;
    this.count++;
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    Objects.checkFromIndexSize(off, len, b.length);
    if (len == 0) {
      return;
    }

    while (len > 0) {
      var last = this.nodeList.last;
      if (last == null || last.curLen == last.buf.length) {
        last = new Node(this.obtainBlock(this.blockSize), 0);
        this.nodeList.insert(last);
        this.nodeCount++;
      }

      var space = last.buf.length - last.curLen;
      var writable = Math.min(space, len);
      System.arraycopy(b, off, last.buf, last.curLen, writable);
      last.curLen += writable;

      off += writable;
      len -= writable;
      this.count += writable;
    }
  }

  public void writeTo(OutputStream out) throws IOException {
    var node = this.nodeList.first;
    while (node != null) {
      out.write(node.buf, 0, node.curLen);
      node = node.next;
    }
  }

  public byte[] toByteArray() {
    var bytes = new byte[(int) this.count];
    var destPos = 0;
    var node = this.nodeList.first;

    while (node != null) {
      System.arraycopy(node.buf, 0, bytes, destPos, node.curLen);
      destPos += node.curLen;
      node = node.next;
    }
    return bytes;
  }

  public int getNodeCount() {
    return this.nodeCount;
  }

  public void iterate(NodeIterator iterator) throws IOException {
    var node = this.nodeList.first;
    while (node != null) {
      iterator.iterate(node.buf, node.curLen);
      node = node.next;
    }
  }

  public String toString(Charset charset) {
    return new String(toByteArray(), 0, (int) this.count, charset);
  }

  public String toString(String charsetName) {
    return toString(Charset.forName(charsetName));
  }

  @Override
  public String toString() {
    return STR.fmt("{}: blockSize={}, nodeCount={}, size={}", ObjectUtils.toIdHash(this), this.blockSize,
        this.nodeCount, this.count);
  }

  public int getBlockSize() {
    return this.blockSize;
  }

  public long size() {
    return this.count;
  }

  public boolean isEmpty() {
    return this.count == 0;
  }

  public void reset() {
    var node = this.nodeList.first;
    while (node != null) {
      this.releaseBlock(node.buf);

      var next = node.next;
      node.next = null;
      node.curLen = 0;
      node = next;
    }

    this.nodeList.first = null;
    this.nodeList.last = null;
    this.count = 0;
    this.nodeCount = 0;
  }

  protected byte[] obtainBlock(int blockSize) {
    return new byte[blockSize];
  }

  protected void releaseBlock(byte[] block) {
  }

  // Serializable implementation

  private void writeObject(ObjectOutputStream out) throws IOException {
    out.writeInt(this.blockSize);
    out.writeLong(this.count);
    out.writeInt(this.nodeCount);

    var node = this.nodeList.first;
    while (node != null) {
      out.writeInt(node.curLen);
      out.write(node.buf, 0, node.curLen);
      node = node.next;
    }
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    this.blockSize = in.readInt();
    this.count = in.readLong();
    this.nodeCount = in.readInt();

    this.nodeList = new NodeList();
    var remaining = this.count;

    while (remaining > 0) {
      var len = in.readInt();
      if (len > this.blockSize) {
        throw new IOException("Serialized block length exceeds blockSize.");
      }

      var buf = this.obtainBlock(this.blockSize);
      in.readFully(buf, 0, len);

      var node = new Node(buf, len);
      if (this.nodeList.first == null) {
        this.nodeList.first = this.nodeList.last = node;
      } else {
        this.nodeList.last.next = node;
        this.nodeList.last = node;
      }

      remaining -= len;
    }
  }

  public interface NodeIterator {
    void iterate(byte[] buf, int len) throws IOException;
  }

  protected static class NodeList {
    Node first;
    Node last;

    public void insert(Node node) {
      if (this.first == null) {
        this.first = this.last = node;
      } else {
        this.last.next = node;
        this.last = node;
      }
    }
  }

  protected static class Node {
    byte[] buf;
    Node next;
    int curLen = 0;

    public Node(byte[] buf, int curLen) {
      this.buf = buf;
      this.curLen = curLen;
    }
  }
}
