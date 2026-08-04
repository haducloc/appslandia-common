// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
