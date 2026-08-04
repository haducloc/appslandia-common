// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.OffsetTime;

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
public class OffsetTimeConverterTest {

  @Test
  public void test_targetType() {
    var converter = new OffsetTimeConverter();
    Assertions.assertEquals(OffsetTime.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new OffsetTimeConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse("09:30:00.999+07:00", formatProvider);
      Assertions.assertNotNull(v);

      Assertions.assertEquals("09:30:00.999+07:00", converter.format(v, formatProvider, true));
      Assertions.assertEquals("09:30:00.999+07:00", converter.format(v, formatProvider, false));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_HHmm() {
    var converter = new OffsetTimeConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse("09:30+07:00", formatProvider);
      Assertions.assertNotNull(v);

      Assertions.assertEquals("09:30:00.000+07:00", converter.format(v, formatProvider, true));
      Assertions.assertEquals("09:30:00.000+07:00", converter.format(v, formatProvider, false));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new OffsetTimeConverter();
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
}
