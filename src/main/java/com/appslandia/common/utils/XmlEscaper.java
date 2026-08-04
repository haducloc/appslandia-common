// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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

  private static char[][] ESCAPE_XML = new char[HIGHEST_ESCXML_CHAR + 1][];
  static {
    ESCAPE_XML['&'] = "&amp;".toCharArray();
    ESCAPE_XML['<'] = "&lt;".toCharArray();
    ESCAPE_XML['>'] = "&gt;".toCharArray();
    ESCAPE_XML['"'] = "&quot;".toCharArray();
    ESCAPE_XML['\''] = "&#39;".toCharArray();
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

  public static void escapeXml(Writer out, String s) throws IOException {
    writeEscapeXml(out, s, ESCAPE_XML);
  }

  public static String escapeXml(String s) {
    if (s == null) {
      return null;
    }
    try (var out = new StringOutput((int) (s.length() * 1.25f))) {
      writeEscapeXml(out, s, ESCAPE_XML);
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
