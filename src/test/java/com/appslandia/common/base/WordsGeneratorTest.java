// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class WordsGeneratorTest {

  @Test
  public void test() {
    for (var i = 1; i <= 1000; i++) {
      var impl = new WordsGenerator().setLength(i);
      var str = impl.generate();

      Assertions.assertEquals(i, str.length());
      Assertions.assertTrue(impl.verify(str));
    }
  }

  @Test
  public void test_DIGITS() {
    for (var i = 1; i <= 1000; i++) {

      var impl = new WordsGenerator().setAlphabet(WordsGenerator.Alphabet.DIGITS).setLength(i);
      var str = impl.generate();

      Assertions.assertEquals(i, str.length());
      Assertions.assertTrue(impl.verify(str));
    }
  }

  @Test
  public void test_DIGITS_AZ() {
    for (var i = 1; i <= 1000; i++) {

      var impl = new WordsGenerator().setAlphabet(WordsGenerator.Alphabet.DIGITS_AZ).setLength(i);
      var str = impl.generate();

      Assertions.assertEquals(i, str.length());
      Assertions.assertTrue(impl.verify(str));
    }
  }

  @Test
  public void test_DIGITS_AZ_UPPER() {
    for (var i = 1; i <= 1000; i++) {

      var impl = new WordsGenerator().setAlphabet(WordsGenerator.Alphabet.DIGITS_AZ_UPPER).setLength(i);
      var str = impl.generate();

      Assertions.assertEquals(i, str.length());
      Assertions.assertTrue(impl.verify(str));
    }
  }

  @Test
  public void test_DIGITS_AZ_LOWER() {
    for (var i = 1; i <= 1000; i++) {

      var impl = new WordsGenerator().setAlphabet(WordsGenerator.Alphabet.DIGITS_AZ_LOWER).setLength(i);
      var str = impl.generate();

      Assertions.assertEquals(i, str.length());
      Assertions.assertTrue(impl.verify(str));
    }
  }
}
