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
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 *
 *
 * @author Loc Ha
 *
 */
public class BOMInputStream extends InputStream {

  private PushbackInputStream in;
  private BOM bom;

  public BOMInputStream(InputStream is) throws IOException {
    this.in = new PushbackInputStream(is, 4);

    var bb = new byte[4];
    var count = this.in.read(bb);
    this.bom = BOM.parse(bb, count);

    if (count > 0) {
      this.in.unread(bb, 0, count);
    }
    if (this.bom != null) {
      this.in.skip(this.bom.length());
    }
  }

  public BOM getBOM() {
    return this.bom;
  }

  @Override
  public int read() throws IOException {
    return this.in.read();
  }

  @Override
  public int read(byte[] b) throws IOException {
    return this.in.read(b);
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return this.in.read(b, off, len);
  }

  @Override
  public long skip(long n) throws IOException {
    return this.in.skip(n);
  }

  @Override
  public int available() throws IOException {
    return this.in.available();
  }

  @Override
  public synchronized void close() throws IOException {
    this.in.close();
  }

  @Override
  public synchronized void mark(int readlimit) {
    this.in.mark(readlimit);
  }

  @Override
  public synchronized void reset() throws IOException {
    this.in.reset();
  }

  @Override
  public boolean markSupported() {
    return this.in.markSupported();
  }
}
