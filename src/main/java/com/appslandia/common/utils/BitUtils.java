// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
        if ((curIterator == null) || !curIterator.hasNext()) {
          curIdx++;

          if (curIdx < bytes.length) {
            curIterator = bitIterator(bytes[curIdx]);
          } else {
            curIterator = Collections.emptyIterator();
          }
        }
        return curIterator;
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
        if ((curIterator == null) || !curIterator.hasNext()) {
          try {
            var v = value.read();
            if (v != -1) {
              curIterator = bitIterator((byte) v);
            } else {
              curIterator = Collections.emptyIterator();
            }
          } catch (IOException ex) {
            throw new UncheckedIOException(ex);
          }
        }
        return curIterator;
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
        return (value & (1 << (index--))) > 0 ? 1 : 0;
      }

      @Override
      public boolean hasNext() {
        return index >= 0;
      }
    };
  }
}
