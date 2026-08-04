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
public class ByteConverterTest {

  @Test
  public void test_targetType() {
    var converter = new ByteConverter();
    Assertions.assertEquals(Byte.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new ByteConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse("123", formatProvider);
      Assertions.assertEquals(123, v.byteValue());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new ByteConverter();
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
    var converter = new ByteConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      converter.parse("1,23", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
    try {
      converter.parse("12.345", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }

  @Test
  public void test_maxMin() {
    var converter = new ByteConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse(Byte.toString(Byte.MAX_VALUE), formatProvider);
      Assertions.assertEquals(Byte.MAX_VALUE, v.byteValue());
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      var v = converter.parse(Byte.toString(Byte.MIN_VALUE), formatProvider);
      Assertions.assertEquals(Byte.MIN_VALUE, v.byteValue());
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_overflow() {
    var converter = new ByteConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      // Max: 127
      converter.parse("128", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
    try {
      // MIN: -128
      converter.parse("-129", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }
}
