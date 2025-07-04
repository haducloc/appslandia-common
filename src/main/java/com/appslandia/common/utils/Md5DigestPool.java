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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import com.appslandia.common.base.FunctionBlock;
import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.threading.BlockingQueuePool;

/**
 *
 * @author Loc Ha
 *
 */
public class Md5DigestPool {

  private final BlockingQueuePool<MessageDigest> pool;

  public Md5DigestPool() {
    this(32);
  }

  public Md5DigestPool(int poolSize) {
    this.pool = new BlockingQueuePool<>(() -> md5(), md5 -> md5.reset(), poolSize);
  }

  public <R> R execute(FunctionBlock<MessageDigest, R> fxBlock) throws Exception {
    return this.pool.execute(fxBlock);
  }

  public MessageDigest obtain() {
    return this.pool.obtain();
  }

  public boolean release(MessageDigest impl) {
    return this.pool.release(impl);
  }

  public void clear() {
    this.pool.clear();
  }

  public Iterator<MessageDigest> iterator() {
    return this.pool.iterator();
  }

  public int getPoolSize() {
    return this.pool.getPoolSize();
  }

  public int getRemainingCapacity() {
    return this.pool.getRemainingCapacity();
  }

  static MessageDigest md5() {
    try {
      return MessageDigest.getInstance("MD5");

    } catch (NoSuchAlgorithmException ex) {
      throw new CryptoException(ex.getMessage(), ex);
    }
  }

  private static volatile Md5DigestPool __default;
  private static final Object MUTEX = new Object();

  public static Md5DigestPool getDefault() {
    var obj = __default;
    if (obj == null) {
      synchronized (MUTEX) {
        if ((obj = __default) == null) {
          __default = obj = initDefault();
        }
      }
    }
    return obj;
  }

  public static void setDefault(Md5DigestPool impl) {
    Asserts.isNull(__default, "Md5DigestPool.__default must be null.");

    if (__default == null) {
      synchronized (MUTEX) {
        if (__default == null) {
          __default = impl;
          return;
        }
      }
    }
  }

  private static Md5DigestPool initDefault() {
    // -Dmd5_pool_size=32
    var poolSize = SYS.resolveInt("{md5_pool_size,env.MD5_POOL_SIZE}", 32);
    return new Md5DigestPool(poolSize);
  }
}
