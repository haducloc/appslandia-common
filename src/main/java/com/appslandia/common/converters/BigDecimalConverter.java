// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
      var nf = formatProvider.getNumberFormat(roundingMode, fractionDigits, false);
      return nf.format(obj);
    }
    return formatProvider.getDecimalFormat(roundingMode, fractionDigits).format(obj);
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
    throw toParsingError(str, getTargetType().getName());
  }
}
