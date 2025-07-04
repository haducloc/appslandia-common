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
