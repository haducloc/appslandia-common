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

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class BigDecimalConverter extends NumberConverter<BigDecimal> {

  public static final String ERROR_MSG_KEY = BigDecimalConverter.class.getName() + ".message";

  final int fractionDigits;
  final RoundingMode roundingMode;

  public BigDecimalConverter() {
    this(3);
  }

  public BigDecimalConverter(int fractionDigits) {
    this(fractionDigits, RoundingMode.HALF_EVEN);
  }

  public BigDecimalConverter(int fractionDigits, RoundingMode roundingMode) {
    this.fractionDigits = fractionDigits;
    this.roundingMode = roundingMode;
  }

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<BigDecimal> getTargetType() {
    return BigDecimal.class;
  }

  @Override
  public String format(BigDecimal obj, FormatProvider formatProvider, boolean localize) {
    if (obj == null) {
      return null;
    }
    if (localize) {
      var nf = formatProvider.getNumberFormat(this.roundingMode, this.fractionDigits, false);
      return nf.format(obj);
    }
    return formatProvider.getDecimalFormat(this.roundingMode, this.fractionDigits).format(obj);
  }

  @Override
  public BigDecimal parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    try {
      return new BigDecimal(str);
    } catch (NumberFormatException ex) {
    }

    var val = parseNumber(str, formatProvider.getNumberParser());

    return switch (val) {
    case null -> throw toParsingError(str, getTargetType().getName());
    case BigDecimal bd -> bd;
    case Double d when Double.isNaN(d) -> throw toParsingError(str, getTargetType().getName());
    default -> new BigDecimal(val.toString());
    };
  }
}
