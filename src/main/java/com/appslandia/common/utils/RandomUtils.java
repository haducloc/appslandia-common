// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.Random;

/**
 *
 * @author Loc Ha
 *
 */
public class RandomUtils {

  public static byte[] nextBytes(int length, Random random) {
    var bytes = new byte[length];
    random.nextBytes(bytes);
    return bytes;
  }

  public static int nextInt(int min, int max, Random random) {
    return min + (int) (random.nextFloat() * (max - min + 1));
  }

  public static int[] nextIndexes(int n, Random random) {
    var indexes = new int[n];
    for (var i = 0; i < n; i++) {
      indexes[i] = i;
    }
    ArrayUtils.shuffle(indexes, random);
    return indexes;
  }

  public static int[] nextInts(int length, int min, int max, Random random) {
    var ints = new int[length];
    for (var i = 0; i < length; i++) {
      ints[i] = nextInt(min, max, random);
    }
    return ints;
  }
}
