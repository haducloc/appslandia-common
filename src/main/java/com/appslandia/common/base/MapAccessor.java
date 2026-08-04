// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Loc Ha
 *
 */
public interface MapAccessor<K, V> extends Map<K, V> {

  @Override
  default public int size() {
    throw new UnsupportedOperationException();
  }

  @Override
  default public boolean isEmpty() {
    throw new UnsupportedOperationException();
  }

  @Override
  default public boolean containsKey(Object key) {
    throw new UnsupportedOperationException();
  }

  @Override
  default public boolean containsValue(Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  default public V put(K key, V value) {
    throw new UnsupportedOperationException();
  }

  @Override
  default public V remove(Object key) {
    throw new UnsupportedOperationException();
  }

  @Override
  default public void putAll(Map<? extends K, ? extends V> m) {
    throw new UnsupportedOperationException();
  }

  @Override
  default public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  default public Set<K> keySet() {
    throw new UnsupportedOperationException();
  }

  @Override
  default public Collection<V> values() {
    throw new UnsupportedOperationException();
  }

  @Override
  default public Set<Entry<K, V>> entrySet() {
    throw new UnsupportedOperationException();
  }
}
