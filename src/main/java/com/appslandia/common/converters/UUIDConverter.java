// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.util.UUID;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class UUIDConverter implements Converter<UUID> {

  public static final String ERROR_MSG_KEY = UUIDConverter.class.getName() + ".message";

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<UUID> getTargetType() {
    return UUID.class;
  }

  @Override
  public String format(UUID obj, FormatProvider formatProvider, boolean localize) {
    if (obj == null) {
      return null;
    }
    return obj.toString();
  }

  @Override
  public UUID parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    try {
      return java.util.UUID.fromString(str);
    } catch (IllegalArgumentException ex) {
    }
    throw toParsingError(str, getTargetType().getName());
  }
}
