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

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.ParseUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class ByteConverter extends NumberConverter<Byte> {

  public static final String ERROR_MSG_KEY = ByteConverter.class.getName() + ".message";

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<Byte> getTargetType() {
    return Byte.class;
  }

  @Override
  public String format(Byte obj, FormatProvider formatProvider, boolean localize) {
    if (obj == null) {
      return null;
    }
    return obj.toString();
  }

  @Override
  public Byte parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    try {
      return ParseUtils.parseByte(str);
    } catch (NumberFormatException ex) {
    }
    throw toParsingError(str, getTargetType().getName());
  }
}
