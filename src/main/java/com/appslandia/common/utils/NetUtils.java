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
    try {
      var addr = InetAddress.getByName(ip);
      return canonicalizeIp(addr);
    } catch (UnknownHostException ex) {
      return null;
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
   * Convert IPv4-mapped IPv6
   */
  public static InetAddress canonicalizeIp(InetAddress addr) {
    var b = addr.getAddress();

    // IPv4
    if (b.length == 4) {
      return addr;
    }

    // IPv4-mapped IPv6
    if (isIpv4MappedIpv6(b)) {
      var ipv4 = new byte[] { b[12], b[13], b[14], b[15] };
      try {
        return InetAddress.getByAddress(ipv4);
      } catch (UnknownHostException ex) {
      }
    }

    // Normal IPv6
    return addr;
  }
}
