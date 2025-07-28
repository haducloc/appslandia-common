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

import java.io.StringReader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.json.GsonProcessor;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonEscaperTest {

  @Test
  public void test_null() {
    String value = null;
    var escaped = JsonEscaper.escape(value);
    Assertions.assertEquals("null", escaped);
  }

  @Test
  public void test_empty() {
    var value = "";
    var escaped = JsonEscaper.escape(value);
    Assertions.assertEquals("\"\"", escaped);
  }

  @Test
  public void test_escape_controlChars() {
    var value = "\"\\/\b\f\n\r\t";
    var escaped = JsonEscaper.escape(value);

    Assertions.assertEquals("\"\\\"\\\\/\\b\\f\\n\\r\\t\"", escaped);

    var json = new GsonProcessor();
    var decoded = json.read(new StringReader(escaped), String.class);
    Assertions.assertEquals(value, decoded);
  }
}
