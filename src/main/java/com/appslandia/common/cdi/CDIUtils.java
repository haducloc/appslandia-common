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

package com.appslandia.common.cdi;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ReflectionUtils;

import jakarta.el.ELProcessor;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CDIUtils {

  public static <T> boolean consumeReference(BeanManager beanManager, Class<? extends T> type, Consumer<T> consumer) {
    return consumeReference(beanManager, type, ReflectionUtils.EMPTY_ANNOTATIONS, consumer);
  }

  public static <T> boolean consumeReference(BeanManager beanManager, Class<? extends T> type, Annotation[] qualifiers,
      Consumer<T> consumer) {
    Set<Bean<?>> matchedBeans = beanManager.getBeans(type, qualifiers);
    if (matchedBeans.isEmpty()) {
      return false;
    }
    Bean<?> bean = beanManager.resolve(matchedBeans);
    final CreationalContext<T> ctx = ObjectUtils.cast(beanManager.createCreationalContext(bean));

    try {
      T t = ObjectUtils.cast(beanManager.getReference(bean, type, ctx));
      consumer.accept(t);

      return true;
    } finally {
      ctx.release();
    }
  }

  public static <T> BeanInstance<T> getReference(BeanManager beanManager, Class<? extends T> type,
      Annotation... qualifiers) {
    Set<Bean<?>> matchedBeans = beanManager.getBeans(type, qualifiers);
    if (matchedBeans.isEmpty()) {
      return null;
    }
    Bean<?> bean = beanManager.resolve(matchedBeans);
    CreationalContext<T> ctx = ObjectUtils.cast(beanManager.createCreationalContext(bean));

    T t = ObjectUtils.cast(beanManager.getReference(bean, type, ctx));
    return new BeanInstance<>(t, ctx);
  }

  public static <T> BeanInstance<T> getReference(BeanManager beanManager, String name) {
    Set<Bean<?>> matchedBeans = beanManager.getBeans(name);
    if (matchedBeans.isEmpty()) {
      return null;
    }
    Bean<?> bean = beanManager.resolve(matchedBeans);
    CreationalContext<T> ctx = ObjectUtils.cast(beanManager.createCreationalContext(bean));

    T t = ObjectUtils.cast(beanManager.getReference(bean, bean.getBeanClass(), ctx));
    return new BeanInstance<>(t, ctx);
  }

  public static <T, A extends Annotation> void scanReferences(BeanManager beanManager, Class<T> type,
      Annotation[] qualifiers, Class<A> annotationClass, BiConsumer<A, BeanInstance<T>> consumer) {
    Set<Bean<T>> beans = ObjectUtils.cast(beanManager.getBeans(type, qualifiers));
    for (Bean<T> bean : beans) {

      if (annotationClass == null) {
        CreationalContext<T> ctx = ObjectUtils.cast(beanManager.createCreationalContext(bean));
        T t = ObjectUtils.cast(beanManager.getReference(bean, type, ctx));

        consumer.accept(null, new BeanInstance<>(t, ctx));
      } else {
        A annotation = bean.getBeanClass().getDeclaredAnnotation(annotationClass);
        if (annotation != null) {

          CreationalContext<T> ctx = ObjectUtils.cast(beanManager.createCreationalContext(bean));
          T t = ObjectUtils.cast(beanManager.getReference(bean, type, ctx));

          consumer.accept(annotation, new BeanInstance<>(t, ctx));
        }
      }
    }
  }

  public static void scanSuppliers(BeanManager beanManager, Annotation[] qualifiers, Class<?> constraintType,
      Consumer<BeanInstance<CDISupplier>> consumer) {
    Set<Bean<CDISupplier>> beans = ObjectUtils.cast(beanManager.getBeans(CDISupplier.class, qualifiers));
    for (Bean<CDISupplier> bean : beans) {

      Supplier supplier = bean.getBeanClass().getDeclaredAnnotation(Supplier.class);
      if ((supplier != null) && (supplier.value() == constraintType)) {

        CreationalContext<CDISupplier> ctx = beanManager.createCreationalContext(bean);
        CDISupplier t = ObjectUtils.cast(beanManager.getReference(bean, CDISupplier.class, ctx));

        consumer.accept(new BeanInstance<>(t, ctx));
      }
    }
  }

  public static <T, A extends Annotation> void scanBeanClasses(BeanManager beanManager, Class<T> type,
      Annotation[] qualifiers, Class<A> annotationClass, BiConsumer<A, Class<?>> consumer) {
    Set<Bean<T>> beans = ObjectUtils.cast(beanManager.getBeans(type, qualifiers));
    for (Bean<T> bean : beans) {

      A annotation = bean.getBeanClass().getDeclaredAnnotation(annotationClass);
      if (annotation != null) {
        consumer.accept(annotation, bean.getBeanClass());
      }
    }
  }

  public static ELProcessor getELProcessor(BeanManager beanManager) {
    ELProcessor elProcessor = new ELProcessor();
    elProcessor.getELManager().addELResolver(beanManager.getELResolver());

    return elProcessor;
  }
}
