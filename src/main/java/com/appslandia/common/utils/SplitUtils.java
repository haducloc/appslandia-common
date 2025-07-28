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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Loc Ha
 *
 */
public class SplitUtils {

  private static final Pattern NEWLINE_SEP_PATTERN = Pattern.compile("(\r?\n)+");

  public static String[] splitByLine(String str) {
    return splitByLine(str, SplittingBehavior.SKIP_NULL);
  }

  public static String[] splitByLine(String str, SplittingBehavior behavior) {
    return split(str, NEWLINE_SEP_PATTERN, behavior);
  }

  public static String[] split(String str, Pattern separator) {
    return split(str, separator, SplittingBehavior.SKIP_NULL);
  }

  public static String[] split(String str, Pattern separator, SplittingBehavior behavior) {
    if (str == null) {
      return StringUtils.EMPTY_ARRAY;
    }
    var items = separator.split(str);
    List<String> list = new ArrayList<>(items.length);

    for (String item : items) {
      item = convertItem(item, behavior);

      if (item != null) {
        list.add(item);
      } else {
        if (behavior != SplittingBehavior.SKIP_NULL) {
          list.add(null);
        }
      }
    }
    return list.toArray(new String[list.size()]);
  }

  public static String[] splitByComma(String str) {
    return splitByComma(str, SplittingBehavior.SKIP_NULL);
  }

  public static String[] splitByComma(String str, SplittingBehavior behavior) {
    return split(str, ',', behavior);
  }

  public static String[] split(String str, char separator) {
    return split(str, separator, SplittingBehavior.SKIP_NULL);
  }

  public static String[] split(String str, char separator, SplittingBehavior behavior) {
    if (str == null) {
      return StringUtils.EMPTY_ARRAY;
    }

    List<String> list = new ArrayList<>();
    var currentItem = new StringBuilder();
    var escapeNextChar = false;

    for (var i = 0; i < str.length(); i++) {
      var c = str.charAt(i);

      if (escapeNextChar) {
        currentItem.append(c);
        escapeNextChar = false;

      } else if (c == '\\') {
        escapeNextChar = true;

      } else if (c == separator) {
        var item = convertItem(currentItem.toString(), behavior);

        if (item != null) {
          list.add(item);
        } else {
          if (behavior != SplittingBehavior.SKIP_NULL) {
            list.add(null);
          }
        }

        currentItem.setLength(0);
      } else {
        currentItem.append(c);
      }
    }

    // Last item
    var item = convertItem(currentItem.toString(), behavior);

    if (item != null) {
      list.add(item);
    } else {
      if (behavior != SplittingBehavior.SKIP_NULL) {
        list.add(null);
      }
    }
    return list.toArray(new String[list.size()]);
  }

  private static String convertItem(String item, SplittingBehavior behavior) {
    if (behavior == null || behavior == SplittingBehavior.ORIGINAL) {
      return item;
    } else {
      item = item.strip();
      return !item.isEmpty() ? item : null;
    }
  }
}
