// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class InstantConverter implements Converter<Instant> {

  public static final String ERROR_MSG_KEY = InstantConverter.class.getName() + ".message";

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<Instant> getTargetType() {
    return Instant.class;
  }

  @Override
  public String format(Instant obj, FormatProvider formatProvider, boolean localize) {
    if (obj == null) {
      return null;
    }
    return obj.toString();
  }

  @Override
  public Instant parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    try {
      return Instant.parse(str);
    } catch (DateTimeParseException ex) {
    }
    throw toParsingError(str, getTargetType().getName());
  }
}
