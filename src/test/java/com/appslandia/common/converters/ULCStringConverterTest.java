// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ULCStringConverterTest {

  @Test
  public void test_targetType() {
    ULCStringConverter converter = new ULCStringConverter(true);
    Assertions.assertEquals(String.class, converter.getTargetType());
  }

  @Test
  public void test_lower() {
    ULCStringConverter converter = new ULCStringConverter(false);
    try {
      String value = "JavaEE-7";
      String str = converter.parse(value, new FormatProviderImpl(Language.EN_US));

      Assertions.assertEquals("javaee-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_upper() {
    ULCStringConverter converter = new ULCStringConverter(true);
    try {
      String value = "JavaEE-7";
      String str = converter.parse(value, new FormatProviderImpl(Language.EN_US));

      Assertions.assertEquals("JAVAEE-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_en_lower() {
    ULCStringConverter converter = new ULCStringConverter(false, Locale.ENGLISH);
    try {
      String value = "JavaEE-7";
      String str = converter.parse(value, null);

      Assertions.assertEquals("javaee-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_en_upper() {
    ULCStringConverter converter = new ULCStringConverter(true, Locale.ENGLISH);
    try {
      String value = "JavaEE-7";
      String str = converter.parse(value, null);

      Assertions.assertEquals("JAVAEE-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invariant_lower() {
    ULCStringConverter converter = new ULCStringConverter(false, Locale.ROOT);
    try {
      String value = "JavaEE-7";
      String str = converter.parse(value, null);

      Assertions.assertEquals("javaee-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invariant_upper() {
    ULCStringConverter converter = new ULCStringConverter(true, Locale.ROOT);
    try {
      String value = "JavaEE-7";
      String str = converter.parse(value, null);

      Assertions.assertEquals("JAVAEE-7", str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    ULCStringConverter converter = new ULCStringConverter(true);
    try {
      String value = null;
      String str = converter.parse(value, new FormatProviderImpl(Language.EN_US));

      Assertions.assertNull(str);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
