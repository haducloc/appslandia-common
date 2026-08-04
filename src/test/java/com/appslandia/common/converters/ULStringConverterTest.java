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
public class ULStringConverterTest {

  @Test
  public void test_targetType() {
    var converter = new ULCStringConverter(true);
    Assertions.assertEquals(String.class, converter.getTargetType());
  }

  @Test
  public void test_lower() {
    var converter = new ULCStringConverter(false);
    try {
      var value = "JavaEE-7";
      var str = converter.parse(value, new FormatProviderImpl(Language.EN_US));

      Assertions.assertEquals("javaee-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_upper() {
    var converter = new ULCStringConverter(true);
    try {
      var value = "JavaEE-7";
      var str = converter.parse(value, new FormatProviderImpl(Language.EN_US));

      Assertions.assertEquals("JAVAEE-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_en_lower() {
    var converter = new ULCStringConverter(false);
    try {
      var value = "JavaEE-7";
      var str = converter.parse(value, null);

      Assertions.assertEquals("javaee-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_en_upper() {
    var converter = new ULCStringConverter(true);
    try {
      var value = "JavaEE-7";
      var str = converter.parse(value, null);

      Assertions.assertEquals("JAVAEE-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invariant_lower() {
    var converter = new ULCStringConverter(false);
    try {
      var value = "JavaEE-7";
      var str = converter.parse(value, null);

      Assertions.assertEquals("javaee-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invariant_upper() {
    var converter = new ULCStringConverter(true);
    try {
      var value = "JavaEE-7";
      var str = converter.parse(value, null);

      Assertions.assertEquals("JAVAEE-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new ULCStringConverter(true);
    try {
      String value = null;
      var str = converter.parse(value, new FormatProviderImpl(Language.EN_US));

      Assertions.assertNull(str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
