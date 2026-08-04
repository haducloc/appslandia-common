// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 *
 * @author Loc Ha
 *
 */
public class CharsetUtils {

  public static String parseCharset(String contentType) {
    if (contentType == null) {
      return StandardCharsets.UTF_8.name();
    }
    return parseCharset(contentType, StandardCharsets.UTF_8.name());
  }

  public static String parseCharset(String contentType, String defaultValue) {
    if (contentType == null) {
      return defaultValue;
    }

    var idx = contentType.indexOf(';');
    if (idx < 0) {
      return defaultValue;
    }
    var charset = contentType.substring(idx + 1).strip();

    if (charset.toLowerCase(Locale.ENGLISH).startsWith("charset=")) {
      return charset.substring(8);
    }
    return defaultValue;
  }
}
