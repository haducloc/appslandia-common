// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Loc Ha
 *
 */
public class NetUtils {

  public static InetAddress toIpAddress(String ip) {
    if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
      return null;
    }

    ip = ip.strip();

    // Do not allow DNS resolving here. This method is for IP literals only.
    if (!isIpLiteral(ip)) {
      return null;
    }

    try {
      var addr = InetAddress.getByName(ip);
      return canonicalizeIp(addr);

    } catch (UnknownHostException ex) {
      return null;
    }
  }

  public static InetAddress resolveIpAddress(String hostOrIp) {
    if (hostOrIp == null || hostOrIp.isBlank() || "unknown".equalsIgnoreCase(hostOrIp)) {
      return null;
    }
    hostOrIp = hostOrIp.strip();

    try {
      var addr = InetAddress.getByName(hostOrIp);
      return canonicalizeIp(addr);

    } catch (UnknownHostException ex) {
      return null;
    }
  }

  protected static boolean isIpLiteral(String value) {
    return isIpv4Literal(value) || isIpv6Literal(value);
  }

  protected static boolean isIpv4Literal(String value) {
    var parts = value.split("\\.", -1);
    if (parts.length != 4) {
      return false;
    }

    for (var part : parts) {
      if (part.isEmpty()) {
        return false;
      }

      for (var i = 0; i < part.length(); i++) {
        var c = part.charAt(i);
        if (c < '0' || c > '9') {
          return false;
        }
      }

      try {
        var n = Integer.parseInt(part);
        if (n < 0 || n > 255) {
          return false;
        }
      } catch (NumberFormatException ex) {
        return false;
      }
    }

    return true;
  }

  protected static boolean isIpv6Literal(String value) {
    if (value == null || value.indexOf(':') < 0) {
      return false;
    }

    for (var i = 0; i < value.length(); i++) {
      var c = value.charAt(i);

      if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F') || c == ':' || c == '.') {
        continue;
      }

      return false;
    }

    try {
      var addr = InetAddress.getByName(value);
      return addr.getAddress().length == 16;

    } catch (UnknownHostException ex) {
      return false;
    }
  }

  // @formatter:off
  public static boolean isIpv4MappedIpv6(byte[] ipBytes) {
    return ipBytes.length == 16 &&
           ipBytes[0] == 0 && ipBytes[1] == 0 && ipBytes[2] == 0 && ipBytes[3] == 0 &&
           ipBytes[4] == 0 && ipBytes[5] == 0 && ipBytes[6] == 0 && ipBytes[7] == 0 &&
           ipBytes[8] == 0 && ipBytes[9] == 0 &&
           ipBytes[10] == (byte) 0xFF && ipBytes[11] == (byte) 0xFF;
  }
  // @formatter:on

  /**
   * Convert IPv4-mapped IPv6.
   */
  public static InetAddress canonicalizeIp(InetAddress addr) {
    var b = addr.getAddress();

    if (b.length == 4) {
      return addr;
    }

    if (isIpv4MappedIpv6(b)) {
      var ipv4 = new byte[] { b[12], b[13], b[14], b[15] };
      try {
        return InetAddress.getByAddress(ipv4);
      } catch (UnknownHostException ex) {
        throw new IllegalStateException(ex);
      }
    }

    return addr;
  }
}
