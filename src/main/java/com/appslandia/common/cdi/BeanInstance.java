// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.cdi;

import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Instance;

/**
 *
 * @author Loc Ha
 *
 */
public class BeanInstance<T> {

  final T obj;
  final Instance<T> instance;
  final CreationalContext<T> creationalContext;
  final AtomicBoolean destroyed = new AtomicBoolean(false);

  public BeanInstance(T obj, Instance<T> instance) {
    this(obj, instance, null);
  }

  public BeanInstance(T obj, CreationalContext<T> creationalContext) {
    this(obj, null, creationalContext);
  }

  protected BeanInstance(T obj, Instance<T> instance, CreationalContext<T> creationalContext) {
    this.obj = obj;
    this.instance = instance;
    this.creationalContext = creationalContext;
  }

  public void destroy() {
    if (destroyed.compareAndSet(false, true)) {

      if (instance != null) {
        instance.destroy(obj);
      } else {
        creationalContext.release();
      }
    }
  }

  public T get() {
    if (destroyed.get()) {
      throw new IllegalStateException("The instance is already destroyed.");
    }
    return obj;
  }
}
