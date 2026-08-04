// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
