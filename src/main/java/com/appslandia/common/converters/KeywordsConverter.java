// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.KeywordUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class KeywordsConverter implements Converter<String> {

  public static final String ERROR_MSG_KEY = KeywordsConverter.class.getName() + ".message";

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
    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }
    var isValid = new Out<Boolean>();
    var keywords = KeywordUtils.toKeywords(str, isValid);

    if (!isValid.get()) {
      throw toParsingError(str, "Keywords");
    }
    return keywords;
  }
}
