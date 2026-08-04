// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.OffsetDateTime;

import com.appslandia.common.base.TemporalFormatException;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ParseUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class OffsetDateTimeConverter extends TemporalConverter<OffsetDateTime> {

  public static final String ERROR_MSG_KEY = OffsetDateTimeConverter.class.getName() + ".message";

  public OffsetDateTimeConverter() {
    super(DateUtils.ISO8601_DATETIMEZ_F3);
  }

  public OffsetDateTimeConverter(String isoPattern) {
    super(isoPattern);
  }

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<OffsetDateTime> getTargetType() {
    return OffsetDateTime.class;
  }

  @Override
  protected OffsetDateTime doParse(String str) {
    try {
      return ParseUtils.parseOffsetDateTime(str, DateUtils.ISO8601_PATTERNS_DATETIMEZ);

    } catch (TemporalFormatException ex) {
      return null;
    }
  }
}
