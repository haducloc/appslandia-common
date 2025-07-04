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

import java.time.temporal.Temporal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.Language;
import com.appslandia.common.base.TemporalPatterns;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class TemporalConverter<T extends Temporal> implements Converter<T> {

  final String isoPattern;

  public TemporalConverter(String isoPattern) {
    this.isoPattern = isoPattern;
  }

  /**
   * Return null if the given string fails to parse.
   *
   * @param str
   * @param formatProvider
   * @return
   */
  protected abstract T doParse(String str, FormatProvider formatProvider);

  @Override
  public String format(T obj, FormatProvider formatProvider, boolean localize) {
    if (obj == null) {
      return null;
    }
    if (localize) {
      var pattern = formatProvider.getLanguage().getTemporalPattern(this.isoPattern);
      return DateUtils.getFormatter(pattern).format(obj);
    }
    return DateUtils.getFormatter(this.isoPattern).format(obj);
  }

  @Override
  public T parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    var t = doParse(str, formatProvider);
    if (t == null) {
      throw toParsingError(str, getTargetType().getName());
    }
    return t;
  }

  private static final class TemporalPatternsHolder {
    private static final ConcurrentMap<Language, TemporalPatterns> PATTERNS = new ConcurrentHashMap<>();
  }

  public static TemporalPatterns getTemporalPatterns(Language language) {
    return TemporalPatternsHolder.PATTERNS.computeIfAbsent(language, l -> new TemporalPatterns().setLanguage(l));
  }
}
