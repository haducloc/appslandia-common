// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 *
 * @author Loc Ha
 *
 */
public class LazyRef<T> {

  private final boolean softRef;
  private final Object mutex = new Object();

  private volatile Reference<T> ref = null;
  private Supplier<T> supplier;
  private final AtomicBoolean destroyed = new AtomicBoolean(false);

  public LazyRef(Supplier<T> supplier, boolean softRef) {
    this.supplier = supplier;
    this.softRef = softRef;
  }

  public T get() {
    if (destroyed.get()) {
      throw new IllegalStateException("Cannot get() after destroy() has run.");
    }

    var value = (ref != null) ? ref.get() : null;
    if (value == null) {
      synchronized (mutex) {
        value = (ref != null) ? ref.get() : null;
        if (value == null) {

          value = supplier.get();
          ref = softRef ? new SoftReference<>(value) : new WeakReference<>(value);
        }
      }
    }
    return value;
  }

  public void destroy() {
    if (destroyed.compareAndSet(false, true)) {
      synchronized (mutex) {
        if (ref != null) {
          ref.clear();
          ref = null;
        }
        supplier = null;
      }
    }
  }
}
