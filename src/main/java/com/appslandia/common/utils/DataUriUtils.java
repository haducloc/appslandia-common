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
