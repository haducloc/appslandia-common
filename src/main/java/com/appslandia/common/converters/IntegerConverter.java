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
public class IntegerConverter extends NumberConverter<Integer> {

  public static final String ERROR_MSG_KEY = IntegerConverter.class.getName() + ".message";

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<Integer> getTargetType() {
    return Integer.class;
  }

  @Override
  public String format(Integer obj, FormatProvider formatProvider, boolean localize) {
    if (obj == null) {
      return null;
    }
    return obj.toString();
  }

  @Override
  public Integer parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    try {
      return ParseUtils.parseInt(str);
    } catch (NumberFormatException ex) {
    }
    throw toParsingError(str, getTargetType().getName());
  }
}
