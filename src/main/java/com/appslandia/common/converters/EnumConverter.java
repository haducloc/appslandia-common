// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
    return type;
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
      return Enum.valueOf(type, str.toUpperCase(Locale.ENGLISH));
    } catch (IllegalArgumentException ex) {
    }
    throw toParsingError(str, getTargetType().getName());
  }
}
