// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Loc Ha
 *
 */
public class LruCache<K, V> implements Serializable {
  private static final long serialVersionUID = 1L;

  final Mutex mutex = new Mutex();
  final Map<K, V> cache;

  public LruCache(final int cacheSize) {
    cache = new LruMap<>(cacheSize);
  }

  public void put(K k, V v) {
    synchronized (mutex) {
      cache.put(k, v);
    }
  }

  public boolean contains(K k) {
    synchronized (mutex) {
      return cache.containsKey(k);
    }
  }

  public V remove(K k) {
    synchronized (mutex) {
      return cache.remove(k);
    }
  }

  public V get(K k) {
    synchronized (mutex) {
      return cache.get(k);
    }
  }
}
