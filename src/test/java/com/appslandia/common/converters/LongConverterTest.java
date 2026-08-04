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
public class LongConverterTest {

  @Test
  public void test_targetType() {
    var converter = new LongConverter();
    Assertions.assertEquals(Long.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new LongConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse("12345", formatProvider);
      Assertions.assertEquals(12345, v.longValue());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new LongConverter();
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
    var converter = new LongConverter();
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
    var converter = new LongConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse(Long.toString(Long.MAX_VALUE), formatProvider);
      Assertions.assertEquals(Long.MAX_VALUE, v.longValue());
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      var v = converter.parse(Long.toString(Long.MIN_VALUE), formatProvider);
      Assertions.assertEquals(Long.MIN_VALUE, v.longValue());
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
