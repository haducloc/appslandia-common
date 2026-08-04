// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpNetwork {

  private final InetAddress address;
  private final int prefix;

  private final BigInteger mask;
  private final BigInteger networkValue;

  public IpNetwork(InetAddress address, int prefix) {
    Arguments.notNull(address);

    var canonicalAddr = NetUtils.canonicalizeIp(address);
    var bytes = canonicalAddr.getAddress();
    var totalBits = bytes.length * 8;

    Arguments.isTrue(prefix >= 0 && prefix <= totalBits);

    this.prefix = prefix;
    this.mask = prefixToMask(prefix, totalBits);
    this.networkValue = new BigInteger(1, bytes).and(this.mask);
    this.address = toInetAddress(this.networkValue, bytes.length);
  }

  public InetAddress getAddress() {
    return address;
  }

  public int getPrefix() {
    return prefix;
  }

  public boolean contains(InetAddress ip) {
    Arguments.notNull(ip);

    var ipBytes = NetUtils.canonicalizeIp(ip).getAddress();
    if (ipBytes.length != this.address.getAddress().length) {
      return false;
    }

    var ipVal = new BigInteger(1, ipBytes);
    return ipVal.and(this.mask).equals(this.networkValue);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof IpNetwork other)) {
      return false;
    }
    return address.equals(other.address) && prefix == other.prefix;
  }

  @Override
  public int hashCode() {
    var result = address.hashCode();
    result = 31 * result + prefix;
    return result;
  }

  @Override
  public String toString() {
    return address.getHostAddress() + "/" + prefix;
  }

  public static IpNetwork parse(String network) {
    if (network == null) {
      return null;
    }

    var parts = network.split("/", 2);
    if (parts.length != 2) {
      return null;
    }

    var ipPart = parts[0].strip();
    var prefixPart = parts[1].strip();

    if (ipPart.isEmpty() || prefixPart.isEmpty()) {
      return null;
    }

    int prefix;
    try {
      prefix = Integer.parseInt(prefixPart);
      if (prefix < 0) {
        return null;
      }
    } catch (NumberFormatException ex) {
      return null;
    }

    var addr = NetUtils.toIpAddress(ipPart);
    if (addr == null) {
      return null;
    }

    var ipBytes = addr.getAddress();
    var isIpv4 = ipBytes.length == 4;

    if ((isIpv4 && prefix > 32) || (!isIpv4 && prefix > 128)) {
      return null;
    }

    return new IpNetwork(addr, prefix);
  }

  private static BigInteger prefixToMask(int prefix, int totalBits) {
    if (prefix == 0) {
      return BigInteger.ZERO;
    }

    return BigInteger.ONE.shiftLeft(prefix).subtract(BigInteger.ONE).shiftLeft(totalBits - prefix);
  }

  private static InetAddress toInetAddress(BigInteger value, int length) {
    try {
      return InetAddress.getByAddress(toBytes(value, length));
    } catch (UnknownHostException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private static byte[] toBytes(BigInteger value, int length) {
    var raw = value.toByteArray();
    var bytes = new byte[length];

    var srcPos = Math.max(0, raw.length - length);
    var destPos = Math.max(0, length - raw.length);
    var copyLen = Math.min(raw.length - srcPos, length);

    System.arraycopy(raw, srcPos, bytes, destPos, copyLen);
    return bytes;
  }
}
