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
