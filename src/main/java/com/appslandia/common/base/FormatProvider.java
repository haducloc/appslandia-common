// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author Loc Ha
 *
 */
public interface FormatProvider {

  Language getLanguage();

  NumberFormat getNumberParser();

  NumberFormat getNumberFormat(RoundingMode roundingMode, int fractionDigits, boolean grouping);

  NumberFormat getPercentFormat(RoundingMode roundingMode, int fractionDigits, boolean grouping);

  NumberFormat getCurrencyFormat(RoundingMode roundingMode, int fractionDigits, boolean grouping);

  DecimalFormat getDecimalFormat(RoundingMode roundingMode, int fractionDigits);
}
