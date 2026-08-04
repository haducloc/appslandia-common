// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class LocalDateConverter extends TemporalConverter<LocalDate> {

  public static final String ERROR_MSG_KEY = LocalDateConverter.class.getName() + ".message";

  public LocalDateConverter() {
    super(DateUtils.ISO8601_DATE);
  }

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<LocalDate> getTargetType() {
    return LocalDate.class;
  }

  @Override
  protected LocalDate doParse(String str) {
    try {
      return LocalDate.parse(str);

    } catch (DateTimeParseException ex) {
      return null;
    }
  }
}
