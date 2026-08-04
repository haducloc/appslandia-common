// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.Language;

/**
 *
 * @author Loc Ha
 *
 */
public class StringConverterTest {

  @Test
  public void test_targetType() {
    var converter = new StringConverter();
    Assertions.assertEquals(String.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new StringConverter();
    try {
      var value = " test ";
      var str = converter.parse(value, new FormatProviderImpl(Language.EN_US));

      Assertions.assertEquals("test", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new StringConverter();
    try {
      String value = null;
      var str = converter.parse(value, new FormatProviderImpl(Language.EN_US));

      Assertions.assertNull(str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
