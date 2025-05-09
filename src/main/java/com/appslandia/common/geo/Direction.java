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

package com.appslandia.common.geo;

import java.util.Locale;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public enum Direction {

  NORTH("N"), EAST("E"), SOUTH("S"), WEST("W");

  final String symbol;

  static final Direction[] DIRECTIONS = Direction.values();

  private Direction(String symbol) {
    this.symbol = symbol;
  }

  public String symbol() {
    return this.symbol;
  }

  public boolean isX() {
    return this == EAST || this == WEST;
  }

  public boolean isY() {
    return this == NORTH || this == SOUTH;
  }

  public Direction reverse() {
    return turn(2);
  }

  public Direction right() {
    return turn(1);
  }

  public Direction left() {
    return turn(-1);
  }

  public Direction turn(int n) {
    if (n == 0) {
      return this;
    }

    // 0:N, 1:E, 2:S, 3:W
    var next = (this.ordinal() + n) % 4;

    if (next < 0) {
      next = (next + 4) % 4;
    }
    return DIRECTIONS[next];
  }

  public static Direction parseValue(String symbol) {
    Arguments.notNull(symbol);
    symbol = symbol.toUpperCase(Locale.ENGLISH);

    return switch (symbol) {
    case "N" -> NORTH;
    case "E" -> EAST;
    case "S" -> SOUTH;
    case "W" -> WEST;
    default -> throw new IllegalArgumentException(STR.fmt("symbol '{}' is invalid.", symbol));
    };
  }
}
