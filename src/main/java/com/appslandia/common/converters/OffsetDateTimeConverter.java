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

import java.time.OffsetDateTime;

import com.appslandia.common.base.FormatProvider;
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
  protected OffsetDateTime doParse(String str, FormatProvider formatProvider) {
    try {
      return ParseUtils.parseOffsetDateTime(str,
          getTemporalPatterns(formatProvider.getLanguage()).getOffsetDateTimePatterns());
    } catch (TemporalFormatException ex) {
      return null;
    }
  }
}
