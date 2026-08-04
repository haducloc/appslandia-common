// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.factory;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ReflectionException;
import com.appslandia.common.utils.ReflectionUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;

/**
 *
 * @author Loc Ha
 *
 */
public class FactoryUtils {

  public static <T> T postConstruct(T obj) {
    Arguments.notNull(obj);

    ReflectionUtils.traverse(obj.getClass(), new ReflectionUtils.MethodHandler() {

      @Override
      public boolean matches(Method m) {
        return m.getDeclaredAnnotation(PostConstruct.class) != null;
      }

      @Override
      public boolean handle(Method m) throws ReflectionException {
        ReflectionUtils.invoke(m, obj);
        return false;
      }
    });
    return obj;
  }

  public static void preDestroy(Object obj) {
    Arguments.notNull(obj);

    ReflectionUtils.traverse(obj.getClass(), new ReflectionUtils.MethodHandler() {

      @Override
      public boolean matches(Method m) {
        return m.getDeclaredAnnotation(PreDestroy.class) != null;
      }

      @Override
      public boolean handle(Method m) throws ReflectionException {
        ReflectionUtils.invoke(m, obj);
        return false;
      }
    });
  }

  public static Method getProduceMethod(ObjectProducer<?> producer) {
    var m = ReflectionUtils.getDeclaredMethod(producer.getClass(), "produce", ObjectFactory.class);
    return m;
  }

  public static ObjectScope parseScope(AnnotatedElement element) {
    if (element.getDeclaredAnnotation(ApplicationScoped.class) != null) {
      return ObjectScope.SINGLETON;
    }
    if (element.getDeclaredAnnotation(Dependent.class) != null) {
      return ObjectScope.PROTOTYPE;
    }
    if (element.getDeclaredAnnotation(SessionScoped.class) != null
        || element.getDeclaredAnnotation(RequestScoped.class) != null) {
      throw new IllegalArgumentException("SessionScoped/RequestScoped is unsupported.");
    }
    return ObjectScope.SINGLETON;
  }
}
