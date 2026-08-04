// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class CaseInsensitiveMap<V> implements Map<String, V>, Serializable {
  private static final long serialVersionUID = 1L;

  protected final Map<String, V> map;

  public CaseInsensitiveMap() {
    this(new HashMap<>());
  }

  public CaseInsensitiveMap(Map<String, V> backingMap) {
    map = backingMap;
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return (key instanceof String s) && map.containsKey(toKey(s));
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return (key instanceof String s) ? map.get(toKey(s)) : null;
  }

  @Override
  public V put(String key, V value) {
    return map.put(toKey(key), value);
  }

  @Override
  public V remove(Object key) {
    return (key instanceof String s) ? map.remove(toKey(s)) : null;
  }

  @Override
  public void putAll(Map<? extends String, ? extends V> m) {
    for (Entry<? extends String, ? extends V> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<String> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<V> values() {
    return map.values();
  }

  @Override
  public Set<Entry<String, V>> entrySet() {
    return map.entrySet();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    return map.equals(o);
  }

  @Override
  public int hashCode() {
    return map.hashCode();
  }

  @Override
  public String toString() {
    return ObjectUtils.toStringWrapper(this, map);
  }

  static String toKey(String key) {
    return (key != null) ? key.toLowerCase(Locale.ROOT) : null;
  }
}
