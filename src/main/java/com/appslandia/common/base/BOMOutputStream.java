// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 *
 * @author Loc Ha
 *
 */
public class BOMOutputStream extends OutputStream {

  final OutputStream os;
  final BOM bom;

  private boolean wroteBom;

  public BOMOutputStream(OutputStream os, String encoding) {
    this(os, BOM.parse(encoding));
  }

  public BOMOutputStream(OutputStream os, BOM bom) {
    this.os = os;
    this.bom = bom;
  }

  public BOM getBOM() {
    return bom;
  }

  protected void writeBOM() throws IOException {
    if (bom == null) {
      return;
    }
    if (!wroteBom) {
      os.write(bom.getBytes());
      wroteBom = true;
    }
  }

  @Override
  public void write(int b) throws IOException {
    writeBOM();
    os.write(b);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    writeBOM();
    os.write(b, off, len);
  }

  @Override
  public void write(byte[] b) throws IOException {
    writeBOM();
    os.write(b, 0, b.length);
  }

  @Override
  public void flush() throws IOException {
    os.flush();
  }

  @Override
  public void close() throws IOException {
    os.close();
  }
}
