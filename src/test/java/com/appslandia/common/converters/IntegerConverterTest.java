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
public class IntegerConverterTest {

  @Test
  public void test_targetType() {
    var converter = new IntegerConverter();
    Assertions.assertEquals(Integer.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new IntegerConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse("12345", formatProvider);
      Assertions.assertEquals(12345, v.intValue());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new IntegerConverter();
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
  public void test_invalid() {
    var converter = new IntegerConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      converter.parse("12,345", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
    try {
      converter.parse("12345.67", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }

  @Test
  public void test_maxMin() {
    var converter = new IntegerConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse(Integer.toString(Integer.MAX_VALUE), formatProvider);
      Assertions.assertEquals(Integer.MAX_VALUE, v.intValue());
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      var v = converter.parse(Integer.toString(Integer.MIN_VALUE), formatProvider);
      Assertions.assertEquals(Integer.MIN_VALUE, v.intValue());
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_overflow() {
    var converter = new IntegerConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      // Max: 2147483647
      converter.parse("2147483648", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
    try {
      // MIN: -2147483648
      converter.parse("-2147483649", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }
}
