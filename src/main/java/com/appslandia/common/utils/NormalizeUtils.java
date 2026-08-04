// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
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

  private static final Pattern WSPT1_PATTERN = Pattern.compile("[\\s\\p{Punct}]+");

  public static String normalizeLabel(String str) {
    str = normalize(str, "-", WSPT1_PATTERN);
    str = StringUtils.trimChar(str, '-');

    return StringUtils.toLowerCase(str, Locale.ROOT);
  }

  private static final Pattern CRLF3_PATTERN = Pattern.compile("(\r?\n){3,}");

  public static String normalizeText(String text) {
    if (text == null) {
      return null;
    }
    var br = new BufferedReader(new StringReader(text));
    List<String> lines = null;
    try {
      lines = IOUtils.readAllLines(br);
    } catch (IOException ex) {
      // Never happen
    }

    for (var i = 0; i < lines.size(); i++) {
      var line = lines.get(i).stripTrailing();
      lines.set(i, !line.isBlank() ? line : "");
    }

    // Build Text
    text = String.join(System.lineSeparator(), lines);

    // Collapse (\r?\n){3,}
    text = normalize(text, System.lineSeparator().repeat(2), CRLF3_PATTERN);
    return text;
  }

  private static final Pattern WS1_PATTERN = Pattern.compile("\\s+");

  public static String normalizeString(String str) {
    if (str == null) {
      return null;
    }
    return normalize(str, " ", WS1_PATTERN);
  }

  private static final Pattern CRLF1_PATTERN = Pattern.compile("(\r?\n)+");

  public static String normalizeHtml(String html) {
    if (html == null) {
      return null;
    }
    html = normalize(html, "", CRLF1_PATTERN);
    return html;
  }

  public static String removeSp(String str) {
    if (str == null) {
      return null;
    }
    return normalize(str, "", WS1_PATTERN);
  }

  public static String removeSpPunct(String str) {
    if (str == null) {
      return null;
    }
    return normalize(str, "", WSPT1_PATTERN);
  }

  private static final Pattern NON_DIGITS_PATTERN = Pattern.compile("[^\\d]+");

  public static String digitOnly(String str) {
    if (str == null) {
      return null;
    }
    return normalize(str, "", NON_DIGITS_PATTERN);
  }

  private static final Pattern NON_ASCII_PATTERN = Pattern.compile("[^\\x20-\\x7E]");

  public static String normalizeAscii(String str, String replaceBy) {
    if (str == null) {
      return null;
    }
    return normalize(str, replaceBy, NON_ASCII_PATTERN);
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
