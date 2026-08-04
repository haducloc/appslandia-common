// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.LocalTime;

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
public class LocalTimeConverterTest {

  @Test
  public void test_targetType() {
    var converter = new LocalTimeConverter();
    Assertions.assertEquals(LocalTime.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new LocalTimeConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse("09:30:00.999", formatProvider);
      Assertions.assertNotNull(v);

      Assertions.assertEquals("09:30:00.999", converter.format(v, formatProvider, true));
      Assertions.assertEquals("09:30:00.999", converter.format(v, formatProvider, false));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_HHmm() {
    var converter = new LocalTimeConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse("09:30", formatProvider);
      Assertions.assertNotNull(v);

      Assertions.assertEquals("09:30:00.000", converter.format(v, formatProvider, true));
      Assertions.assertEquals("09:30:00.000", converter.format(v, formatProvider, false));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new LocalTimeConverter();
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
