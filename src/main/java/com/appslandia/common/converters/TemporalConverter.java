// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import java.time.temporal.Temporal;

import com.appslandia.common.base.FormatProvider;
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
   * @return
   */
  protected abstract T doParse(String str);

  @Override
  public String format(T obj, FormatProvider formatProvider, boolean localize) {
    if (obj == null) {
      return null;
    }
    if (localize) {
      var pattern = formatProvider.getLanguage().getTemporalPattern(isoPattern);
      return DateUtils.getFormatter(pattern).format(obj);
    }
    return DateUtils.getFormatter(isoPattern).format(obj);
  }

  @Override
  public T parse(String str, FormatProvider formatProvider) throws ConverterException {
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    var t = doParse(str);
    if (t != null) {
      return t;
    }
    throw toParsingError(str, getTargetType().getName());
  }
}
