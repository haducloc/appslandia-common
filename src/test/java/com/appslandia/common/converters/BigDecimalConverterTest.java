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

import java.math.BigDecimal;
import java.math.RoundingMode;

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
public class BigDecimalConverterTest {

  @Test
  public void test_targetType() {
    var converter = new BigDecimalConverter(3, RoundingMode.HALF_EVEN);
    Assertions.assertEquals(BigDecimal.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new BigDecimalConverter(3, RoundingMode.HALF_EVEN);
    FormatProvider formatProvider = new FormatProviderImpl(Language.VI_VN);
    try {
      var v = converter.parse("1234.1235", formatProvider);
      Assertions.assertEquals(1234.1235, v.doubleValue(), 0.00001);

      Assertions.assertEquals("1234,124", converter.format(v, formatProvider, true));
      Assertions.assertEquals("1234.124", converter.format(v, formatProvider, false));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new BigDecimalConverter(3, RoundingMode.HALF_EVEN);
    FormatProvider formatProvider = new FormatProviderImpl(Language.VI_VN);
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
    var converter = new BigDecimalConverter(3, RoundingMode.HALF_EVEN);
    FormatProvider formatProvider = new FormatProviderImpl(Language.VI_VN);
    try {
      converter.parse("12.345,678", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }
}
