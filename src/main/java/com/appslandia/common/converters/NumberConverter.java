// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class NumberConverter<T extends Number> implements Converter<T> {

  protected static Number parseNumber(String str, NumberFormat numberFormat) {
    var pos = new ParsePosition(0);
    var parsedValue = numberFormat.parse(str, pos);

    if ((pos.getErrorIndex() < 0) && (pos.getIndex() == str.length()) && (parsedValue != null)) {
      return parsedValue;
    }
    return null;
  }
}
