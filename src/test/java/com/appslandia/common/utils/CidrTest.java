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
public class CidrTest {

  @Test
  public void testIpMatchesCidr_ipv4ExactMatch() throws Exception {
    var ip = InetAddress.getByName("203.0.113.45");
    var cidr = Cidr.parse("203.0.113.45/32");
    Assertions.assertTrue(cidr.matches(ip));
  }

  @Test
  public void testIpMatchesCidr_ipv4InsidePrefix() throws Exception {
    var ip = InetAddress.getByName("192.168.1.15");
    var cidr = Cidr.parse("192.168.1.0/24");
    Assertions.assertTrue(cidr.matches(ip));
  }

  @Test
  public void testIpMatchesCidr_ipv4OutsidePrefix() throws Exception {
    var ip = InetAddress.getByName("192.168.2.15");
    var cidr = Cidr.parse("192.168.1.0/24");
    Assertions.assertFalse(cidr.matches(ip));
  }

  // ipMatchesCidr() - IPv6

  @Test
  public void testIpMatchesCidr_ipv6ExactMatch() throws Exception {
    var ip = InetAddress.getByName("2001:db8::1");
    var cidr = Cidr.parse("2001:db8::1/128");
    Assertions.assertTrue(cidr.matches(ip));
  }

  @Test
  public void testIpMatchesCidr_ipv6InsidePrefix() throws Exception {
    var ip = InetAddress.getByName("2001:db8:0:0::abcd");
    var cidr = Cidr.parse("2001:db8::/32");
    Assertions.assertTrue(cidr.matches(ip));
  }

  @Test
  public void testIpMatchesCidr_ipv6OutsidePrefix() throws Exception {
    var ip = InetAddress.getByName("2001:db9::1");
    var cidr = Cidr.parse("2001:db8::/32");
    Assertions.assertFalse(cidr.matches(ip));
  }

  // IPv4 vs IPv6 mismatch

  @Test
  public void testIpMatchesCidr_ipv4vsIPv6Mismatch() throws Exception {
    var ipv4 = InetAddress.getByName("203.0.113.45");
    var cidr = Cidr.parse("2001:db8::/32");
    Assertions.assertFalse(cidr.matches(ipv4));
  }

  // IPv4-mapped IPv6 + CIDR matching

  @Test
  public void testIpMatchesCidr_ipv4MappedIPv6MatchesIpv4CIDR() throws Exception {
    var mapped = InetAddress.getByName("::ffff:203.0.113.45");
    var cidr = Cidr.parse("203.0.113.45/32");
    Assertions.assertTrue(cidr.matches(mapped));
  }

  @Test
  public void testIpMatchesCidr_ipv4MappedIPv6WrongCIDR() throws Exception {
    var mapped = InetAddress.getByName("::ffff:203.0.113.45");
    var cidr = Cidr.parse("203.0.113.99/32");
    Assertions.assertFalse(cidr.matches(mapped));
  }
}
