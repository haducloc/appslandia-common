package com.appslandia.common.utils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Arrays;

public class Cidr {

  private final InetAddress address;
  private final int prefix;

  public Cidr(InetAddress address, int prefix) {
    Arguments.notNull(address);
    this.address = NetUtils.canonicalizeIp(address);
    this.prefix = prefix;
  }

  public InetAddress getAddress() {
    return address;
  }

  public int getPrefix() {
    return prefix;
  }

  public boolean matches(InetAddress ip) {
    Arguments.notNull(ip);

    try {
      var normalizedIp = NetUtils.canonicalizeIp(ip);
      var network = address;

      var ipBytes = normalizedIp.getAddress();
      var networkBytes = network.getAddress();

      var totalBits = ipBytes.length * 8;

      // IPv4 vs IPv6 mismatch => cannot match
      if (ipBytes.length != networkBytes.length) {
        return false;
      }

      // /32 (IPv4) or /128 (IPv6) exact match optimization
      if (prefix == totalBits) {
        return normalizedIp.equals(network);
      }

      var ipVal = new BigInteger(1, ipBytes);
      var netVal = new BigInteger(1, networkBytes);
      var mask = prefixToMask(prefix, totalBits);

      return ipVal.and(mask).equals(netVal.and(mask));
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Cidr other)) {
      return false;
    }
    return prefix == other.prefix && Arrays.equals(address.getAddress(), other.address.getAddress());
  }

  @Override
  public int hashCode() {
    var result = Arrays.hashCode(address.getAddress());
    result = 31 * result + prefix;
    return result;
  }

  @Override
  public String toString() {
    return address.getHostAddress() + "/" + prefix;
  }

  private static BigInteger prefixToMask(int prefix, int totalBits) {
    var mask = BigInteger.ZERO;
    for (var i = 0; i < prefix; i++) {
      mask = mask.setBit(totalBits - 1 - i);
    }
    return mask;
  }

  public static Cidr parse(String cidr) throws IllegalArgumentException {
    if (cidr == null) {
      return null;
    }

    var parts = cidr.split("/");
    if (parts.length != 2) {
      throw new IllegalArgumentException("CIDR must be in format <ip>/<prefix>.");
    }

    var ipPart = parts[0].strip();
    var prefixPart = parts[1].strip();
    int prefix;

    try {
      prefix = Integer.parseInt(prefixPart);
      if (prefix < 0) {
        throw new IllegalArgumentException("CIDR prefix must be >= 0.");
      }
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("CIDR prefix must be an integer.");
    }

    var addr = NetUtils.toIpAddress(ipPart);
    var ipBytes = addr.getAddress();
    var isIpv4 = (ipBytes.length == 4);

    if (isIpv4 && prefix > 32) {
      throw new IllegalArgumentException("IPv4 CIDR prefix cannot exceed 32.");
    }
    if (!isIpv4 && prefix > 128) {
      throw new IllegalArgumentException("IPv6 CIDR prefix cannot exceed 128.");
    }
    return new Cidr(addr, prefix);
  }
}
