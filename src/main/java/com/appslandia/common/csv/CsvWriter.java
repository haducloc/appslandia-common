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

package com.appslandia.common.csv;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvWriter {

  final BufferedWriter out;
  final CsvProcessor csvProcessor;

  public CsvWriter(BufferedWriter out) {
    this(out, CsvProcessor.INSTANCE);
  }

  public CsvWriter(BufferedWriter out, CsvProcessor csvProcessor) {
    this.out = out;
    this.csvProcessor = csvProcessor;
  }

  public CsvWriter newLine() throws IOException {
    this.out.newLine();
    return this;
  }

  public CsvWriter outSeparator() throws IOException {
    this.out.write(this.csvProcessor.getSeparator());
    return this;
  }

  public CsvWriter out(Object value) throws IOException {
    return out(value, false);
  }

  public CsvWriter out(Object value, boolean firstSeparator) throws IOException {
    if (firstSeparator) {
      outSeparator();
    }

    String escapedValue = this.csvProcessor.escape(value);
    this.out.write(escapedValue);

    return this;
  }

  public CsvWriter out(Object... values) throws IOException {
    for (int i = 0; i < values.length; i++) {
      out(values[i]);

      if (i < values.length - 1) {
        outSeparator();
      }
    }
    return this;
  }
}
