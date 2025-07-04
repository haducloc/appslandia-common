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

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author Loc Ha
 *
 */
public class NormalizeUtils {

  public interface DecomposedCharacterConverter {
    char convert(char decomposedCharacter);
  }

  private static DecomposedCharacterConverter decomposedCharacterConverter;

  public static void setDecomposedCharacterConverter(DecomposedCharacterConverter converter) {
    Arguments.isNull(decomposedCharacterConverter);
    decomposedCharacterConverter = converter;
  }

  private static final Pattern STRIP_ACCENTS_PATTERN = Pattern.compile("\\p{M}+");

  public static String unaccent(String str) {
    if (str == null) {
      return null;
    }
    var normalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
    var decomposed = new StringBuilder(normalizedString);

    if (decomposedCharacterConverter != null) {
      for (var i = 0; i < decomposed.length(); i++) {
        var converted = decomposedCharacterConverter.convert(decomposed.charAt(i));

        if (converted != decomposed.charAt(i)) {
          decomposed.deleteCharAt(i);
          decomposed.insert(i, converted);
        }
      }
    }
    return STRIP_ACCENTS_PATTERN.matcher(decomposed).replaceAll(StringUtils.EMPTY_STRING);
  }

  private static final Pattern WTSP_PUNCT_PATTERN = Pattern.compile("[\\s\\p{Punct}]+");

  public static String normalizeLabel(String str) {
    str = normalize(str, "-", WTSP_PUNCT_PATTERN);
    str = StringUtils.trimChar(str, '-');
    return StringUtils.toLowerCase(str, Locale.ROOT);
  }

  private static final Pattern SP2_PATTERN = Pattern.compile("( ){2,}");
  private static final Pattern CRLF_PATTERN = Pattern.compile("\r\n");
  private static final Pattern LF3_PATTERN = Pattern.compile("(\n\\s*){3,}");

  public static String normalizeText(String text) {
    if (text == null) {
      return null;
    }
    text = normalize(text, " ", SP2_PATTERN);
    text = normalize(text, "\n", CRLF_PATTERN);
    text = normalize(text, "\n\n", LF3_PATTERN);
    return text;
  }

  private static final Pattern WTSP_PATTERN = Pattern.compile("\\s+");

  public static String normalizeString(String str) {
    if (str == null) {
      return null;
    }
    return normalize(str, " ", WTSP_PATTERN);
  }

  private static final Pattern CR_OPT_LF_PATTERN = Pattern.compile("\r?\n");

  public static String normalizeHtml(String html) {
    if (html == null) {
      return null;
    }
    html = normalize(html, "", CR_OPT_LF_PATTERN);
    return html;
  }

  public static String removeSp(String str) {
    if (str == null) {
      return null;
    }
    return normalize(str, "", WTSP_PATTERN);
  }

  public static String removeSpPunct(String str) {
    if (str == null) {
      return null;
    }
    return normalize(str, "", WTSP_PUNCT_PATTERN);
  }

  private static final Pattern NON_DIGITS_PATTERN = Pattern.compile("[^\\d]+");

  public static String digitOnly(String str) {
    if (str == null) {
      return null;
    }
    return normalize(str, "", NON_DIGITS_PATTERN);
  }

  public static String normalize(String str, String replBy, Pattern... replPatterns) {
    Arguments.notNull(replBy);

    if (str == null) {
      return null;
    }
    var repl = replBy.isEmpty() ? "" : Matcher.quoteReplacement(replBy);

    for (Pattern p : replPatterns) {
      str = p.matcher(str).replaceAll(repl);
    }
    return StringUtils.trimToNull(str);
  }

  public static String toName(String name) {
    if (name == null) {
      return null;
    }
    name = normalizeString(name);
    if (name == null) {
      return null;
    }

    var result = new StringBuilder();
    var capitalizeNext = true;

    for (char c : name.toCharArray()) {
      if (Character.isLetter(c)) {
        result.append(capitalizeNext ? Character.toTitleCase(c) : Character.toLowerCase(c));
        capitalizeNext = false;
      } else {
        result.append(c);
        capitalizeNext = (c == ' ' || c == '-' || c == '\'');
      }
    }
    return result.toString();
  }

  public static String stringAsID(String str) {
    if (str == null) {
      return "null";
    }
    str = removeSpPunct(str);
    return (str != null) ? StringUtils.toLowerCase(str, Locale.ROOT) : "null";
  }

  public static String valuesAsID(Object... values) {
    Arguments.hasElements(values);

    return Arrays.stream(values).map(v -> {
      return stringAsID(String.valueOf(v));

    }).collect(Collectors.joining("-"));
  }
}
