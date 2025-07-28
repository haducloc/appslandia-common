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
import java.io.Writer;
import java.util.Objects;

/**
 * @see java.io.StringWriter
 *
 * @author Loc Ha
 *
 */
public class StringOutput extends Writer {

  private final StringBuilder buf;

  public StringOutput() {
    buf = new StringBuilder();
  }

  public StringOutput(int initialSize) {
    if (initialSize < 0) {
      throw new IllegalArgumentException("Negative buffer size");
    }
    buf = new StringBuilder(initialSize);
  }

  @Override
  public void write(int c) {
    buf.append((char) c);
  }

  @Override
  public void write(char[] cbuf, int off, int len) {
    Objects.checkFromIndexSize(off, len, cbuf.length);
    if (len == 0) {
      return;
    }
    buf.append(cbuf, off, len);
  }

  @Override
  public void write(String str) {
    buf.append(str);
  }

  @Override
  public void write(String str, int off, int len) {
    buf.append(str, off, off + len);
  }

  @Override
  public StringOutput append(CharSequence csq) {
    var s = (csq == null ? "null" : csq.toString());
    write(s, 0, s.length());
    return this;
  }

  @Override
  public StringOutput append(CharSequence csq, int start, int end) {
    if (csq == null) {
      csq = "null";
    }
    return append(csq.subSequence(start, end));
  }

  @Override
  public StringOutput append(char c) {
    write(c);
    return this;
  }

  @Override
  public String toString() {
    return buf.toString();
  }

  public StringBuilder getBuffer() {
    return buf;
  }

  @Override
  public void flush() {
  }

  @Override
  public void close() throws IOException {
  }
}
