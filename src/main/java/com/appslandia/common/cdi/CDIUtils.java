// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.cdi;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ReflectionUtils;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

/**
 *
 * @author Loc Ha
 *
 */
public class CDIUtils {

  public static <T> void consumeSuppliers(BeanManager beanManager, Class<T> typeSupplied,
      Consumer<CDISupplier> consumer) {
    Set<Bean<CDISupplier>> beans = ObjectUtils
        .cast(beanManager.getBeans(CDISupplier.class, ReflectionUtils.EMPTY_ANNOTATIONS));

    for (var bean : beans) {
      var supplier = bean.getBeanClass().getDeclaredAnnotation(Supplier.class);

      if ((supplier != null) && (supplier.value() == typeSupplied)) {
        CreationalContext<CDISupplier> ctx = ObjectUtils.cast(beanManager.createCreationalContext(bean));

        try {
          CDISupplier t = ObjectUtils.cast(beanManager.getReference(bean, CDISupplier.class, ctx));
          consumer.accept(t);
        } finally {
          ctx.release();
        }
      }
    }
  }

  public static <T> boolean consumeReference(BeanManager beanManager, Class<T> type, Consumer<T> consumer) {
    return consumeReference(beanManager, type, ReflectionUtils.EMPTY_ANNOTATIONS, consumer);
  }

  public static <T> boolean consumeReference(BeanManager beanManager, Class<T> type, Annotation[] qualifiers,
      Consumer<T> consumer) {
    var matchedBeans = beanManager.getBeans(type, qualifiers);
    if (matchedBeans.isEmpty()) {
      return false;
    }
    var bean = beanManager.resolve(matchedBeans);
    CreationalContext<T> ctx = ObjectUtils.cast(beanManager.createCreationalContext(bean));

    try {
      T t = ObjectUtils.cast(beanManager.getReference(bean, type, ctx));
      consumer.accept(t);

      return true;
    } finally {
      ctx.release();
    }
  }

  public static <T> BeanInstance<T> getReference(BeanManager beanManager, Class<T> type, Annotation... qualifiers) {
    var matchedBeans = beanManager.getBeans(type, qualifiers);
    if (matchedBeans.isEmpty()) {
      return null;
    }
    var bean = beanManager.resolve(matchedBeans);
    CreationalContext<T> ctx = ObjectUtils.cast(beanManager.createCreationalContext(bean));

    T t = ObjectUtils.cast(beanManager.getReference(bean, type, ctx));
    return new BeanInstance<>(t, ctx);
  }

  public static <T> BeanInstance<T> getReference(BeanManager beanManager, String name) {
    var matchedBeans = beanManager.getBeans(name);
    if (matchedBeans.isEmpty()) {
      return null;
    }
    var bean = beanManager.resolve(matchedBeans);
    CreationalContext<T> ctx = ObjectUtils.cast(beanManager.createCreationalContext(bean));

    T t = ObjectUtils.cast(beanManager.getReference(bean, bean.getBeanClass(), ctx));
    return new BeanInstance<>(t, ctx);
  }

  public static <T, A extends Annotation> void scanReferences(BeanManager beanManager, Class<T> type,
      Annotation[] qualifiers, BiConsumer<Class<?>, BeanInstance<T>> consumer) {
    Set<Bean<T>> beans = ObjectUtils.cast(beanManager.getBeans(type, qualifiers));
    for (var bean : beans) {

      CreationalContext<T> ctx = ObjectUtils.cast(beanManager.createCreationalContext(bean));
      T t = ObjectUtils.cast(beanManager.getReference(bean, type, ctx));
      consumer.accept(bean.getBeanClass(), new BeanInstance<>(t, ctx));
    }
  }

  public static <T, A extends Annotation> void scanBeanClasses(BeanManager beanManager, Class<T> type,
      Annotation[] qualifiers, Consumer<Class<?>> consumer) {
    Set<Bean<T>> beans = ObjectUtils.cast(beanManager.getBeans(type, qualifiers));

    for (var bean : beans) {
      consumer.accept(bean.getBeanClass());
    }
  }
}
