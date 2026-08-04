// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.io.IOException;
import java.io.InputStream;

import com.appslandia.common.base.BaseEncoder;

/**
 *
 * @author Loc Ha
 *
 */
public class DataUriUtils {

  public static String toDataUriBase64(String mediaType, byte[] data) {
    var sb = new StringBuilder();
    sb.append("data:");

    if (!StringUtils.isNullOrEmpty(mediaType)) {
      sb.append(mediaType);
    }

    sb.append(";base64,");

    if (data != null && data.length > 0) {
      sb.append(BaseEncoder.BASE64.encode(data));
    }
    return sb.toString();
  }

  public static String toDataUriBase64(String mediaType, InputStream data) throws IOException {
    return toDataUriBase64(mediaType, IOUtils.toByteArray(data));
  }

  public static String toDataUriBase64(String mediaType, String data) {
    var sb = new StringBuilder();
    sb.append("data:");

    if (!StringUtils.isNullOrEmpty(mediaType)) {
      sb.append(mediaType);
    }

    sb.append(',');

    if (data != null && data.length() > 0) {
      sb.append(URLEncoding.encodeParam(data, false));
    }
    return sb.toString();
  }

  public static boolean isDataUri(String input) {
    Arguments.notNull(input);
    return input.startsWith("data:") && input.contains(",");
  }
}
