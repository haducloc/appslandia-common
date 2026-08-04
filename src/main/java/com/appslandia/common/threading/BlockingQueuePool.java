// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
    this(supplier, poolSize, null);
  }

  public BlockingQueuePool(Supplier<T> supplier, int poolSize, Consumer<T> reset) {
    Arguments.notNull(supplier);

    this.supplier = supplier;
    this.reset = reset;
    this.poolSize = poolSize;

    pool = new ArrayBlockingQueue<>(poolSize);
  }

  public <R> R execute(FunctionBlock<T, R> fxBlock) {
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
    var impl = pool.poll();
    if (impl != null) {
      return impl;
    }
    return supplier.get();
  }

  public boolean release(T impl) {
    Arguments.notNull(impl);
    if (reset != null) {
      reset.accept(impl);
    }
    return pool.offer(impl);
  }

  public void clear() {
    pool.clear();
  }

  public Iterator<T> iterator() {
    return pool.iterator();
  }

  public int getPoolSize() {
    return poolSize;
  }

  public int getRemainingCapacity() {
    return pool.remainingCapacity();
  }

  @Override
  public String toString() {
    var ts = ObjectUtils.toIdHash(this);
    return ts + "(poolSize=" + poolSize + ", remainingCapacity:=" + pool.remainingCapacity() + ")";
  }
}
