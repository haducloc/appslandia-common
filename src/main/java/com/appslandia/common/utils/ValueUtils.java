// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.Arrays;

/**
 *
 * @author Loc Ha
 *
 */
public class ValueUtils {

  public static int valueOrMin(Integer checkValue, int min) {
    if ((checkValue == null) || (checkValue < min)) {
      return min;
    }
    return checkValue;
  }

  public static long valueOrMin(Long checkValue, long min) {
    if ((checkValue == null) || (checkValue < min)) {
      return min;
    }
    return checkValue;
  }

  public static int valueOrMax(Integer checkValue, int max) {
    if ((checkValue == null) || (checkValue > max)) {
      return max;
    }
    return checkValue;
  }

  public static long valueOrMax(Long checkValue, long max) {
    if ((checkValue == null) || (checkValue > max)) {
      return max;
    }
    return checkValue;
  }

  public static int inRange(Integer checkValue, int min, int max) {
    if ((checkValue == null) || (checkValue < min)) {
      return min;
    }
    if (checkValue > max) {
      return max;
    }
    return checkValue;
  }

  public static long inRange(Long checkValue, long min, long max) {
    if ((checkValue == null) || (checkValue < min)) {
      return min;
    }
    if (checkValue > max) {
      return max;
    }
    return checkValue;
  }

  public static double inRange(Double checkValue, double min, double max) {
    if ((checkValue == null) || (Double.compare(checkValue, min) < 0)) {
      return min;
    }
    if (Double.compare(checkValue, max) > 0) {
      return max;
    }
    return checkValue;
  }

  public static <T> T valueOrNull(T checkValue, T[] validValues) {
    Arguments.hasElements(validValues);

    if (checkValue == null) {
      return null;
    }
    if (Arrays.stream(validValues).anyMatch(v -> checkValue.equals(v))) {
      return checkValue;
    }
    return null;
  }

  public static Integer valueOrNull(Integer checkValue, int[] validValues) {
    Arguments.isTrue((validValues != null) && (validValues.length > 0));

    if (checkValue == null) {
      return null;
    }
    if (Arrays.stream(validValues).anyMatch(v -> v == checkValue)) {
      return checkValue;
    }
    return null;
  }

  public static <T> T valueOrAlt(T checkValue, T altValue) {
    return (checkValue != null) ? checkValue : altValue;
  }

  public static int valueOrAlt(Integer checkValue, int altValue) {
    return (checkValue != null) ? checkValue : altValue;
  }

  public static long valueOrAlt(Long checkValue, long altValue) {
    return (checkValue != null) ? checkValue : altValue;
  }

  public static boolean allOrNoneNull(Object... values) {
    var nullCount = Arrays.stream(values).filter(v -> v == null).count();
    return nullCount == 0 || nullCount == values.length;
  }
}
