// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
