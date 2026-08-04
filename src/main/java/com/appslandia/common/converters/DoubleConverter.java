// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.math.RoundingMode;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.NonFiniteNumberException;
import com.appslandia.common.utils.ParseUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class DoubleConverter extends NumberConverter<Double> {

  public static final String ERROR_MSG_KEY = DoubleConverter.class.getName() + ".message";

  final int fractionDigits;
  final RoundingMode roundingMode;

  public DoubleConverter() {
    this(3);
  }

  public DoubleConverter(int fractionDigits) {
    this(fractionDigits, RoundingMode.HALF_EVEN);
  }

  public DoubleConverter(int fractionDigits, RoundingMode roundingMode) {
    this.fractionDigits = fractionDigits;
    this.roundingMode = roundingMode;
  }

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<Double> getTargetType() {
    return Double.class;
  }

  @Override
  public String format(Double obj, FormatProvider formatProvider, boolean localize) {
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
  public Double parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    try {
      return ParseUtils.parseDouble(str);

    } catch (NonFiniteNumberException | NumberFormatException ex) {
    }
    throw toParsingError(str, getTargetType().getName());
  }
}
