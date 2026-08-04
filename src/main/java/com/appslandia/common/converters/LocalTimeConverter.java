// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.LocalTime;

import com.appslandia.common.base.TemporalFormatException;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ParseUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class LocalTimeConverter extends TemporalConverter<LocalTime> {

  public static final String ERROR_MSG_KEY = LocalTimeConverter.class.getName() + ".message";

  public LocalTimeConverter() {
    super(DateUtils.ISO8601_TIME_F3);
  }

  public LocalTimeConverter(String isoPattern) {
    super(isoPattern);
  }

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<LocalTime> getTargetType() {
    return LocalTime.class;
  }

  @Override
  protected LocalTime doParse(String str) {
    try {
      return ParseUtils.parseLocalTime(str, DateUtils.ISO8601_PATTERNS_TIME);

    } catch (TemporalFormatException ex) {
      return null;
    }
  }
}
