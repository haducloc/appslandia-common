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

import java.util.BitSet;

/**
 *
 * @author Loc Ha
 *
 */
public class BitMap extends BitSet {
  private static final long serialVersionUID = 1L;

  public BitMap() {
  }

  public BitMap(int bits) {
    super(bits);
  }

  public BitMap(BitSet bitSet) {
    super(bitSet.size());
    or(bitSet);
  }

  public BitMap on(int fromIdx, int toIdxExclusive) {
    set(fromIdx, toIdxExclusive);
    return this;
  }

  public BitMap on(int... indexes) {
    for (int index : indexes) {
      set(index);
    }
    return this;
  }

  public BitMap on(String charRangles) {
    for (char chr : CharUtils.toCharRanges(charRangles)) {
      set(chr);
    }
    return this;
  }

  public BitMap off(int fromIdx, int toIdxExclusive) {
    clear(fromIdx, toIdxExclusive);
    return this;
  }

  public BitMap off(int... indexes) {
    for (int index : indexes) {
      clear(index);
    }
    return this;
  }

  public BitMap off(String charRangles) {
    for (char chr : CharUtils.toCharRanges(charRangles)) {
      clear(chr);
    }
    return this;
  }

  public BitMap toggle(int fromIdx, int toIdxExclusive) {
    flip(fromIdx, toIdxExclusive);
    return this;
  }

  public BitMap toggle(int... indexes) {
    for (int index : indexes) {
      flip(index);
    }
    return this;
  }

  public BitMap toggle(String charRangles) {
    for (char chr : CharUtils.toCharRanges(charRangles)) {
      flip(chr);
    }
    return this;
  }

  @Override
  public BitMap clone() {
    var impl = new BitMap(this.size());
    impl.or(this);
    return impl;
  }
}
