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

import java.util.Locale;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class EnumConverter<T extends Enum<T>> implements Converter<T> {

  final Class<T> type;

  public EnumConverter(Class<T> type) {
    this.type = Arguments.notNull(type);
  }

  @Override
  public Class<T> getTargetType() {
    return this.type;
  }

  @Override
  public String format(T obj, FormatProvider formatProvider, boolean localize) {
    if (obj == null) {
      return null;
    }
    return obj.toString();
  }

  @Override
  public T parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    try {
      return Enum.valueOf(this.type, str.toUpperCase(Locale.ENGLISH));
    } catch (IllegalArgumentException ex) {
    }
    throw toParsingError(str, getTargetType().getName());
  }
}
