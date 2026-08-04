// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

/**
 *
 * @author Loc Ha
 *
 */
public class MathUtils {

  public static int digitCount(long n) {
    Arguments.isTrue(n >= 0);

    if (n == 0) {
      return 1;
    }
    return (int) (Math.log10(n) + 1);
  }

  public static boolean isPow2(long n) {
    if ((n < 1) || (n > Long.MAX_VALUE / 2 + 1)) {
      return false;
    }
    return (n & (n - 1)) == 0;
  }

  public static int toNearestMultipleOf(int number, int minBound) {
    Arguments.isTrue(minBound >= 0);
    Arguments.isTrue(number > 0);

    return ((minBound + number - 1) / number) * number;
  }

  public static byte[] toByteArray(int value) {
    var bytes = new byte[4];
    for (var i = 3; i >= 0; i--) {
      bytes[i] = (byte) (value & 0xff);
      value >>= 8;
    }
    return bytes;
  }

  public static int toInt(byte[] bytes) {
    // @formatter:off
		return (bytes[0] & 0xff) << 24 | (bytes[1] & 0xff) << 16
				| (bytes[2] & 0xff) << 8 | (bytes[3] & 0xff);
		// @formatter:on
  }

  public static byte[] toByteArray(long value) {
    var bytes = new byte[8];
    for (var i = 7; i >= 0; i--) {
      bytes[i] = (byte) (value & 0xffL);
      value >>= 8;
    }
    return bytes;
  }

  public static long toLong(byte[] bytes) {
    // @formatter:off
		return (bytes[0] & 0xffL) << 56 | (bytes[1] & 0xffL) << 48
				| (bytes[2] & 0xffL) << 40 | (bytes[3] & 0xffL) << 32
				| (bytes[4] & 0xffL) << 24 | (bytes[5] & 0xffL) << 16
				| (bytes[6] & 0xffL) << 8 | (bytes[7] & 0xffL);
		// @formatter:on
  }

  public static byte[] toByteArray(int begin, int end) {
    Arguments.isTrue(begin <= end);

    var byteArray = new byte[end - begin + 1];
    for (var i = begin; i <= end; i++) {
      byteArray[i - begin] = (byte) i;
    }
    return byteArray;
  }
}
