// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class TextTransformTest {

  TextTransform textTransform;

  @BeforeEach
  public void setUp() {
    textTransform = new TextTransform();
  }

  @Test
  public void testTransformTitleCase() {
    textTransform.setCaseType(TextTransform.CaseType.TITLE);
    var result = textTransform.transform("the quick brown fox jumps over the lazy dog");
    Assertions.assertEquals("The Quick Brown Fox Jumps Over the Lazy Dog", result);
  }

  @Test
  public void testTransformUpperCase() {
    textTransform.setCaseType(TextTransform.CaseType.UPPER);
    var result = textTransform.transform("the quick brown fox");
    Assertions.assertEquals("THE QUICK BROWN FOX", result);
  }

  @Test
  public void testTransformLowerCase() {
    textTransform.setCaseType(TextTransform.CaseType.LOWER);
    var result = textTransform.transform("The Quick Brown Fox");
    Assertions.assertEquals("the quick brown fox", result);
  }

  @Test
  public void testTransformCamelCase() {
    textTransform.setCaseType(TextTransform.CaseType.CAMEL);
    var result = textTransform.transform("the quick brown fox jumps over the lazy dog");
    Assertions.assertEquals("theQuickBrownFoxJumpsOverTheLazyDog", result);
  }

  @Test
  public void testTransformPascalCase() {
    textTransform.setCaseType(TextTransform.CaseType.PASCAL);
    var result = textTransform.transform("the quick brown fox jumps over the lazy dog");
    Assertions.assertEquals("TheQuickBrownFoxJumpsOverTheLazyDog", result);
  }

  @Test
  public void testTransformSnakeCase() {
    textTransform.setCaseType(TextTransform.CaseType.SNAKE);
    var result = textTransform.transform("the quick brown fox jumps over the lazy dog");
    Assertions.assertEquals("the_quick_brown_fox_jumps_over_the_lazy_dog", result);
  }

  @Test
  public void testTransformScreamingSnakeCase() {
    textTransform.setCaseType(TextTransform.CaseType.SCREAMING_SNAKE);
    var result = textTransform.transform("the quick brown fox jumps over the lazy dog");
    Assertions.assertEquals("THE_QUICK_BROWN_FOX_JUMPS_OVER_THE_LAZY_DOG", result);
  }

  @Test
  public void testTransformKebabCase() {
    textTransform.setCaseType(TextTransform.CaseType.KEBAB);
    var result = textTransform.transform("the quick brown fox jumps over the lazy dog");
    Assertions.assertEquals("the-quick-brown-fox-jumps-over-the-lazy-dog", result);
  }

  @Test
  public void testTransformTrainCase() {
    textTransform.setCaseType(TextTransform.CaseType.TRAIN);
    var result = textTransform.transform("the quick brown fox jumps over the lazy dog");
    Assertions.assertEquals("The-Quick-Brown-Fox-Jumps-Over-The-Lazy-Dog", result);
  }
}
