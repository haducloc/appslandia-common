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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Loc Ha
 *
 */
public class TypeUtils {

  private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP;
  private static final Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP;

  static {
    Map<Class<?>, Class<?>> primToWrap = new HashMap<>();
    Map<Class<?>, Class<?>> wrapToPrim = new HashMap<>();

    put(primToWrap, wrapToPrim, boolean.class, Boolean.class);
    put(primToWrap, wrapToPrim, byte.class, Byte.class);
    put(primToWrap, wrapToPrim, char.class, Character.class);
    put(primToWrap, wrapToPrim, double.class, Double.class);
    put(primToWrap, wrapToPrim, float.class, Float.class);
    put(primToWrap, wrapToPrim, int.class, Integer.class);
    put(primToWrap, wrapToPrim, long.class, Long.class);
    put(primToWrap, wrapToPrim, short.class, Short.class);
    put(primToWrap, wrapToPrim, void.class, Void.class);

    PRIMITIVE_WRAPPER_MAP = Collections.unmodifiableMap(primToWrap);
    WRAPPER_PRIMITIVE_MAP = Collections.unmodifiableMap(wrapToPrim);
  }

  private static void put(Map<Class<?>, Class<?>> primToWrap, Map<Class<?>, Class<?>> wrapToPrim, Class<?> key,
      Class<?> value) {
    primToWrap.put(key, value);
    wrapToPrim.put(value, key);
  }

  public static Set<Class<?>> allPrimitiveTypes() {
    return PRIMITIVE_WRAPPER_MAP.keySet();
  }

  public static Set<Class<?>> allWrapperTypes() {
    return WRAPPER_PRIMITIVE_MAP.keySet();
  }

  public static boolean isWrapperType(Class<?> type) {
    return WRAPPER_PRIMITIVE_MAP.containsKey(type);
  }

  public static Class<?> wrap(Class<?> type) {
    Class<?> wrapped = PRIMITIVE_WRAPPER_MAP.get(type);
    return (wrapped == null) ? type : wrapped;
  }

  public static Class<?> unwrap(Class<?> type) {
    Class<?> unwrapped = WRAPPER_PRIMITIVE_MAP.get(type);
    return (unwrapped == null) ? type : unwrapped;
  }

  public static boolean isPrimitiveOrWrapper(Class<?> type) {
    if (type.isPrimitive()) {
      return true;
    }
    return WRAPPER_PRIMITIVE_MAP.containsKey(wrap(type));
  }

  private static final Map<Class<?>, Object> PRIMITIVE_DEFAULT_MAP;

  static {
    Map<Class<?>, Object> defaults = new HashMap<>();

    put(defaults, boolean.class, false);
    put(defaults, char.class, '\0');
    put(defaults, byte.class, (byte) 0);
    put(defaults, short.class, (short) 0);
    put(defaults, int.class, 0);
    put(defaults, long.class, 0L);
    put(defaults, float.class, 0f);
    put(defaults, double.class, 0d);

    PRIMITIVE_DEFAULT_MAP = Collections.unmodifiableMap(defaults);
  }

  private static <T> void put(Map<Class<?>, Object> map, Class<T> type, T value) {
    map.put(type, value);
  }

  public static <T> T defaultValue(Class<T> type) {
    if (!type.isPrimitive()) {
      return null;
    }
    return ObjectUtils.cast(PRIMITIVE_DEFAULT_MAP.get(type));
  }
}
