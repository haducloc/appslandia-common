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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 *
 * @author Loc Ha
 *
 */
public class ReflectionUtils {

  public static final Annotation[] EMPTY_ANNOTATIONS = {};

  public interface FieldHandler {

    boolean matches(Field field);

    boolean handle(Field field) throws ReflectionException;
  }

  public interface MethodHandler {

    boolean matches(Method m);

    boolean handle(Method m) throws ReflectionException;
  }

  public static Field findField(Class<?> clazz, final String property) throws ReflectionException {
    return traverse(clazz, new FieldHandler() {

      @Override
      public boolean matches(Field f) {
        if (f.getName().equals(property)) {
          return true;
        }
        if (f.getType() == boolean.class) {
          if (f.getName().equals("is" + StringUtils.firstUpperCase(property, Locale.ENGLISH))) {
            return true;
          }
        }
        return false;
      }

      @Override
      public boolean handle(Field field) throws ReflectionException {
        return false;
      }
    });
  }

  public static Method findMethod(Class<?> clazz, final String methodName) throws ReflectionException {
    return traverse(clazz, new MethodHandler() {

      @Override
      public boolean matches(Method m) {
        return m.getName().equals(methodName);
      }

      @Override
      public boolean handle(Method m) throws ReflectionException {
        return false;
      }
    });
  }

  public static List<Method> findMethods(Class<?> clazz, final String methodName, final Class<?>... parameterTypes)
      throws ReflectionException {
    final List<Method> mths = new ArrayList<>();

    traverse(clazz, new MethodHandler() {

      @Override
      public boolean matches(Method m) {
        if (!m.getName().equals(methodName) || (parameterTypes.length != m.getParameterCount())) {
          return false;
        }
        if (parameterTypes.length == 0) {
          return true;
        }
        final var mpTypes = m.getParameterTypes();
        return IntStream.range(0, mpTypes.length).allMatch(idx -> mpTypes[idx].isAssignableFrom(parameterTypes[idx]));
      }

      @Override
      public boolean handle(Method m) throws ReflectionException {
        mths.add(m);
        return true;
      }
    });

    return mths;
  }

  public static Field traverse(Class<?> clazz, FieldHandler handler) throws ReflectionException {
    Field matched = null;
    while (clazz != null) {
      for (Field field : clazz.getDeclaredFields()) {
        if (handler.matches(field)) {

          matched = field;
          if (!handler.handle(field)) {
            return matched;
          }
        }
      }
      clazz = clazz.getSuperclass();
    }
    return matched;
  }

  public static Method traverse(Class<?> clazz, MethodHandler handler) throws ReflectionException {
    Method matched = null;
    while (clazz != null) {
      var methods = (clazz.isInterface() ? clazz.getMethods() : clazz.getDeclaredMethods());

      for (Method m : methods) {
        if (handler.matches(m)) {

          matched = m;
          if (!handler.handle(m)) {
            return matched;
          }
        }
      }
      clazz = clazz.getSuperclass();
    }
    return matched;
  }

