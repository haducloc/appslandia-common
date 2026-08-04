// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

/**
 *
 *
 * @author Loc Ha
 *
 */
public class HexUtils {

  private static final char[] HEX_CHARS = CharUtils.toCharRanges("0-9a-f");

  public static String encodeHexToString(byte[] src) {
    var sb = new StringBuilder(src.length * 2);
    appendAsHex(sb, src);
    return sb.toString();
  }

  public static void appendAsHex(StringBuilder sb, byte[] src) {
    for (byte b : src) {
      sb.append(HEX_CHARS[(b & 0xf0) >> 4]);
      sb.append(HEX_CHARS[b & 0x0f]);
    }
  }
}
