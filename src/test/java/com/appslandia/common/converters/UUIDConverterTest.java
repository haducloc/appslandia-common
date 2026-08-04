// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.util.UUID;

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
public class UUIDConverterTest {

  @Test
  public void test_targetType() {
    var converter = new UUIDConverter();
    Assertions.assertEquals(UUID.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new UUIDConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      var v = converter.parse("3A843675-EC8A-4F2A-A6C7-B4630A830366", formatProvider);
      Assertions.assertNotNull(v);
      Assertions.assertEquals("3a843675-ec8a-4f2a-a6c7-b4630a830366", converter.format(v, formatProvider, true));
      Assertions.assertEquals("3a843675-ec8a-4f2a-a6c7-b4630a830366", converter.format(v, formatProvider, false));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new UUIDConverter();
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
    var converter = new UUIDConverter();
    FormatProvider formatProvider = new FormatProviderImpl(Language.EN_US);
    try {
      converter.parse("3a843675-ec8a-4f2a-a6c7", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }
}
