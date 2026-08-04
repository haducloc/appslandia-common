// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.math.RoundingMode;

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
public class DoubleConverterTest {

  @Test
  public void test_targetType() {
    var converter = new DoubleConverter(3, RoundingMode.HALF_EVEN);
    Assertions.assertEquals(Double.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new DoubleConverter(3, RoundingMode.HALF_EVEN);
    FormatProvider formatProvider = new FormatProviderImpl(Language.VI_VN);
    try {
      var v = converter.parse("1234.1236", formatProvider);
      Assertions.assertEquals(1234.1236, v, 0.00001);

      Assertions.assertEquals("1234,124", converter.format(v, formatProvider, true));
      Assertions.assertEquals("1234.124", converter.format(v, formatProvider, false));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new DoubleConverter(3, RoundingMode.HALF_EVEN);
    FormatProvider formatProvider = new FormatProviderImpl(Language.VI_VN);
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
  public void test_negative_zero() {
    var converter = new DoubleConverter(3, RoundingMode.HALF_EVEN);
    FormatProvider formatProvider = new FormatProviderImpl(Language.VI_VN);
    try {
      var val = converter.format(-0.0001d, formatProvider, true);
      Assertions.assertEquals("-0,000", val);

      val = converter.format(-0.0001d, formatProvider, false);
      Assertions.assertEquals("-0.000", val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invalid() {
    var converter = new DoubleConverter(3, RoundingMode.HALF_EVEN);
    FormatProvider formatProvider = new FormatProviderImpl(Language.VI_VN);
    try {
      converter.parse("12.345,678", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }
}
