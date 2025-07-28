// The MIT License (MIT)
// Copyright © 2015 Loc Ha

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.converters;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.DateUtils;

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
  protected LocalTime doParse(String str, FormatProvider formatProvider) {
    return parse(str, getTemporalPatterns(formatProvider.getLanguage()).getTimePatterns());
  }

  private LocalTime parse(String value, Collection<String> patterns) {
    for (String pattern : patterns) {
      if (pattern.length() != value.length()) {
        continue;
      }
      try {
        return LocalTime.parse(value, DateUtils.getFormatter(pattern));
      } catch (DateTimeParseException ex) {
      }
    }
    return null;
  }
}
