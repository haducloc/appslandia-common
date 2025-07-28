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
public class FloatConverterTest {

  @Test
  public void test_targetType() {
    var converter = new FloatConverter(2, RoundingMode.HALF_EVEN);
    Assertions.assertEquals(Float.class, converter.getTargetType());
  }

  @Test
  public void test() {
    var converter = new FloatConverter(2, RoundingMode.HALF_EVEN);
    FormatProvider formatProvider = new FormatProviderImpl(Language.VI_VN);
    try {
      var v = converter.parse("1234.126", formatProvider);
      Assertions.assertEquals(1234.126, v.doubleValue(), 0.0001);

      Assertions.assertEquals("1234,13", converter.format(v, formatProvider, true));
      Assertions.assertEquals("1234.13", converter.format(v, formatProvider, false));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_null() {
    var converter = new FloatConverter(2, RoundingMode.HALF_EVEN);
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
  public void test_negative_zero() {
    var converter = new FloatConverter(3, RoundingMode.HALF_EVEN);
    FormatProvider formatProvider = new FormatProviderImpl(Language.VI_VN);
    try {
      var val = converter.format(-0.0001f, formatProvider, true);
      Assertions.assertEquals("-0,000", val);

      val = converter.format(-0.0001f, formatProvider, false);
      Assertions.assertEquals("-0.000", val);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_invalid() {
    var converter = new FloatConverter(2, RoundingMode.HALF_EVEN);
    FormatProvider formatProvider = new FormatProviderImpl(Language.VI_VN);
    try {
      converter.parse("1.234,56", formatProvider);
      Assertions.fail();
    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ConverterException);
    }
  }
}
