// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.LocalDateTime;

import com.appslandia.common.base.TemporalFormatException;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ParseUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class LocalDateTimeConverter extends TemporalConverter<LocalDateTime> {

  public static final String ERROR_MSG_KEY = LocalDateTimeConverter.class.getName() + ".message";

  public LocalDateTimeConverter() {
    super(DateUtils.ISO8601_DATETIME_F3);
  }

  public LocalDateTimeConverter(String isoPattern) {
    super(isoPattern);
  }

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<LocalDateTime> getTargetType() {
    return LocalDateTime.class;
  }

  @Override
  protected LocalDateTime doParse(String str) {
    try {
      return ParseUtils.parseLocalDateTime(str, DateUtils.ISO8601_PATTERNS_DATETIME);

    } catch (TemporalFormatException ex) {
      return null;
    }
  }
}
