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
    if (this.destroyed.get()) {
      throw new IllegalStateException("Cannot get() after destroy() has run.");
    }

    var value = (this.ref != null) ? this.ref.get() : null;
    if (value == null) {
      synchronized (this.mutex) {
        value = (this.ref != null) ? this.ref.get() : null;
        if (value == null) {

          value = this.supplier.get();
          this.ref = this.softRef ? new SoftReference<>(value) : new WeakReference<>(value);
        }
      }
    }
    return value;
  }

  public void destroy() {
    if (this.destroyed.compareAndSet(false, true)) {
      synchronized (this.mutex) {
        if (this.ref != null) {
          this.ref.clear();
          this.ref = null;
        }
        this.supplier = null;
      }
    }
  }
}
