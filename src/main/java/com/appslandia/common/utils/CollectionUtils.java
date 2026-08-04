// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author Loc Ha
 *
 */
public class CollectionUtils {

  public static <E> Set<E> toUnmodifiableSet(Set<E> elements) {
    return toUnmodifiableSet(HashSet::new, elements);
  }

  public static <E> Set<E> toUnmodifiableSet(Function<Set<E>, Set<E>> copyFunction, Set<E> elements) {
    return (elements == null || elements.isEmpty()) ? Collections.emptySet()
        : Collections.unmodifiableSet(copyFunction.apply(elements));
  }

  @SafeVarargs
  public static <E> Set<E> toUnmodifiableSet(Supplier<Set<E>> copyFunction, E... elements) {
    return (elements == null || elements.length == 0) ? Collections.emptySet()
        : Collections.unmodifiableSet(toSet(copyFunction, elements));
  }

  @SafeVarargs
  public static <E> Set<E> unmodifiableSet(Set<E> backingSet, E... elements) {
    return Collections.unmodifiableSet(addAll(backingSet, elements));
  }

  @SafeVarargs
  public static <E> Set<E> toSet(E... elements) {
    return toSet(HashSet::new, elements);
  }

  @SafeVarargs
  public static <E> Set<E> toSet(Supplier<Set<E>> copyFunction, E... elements) {
    return addAll(copyFunction.get(), elements);
  }

  @SafeVarargs
  public static <E> Set<E> addAll(Set<E> set, E... elements) {
    if (elements != null) {
      Collections.addAll(set, elements);
    }
    return set;
  }

  public static <E> List<E> toUnmodifiableList(List<E> elements) {
    return toUnmodifiableList(ArrayList::new, elements);
  }

  public static <E> List<E> toUnmodifiableList(Function<List<E>, List<E>> copyFunction, List<E> elements) {
    return (elements == null || elements.isEmpty()) ? Collections.emptyList()
        : Collections.unmodifiableList(copyFunction.apply(elements));
  }

  @SafeVarargs
  public static <E> List<E> toUnmodifiableList(Supplier<List<E>> copyFunction, E... elements) {
    return (elements == null || elements.length == 0) ? Collections.emptyList()
        : Collections.unmodifiableList(toList(copyFunction, elements));
  }

  @SafeVarargs
  public static <E> List<E> unmodifiableList(List<E> backingList, E... elements) {
    return Collections.unmodifiableList(addAll(backingList, elements));
  }

  @SafeVarargs
  public static <E> List<E> toList(E... elements) {
    return toList(ArrayList::new, elements);
  }

  @SafeVarargs
  public static <E> List<E> toList(Supplier<List<E>> copyFunction, E... elements) {
    return addAll(copyFunction.get(), elements);
  }

  @SafeVarargs
  public static <E> List<E> addAll(List<E> list, E... elements) {
    if (elements != null) {
      Collections.addAll(list, elements);
    }
    return list;
  }

  public static <K, V> Map<K, V> toUnmodifiableMap(Map<K, V> entries) {
    return toUnmodifiableMap(HashMap::new, entries);
  }

  public static <K, V> Map<K, V> toUnmodifiableMap(Function<Map<K, V>, Map<K, V>> copyFunction, Map<K, V> entries) {
    return (entries == null || entries.isEmpty()) ? Collections.emptyMap()
        : Collections.unmodifiableMap(copyFunction.apply(entries));
  }

  public static <K, V> Map<V, K> inverse(Map<K, V> map, Map<V, K> reverseMap) {
    for (Entry<K, V> entry : map.entrySet()) {
      reverseMap.put(entry.getValue(), entry.getKey());
    }
    return reverseMap;
  }
}
