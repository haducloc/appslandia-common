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

  public CaseInsensitiveMap(Map<String, V> innerMap) {
    map = innerMap;
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
    return java.util.Collections.unmodifiableSet(map.keySet());
  }

  @Override
  public Collection<V> values() {
    return java.util.Collections.unmodifiableCollection(map.values());
  }

  @Override
  public Set<Entry<String, V>> entrySet() {
    return java.util.Collections.unmodifiableSet(map.entrySet());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Map)) {
      return false;
    }
    Map<?, ?> that = (Map<?, ?>) o;
    return map.equals(that);
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
