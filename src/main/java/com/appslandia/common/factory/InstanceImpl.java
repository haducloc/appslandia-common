// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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

package com.appslandia.common.factory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.STR;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.TypeLiteral;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class InstanceImpl<T> implements Instance<T> {

  final Class<?> type;
  final Annotation[] qualifiers;
  final List<ObjectInstance> instances;

  public InstanceImpl(Class<?> type, Annotation[] qualifiers, List<ObjectInstance> instances) {
    this.type = type;
    this.qualifiers = qualifiers;
    this.instances = instances;
  }

  public int getCount() {
    return this.instances.size();
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      int index = -1;

      @Override
      public T next() {
        ObjectInstance objInst = instances.get(++this.index);
        return ObjectUtils.cast(objInst.getInstance());
      }

      @Override
      public boolean hasNext() {
        return this.index < instances.size() - 1;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public T get() {
    if (isUnsatisfied()) {
      throw new ObjectException(
          STR.fmt("Unsatisfied dependency: type={}, qualifiers={}.", this.type, Arrays.toString(this.qualifiers)));
    }
    if (isAmbiguous()) {
      throw new ObjectException(
          STR.fmt("Ambiguous dependency: type={}, qualifiers={}.", this.type, Arrays.toString(this.qualifiers)));
    }
    return ObjectUtils.cast(this.instances.get(0).getInstance());
  }

  @Override
  public Instance<T> select(Annotation... qualifiers) {
    if (qualifiers.length == 0) {
      return this;
    }
    Annotation[] childQualifiers = getChildQualifiers(qualifiers);
    List<ObjectInstance> sub = new ArrayList<>();

    for (ObjectInstance objInst : this.instances) {
      if (AnnotationUtils.hasAnnotations(objInst.definition.getQualifiers(), childQualifiers)) {
        sub.add(objInst);
      }
    }
    return new InstanceImpl<>(this.type, childQualifiers, sub);
  }

  @Override
  public <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
    Arguments.notNull(subtype);

    if ((this.type == subtype) && (qualifiers.length == 0)) {
      return ObjectUtils.cast(this);
    }
    Annotation[] childQualifiers = getChildQualifiers(qualifiers);
    List<ObjectInstance> sub = new ArrayList<>();

    for (ObjectInstance objInst : this.instances) {
      if (AnnotationUtils.hasAnnotations(objInst.definition.getQualifiers(), childQualifiers)) {

        if (subtype.isInstance(objInst.getInstance())) {
          sub.add(objInst);
        }
      }
    }
    return new InstanceImpl<>(subtype, childQualifiers, sub);
  }

  @Override
  public <U extends T> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isUnsatisfied() {
    return this.instances.isEmpty();
  }

  @Override
  public boolean isAmbiguous() {
    return this.instances.size() > 1;
  }

  @Override
  public void destroy(T impl) {
    for (ObjectInstance objInst : this.instances) {
      if (objInst.destroy(impl)) {
        return;
      }
    }
    throw new UnsupportedOperationException("destroy(" + impl.getClass().getName() + ")");
  }

  private Annotation[] getChildQualifiers(Annotation[] qualifiers) {
    Set<Annotation> s = CollectionUtils.toSet(new LinkedHashSet<>(), this.qualifiers);
    CollectionUtils.toSet(s, qualifiers);
    return s.toArray(new Annotation[s.size()]);
  }

  @Override
  public Handle<T> getHandle() {
    return new HandleImpl();
  }

  @Override
  public Iterable<? extends Handle<T>> handles() {
    List<Handle<T>> handles = new ArrayList<>();

    for (int idx = 0; idx < this.instances.size(); idx++) {
      handles.add(new HandleImpl(idx));
    }
    return Collections.unmodifiableList(handles);
  }

  private class HandleImpl implements Handle<T> {

    final Integer index;

    private volatile T instance;
    final Object mutex = new Object();

    public HandleImpl() {
      this(null);
    }

    public HandleImpl(Integer index) {
      this.index = index;
    }

    @Override
    public T get() {
      T obj = this.instance;
      if (obj == null) {
        synchronized (mutex) {
          if ((obj = this.instance) == null) {
            if (this.index == null) {
              this.instance = obj = InstanceImpl.this.get();
            } else {
              ObjectInstance objInst = InstanceImpl.this.instances.get(this.index);
              this.instance = obj = ObjectUtils.cast(objInst.getInstance());
            }
          }
        }
      }
      return obj;
    }

    @Override
    public Bean<T> getBean() {
      throw new UnsupportedOperationException();
    }

    // Should be invoked once

    @Override
    public void destroy() {
      T obj = this.instance;
      if (obj != null) {
        InstanceImpl.this.destroy(obj);
      }
    }

    @Override
    public void close() {
    }
  }
}
