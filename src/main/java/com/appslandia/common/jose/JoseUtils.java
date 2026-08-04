// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.util.Date;
import java.util.regex.Pattern;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class JoseUtils {

  private static final Pattern JOSE_SEP_PATTERN = Pattern.compile("\\.");

  public static String[] parseJws(String token) {
    Arguments.notNull(token);
    var parts = JOSE_SEP_PATTERN.split(token);

    if (parts.length == 2) {
      return token.endsWith(".") ? new String[] { parts[0], parts[1], "" } : null;
    }
    if (parts.length != 3) {
      return null;
    }
    return parts;
  }

  public static String toJwsData(String header, String payload) {
    return new StringBuilder(header.length() + 1 + payload.length()).append(header).append('.').append(payload)
        .toString();
  }

  public static String toJwsToken(String header, String payload, String signature) {
    return new StringBuilder(header.length() + 1 + payload.length() + 1 + signature.length()).append(header).append('.')
        .append(payload).append('.').append(signature).toString();
  }

  // number of seconds from 1970-01-01T00:00:00Z UTC until the specified UTC
  // date/time
  public static Long toNumericDate(Date value) {
    if (value == null) {
      return null;
    }
    return value.getTime() / 1000;
  }

  public static Long toNumericDate(Long timeInMs) {
    if (timeInMs == null) {
      return null;
    }
    return timeInMs / 1000;
  }

  public static boolean isFutureTime(long numericDate, int leewaySec) {
    return (System.currentTimeMillis() / 1000) - leewaySec < numericDate;
  }

  public static boolean isPastTime(long numericDate, int leewaySec) {
    return (System.currentTimeMillis() / 1000) + leewaySec > numericDate;
  }

  public static Date toDate(Long numericDate) {
    if (numericDate == null) {
      return null;
    }
    return new Date(numericDate * 1000);
  }

  public static BaseEncoder getJoseBase64() {
    return BaseEncoder.BASE64_URL_NP;
  }
}
