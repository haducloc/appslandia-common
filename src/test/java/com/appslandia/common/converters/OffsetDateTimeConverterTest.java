// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.OffsetDateTime;

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
public class OffsetDateTimeConverterTest {

  @Test
  public void test_targetType() {
    var converter = new OffsetDateTimeConverter();
    Assertions.assertEquals(OffsetDateTime.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new OffsetDateTimeConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse("2010-10-10T09:30:00.999+07:00", formatProvider);
      Assertions.assertNotNull(v);

      Assertions.assertEquals("10/10/2010 09:30:00.999+07:00", converter.format(v, formatProvider, true));
      Assertions.assertEquals("2010-10-10T09:30:00.999+07:00", converter.format(v, formatProvider, false));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_HHmm() {
    var converter = new OffsetDateTimeConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse("2010-10-10T09:30+07:00", formatProvider);
      Assertions.assertNotNull(v);

      Assertions.assertEquals("10/10/2010 09:30:00.000+07:00", converter.format(v, formatProvider, true));
      Assertions.assertEquals("2010-10-10T09:30:00.000+07:00", converter.format(v, formatProvider, false));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new OffsetDateTimeConverter();
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
    var converter = new OffsetDateTimeConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      converter.parse("10/10/2010T09:30:61+07:00", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }
}
