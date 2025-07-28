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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class DirectionTest {

  @Test
  public void test_symbol() {
    Assertions.assertEquals("E", Direction.EAST.symbol());
    Assertions.assertEquals("W", Direction.WEST.symbol());
    Assertions.assertEquals("S", Direction.SOUTH.symbol());
    Assertions.assertEquals("N", Direction.NORTH.symbol());
  }

  @Test
  public void test_parseValue() {
    Assertions.assertEquals(Direction.EAST, Direction.parseValue("E"));
    Assertions.assertEquals(Direction.WEST, Direction.parseValue("W"));
    Assertions.assertEquals(Direction.SOUTH, Direction.parseValue("S"));
    Assertions.assertEquals(Direction.NORTH, Direction.parseValue("N"));
  }

  @Test
  public void test_directions() {
    Assertions.assertTrue(Direction.EAST.isX());
    Assertions.assertTrue(Direction.WEST.isX());

    Assertions.assertTrue(Direction.SOUTH.isY());
    Assertions.assertTrue(Direction.NORTH.isY());
  }

  @Test
  public void test_reverse() {
    Assertions.assertEquals(Direction.WEST, Direction.EAST.reverse());
    Assertions.assertEquals(Direction.EAST, Direction.WEST.reverse());
    Assertions.assertEquals(Direction.NORTH, Direction.SOUTH.reverse());
    Assertions.assertEquals(Direction.SOUTH, Direction.NORTH.reverse());
  }

  @Test
  public void test_right() {
    Assertions.assertEquals(Direction.SOUTH, Direction.EAST.right());
    Assertions.assertEquals(Direction.WEST, Direction.SOUTH.right());
    Assertions.assertEquals(Direction.NORTH, Direction.WEST.right());
    Assertions.assertEquals(Direction.EAST, Direction.NORTH.right());
  }

  @Test
  public void test_right_n() {
    Assertions.assertEquals(Direction.EAST, Direction.EAST.turn(0));
    Assertions.assertEquals(Direction.SOUTH, Direction.SOUTH.turn(0));
    Assertions.assertEquals(Direction.WEST, Direction.WEST.turn(0));
    Assertions.assertEquals(Direction.NORTH, Direction.NORTH.turn(0));

    Assertions.assertEquals(Direction.SOUTH, Direction.EAST.turn(1));
    Assertions.assertEquals(Direction.WEST, Direction.SOUTH.turn(1));
    Assertions.assertEquals(Direction.NORTH, Direction.WEST.turn(1));
    Assertions.assertEquals(Direction.EAST, Direction.NORTH.turn(1));

    Assertions.assertEquals(Direction.EAST, Direction.EAST.turn(4));
    Assertions.assertEquals(Direction.SOUTH, Direction.SOUTH.turn(4));
    Assertions.assertEquals(Direction.WEST, Direction.WEST.turn(4));
    Assertions.assertEquals(Direction.NORTH, Direction.NORTH.turn(4));
  }

  @Test
  public void test_left() {
    Assertions.assertEquals(Direction.NORTH, Direction.EAST.left());
    Assertions.assertEquals(Direction.WEST, Direction.NORTH.left());
    Assertions.assertEquals(Direction.SOUTH, Direction.WEST.left());
    Assertions.assertEquals(Direction.EAST, Direction.SOUTH.left());
  }

  @Test
  public void test_left_n() {

    Assertions.assertEquals(Direction.NORTH, Direction.EAST.turn(-1));
    Assertions.assertEquals(Direction.WEST, Direction.NORTH.turn(-1));
    Assertions.assertEquals(Direction.SOUTH, Direction.WEST.turn(-1));
    Assertions.assertEquals(Direction.EAST, Direction.SOUTH.turn(-1));

    Assertions.assertEquals(Direction.EAST, Direction.EAST.turn(-4));
    Assertions.assertEquals(Direction.SOUTH, Direction.SOUTH.turn(-4));
    Assertions.assertEquals(Direction.WEST, Direction.WEST.turn(-4));
    Assertions.assertEquals(Direction.NORTH, Direction.NORTH.turn(-4));
  }
}
