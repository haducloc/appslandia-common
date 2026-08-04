// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
    in = new PushbackInputStream(is, 4);

    var bb = new byte[4];
    var count = in.read(bb);
    bom = BOM.parse(bb, count);

    if (count > 0) {
      in.unread(bb, 0, count);
    }
    if (bom != null) {
      in.skip(bom.length());
    }
  }

  public BOM getBOM() {
    return bom;
  }

  @Override
  public int read() throws IOException {
    return in.read();
  }

  @Override
  public int read(byte[] b) throws IOException {
    return in.read(b);
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return in.read(b, off, len);
  }

  @Override
  public long skip(long n) throws IOException {
    return in.skip(n);
  }

  @Override
  public int available() throws IOException {
    return in.available();
  }

  @Override
  public synchronized void close() throws IOException {
    in.close();
  }

  @Override
  public synchronized void mark(int readlimit) {
    in.mark(readlimit);
  }

  @Override
  public synchronized void reset() throws IOException {
    in.reset();
  }

  @Override
  public boolean markSupported() {
    return in.markSupported();
  }
}
