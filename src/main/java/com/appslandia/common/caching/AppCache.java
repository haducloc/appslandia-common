// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.caching;

import java.util.Set;

/**
 *
 * @author Loc Ha
 *
 */
public interface AppCache<K, V> {

  V get(K key);

  void put(K key, V value);

  boolean putIfAbsent(K key, V value);

  boolean containsKey(K key);

  boolean remove(K key);

  boolean remove(K key, V oldValue);

  void removeAll(Set<? extends K> keys);

  boolean replace(K key, V value);

  boolean replace(K key, V oldValue, V newValue);

  void clear();
}
