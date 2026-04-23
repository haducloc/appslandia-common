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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class NetUtilsTest {

  // toIpAddress()

  @Test
  public void testToIpAddress_validIPv4() {
    var ip = NetUtils.toIpAddress("203.0.113.45");
    Assertions.assertNotNull(ip);
    Assertions.assertEquals("203.0.113.45", ip.getHostAddress());
  }

  @Test
  public void testToIpAddress_trimmed() {
    var ip = NetUtils.toIpAddress("   203.0.113.45   ");
    Assertions.assertNotNull(ip);
    Assertions.assertEquals("203.0.113.45", ip.getHostAddress());
  }

  @Test
  public void testToIpAddress_nullOrBlank() {
    Assertions.assertNull(NetUtils.toIpAddress(null));
    Assertions.assertNull(NetUtils.toIpAddress(""));
    Assertions.assertNull(NetUtils.toIpAddress("   "));
  }

  @Test
  public void testToIpAddress_unknownLiteral() {
    Assertions.assertNull(NetUtils.toIpAddress("unknown"));
    Assertions.assertNull(NetUtils.toIpAddress("UnKnown")); // case-insensitive
  }

  @Test
  public void testToIpAddress_invalid() {
    Assertions.assertNull(NetUtils.toIpAddress("999.999.999.999"));
    Assertions.assertNull(NetUtils.toIpAddress("bad IP"));
  }

  // isIpv4MappedIpv6()

  @Test
  public void testIsIpv4MappedIpv6_true() {
    var mapped = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0xFF, (byte) 0xFF, (byte) 203, (byte) 0, (byte) 113,
        (byte) 45 };
    Assertions.assertTrue(NetUtils.isIpv4MappedIpv6(mapped));
  }

  @Test
  public void testIsIpv4MappedIpv6_false() {
    var ipv6 = new byte[16]; // all zero = :: (not IPv4 mapped)
    Assertions.assertFalse(NetUtils.isIpv4MappedIpv6(ipv6));
  }

  // canonicalizeIp()

  @Test
  public void testCanonicalizeIp_ipv4Unchanged() throws Exception {
    var ipv4 = InetAddress.getByName("203.0.113.45");
    var canonical = NetUtils.canonicalizeIp(ipv4);
    Assertions.assertEquals("203.0.113.45", canonical.getHostAddress());
  }

  @Test
  public void testCanonicalizeIp_ipv4MappedIPv6Converted() throws Exception {
    var mapped = InetAddress.getByName("::ffff:203.0.113.45");
    var canonical = NetUtils.canonicalizeIp(mapped);
    Assertions.assertEquals("203.0.113.45", canonical.getHostAddress());
  }

  @Test
  public void testCanonicalizeIp_normalIPv6() throws Exception {
    var ipv6 = InetAddress.getByName("2001:db8::1");
    var canonical = NetUtils.canonicalizeIp(ipv6);
    Assertions.assertEquals(ipv6.getHostAddress(), canonical.getHostAddress());
  }
}
