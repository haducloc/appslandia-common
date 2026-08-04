// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Objects;

/**
 * @see java.io.CharArrayWriter
 *
 * @author Loc Ha
 *
 */
public class CharArrayOutput extends Writer {

  protected char[] buf;

  protected int count;

  public CharArrayOutput() {
    this(32);
  }

  public CharArrayOutput(int initialSize) {
    if (initialSize < 0) {
      throw new IllegalArgumentException("Negative initial size: " + initialSize);
    }
    buf = new char[initialSize];
  }

  @Override
  public void write(int c) {
    var newcount = count + 1;
    if (newcount > buf.length) {
      buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
    }
    buf[count] = (char) c;
    count = newcount;
  }

  @Override
  public void write(char[] c, int off, int len) {
    Objects.checkFromIndexSize(off, len, c.length);
    if (len == 0) {
      return;
    }
    var newcount = count + len;
    if (newcount > buf.length) {
      buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
    }
    System.arraycopy(c, off, buf, count, len);
    count = newcount;
  }

  @Override
  public void write(String str, int off, int len) {
    var newcount = count + len;
    if (newcount > buf.length) {
      buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
    }
    str.getChars(off, off + len, buf, count);
    count = newcount;
  }

  public void writeTo(Writer out) throws IOException {
    out.write(buf, 0, count);
  }

  @Override
  public CharArrayOutput append(CharSequence csq) {
    var s = (csq == null ? "null" : csq.toString());
    write(s, 0, s.length());
    return this;
  }

  @Override
  public CharArrayOutput append(CharSequence csq, int start, int end) {
    if (csq == null) {
      csq = "null";
    }
    return append(csq.subSequence(start, end));
  }

  @Override
  public CharArrayOutput append(char c) {
    write(c);
    return this;
  }

  public void reset() {
    count = 0;
  }

  public char[] toCharArray() {
    return Arrays.copyOf(buf, count);
  }

  public int size() {
    return count;
  }

  @Override
  public String toString() {
    return new String(buf, 0, count);
  }

  @Override
  public void flush() {
  }

  @Override
  public void close() {
  }
}
