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
import java.io.UncheckedIOException;
import java.io.Writer;

import com.appslandia.common.base.StringOutput;

/**
 *
 * @author Loc Ha
 *
 */
public class XmlEscaper {

  private static final int HIGHEST_ESCXML_CHAR = '>';

  private static char[][] ESCAPE_CONTENT = new char[HIGHEST_ESCXML_CHAR + 1][];
  static {
    ESCAPE_CONTENT['&'] = "&amp;".toCharArray();
    ESCAPE_CONTENT['<'] = "&lt;".toCharArray();
    ESCAPE_CONTENT['>'] = "&gt;".toCharArray();
  }

  private static char[][] ESCAPE_ATTRIBUTE = new char[HIGHEST_ESCXML_CHAR + 1][];
  static {
    ESCAPE_ATTRIBUTE['&'] = "&amp;".toCharArray();
    ESCAPE_ATTRIBUTE['<'] = "&lt;".toCharArray();
    ESCAPE_ATTRIBUTE['>'] = "&gt;".toCharArray();
    ESCAPE_ATTRIBUTE['"'] = "&quot;".toCharArray();
    ESCAPE_ATTRIBUTE['\''] = "&#39;".toCharArray();
  }

  public static void escapeContent(Writer out, String s) throws IOException {
    writeEscapeXml(out, s, ESCAPE_CONTENT);
  }

  public static String escapeContent(String s) {
    if (s == null) {
      return null;
    }
    try (var out = new StringOutput((int) (s.length() * 1.25f))) {
      writeEscapeXml(out, s, ESCAPE_CONTENT);
      return out.toString();

    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  public static void escapeAttribute(Writer out, String s) throws IOException {
    writeEscapeXml(out, s, ESCAPE_ATTRIBUTE);
  }

  public static String escapeAttribute(String s) {
    if (s == null) {
      return null;
    }
    try (var out = new StringOutput((int) (s.length() * 1.25f))) {
      writeEscapeXml(out, s, ESCAPE_ATTRIBUTE);
      return out.toString();

    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  static void writeEscapeXml(Writer out, String s, char[][] escapeXml) throws IOException {
    var start = 0;
    var srcChars = s.toCharArray();
    var length = s.length();

    for (var i = 0; i < length; i++) {
      var c = srcChars[i];
      if (c <= HIGHEST_ESCXML_CHAR) {

        var escaped = escapeXml[c];
        if (escaped != null) {

          // add un_escaped portion
          if (start < i) {
            out.write(srcChars, start, i - start);
          }

          // add escaped
          out.write(escaped);
          start = i + 1;
        }
      }
    }
    // add rest of un_escaped portion
    if (start < length) {
      out.write(srcChars, start, length - start);
    }
  }
}
