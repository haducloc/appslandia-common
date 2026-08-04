// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.util.Locale;

import com.appslandia.common.base.FormatProvider;

/**
 *
 * @author Loc Ha
 *
 */
public class ULCStringConverter implements Converter<String> {

  public static final String ERROR_MSG_KEY = ULCStringConverter.class.getName() + ".message";

  final boolean uppercase;

  public ULCStringConverter(boolean uppercase) {
    this.uppercase = uppercase;
  }

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<String> getTargetType() {
    return String.class;
  }

  @Override
  public String format(String obj, FormatProvider formatProvider, boolean localize) {
    return obj;
  }

  @Override
  public String parse(String str, FormatProvider formatProvider) throws ConverterException {
    if (str == null) {
      return str;
    }
    return uppercase ? str.toUpperCase(Locale.ROOT) : str.toLowerCase(Locale.ROOT);
  }
}
