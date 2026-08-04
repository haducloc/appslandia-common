// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.OffsetTime;

import com.appslandia.common.base.TemporalFormatException;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ParseUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class OffsetTimeConverter extends TemporalConverter<OffsetTime> {

  public static final String ERROR_MSG_KEY = OffsetTimeConverter.class.getName() + ".message";

  public OffsetTimeConverter() {
    super(DateUtils.ISO8601_TIMEZ_F3);
  }

  public OffsetTimeConverter(String isoPattern) {
    super(isoPattern);
  }

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<OffsetTime> getTargetType() {
    return OffsetTime.class;
  }

  @Override
  protected OffsetTime doParse(String str) {
    try {
      return ParseUtils.parseOffsetTime(str, DateUtils.ISO8601_PATTERNS_TIMEZ);

    } catch (TemporalFormatException ex) {
      return null;
    }
  }
}
