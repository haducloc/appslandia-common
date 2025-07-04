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

package com.appslandia.common.utils;

import java.util.Arrays;
import java.util.Iterator;

import com.appslandia.common.base.FunctionBlock;
import com.appslandia.common.threading.BlockingQueuePool;

/**
 *
 * @author Loc Ha
 *
 */
public class ByteBlockPool {

  final int poolSize;
  final int blockSize;

  private final BlockingQueuePool<byte[]> pool;

  public ByteBlockPool(int poolSize, int blockSize, boolean resetOnRelease) {
    this.poolSize = poolSize;
    this.blockSize = blockSize;

    this.pool = new BlockingQueuePool<>(() -> new byte[blockSize], b -> {
      if (resetOnRelease) {
        Arrays.fill(b, (byte) 0);
      }
    }, poolSize);
  }

  public <R> R execute(FunctionBlock<byte[], R> fxBlock) throws Exception {
    return this.pool.execute(fxBlock);
  }

  public byte[] obtain() {
    return this.pool.obtain();
  }

  public boolean release(byte[] impl) {
    return this.pool.release(impl);
  }

  public void clear() {
    this.pool.clear();
  }

  public Iterator<byte[]> iterator() {
    return this.pool.iterator();
  }

  public int getPoolSize() {
    return this.pool.getPoolSize();
  }

  public int getBlockSize() {
    return this.blockSize;
  }

  public int getRemainingCapacity() {
    return this.pool.getRemainingCapacity();
  }
}
