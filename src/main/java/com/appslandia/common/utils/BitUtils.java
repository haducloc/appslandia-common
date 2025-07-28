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

package com.appslandia.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author Loc Ha
 *
 */
public class BitUtils {

  public static Iterator<Integer> bitIterator(int value) {
    return bitIterator(MathUtils.toByteArray(value));
  }

  public static Iterator<Integer> bitIterator(long value) {
    return bitIterator(MathUtils.toByteArray(value));
  }

  public static Iterator<Integer> bitIterator(byte... bytes) {
    return new Iterator<>() {

      private int curIdx = -1;
      private Iterator<Integer> curIterator;

      private Iterator<Integer> getCurIterator() {
        if ((this.curIterator == null) || !this.curIterator.hasNext()) {
          this.curIdx++;

          if (this.curIdx < bytes.length) {
            this.curIterator = bitIterator(bytes[this.curIdx]);
          } else {
            this.curIterator = Collections.emptyIterator();
          }
        }
        return this.curIterator;
      }

      @Override
      public Integer next() {
        return getCurIterator().next();
      }

      @Override
      public boolean hasNext() {
        return getCurIterator().hasNext();
      }
    };
  }

  public static Iterator<Integer> bitIterator(InputStream value) {
    return new Iterator<>() {

      private Iterator<Integer> curIterator;

      private Iterator<Integer> getCurIterator() {
        if ((this.curIterator == null) || !this.curIterator.hasNext()) {
          try {
            var v = value.read();
            if (v != -1) {
              this.curIterator = bitIterator((byte) v);
            } else {
              this.curIterator = Collections.emptyIterator();
            }
          } catch (IOException ex) {
            throw new UncheckedIOException(ex);
          }
        }
        return this.curIterator;
      }

      @Override
      public Integer next() {
        return getCurIterator().next();
      }

      @Override
      public boolean hasNext() {
        return getCurIterator().hasNext();
      }
    };
  }

  public static Iterator<Integer> bitIterator(byte value) {

    return new Iterator<>() {

      int index = 7;

      @Override
      public Integer next() {
        return (value & (1 << (this.index--))) > 0 ? 1 : 0;
      }

      @Override
      public boolean hasNext() {
        return this.index >= 0;
      }
    };
  }
}
