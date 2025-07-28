// The MIT License (MIT)
// Copyright © 2015 Loc Ha

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.converters;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.Language;

/**
 *
 * @author Loc Ha
 *
 */
public class CasedStringConverterTest {

  @Test
  public void test_targetType() {
    var converter = new ULStringConverter(true);
    Assertions.assertEquals(String.class, converter.getTargetType());
  }

  @Test
  public void test_lower() {
    var converter = new ULStringConverter(false);
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
    var converter = new ULStringConverter(true);
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
    var converter = new ULStringConverter(false, Locale.ENGLISH);
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
    var converter = new ULStringConverter(true, Locale.ENGLISH);
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
    var converter = new ULStringConverter(false, Locale.ROOT);
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
    var converter = new ULStringConverter(true, Locale.ROOT);
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
    var converter = new ULStringConverter(true);
    try {
      String value = null;
      var str = converter.parse(value, new FormatProviderImpl(Language.EN_US));

      Assertions.assertNull(str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
