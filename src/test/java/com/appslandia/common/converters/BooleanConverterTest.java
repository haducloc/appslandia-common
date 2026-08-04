// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.Language;

/**
 *
 * @author Loc Ha
 *
 */
public class BooleanConverterTest {

  @Test
  public void test_targetType() {
    var converter = new BooleanConverter();
    Assertions.assertEquals(Boolean.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new BooleanConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var val = converter.parse("true", formatProvider);
      Assertions.assertNotNull(val);
      Assertions.assertTrue(val);

      val = converter.parse("false", formatProvider);
      Assertions.assertNotNull(val);
      Assertions.assertFalse(val);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new BooleanConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var val = converter.parse(null, formatProvider);
      Assertions.assertNull(val);

      val = converter.parse("", formatProvider);
      Assertions.assertNull(val);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_yesno() {
    var converter = new BooleanConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var val = converter.parse("yes", formatProvider);
      Assertions.assertNotNull(val);
      Assertions.assertTrue(val);

      val = converter.parse("no", formatProvider);
      Assertions.assertNotNull(val);
      Assertions.assertFalse(val);

    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }

  @Test
  public void test_yn() {
    var converter = new BooleanConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var val = converter.parse("y", formatProvider);
      Assertions.assertNotNull(val);
      Assertions.assertTrue(val);

      val = converter.parse("n", formatProvider);
      Assertions.assertNotNull(val);
      Assertions.assertFalse(val);

    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }

  @Test
  public void test_tf() {
    var converter = new BooleanConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var val = converter.parse("t", formatProvider);
      Assertions.assertNotNull(val);
      Assertions.assertTrue(val);

      val = converter.parse("f", formatProvider);
      Assertions.assertNotNull(val);
      Assertions.assertFalse(val);

    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }
}
