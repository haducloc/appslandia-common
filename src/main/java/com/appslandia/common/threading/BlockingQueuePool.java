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

package com.appslandia.common.threading;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.appslandia.common.base.FunctionBlock;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class BlockingQueuePool<T> {

  private final Supplier<T> supplier;
  private final Consumer<T> reset;

  private final int poolSize;
  private final BlockingQueue<T> pool;

  public BlockingQueuePool(Supplier<T> supplier, int poolSize) {
    this(supplier, null, poolSize);
  }

  public BlockingQueuePool(Supplier<T> supplier, Consumer<T> reset, int poolSize) {
    Arguments.notNull(supplier);

    this.supplier = supplier;
    this.reset = reset;
    this.poolSize = poolSize;

    this.pool = new ArrayBlockingQueue<>(poolSize);
  }

  public <R> R execute(FunctionBlock<T, R> fxBlock) throws Exception {
    Arguments.notNull(fxBlock);

    T t = null;
    try {
      t = obtain();
      return fxBlock.run(t);

    } finally {
      if (t != null) {
        release(t);
      }
    }
  }

  public T obtain() {
    var impl = this.pool.poll();
    if (impl != null) {
      return impl;
    }
    return this.supplier.get();
  }

  public boolean release(T impl) {
    Arguments.notNull(impl);
    if (this.reset != null) {
      this.reset.accept(impl);
    }
    return this.pool.offer(impl);
  }

  public void clear() {
    this.pool.clear();
  }

  public Iterator<T> iterator() {
    return this.pool.iterator();
  }

  public int getPoolSize() {
    return this.poolSize;
  }

  public int getRemainingCapacity() {
    return this.pool.remainingCapacity();
  }

  @Override
  public String toString() {
    var ts = ObjectUtils.toIdHash(this);
    return ts + "(poolSize=" + this.poolSize + ", remainingCapacity:=" + this.pool.remainingCapacity() + ")";
  }
}
