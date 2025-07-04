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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Loc Ha
 *
 */
public class CollectionUtils {

  @SafeVarargs
  public static <V> Set<V> unmodifiableSet(V... elements) {
    return (elements.length > 0) ? unmodifiableSet(new HashSet<V>(), elements) : Collections.emptySet();
  }

  @SafeVarargs
  public static <V> Set<V> unmodifiableSet(Set<V> s, V... elements) {
    return Collections.unmodifiableSet(toSet(s, elements));
  }

  @SafeVarargs
  public static <V> Set<V> toSet(V... elements) {
    return toSet(new HashSet<V>(), elements);
  }

  @SafeVarargs
  public static <V> Set<V> toSet(Set<V> s, V... elements) {
    Arrays.stream(elements).forEach(e -> s.add(e));
    return s;
  }

  @SafeVarargs
  public static <V> List<V> unmodifiableList(V... elements) {
    return (elements.length > 0) ? unmodifiableList(new ArrayList<V>(), elements) : Collections.emptyList();
  }

  @SafeVarargs
  public static <V> List<V> unmodifiableList(List<V> l, V... elements) {
    return Collections.unmodifiableList(toList(l, elements));
  }

  @SafeVarargs
  public static <V> List<V> toList(V... elements) {
    return toList(new ArrayList<V>(), elements);
  }

  @SafeVarargs
  public static <V> List<V> toList(List<V> l, V... elements) {
    Arrays.stream(elements).forEach(e -> l.add(e));
    return l;
  }

  public static <V> Set<V> unmodifiable(Set<V> set) {
    return ((set != null) && !set.isEmpty()) ? Collections.unmodifiableSet(set) : Collections.emptySet();
  }

  public static <V> List<V> unmodifiable(List<V> list) {
    return ((list != null) && !list.isEmpty()) ? Collections.unmodifiableList(list) : Collections.emptyList();
  }

  public static <K, V> Map<K, V> unmodifiable(Map<K, V> map) {
    return ((map != null) && !map.isEmpty()) ? Collections.unmodifiableMap(map) : Collections.emptyMap();
  }

  public static <K, V> Map<V, K> inverse(Map<K, V> m, Map<V, K> newMap) {
    for (Entry<K, V> entry : m.entrySet()) {
      newMap.put(entry.getValue(), entry.getKey());
    }
    return newMap;
  }

  public static <T> boolean hasElements(Collection<T> c) {
    return (c != null) && (c.size() > 0);
  }

  public static <K, V> boolean hasEntries(Map<K, V> m) {
    return (m != null) && (m.size() > 0);
  }
}
