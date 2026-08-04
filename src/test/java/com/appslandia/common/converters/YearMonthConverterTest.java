// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.YearMonth;

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
public class YearMonthConverterTest {

  @Test
  public void test_targetType() {
    var converter = new YearMonthConverter();
    Assertions.assertEquals(YearMonth.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new YearMonthConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse("2010-10", formatProvider);
      Assertions.assertNotNull(v);
      Assertions.assertEquals("10/2010", converter.format(v, formatProvider, true));
      Assertions.assertEquals("2010-10", converter.format(v, formatProvider, false));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new YearMonthConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse(null, formatProvider);
      Assertions.assertNull(v);

      v = converter.parse("", formatProvider);
      Assertions.assertNull(v);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invalid() {
    var converter = new YearMonthConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      converter.parse("2010-13", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }
}
