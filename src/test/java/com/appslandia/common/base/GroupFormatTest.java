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

package com.appslandia.common.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GroupFormatTest {

  @Test
  public void test() {
    try {
      GroupFormat format = new GroupFormat("({3}) {3}-{4}");
      String result = format.format("4024130224");

      Assertions.assertEquals(10, format.getInputLength());
      Assertions.assertEquals("(402) 413-0224", result);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_spaces() {
    try {
      GroupFormat format = new GroupFormat("({ 3 }) {  3}-{4}");
      String result = format.format("4024130224");

      Assertions.assertEquals(10, format.getInputLength());
      Assertions.assertEquals("(402) 413-0224", result);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_noGroup() {
    try {
      GroupFormat format = new GroupFormat("ABC-123");
      Assertions.assertEquals(0, format.getInputLength());

      String result = format.format("XYZ");
      Assertions.assertEquals("XYZ", result);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_skippedFormat() {
    try {
      GroupFormat format = new GroupFormat("({3}) {3}-{4}");
      String result = format.format("402413022");
      Assertions.assertEquals("402413022", result);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_validate() {
    try {
      GroupFormat format = new GroupFormat("({3}) {3}-{4}", true);
      format.format("402413022");
      Assertions.fail();

    } catch (Exception ex) {
    }
  }
}
