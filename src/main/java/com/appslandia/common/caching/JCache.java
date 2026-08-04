// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.caching;

import java.util.Set;

import javax.cache.Cache;

/**
 *
 * @author Loc Ha
 *
 */
public class JCache<K, V> implements AppCache<K, V> {

  final Cache<K, V> cache;

  public JCache(Cache<K, V> cache) {
    this.cache = cache;
  }

  @Override
  public V get(K key) {
    return cache.get(key);
  }

  @Override
  public void put(K key, V value) {
    cache.put(key, value);
  }

  @Override
  public boolean putIfAbsent(K key, V value) {
    return cache.putIfAbsent(key, value);
  }

  @Override
  public boolean containsKey(K key) {
    return cache.containsKey(key);
  }

  @Override
  public boolean remove(K key) {
    return cache.remove(key);
  }

  @Override
  public boolean remove(K key, V oldValue) {
    return cache.remove(key, oldValue);
  }

  @Override
  public void removeAll(Set<? extends K> keys) {
    cache.removeAll(keys);
  }

  @Override
  public boolean replace(K key, V value) {
    return cache.replace(key, value);
  }

  @Override
  public boolean replace(K key, V oldValue, V newValue) {
    return cache.replace(key, oldValue, newValue);
  }

  @Override
  public void clear() {
    cache.clear();
  }
}
