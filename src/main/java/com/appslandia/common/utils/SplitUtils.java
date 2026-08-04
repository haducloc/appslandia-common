// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
    if (str == null) {
      return StringUtils.EMPTY_ARRAY;
    }
    var list = split(str, NEWLINE_SEP_PATTERN, behavior);
    return list.toArray(new String[list.size()]);
  }

  public static String[] split(String str, Pattern separator) {
    if (str == null) {
      return StringUtils.EMPTY_ARRAY;
    }
    var list = split(str, separator, SplittingBehavior.SKIP_NULL);
    return list.toArray(new String[list.size()]);
  }

  public static List<String> split(String str, Pattern separator, SplittingBehavior behavior) {
    Arguments.notNull(str);

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
    return list;
  }

  public static String[] splitByEqual(String str) {
    return splitByEqual(str, SplittingBehavior.SKIP_NULL);
  }

  public static String[] splitByEqual(String str, SplittingBehavior behavior) {
    if (str == null) {
      return StringUtils.EMPTY_ARRAY;
    }
    var list = split(str, '=', behavior);
    return list.toArray(new String[list.size()]);
  }

  public static String[] splitBySemi(String str) {
    return splitBySemi(str, SplittingBehavior.SKIP_NULL);
  }

  public static String[] splitBySemi(String str, SplittingBehavior behavior) {
    if (str == null) {
      return StringUtils.EMPTY_ARRAY;
    }
    var list = split(str, ':', behavior);
    return list.toArray(new String[list.size()]);
  }

  public static String[] splitByComma(String str) {
    return splitByComma(str, SplittingBehavior.SKIP_NULL);
  }

  public static String[] splitByComma(String str, SplittingBehavior behavior) {
    if (str == null) {
      return StringUtils.EMPTY_ARRAY;
    }
    var list = split(str, ',', behavior);
    return list.toArray(new String[list.size()]);
  }

  public static String[] split(String str, char separator) {
    if (str == null) {
      return StringUtils.EMPTY_ARRAY;
    }
    var list = split(str, separator, SplittingBehavior.SKIP_NULL);
    return list.toArray(new String[list.size()]);
  }

  public static List<String> split(String str, char separator, SplittingBehavior behavior) {
    Arguments.notNull(str);

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
    return list;
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
