// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.ParseUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class ByteConverter extends NumberConverter<Byte> {

  public static final String ERROR_MSG_KEY = ByteConverter.class.getName() + ".message";

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<Byte> getTargetType() {
    return Byte.class;
  }

  @Override
  public String format(Byte obj, FormatProvider formatProvider, boolean localize) {
    if (obj == null) {
      return null;
    }
    return obj.toString();
  }

  @Override
  public Byte parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    try {
      return ParseUtils.parseByte(str);
    } catch (NumberFormatException ex) {
    }
    throw toParsingError(str, getTargetType().getName());
  }
}
