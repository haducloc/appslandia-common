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

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

import com.appslandia.common.base.CharArrayOutput;

/**
 *
 * @author Loc Ha
 *
 */
public class URLEncoding {

  enum EncodeType {
    URL_PARAM, URL_PATH
  }

  static final BitSet URL_PARAM_NOT_ENCODED;
  static final int caseDiff = ('a' - 'A');

  static {
    // ALPHA, DIGIT
    // "-", "_", ".", "~"
    // The space character " " is converted into a plus sign "+".

    // @formatter:off
		URL_PARAM_NOT_ENCODED = new BitMap(256).on("a-zA-Z0-9")
		    .on('-', '_', '.', '~').on(' ');
	// @formatter:on
  }
  static final BitSet URL_PARAM_NOT_ENCODED_SP = new BitMap(URL_PARAM_NOT_ENCODED).off(' ');

  static final BitSet URL_PATH_NOT_ENCODED;

  static {
    // ALPHA, DIGIT, "-", ".", "_", "~"
    // gen-delims = ":", "@"
    // sub-delims = "!", "$", "&", "'", "(", ")", "*", "+", ",", ";", "="

    // @formatter:off
		URL_PATH_NOT_ENCODED = new BitMap(256).on("a-zA-Z0-9")
				.on('-', '.', '_', '~').on(':', '@')
				.on('!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=');
	// @formatter:on
  }

  public static String encodeParam(String s) {
    if (s == null) {
      return null;
    }
    return encodeParam(s, true);
  }

  public static String encodeParam(String s, boolean spaceToPlus) {
    if (s == null) {
      return null;
    }
    return encode(s, spaceToPlus ? URL_PARAM_NOT_ENCODED : URL_PARAM_NOT_ENCODED_SP);
  }

  public static String decodeParam(String s) {
    if (s == null) {
      return null;
    }
    return decode(s, EncodeType.URL_PARAM);
  }

  public static String encodePath(String s) {
    if (s == null) {
      return null;
    }
    return encode(s, URL_PATH_NOT_ENCODED);
  }

  public static String decodePath(String s) {
    if (s == null) {
      return null;
    }
    return decode(s, EncodeType.URL_PATH);
  }

  static String encode(String s, BitSet notEncoding) {
    var needToChange = false;
    var out = new StringBuilder((int) (s.length() * 1.25));
    var charArrayOut = new CharArrayOutput();

    for (var i = 0; i < s.length();) {
      int c = s.charAt(i);
      if (notEncoding.get(c)) {
        if (c == ' ') {
          c = '+';
          needToChange = true;
        }
        out.append((char) c);
        i++;
      } else {
        // convert to external encoding before hex conversion
        do {
          charArrayOut.write(c);

          // surrogate pair: high and low
          if (c >= 0xD800 && c <= 0xDBFF) {
            if ((i + 1) < s.length()) {
              int d = s.charAt(i + 1);
              if (d >= 0xDC00 && d <= 0xDFFF) {
                charArrayOut.write(d);
                i++;
              }
            }
          }
          i++;
        } while (i < s.length() && !notEncoding.get((c = s.charAt(i))));

        // charArrayOut.flush();
        var str = charArrayOut.toString();
        var ba = str.getBytes(StandardCharsets.UTF_8);
        for (byte b : ba) {
          out.append('%');
          var ch = Character.forDigit((b >> 4) & 0xF, 16);
          // converting to use uppercase letter as part of
          // the hex value if ch is a letter.
          if (Character.isLetter(ch)) {
            ch -= caseDiff;
          }
          out.append(ch);
          ch = Character.forDigit(b & 0xF, 16);
          if (Character.isLetter(ch)) {
            ch -= caseDiff;
          }
          out.append(ch);
        }
        charArrayOut.reset();
        needToChange = true;
      }
    }
    charArrayOut.close();
    return (needToChange ? out.toString() : s);
  }

  static String decode(String s, EncodeType type) {
    var needToChange = false;
    var numChars = s.length();
    var sb = new StringBuilder(numChars > 128 ? (int) (numChars / 1.25) : numChars);
    var i = 0;

    char c;
    byte[] bytes = null;
    while (i < numChars) {
      c = s.charAt(i);
      switch (c) {
      case '+':
        if (type == EncodeType.URL_PARAM) {
          sb.append(' ');
          needToChange = true;
        } else {
          sb.append(c);
        }
        i++;
        break;
      case '%':
        try {
          // (numChars-i)/3 is an upper bound for the number
          // of remaining bytes
          if (bytes == null) {
            bytes = new byte[(numChars - i) / 3];
          }
          var pos = 0;

          while (((i + 2) < numChars) && (c == '%')) {
            var v = Integer.parseInt(s, i + 1, i + 3, 16);
            if (v < 0) {
              throw new IllegalArgumentException("Illegal hex characters in escape (%) pattern - negative value.");
            }
            bytes[pos++] = (byte) v;
            i += 3;
            if (i < numChars) {
              c = s.charAt(i);
            }
          }

          // A trailing, incomplete byte encoding such as
          // "%x" will cause an exception to be thrown

          if ((i < numChars) && (c == '%')) {
            throw new IllegalArgumentException("Incomplete trailing escape (%) pattern.");
          }

          sb.append(new String(bytes, 0, pos, StandardCharsets.UTF_8));
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Illegal hex characters in escape (%) pattern - " + e.getMessage());
        }
        needToChange = true;
        break;
      default:
        sb.append(c);
        i++;
        break;
      }
    }
    return (needToChange ? sb.toString() : s);
  }
}
