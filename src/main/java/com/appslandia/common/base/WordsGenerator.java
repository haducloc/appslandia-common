// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.regex.Pattern;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.CharUtils;
import com.appslandia.common.utils.SecureRand;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class WordsGenerator extends InitializingObject implements TextGenerator {

  private static final char[] ALPHABET_LOWER = CharUtils.toCharRanges("a-z");
  private static final char[] ALPHABET_UPPER = CharUtils.toCharRanges("A-Z");
  private static final char[] ALPHABET_DIGITS = CharUtils.toCharRanges("0-9");

  private static final Pattern DIGITS_PATTERN = Pattern.compile("[\\d]+");
  private static final Pattern DIGITS_AZ_PATTERN = Pattern.compile("[a-zA-Z\\d]+");
  private static final Pattern DIGITS_AZ_LOWER_PATTERN = Pattern.compile("[a-z\\d]+");
  private static final Pattern DIGITS_AZ_UPPER_PATTERN = Pattern.compile("[A-Z\\d]+");

  private int length;
  private Alphabet alphabet;

  public enum Alphabet {
    DIGITS, AZ, AZ_UPPER, AZ_LOWER, DIGITS_AZ_UPPER, DIGITS_AZ_LOWER, DIGITS_AZ
  }

  @Override
  protected void init() throws Exception {
    Arguments.isTrue(length > 0, "length is required.");
    alphabet = ValueUtils.valueOrAlt(alphabet, Alphabet.DIGITS_AZ);
  }

  @Override
  public String generate() {
    initialize();
    return switch (alphabet) {
    case DIGITS_AZ -> generate(new char[][] { ALPHABET_DIGITS, ALPHABET_UPPER, ALPHABET_LOWER });
    case DIGITS_AZ_UPPER -> generate(new char[][] { ALPHABET_DIGITS, ALPHABET_UPPER });
    case DIGITS_AZ_LOWER -> generate(new char[][] { ALPHABET_DIGITS, ALPHABET_LOWER });
    case AZ -> generate(new char[][] { ALPHABET_UPPER, ALPHABET_LOWER });
    case AZ_UPPER -> generate(new char[][] { ALPHABET_UPPER });
    case AZ_LOWER -> generate(new char[][] { ALPHABET_LOWER });
    default -> generate(new char[][] { ALPHABET_DIGITS });
    };
  }

  private String generate(char[][] sources) {
    var rdChars = CharUtils.randomChars(length, sources, SecureRand.getInstance());
    return new String(rdChars, 0, length);
  }

  @Override
  public boolean verify(String value) {
    Arguments.notNull(value);
    if (value.length() != length) {
      return false;
    }
    return switch (alphabet) {
    case DIGITS_AZ -> DIGITS_AZ_PATTERN.matcher(value).matches();
    case DIGITS_AZ_UPPER -> DIGITS_AZ_UPPER_PATTERN.matcher(value).matches();
    case DIGITS_AZ_LOWER -> DIGITS_AZ_LOWER_PATTERN.matcher(value).matches();
    default -> DIGITS_PATTERN.matcher(value).matches();
    };
  }

  public WordsGenerator setLength(int length) {
    assertNotInitialized();
    this.length = length;
    return this;
  }

  public WordsGenerator setAlphabet(Alphabet alphabet) {
    assertNotInitialized();
    this.alphabet = alphabet;
    return this;
  }
}
