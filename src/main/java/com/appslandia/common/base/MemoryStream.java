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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MemoryStream extends OutputStream implements Serializable {
  private static final long serialVersionUID = 1L;

  private int blockSize;
  private NodeList nodeList;

  private long count;
  private int nodeCount;

  public MemoryStream() {
    this(512);
  }

  public MemoryStream(int blockSize) {
    Asserts.isTrue(blockSize > 0);

    this.blockSize = blockSize;
    this.nodeList = new NodeList(new Node(new byte[this.blockSize], 0));
    this.nodeCount = 1;
  }

  @Override
  public void write(int b) throws IOException {
    Node lastNode = this.nodeList.last;
    int lenAv = lastNode.buf.length - lastNode.curLen;

    if (lenAv >= 1) {
      lastNode.buf[lastNode.curLen] = (byte) b;
      lastNode.curLen += 1;

    } else {
      byte[] newBuf = new byte[this.blockSize];
      newBuf[0] = (byte) b;
      this.nodeList.insert(new Node(newBuf, 1));
      this.nodeCount += 1;
    }
    this.count += 1;
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
      throw new IndexOutOfBoundsException();
    } else if (len == 0) {
      return;
    }

    Node lastNode = this.nodeList.last;
    int lenAv = lastNode.buf.length - lastNode.curLen;

    if (lenAv >= len) {
      System.arraycopy(b, off, lastNode.buf, lastNode.curLen, len);
      lastNode.curLen += len;

    } else {
      int moreLen = len - lenAv;
      int addBlocks = moreLen / (this.blockSize);
      if (addBlocks * this.blockSize < moreLen) {
        addBlocks++;
      }
      byte[] newBuf = new byte[addBlocks * this.blockSize];

      if (lenAv == 0) {
        System.arraycopy(b, off, newBuf, 0, len);
      } else {
        System.arraycopy(b, off, lastNode.buf, lastNode.curLen, lenAv);
        lastNode.curLen = lastNode.buf.length;

        System.arraycopy(b, off + lenAv, newBuf, 0, moreLen);
      }

      this.nodeList.insert(new Node(newBuf, moreLen));
      this.nodeCount += 1;
    }
    this.count += len;
  }

  public void writeTo(OutputStream out) throws IOException {
    Node node = this.nodeList.first;
    while (node != null) {

      out.write(node.buf, 0, node.curLen);
      node = node.next;
    }
  }

  public byte[] toByteArray() {
    byte[] bytes = new byte[(int) this.count];
    int destPos = 0;
    Node node = this.nodeList.first;

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

  public byte[] digest(String algorithm) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance(algorithm);

    Node node = this.nodeList.first;
    while (node != null) {

      md.update(node.buf, 0, node.curLen);
      node = node.next;
    }
    return md.digest();
  }

  public void iterate(NodeIterator iterator) throws IOException {
    Node node = this.nodeList.first;
    while (node != null) {

      iterator.nextNode(node.buf, node.curLen);
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
        this.getNodeCount(), this.count);
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
    this.nodeList.first.next = null;
    this.nodeList.first.curLen = 0;
    this.nodeList.last = this.nodeList.first;

    this.count = 0;
    this.nodeCount = 1;
  }

  // Implements Serializable

  private void writeObject(ObjectOutputStream out) throws IOException {
    out.writeInt(this.blockSize);
    out.writeLong(this.count);
    out.writeInt(getNodeCount());

    Node node = this.nodeList.first;
    while (node != null) {

      out.writeInt(node.buf.length);
      out.writeInt(node.curLen);
      out.write(node.buf, 0, node.buf.length);

      node = node.next;
    }
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    this.blockSize = in.readInt();
    this.count = in.readLong();

    int nodeCount = in.readInt();
    while (nodeCount > 0) {

      byte[] buf = new byte[in.readInt()];
      int curLen = in.readInt();
      in.readFully(buf, 0, buf.length);

      if (this.nodeList == null) {
        this.nodeList = new NodeList(new Node(buf, curLen));
      } else {
        this.nodeList.insert(new Node(buf, curLen));
      }
      nodeCount--;
    }
  }

  public interface NodeIterator {
    void nextNode(byte[] buf, int len) throws IOException;
  }

  private static class NodeList {
    final Node first;
    Node last;

    public NodeList(Node first) {
      this.last = this.first = first;
    }

    public void insert(Node node) {
      this.last.next = node;
      this.last = node;
    }
  }

  private static class Node {
    final byte[] buf;
    Node next;
    int curLen = 0;

    public Node(byte[] buf, int len) {
      this.buf = buf;
      this.curLen = len;
    }
  }
}
