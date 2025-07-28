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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.appslandia.common.base.CleanupManager;
import com.appslandia.common.base.InitializeException;

/**
 *
 * @author Loc Ha
 *
 */
public class ModelUtils {

  // Collections.synchronizedMap(new WeakHashMap<>());
  private static final Map<Class<?>, BeanInfo> beanInfoCache = new ConcurrentHashMap<>();

  public static BeanInfo getBeanInfo(Class<?> clazz) {
    return beanInfoCache.computeIfAbsent(clazz, clz -> {
      try {
        var bi = Introspector.getBeanInfo(clz);
        Introspector.flushFromCaches(clz);
        return bi;

      } catch (IntrospectionException ex) {
        throw new InitializeException(ex.getMessage(), ex);
      }
    });
  }

  static {
    CleanupManager.register(new Runnable() {

      @Override
      public void run() {
        beanInfoCache.clear();
      }
    });
  }

  public static <T> void copy(T dest, T src, Function<String, Boolean> forProps) throws ReflectionException {
    try {
      var destBI = getBeanInfo(dest.getClass());

      for (PropertyDescriptor dpd : destBI.getPropertyDescriptors()) {
        if (!forProps.apply(dpd.getName())) {
          continue;
        }
        Asserts.notNull(dpd.getWriteMethod());
        Asserts.notNull(dpd.getReadMethod());

        var value = dpd.getReadMethod().invoke(src);
        dpd.getWriteMethod().invoke(dest, value);
      }
    } catch (ReflectiveOperationException ex) {
      throw new ReflectionException(ex);
    } catch (Exception ex) {
      throw ExceptionUtils.toUncheckedException(ex);
    }
  }

  public static <T> void copy(T dest, T src, String... forProps) throws ReflectionException {
    copy(dest, src, prop -> Arrays.stream(forProps).anyMatch(p -> p.equals(prop)));
  }

  public static <D, S> void copyProps(D dest, S src, Function<String, Boolean> forProps) throws ReflectionException {
    try {
      var destBI = getBeanInfo(dest.getClass());
      var srcBI = getBeanInfo(src.getClass());

      for (PropertyDescriptor dpd : destBI.getPropertyDescriptors()) {
        if (!forProps.apply(dpd.getName())) {
          continue;
        }
        Asserts.notNull(dpd.getWriteMethod());

        PropertyDescriptor spd = null;
        for (PropertyDescriptor sp : srcBI.getPropertyDescriptors()) {
          if (sp.getName().equals(dpd.getName())) {
            spd = sp;
            break;
          }
        }
        Asserts.notNull(spd);
        Asserts.notNull(spd.getReadMethod());

        var value = spd.getReadMethod().invoke(src);
        dpd.getWriteMethod().invoke(dest, value);
      }
    } catch (ReflectiveOperationException ex) {
      throw new ReflectionException(ex);
    } catch (Exception ex) {
      throw ExceptionUtils.toUncheckedException(ex);
    }
  }

  public static <D, S> void copyProps(D dest, S src, String... forProps) throws ReflectionException {
    copyProps(dest, src, prop -> Arrays.stream(forProps).anyMatch(p -> p.equals(prop)));
  }
}
