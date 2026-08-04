// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.UserProfileUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class PhoneNumberConverter implements Converter<String> {

  public static final String ERROR_MSG_KEY = PhoneNumberConverter.class.getName() + ".message";

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
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    str = UserProfileUtils.toPhoneNumber(str);

    if (!UserProfileUtils.isValidPhoneNumber(str)) {
      throw toParsingError(str, "Phone Number");
    }
    return str;
  }
}
