// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.net.InetAddress;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class IpNetworkTest {

  @Test
  public void test_ipv4ExactMatch() throws Exception {
    var ip = InetAddress.getByName("203.0.113.45");
    var network = IpNetwork.parse("203.0.113.45/32");

    Assertions.assertTrue(network.contains(ip));
  }

  @Test
  public void test_ipv4InsidePrefix() throws Exception {
    var ip = InetAddress.getByName("192.168.1.15");
    var network = IpNetwork.parse("192.168.1.0/24");

    Assertions.assertTrue(network.contains(ip));
  }

  @Test
  public void test_ipv4OutsidePrefix() throws Exception {
    var ip = InetAddress.getByName("192.168.2.15");
    var network = IpNetwork.parse("192.168.1.0/24");

    Assertions.assertFalse(network.contains(ip));
  }

  @Test
  public void test_ipv6ExactMatch() throws Exception {
    var ip = InetAddress.getByName("2001:db8::1");
    var network = IpNetwork.parse("2001:db8::1/128");

    Assertions.assertTrue(network.contains(ip));
  }

  @Test
  public void test_ipv6InsidePrefix() throws Exception {
    var ip = InetAddress.getByName("2001:db8:0:0::abcd");
    var network = IpNetwork.parse("2001:db8::/32");

    Assertions.assertTrue(network.contains(ip));
  }

  @Test
  public void test_ipv6OutsidePrefix() throws Exception {
    var ip = InetAddress.getByName("2001:db9::1");
    var network = IpNetwork.parse("2001:db8::/32");

    Assertions.assertFalse(network.contains(ip));
  }

  @Test
  public void test_ipv4VsIpv6Mismatch() throws Exception {
    var ip = InetAddress.getByName("203.0.113.45");
    var network = IpNetwork.parse("2001:db8::/32");

    Assertions.assertFalse(network.contains(ip));
  }

  @Test
  public void test_ipv4MappedIpv6MatchesIpv4Network() throws Exception {
    var ip = InetAddress.getByName("::ffff:203.0.113.45");
    var network = IpNetwork.parse("203.0.113.45/32");

    Assertions.assertTrue(network.contains(ip));
  }

  @Test
  public void test_ipv4MappedIpv6OutsideIpv4Network() throws Exception {
    var ip = InetAddress.getByName("::ffff:203.0.113.45");
    var network = IpNetwork.parse("203.0.113.99/32");

    Assertions.assertFalse(network.contains(ip));
  }
}
