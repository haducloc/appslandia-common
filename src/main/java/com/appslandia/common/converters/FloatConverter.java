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
public class FloatConverter extends NumberConverter<Float> {

  public static final String ERROR_MSG_KEY = FloatConverter.class.getName() + ".message";

  final int fractionDigits;
  final RoundingMode roundingMode;

  public FloatConverter() {
    this(3);
  }

  public FloatConverter(int fractionDigits) {
    this(fractionDigits, RoundingMode.HALF_EVEN);
  }

  public FloatConverter(int fractionDigits, RoundingMode roundingMode) {
    this.fractionDigits = fractionDigits;
    this.roundingMode = roundingMode;
  }

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<Float> getTargetType() {
    return Float.class;
  }

  @Override
  public String format(Float obj, FormatProvider formatProvider, boolean localize) {
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
  public Float parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    try {
      return ParseUtils.parseFloat(str);

    } catch (NonFiniteNumberException | NumberFormatException ex) {
    }
    throw toParsingError(str, getTargetType().getName());
  }
}
