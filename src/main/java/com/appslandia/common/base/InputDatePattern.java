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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class InputDatePattern {

  private static volatile InputDatePattern __default;
  private static final Object MUTEX = new Object();

  public static InputDatePattern getDefault() {
    InputDatePattern obj = __default;
    if (obj == null) {
      synchronized (MUTEX) {
        if ((obj = __default) == null) {
          __default = obj = new DefaultInputDatePattern();
        }
      }
    }
    return obj;
  }

  public static void setDefault(InputDatePattern impl) {
    Asserts.isNull(__default, "DatePatternParser.__default must be null.");
    __default = impl;
  }

  public String parse(Locale locale) {
    String pattern = doParse(locale);
    if (!isInputDatePattern(pattern)) {
      throw new AssertException("The input date pattern is invalid: " + pattern);
    }
    return pattern;
  }

  protected abstract String doParse(Locale locale);

  public static class DefaultInputDatePattern extends InputDatePattern {

    @Override
    protected String doParse(Locale locale) {
      // isoDate
      String isoDate = null;
      try {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        isoDate = df.format(new SimpleDateFormat(DateUtils.ISO8601_DATE).parse("3333-11-22"));
      } catch (ParseException ex) {
        throw new Error(ex);
      }

      // Parse letters and separator
      Set<Character> letters = new LinkedHashSet<>();
      Character separator = null;

      for (int i = 0; i < isoDate.length(); i++) {
        char ch = isoDate.charAt(i);

        if (ch == '1') {
          letters.add('M');
        } else if (ch == '2') {
          letters.add('d');
        } else if (ch == '3') {
          letters.add('y');
        } else {
          if (separator == null) {
            if (ch == '-' || ch == '/' || ch == '.') {
              separator = ch;
            }
          }
        }
      }

      // If separator known & letters are yMd
      if ((separator != null) && (letters.size() == 3)) {
        StringBuilder datePt = new StringBuilder(10);

        for (Character ch : letters) {
          if (datePt.length() > 0) {
            datePt.append(separator);
          }
          datePt.append(ch);
          datePt.append(ch);

          if (ch.equals('y')) {
            datePt.append(ch);
            datePt.append(ch);
          }
        }
        return datePt.toString();
      }
      throw new AssertException("Failed to parse the date pattern for the specified locale: " + locale);
    }
  }

  private static boolean isInputDatePattern(String datePattern) {
    Asserts.notNull(datePattern);

    if (datePattern.length() != 10) {
      return false;
    }
    if (!datePattern.contains("dd") || !datePattern.contains("MM") || !datePattern.contains("yyyy")) {
      return false;
    }

    // separators
    HashMap<Character, Integer> separators = new HashMap<>(2);
    char sep = 0;

    for (int i = 0; i < datePattern.length(); i++) {
      char c = datePattern.charAt(i);

      if (c != 'd' && c != 'M' && c != 'y') {
        separators.compute(c, (k, v) -> v != null ? v + 1 : 1);

        sep = c;
      }
    }

    if (separators.size() != 1) {
      return false;
    }
    if (separators.get(sep) != 2) {
      return false;
    }
    if (sep != '-' && sep != '/' && sep != '.') {
      return false;
    }
    return true;
  }
}
