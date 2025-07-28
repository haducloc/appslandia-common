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
import com.appslandia.common.utils.NormalizeUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class StringConverter implements Converter<String> {

  public static final String ERROR_MSG_KEY = StringConverter.class.getName() + ".message";

  @Override
  public String getErrorMsgKey() {
    return ERROR_MSG_KEY;
  }

  @Override
  public Class<String> getTargetType() {
    return String.class;
  }

  @Override
  public String format(String obj, FormatProvider formatProvider, boolean localize) {
    return obj;
  }

  @Override
  public String parse(String str, FormatProvider formatProvider) throws ConverterException {
    if (str == null) {
      return null;
    }
    return NormalizeUtils.normalizeString(str);
  }
}
