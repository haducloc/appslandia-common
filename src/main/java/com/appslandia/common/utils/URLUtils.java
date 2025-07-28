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

package com.appslandia.common.utils;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 *
 * @author Loc Ha
 *
 */
public class URLUtils {

  public static String toQueryParams(Map<String, Object> parameterMap) {
    if (parameterMap == null) {
      return null;
    }
    var sb = new StringBuilder(parameterMap.size() * 16);

    for (Map.Entry<String, Object> param : parameterMap.entrySet()) {
      if (param.getValue() == null) {
        continue;
      }
      if (sb.length() > 0) {
        sb.append('&');
      }
      addQueryParam(sb, param.getKey(), param.getValue());
    }
    return sb.length() > 0 ? sb.toString() : null;
  }

  public static void addQueryParam(StringBuilder sb, String name, Object value) {
    Arguments.notNull(value);

    // Basic Types
    if (!value.getClass().isArray()) {
      sb.append(URLEncoding.encodeParam(name)).append('=').append(URLEncoding.encodeParam(value.toString()));
      return;
    }

    // Array
    var len = Array.getLength(value);
    var addedSub = false;

    for (var i = 0; i < len; i++) {
      var subVal = Array.get(value, i);
      if (subVal == null) {
        continue;
      }

      if (!addedSub) {
        addedSub = true;
      } else {
        sb.append('&');
      }
      sb.append(URLEncoding.encodeParam(name)).append('=').append(URLEncoding.encodeParam(subVal.toString()));
    }
  }

  public static String validQueryOrNull(String queryString) {
    if (queryString == null) {
      return null;
    }
    try {
      var params = URLUtils.parseParams(queryString, new LinkedHashMap<>());
      return URLUtils.toQueryParams(params);

    } catch (IllegalArgumentException ex) {
      return null;
    }
  }

  public static String toUrl(String url, Map<String, Object> moreParameters) {
    if (moreParameters == null) {
      return url;
    }
    try {
      var uri = new URI(url);
      var sb = new StringBuilder(url.length() + moreParameters.size() * 16);

      if (uri.getScheme() != null) {
        sb.append(uri.getScheme()).append("://").append(uri.getRawAuthority());
      }
      Arguments.notNull(uri.getRawPath());
      sb.append(uri.getRawPath());

      var addedQuest = false;
      if (uri.getRawQuery() != null) {
        sb.append('?').append(uri.getRawQuery());

        addedQuest = true;
      }

      for (Map.Entry<String, Object> param : moreParameters.entrySet()) {
        if (param.getValue() == null) {
          continue;
        }
        if (!addedQuest) {
          sb.append('?');
          addedQuest = true;
        } else {
          sb.append('&');
        }
        addQueryParam(sb, param.getKey(), param.getValue());
      }

      if (uri.getRawFragment() != null) {
        sb.append('#').append(uri.getRawFragment());
      }
      return sb.toString();

    } catch (URISyntaxException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public static Map<String, Object> parseParams(String queryString, Map<String, Object> params) {
    return parseParams(queryString, params, true);
  }

  public static Map<String, Object> parseParams(String queryString, Map<String, Object> params, boolean parseArray) {
    if (queryString == null) {
      return params;
    }
    var startIdx = 0;
    int endIdx;

    while ((endIdx = queryString.indexOf('&', startIdx)) != -1) {
      var pair = queryString.substring(startIdx, endIdx);

      if (!parsePair(pair, params, parseArray)) {
        throw new IllegalArgumentException(STR.fmt("queryString '{}' is invalid.", queryString));
      }
      startIdx = endIdx + 1;
    }
    if (startIdx < queryString.length()) {
      var pair = queryString.substring(startIdx);

      if (!parsePair(pair, params, parseArray)) {
        throw new IllegalArgumentException(STR.fmt("queryString '{}' is invalid.", queryString));
      }
    }
    return params;
  }

  private static boolean parsePair(String pair, Map<String, Object> params, boolean parseArray) {
    var idx = pair.indexOf('=');
    if (idx <= 0) {
      return false;
    }
    var name = URLEncoding.decodeParam(pair.substring(0, idx));
    var value = pair.substring(idx + 1);
    var decodedVal = !value.isEmpty() ? URLEncoding.decodeParam(value) : null;

    // String Value?
    if (!parseArray) {
      params.putIfAbsent(name, decodedVal);
      return true;
    }

    // String[] Value
    if (!params.containsKey(name)) {
      params.put(name, decodedVal);

    } else {
      var addedValue = params.get(name);
      if (addedValue == null || addedValue instanceof String) {
        params.put(name, new String[] { (String) addedValue, decodedVal });
      } else {
        params.put(name, ArrayUtils.append((String[]) addedValue, new String[] { decodedVal }));
      }
    }
    return true;
  }
}
