// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class CaseInsensitiveSet implements Set<String>, Serializable {
  private static final long serialVersionUID = 1L;

  protected final Set<String> elements;

  public CaseInsensitiveSet() {
    this(new HashSet<>());
  }

  public CaseInsensitiveSet(Set<String> backingSet) {
    this.elements = backingSet;
  }

  @Override
  public int size() {
    return elements.size();
  }

  @Override
  public boolean isEmpty() {
    return elements.isEmpty();
  }

  @Override
  public boolean contains(Object e) {
    return (e instanceof String s) && elements.contains(toValue(s));
  }

  @Override
  public Iterator<String> iterator() {
    return elements.iterator();
  }

  @Override
  public Object[] toArray() {
    return elements.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return elements.toArray(a);
  }

  @Override
  public boolean add(String e) {
    return elements.add(toValue(e));
  }

  @Override
  public boolean remove(Object e) {
    return (e instanceof String s) && elements.remove(toValue(s));
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object e : c) {
      if (!contains(e)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends String> c) {
    var modified = false;
    for (String e : c) {
      if (add(e)) {
        modified = true;
      }
    }
    return modified;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    var modified = false;
    var it = elements.iterator();
    while (it.hasNext()) {
      var e = it.next();
      if (!containsIn(c, e)) {
        it.remove();
        modified = true;
      }
    }
    return modified;
  }

  static boolean containsIn(Collection<?> c, String value) {
    for (Object e : c) {
      if ((e instanceof String s) && toValue(s).equals(value)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    var modified = false;
    for (Object e : c) {
      if (remove(e)) {
        modified = true;
      }
    }
    return modified;
  }

  @Override
  public void clear() {
    elements.clear();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    return elements.equals(o);
  }

  @Override
  public int hashCode() {
    return elements.hashCode();
  }

  @Override
  public String toString() {
    return ObjectUtils.toStringWrapper(this, elements);
  }

  static String toValue(String value) {
    return (value != null) ? value.toLowerCase(Locale.ROOT) : null;
  }
}
