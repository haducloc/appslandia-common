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

package com.appslandia.common.base;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.appslandia.common.utils.Arguments;

/**
 *
 *
 * @author Loc Ha
 *
 */
public enum BaseEncoder {

  BASE64, BASE64_NP, BASE64_URL, BASE64_URL_NP, BASE64_MIME, BASE64_MIME_NP;

  public String encode(byte[] message) {
    Arguments.notNull(message);
    return switch (this) {
    case BASE64 -> new String(Base64.getEncoder().encode(message), StandardCharsets.ISO_8859_1);
    case BASE64_NP -> new String(Base64.getEncoder().withoutPadding().encode(message), StandardCharsets.ISO_8859_1);
    case BASE64_URL -> new String(Base64.getUrlEncoder().encode(message), StandardCharsets.ISO_8859_1);
    case BASE64_URL_NP ->
      new String(Base64.getUrlEncoder().withoutPadding().encode(message), StandardCharsets.ISO_8859_1);
    case BASE64_MIME -> new String(Base64.getMimeEncoder().encode(message), StandardCharsets.ISO_8859_1);
    case BASE64_MIME_NP ->
      new String(Base64.getMimeEncoder().withoutPadding().encode(message), StandardCharsets.ISO_8859_1);
    default -> throw new Error();
    };
  }

  public byte[] decode(String encoded) {
    Arguments.notNull(encoded);
    return switch (this) {
    case BASE64 -> Base64.getDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));
    case BASE64_NP -> Base64.getDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));
    case BASE64_URL -> Base64.getUrlDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));
    case BASE64_URL_NP -> Base64.getUrlDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));
    case BASE64_MIME -> Base64.getMimeDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));
    case BASE64_MIME_NP -> Base64.getMimeDecoder().decode(encoded.getBytes(StandardCharsets.ISO_8859_1));
    default -> throw new Error();
    };
  }
}
