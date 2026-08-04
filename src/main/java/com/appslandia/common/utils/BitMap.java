// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
    var impl = new BitMap(size());
    impl.or(this);
    return impl;
  }
}
