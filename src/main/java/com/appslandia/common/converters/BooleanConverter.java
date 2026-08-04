// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.util.Locale;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.ParseUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class BooleanConverter implements Converter<Boolean> {

  public static final String ERROR_MSG_KEY = BooleanConverter.class.getName() + ".message";

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<Boolean> getTargetType() {
    return Boolean.class;
  }

  @Override
  public String format(Boolean obj, FormatProvider formatProvider, boolean localize) {
    if (obj == null) {
      return null;
    }
    return obj.toString();
  }

  @Override
  public Boolean parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    var val = str.toLowerCase(Locale.ENGLISH);
    if (ParseUtils.isTrueValue(val)) {
      return Boolean.TRUE;
    }
    if (ParseUtils.isFalseValue(val)) {
      return Boolean.FALSE;
    }
    throw toParsingError(str, getTargetType().getName());
  }
}
