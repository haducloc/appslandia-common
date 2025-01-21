// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Asserts {

  public static void isTrue(boolean expression) {
    if (!expression) {
      throw new IllegalStateException("The expression must be true.");
    }
  }

  public static void isTrue(boolean expression, String errorMessage) {
    if (!expression) {
      throw new IllegalStateException(errorMessage);
    }
  }

  public static void isTrue(boolean expression, String messageTemplate, Object... templateArgs) {
    if (!expression) {
      throw new IllegalStateException(STR.fmt(messageTemplate, templateArgs));
    }
  }

  public static <T> T notNull(T obj) {
    if (obj == null) {
      throw new IllegalStateException("The obj must not be null.");
    }
    return obj;
  }

  public static <T> T notNull(T obj, String errorMessage) {
    if (obj == null) {
      throw new IllegalStateException(errorMessage);
    }
    return obj;
  }

  public static <T> T notNull(T obj, String messageTemplate, Object... templateArgs) {
    if (obj == null) {
      throw new IllegalStateException(STR.fmt(messageTemplate, templateArgs));
    }
    return obj;
  }

  public static String notBlank(String str) {
    if ((str == null) || str.isBlank()) {
      throw new IllegalStateException("The str must not be null or blank.");
    }
    return str.strip();
  }

  public static String notBlank(String str, String errorMessage) {
    if ((str == null) || str.isBlank()) {
      throw new IllegalStateException(errorMessage);
    }
    return str.strip();
  }

  public static String notBlank(String str, String messageTemplate, Object... templateArgs) {
    if ((str == null) || str.isBlank()) {
      throw new IllegalStateException(STR.fmt(messageTemplate, templateArgs));
    }
    return str.strip();
  }

  public static void isNull(Object obj) {
    if (obj != null) {
      throw new IllegalStateException("The obj must be null.");
    }
  }

  public static void isNull(Object obj, String errorMessage) {
    if (obj != null) {
      throw new IllegalStateException(errorMessage);
    }
  }

  public static void isNull(Object obj, String messageTemplate, Object... templateArgs) {
    if (obj != null) {
      throw new IllegalStateException(STR.fmt(messageTemplate, templateArgs));
    }
  }

  public static int[] hasElements(int[] array) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalStateException("The array must have elements.");
    }
    return array;
  }

  public static int[] hasElements(int[] array, String errorMessage) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalStateException(errorMessage);
    }
    return array;
  }

  public static int[] hasElements(int[] array, String messageTemplate, Object... templateArgs) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalStateException(STR.fmt(messageTemplate, templateArgs));
    }
    return array;
  }

  public static <T> T[] hasElements(T[] array) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalStateException("The array must have elements.");
    }
    return array;
  }

  public static <T> T[] hasElements(T[] array, String errorMessage) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalStateException(errorMessage);
    }
    return array;
  }

  public static <T> T[] hasElements(T[] array, String messageTemplate, Object... templateArgs) {
    if ((array == null) || (array.length == 0)) {
      throw new IllegalStateException(STR.fmt(messageTemplate, templateArgs));
    }
    return array;
  }

  public static <T extends Collection<?>> T hasElements(T collection) {
    if ((collection == null) || (collection.isEmpty())) {
      throw new IllegalStateException("The collection must have elements.");
    }
    return collection;
  }

  public static <T extends Collection<?>> T hasElements(T collection, String errorMessage) {
    if ((collection == null) || (collection.isEmpty())) {
      throw new IllegalStateException(errorMessage);
    }
    return collection;
  }

  public static <T extends Collection<?>> T hasElements(T collection, String messageTemplate, Object... templateArgs) {
    if ((collection == null) || (collection.isEmpty())) {
      throw new IllegalStateException(STR.fmt(messageTemplate, templateArgs));
    }
    return collection;
  }

  public static <M extends Map<?, ?>> M hasEntries(M map) {
    if ((map == null) || (map.isEmpty())) {
      throw new IllegalStateException("The map must have entries.");
    }
    return map;
  }

  public static <M extends Map<?, ?>> M hasEntries(M map, String errorMessage) {
    if ((map == null) || (map.isEmpty())) {
      throw new IllegalStateException(errorMessage);
    }
    return map;
  }

  public static <M extends Map<?, ?>> M hasEntries(M map, String messageTemplate, Object... templateArgs) {
    if ((map == null) || (map.isEmpty())) {
      throw new IllegalStateException(STR.fmt(messageTemplate, templateArgs));
    }
    return map;
  }
}
