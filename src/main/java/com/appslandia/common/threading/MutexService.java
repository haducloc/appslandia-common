// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.threading;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class MutexService<K> extends InitializingObject {

  private final ConcurrentMap<K, Object> mutexMap = new ConcurrentHashMap<>();

  @Override
  protected void init() throws Exception {
  }

  protected Object produceMutex() {
    return new Object();
  }

  public Object getMutex(K key) {
    Arguments.notNull(key);

    return mutexMap.computeIfAbsent(key, k -> produceMutex());
  }
}
