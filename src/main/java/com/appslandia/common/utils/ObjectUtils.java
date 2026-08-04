// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 *
 * @author Loc Ha
 *
 */
public class ObjectUtils {

  public static final String NULL_STR = "null";

  public static String toStringOrEmpty(Object obj) {
    return obj != null ? obj.toString() : StringUtils.EMPTY_STRING;
  }

  public static String toStringOrNull(Object obj) {
    return (obj != null) ? obj.toString() : null;
  }

  public static String toStringWrapper(Object wrapper, Object inner) {
    return STR.fmt("{}({})", toIdHash(wrapper), inner);
  }

  @SuppressWarnings("unchecked")
  public static <F, T> T cast(F obj) throws ClassCastException {
    return (T) obj;
  }

  public static String toIdHash(Object obj) {
    if (obj == null) {
      return NULL_STR;
    }
    return obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
  }

  public static Class<?> getClass(Object obj) {
    if (obj == null) {
      return null;
    }
    return obj.getClass();
  }

  public static void closeQuietly(AutoCloseable obj) {
    if (obj != null) {
      try {
        obj.close();
      } catch (Exception ignore) {
      }
    }
  }

  public static Iterable<?> toIterable(Object obj) {
    if (obj == null) {
      return null;
    }
    if (obj instanceof Iterable) {
      return (Iterable<?>) obj;
    }
    if (obj.getClass().isArray()) {
      return new ArrayUtils.ArrayIterableObj(obj);
    }
    throw new IllegalArgumentException("The obj must be an Iterable or an array.");
  }

  public static <T> String asString(T[] array) {
    if (array == null) {
      return NULL_STR;
    }
    return Arrays.stream(array).map(e -> toStringOrNull(e)).collect(Collectors.joining(", "));
  }

  public static String asString(Object array) {
    Arguments.isTrue((array == null) || array.getClass().isArray());

    if (array == null) {
      return NULL_STR;
    }
    return StreamUtils.stream(new ArrayUtils.ArrayIteratorObj(array)).map(e -> toStringOrNull(e))
        .collect(Collectors.joining(", "));
  }

  public static String asString(Iterable<?> iterable) {
    if (iterable == null) {
      return NULL_STR;
    }
    return StreamUtils.stream(iterable).map(e -> toStringOrNull(e)).collect(Collectors.joining(", "));
  }

  public static String asString(Iterator<?> iterator) {
    if (iterator == null) {
      return NULL_STR;
    }
    return StreamUtils.stream(iterator).map(e -> toStringOrNull(e)).collect(Collectors.joining(", "));
  }

  public static String asString(Enumeration<?> enumer) {
    if (enumer == null) {
      return NULL_STR;
    }
    return StreamUtils.stream(enumer).map(e -> toStringOrNull(e)).collect(Collectors.joining(", "));
  }
}
