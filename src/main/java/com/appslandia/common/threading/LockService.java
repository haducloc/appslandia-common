// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.threading;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class LockService<K> extends InitializingObject {

  private final ConcurrentMap<K, ReentrantLock> lockMap = new ConcurrentHashMap<>();

  @Override
  protected void init() throws Exception {
  }

  protected ReentrantLock produceLock() {
    return new ReentrantLock();
  }

  public ReentrantLock getLock(K key) {
    Arguments.notNull(key);

    return lockMap.computeIfAbsent(key, k -> produceLock());
  }
}