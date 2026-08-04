// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.YearMonth;

import com.appslandia.common.base.TemporalFormatException;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ParseUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class YearMonthConverter extends TemporalConverter<YearMonth> {

  public static final String ERROR_MSG_KEY = YearMonthConverter.class.getName() + ".message";

  public YearMonthConverter() {
    super(DateUtils.ISO8601_YEAR_MONTH);
  }

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<YearMonth> getTargetType() {
    return YearMonth.class;
  }

  @Override
  protected YearMonth doParse(String str) {
    try {
      return ParseUtils.parseYearMonth(str, DateUtils.ISO8601_YEAR_MONTH);

    } catch (TemporalFormatException ex) {
      return null;
    }
  }
}
