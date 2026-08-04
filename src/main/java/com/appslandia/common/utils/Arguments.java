// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Loc Ha
 *
 */
public class Arguments {

  public static void isTrue(boolean expression) {
    if (!expression) {
      throw new IllegalArgumentException("The expression must be true.");
    }
  }

  public static void isTrue(boolean expression, String errorMessage) {
    if (!expression) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  public static void isTrue(boolean expression, String messageTemplate, Object... templateArgs) {
    if (!expression) {
      throw new IllegalArgumentException(STR.fmt(messageTemplate, templateArgs));
    }
  }

  public static <T> T notNull(T obj) {
    if (obj == null) {
      throw new IllegalArgumentException("The obj must not be null.");
    }
    return obj;
  }

  public static <T> T notNull(T obj, String errorMessage) {
    if (obj == null) {
      throw new IllegalArgumentException(errorMessage);
    }
    return obj;
  }

  public static <T> T notNull(T obj, String messageTemplate, Object... templateArgs) {
    if (obj == null) {
      throw new IllegalArgumentException(STR.fmt(messageTemplate, templateArgs));
    }
    return obj;
  }

  public static String notBlank(String str) {
    if ((str == null) || str.isBlank()) {
      throw new IllegalArgumentException("The str must not be null or blank.");
    }
    return str.strip();
  }

  public static String notBlank(String str, String errorMessage) {
    if ((str == null) || str.isBlank()) {
      throw new IllegalArgumentException(errorMessage);
    }
    return str.strip();
  }

  public static String notBlank(String str, String messageTemplate, Object... templateArgs) {
    if ((str == null) || str.isBlank()) {
      throw new IllegalArgumentException(STR.fmt(messageTemplate, templateArgs));
    }
    return str.strip();
  }

  public static void isFinite(float number) {
    if (!Float.isFinite(number)) {
      throw new IllegalArgumentException("The number must be finite.");
    }
  }

  public static void isFinite(float number, String errorMessage) {
    if (!Float.isFinite(number)) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  public static void isFinite(float number, String messageTemplate, Object... templateArgs) {
    if (!Float.isFinite(number)) {
      throw new IllegalArgumentException(STR.fmt(messageTemplate, templateArgs));
    }
  }

  public static void isFinite(double number) {
    if (!Double.isFinite(number)) {
      throw new IllegalArgumentException("The number must be finite.");
    }
  }

  public static void isFinite(double number, String errorMessage) {
    if (!Double.isFinite(number)) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  public static void isFinite(double number, String messageTemplate, Object... templateArgs) {
    if (!Double.isFinite(number)) {
      throw new IllegalArgumentException(STR.fmt(messageTemplate, templateArgs));
    }
  }

  public static void isNull(Object obj) {
    if (obj != null) {
      throw new IllegalArgumentException("The obj must be null.");
    }
  }

  public static void isNull(Object obj, String errorMessage) {
    if (obj != null) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  public static void isNull(Object obj, String messageTemplate, Object... templateArgs) {
    if (obj != null) {
      throw new IllegalArgumentException(STR.fmt(messageTemplate, templateArgs));
    }
  }

  public static int[] hasElements(int[] array) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalArgumentException("The array must have elements.");
    }
    return array;
  }

  public static int[] hasElements(int[] array, String errorMessage) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalArgumentException(errorMessage);
    }
    return array;
  }

  public static int[] hasElements(int[] array, String messageTemplate, Object... templateArgs) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalArgumentException(STR.fmt(messageTemplate, templateArgs));
    }
    return array;
  }

  public static <T> T[] hasElements(T[] array) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalArgumentException("The array must have elements.");
    }
    return array;
  }

  public static <T> T[] hasElements(T[] array, String errorMessage) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalArgumentException(errorMessage);
    }
    return array;
  }

  public static <T> T[] hasElements(T[] array, String messageTemplate, Object... templateArgs) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalArgumentException(STR.fmt(messageTemplate, templateArgs));
    }
    return array;
  }

  public static <T extends Collection<?>> T hasElements(T collection) {
    if ((collection == null) || (collection.isEmpty())) {
      throw new IllegalArgumentException("The collection must have elements.");
    }
    return collection;
  }

  public static <T extends Collection<?>> T hasElements(T collection, String errorMessage) {
    if ((collection == null) || (collection.isEmpty())) {
      throw new IllegalArgumentException(errorMessage);
    }
    return collection;
  }

  public static <T extends Collection<?>> T hasElements(T collection, String messageTemplate, Object... templateArgs) {
    if ((collection == null) || (collection.isEmpty())) {
      throw new IllegalArgumentException(STR.fmt(messageTemplate, templateArgs));
    }
    return collection;
  }

  public static <M extends Map<?, ?>> M hasEntries(M map) {
    if ((map == null) || (map.isEmpty())) {
      throw new IllegalArgumentException("The map must have entries.");
    }
    return map;
  }

  public static <M extends Map<?, ?>> M hasEntries(M map, String errorMessage) {
    if ((map == null) || (map.isEmpty())) {
      throw new IllegalArgumentException(errorMessage);
    }
    return map;
  }

  public static <M extends Map<?, ?>> M hasEntries(M map, String messageTemplate, Object... templateArgs) {
    if ((map == null) || (map.isEmpty())) {
      throw new IllegalArgumentException(STR.fmt(messageTemplate, templateArgs));
    }
    return map;
  }
}
