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

  public CaseInsensitiveSet(Set<String> newSet) {
    this.elements = newSet;
  }

  @Override
  public int size() {
    return this.elements.size();
  }

  @Override
  public boolean isEmpty() {
    return this.elements.isEmpty();
  }

  @Override
  public boolean contains(Object e) {
    return this.elements.contains(toValue((String) e));
  }

  @Override
  public Iterator<String> iterator() {
    return this.elements.iterator();
  }

  @Override
  public Object[] toArray() {
    return this.elements.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return this.elements.toArray(a);
  }

  @Override
  public boolean add(String e) {
    return this.elements.add(toValue(e));
  }

  @Override
  public boolean remove(Object e) {
    return this.elements.remove(toValue((String) e));
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
    var it = this.elements.iterator();
    while (it.hasNext()) {
      var e = it.next();
      if (!c.contains(e)) {
        it.remove();
        modified = true;
      }
    }
    return modified;
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
    this.elements.clear();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Set)) {
      return false;
    }
    Set<?> that = (Set<?>) o;
    return this.elements.equals(that);
  }

  @Override
  public int hashCode() {
    return this.elements.hashCode();
  }

  @Override
  public String toString() {
    return ObjectUtils.toStringWrapper(this, this.elements);
  }

  static String toValue(String value) {
    return (value != null) ? value.toLowerCase(Locale.ROOT) : null;
  }
}
