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
    if (this.destroyed.compareAndSet(false, true)) {

      if (this.instance != null) {
        this.instance.destroy(this.obj);
      } else {
        this.creationalContext.release();
      }
    }
  }

  public T get() {
    if (this.destroyed.get()) {
      throw new IllegalStateException("The instance is already destroyed.");
    }
    return this.obj;
  }
}
