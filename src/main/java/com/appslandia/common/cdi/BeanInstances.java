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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 *
 * @author Loc Ha
 *
 */
public class BeanInstances {

  private final List<BeanInstance<?>> instances = Collections.synchronizedList(new ArrayList<>());
  private final AtomicBoolean destroyed = new AtomicBoolean(false);

  public void add(BeanInstance<?> bi) {
    if (this.destroyed.get()) {
      throw new IllegalStateException("Cannot add() after destroy() has run.");
    }
    this.instances.add(bi);
  }

  public void destroy(Consumer<Exception> logger) {
    if (this.destroyed.compareAndSet(false, true)) {
      var it = this.instances.listIterator(this.instances.size());
      while (it.hasPrevious()) {
        try {
          var bi = it.previous();
          bi.destroy();

        } catch (Exception ex) {
          logger.accept(ex);
        }
      }
      this.instances.clear();
    }
  }
}