  public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationClass) {
    while (clazz != null) {
      var t = clazz.getAnnotation(annotationClass);
      if (t != null) {
        return t;
      }
      for (Class<?> interfaceClass : clazz.getInterfaces()) {
        t = interfaceClass.getAnnotation(annotationClass);
        if (t != null) {
          return t;
        }
      }
      clazz = clazz.getSuperclass();
    }
    return null;
  }

  public static <T> T newInstance(Class<T> clazz) throws ReflectionException {
    try {
      var c = clazz.getDeclaredConstructor();
      if (c.trySetAccessible()) {

        return c.newInstance();
      } else {
        throw new InaccessibleObjectException(STR.fmt("The constructor {} is inaccessible for reflection.", c));
      }

    } catch (ReflectiveOperationException ex) {
      throw new ReflectionException(ex);
    }
  }

  public static Object newInstance(Constructor<?> c, Object... args) throws ReflectionException {
    try {
      if (c.trySetAccessible()) {
        return c.newInstance(args);
      } else {
        throw new InaccessibleObjectException(STR.fmt("The constructor {} is inaccessible for reflection.", c));
      }
    } catch (ReflectiveOperationException ex) {
      throw new ReflectionException(ex);
    }
  }

  public static Object invoke(Method m, Object obj, Object... args) throws ReflectionException {
    try {
      if (m.trySetAccessible()) {
        return m.invoke(obj, args);
      } else {
        throw new InaccessibleObjectException(STR.fmt("The method {} is inaccessible for reflection.", m));
      }

    } catch (ReflectiveOperationException ex) {
      throw new ReflectionException(ex);
    }
  }

  public static void set(Field m, Object obj, Object value) throws ReflectionException {
    try {
      if (m.trySetAccessible()) {
        m.set(obj, value);
      } else {
        throw new InaccessibleObjectException(STR.fmt("The field {} is inaccessible for reflection.", m));
      }
    } catch (ReflectiveOperationException ex) {
      throw new ReflectionException(ex);
    }
  }

  public static Object get(Field m, Object obj) throws ReflectionException {
    try {
      if (m.trySetAccessible()) {
        return m.get(obj);
      } else {
        throw new InaccessibleObjectException(STR.fmt("The field {} is inaccessible for reflection.", m));
      }
    } catch (ReflectiveOperationException ex) {
      throw new ReflectionException(ex);
    }
  }

  public static Class<?> getArgTypes1(Type genericType) {
    if (!(genericType instanceof ParameterizedType)) {
      return null;
    }
    var types = ((ParameterizedType) genericType).getActualTypeArguments();
    if (types.length != 1) {
      return null;
    }
    var type = types[0];
    if (!(type instanceof Class)) {
      return null;
    }
    return (Class<?>) type;
  }

  public static Class<?>[] getArgTypes2(Type genericType) {
    if (!(genericType instanceof ParameterizedType)) {
      return null;
    }
    var types = ((ParameterizedType) genericType).getActualTypeArguments();
    if (types.length != 2) {
      return null;
    }
    var kt = types[0];
    if (!(kt instanceof Class)) {
      return null;
    }
    var vt = types[1];
    if (!(vt instanceof Class)) {
      return null;
    }
    return new Class<?>[] { (Class<?>) kt, (Class<?>) vt };
  }

  public static <T> Class<? extends T> loadClass(String className, ClassLoader loader) throws ReflectionException {
    var cl = (loader != null) ? loader : getDefaultClassLoader();
    try {
      return ObjectUtils.cast(cl.loadClass(className));
    } catch (ReflectiveOperationException ex) {
      throw new ReflectionException(ex);
    }
  }

  public static ClassLoader getDefaultClassLoader() {
    ClassLoader cl = null;
    try {
      cl = Thread.currentThread().getContextClassLoader();
    } catch (Exception ex) {
    }
    if (cl == null) {
      cl = ReflectionUtils.class.getClassLoader();
      if (cl == null) {
        try {
          cl = ClassLoader.getSystemClassLoader();
        } catch (Exception ex) {
        }
      }
    }
    return cl;
  }

  public static boolean isImplementOf(Method implMth, Method interfaceMth) {
    return interfaceMth.getDeclaringClass().isAssignableFrom(implMth.getDeclaringClass())
        && interfaceMth.getName().equals(implMth.getName())
        && Arrays.equals(interfaceMth.getParameterTypes(), implMth.getParameterTypes());
  }

  public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
    try {
      return clazz.getDeclaredMethod(name, parameterTypes);

    } catch (ReflectiveOperationException ex) {
      throw new ReflectionException(ex);
    }
  }

  public static boolean isPublicConst(int modifier) {
    return Modifier.isPublic(modifier) && Modifier.isStatic(modifier) && Modifier.isFinal(modifier);
  }
}
