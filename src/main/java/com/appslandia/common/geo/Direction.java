// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
    return symbol;
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
    var next = (ordinal() + n) % 4;

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
