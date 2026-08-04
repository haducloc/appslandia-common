// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.regex.Pattern;

import com.appslandia.common.base.Out;

/**
 *
 * @author Loc Ha
 *
 */
public class KeywordUtils {

  static final Pattern KEYWORDS_PATTERN = Pattern.compile("[^,]+(\\s*,\\s*[^,]*)*", Pattern.CASE_INSENSITIVE);

  public static String toKeywords(String keywords, Out<Boolean> isValid) {
    isValid.value = true;

    if (keywords == null) {
      return keywords;
    }
    if (!KEYWORDS_PATTERN.matcher(keywords).matches()) {
      isValid.value = false;
      return keywords;
    }
    var kws = SplitUtils.split(keywords, ',');
    if (kws.length == 0) {
      isValid.value = false;

      return keywords;
    }
    return String.join(", ", kws);
  }
}
