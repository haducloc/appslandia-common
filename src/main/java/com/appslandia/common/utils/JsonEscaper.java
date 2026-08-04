// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonEscaper {

  private static final int HIGHEST_SPECIAL = '\\';

  private static char[][] ESCAPE_JSON = new char[HIGHEST_SPECIAL + 1][];
  static {
    var sb = new StringBuilder(6);

    // the control characters (U+0000 through U+001F).
    for (char ch = 0; ch <= 31; ch++) {
      escapeControlChar(ch, sb);

      ESCAPE_JSON[ch] = sb.toString().toCharArray();
    }
    ESCAPE_JSON['"'] = "\\\"".toCharArray();
    ESCAPE_JSON['\\'] = "\\".toCharArray();
    ESCAPE_JSON['/'] = "\\/".toCharArray();

    ESCAPE_JSON['\b'] = "\\b".toCharArray();
    ESCAPE_JSON['\f'] = "\\f".toCharArray();
    ESCAPE_JSON['\n'] = "\\n".toCharArray();
    ESCAPE_JSON['\r'] = "\\r".toCharArray();
    ESCAPE_JSON['\t'] = "\\t".toCharArray();
  }

  static void escapeControlChar(char c, StringBuilder out) {
    out.setLength(0);

    var hs = Integer.toHexString(c);
    out.append("\\u");

    for (var i = 0; i < 4 - hs.length(); i++) {
      out.append('0');
    }
    out.append(hs);
  }

  public static String escape(String value) {
    if (value == null) {
      return "null";
    }
    if (value.isEmpty()) {
      return "\"\"";
    }
    var out = new StringBuilder((int) (value.length() * 1.25f));
    out.append('"');

    var start = 0;
    var srcChars = value.toCharArray();
    var length = value.length();

    for (var i = 0; i < length; i++) {
      var c = srcChars[i];

      if (c <= HIGHEST_SPECIAL) {
        var escaped = ESCAPE_JSON[c];
        if (escaped != null) {

          // add un_escaped portion
          if (start < i) {
            out.append(srcChars, start, i - start);
          }

          // add escaped
          out.append(escaped);
          start = i + 1;
        }
      }
    }

    // add rest of un_escaped portion
    if (start < length) {
      out.append(srcChars, start, length - start);
    }
    out.append('"');
    return out.toString();
  }
}
