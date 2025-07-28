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

package com.appslandia.common.factory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.STR;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.TypeLiteral;

/**
 *
 * @author Loc Ha
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
    return new Iterator<>() {
      int index = -1;

      @Override
      public T next() {
        var inst = instances.get(++this.index);
        return ObjectUtils.cast(inst.get());
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
    var obj = this.instances.get(0).get();
    return ObjectUtils.cast(obj);
  }

  @Override
  public Instance<T> select(Annotation... qualifiers) {
    if (qualifiers.length == 0) {
      return this;
    }
    var childQualifiers = getChildQualifiers(qualifiers);
    List<ObjectInstance> sub = new ArrayList<>();

    for (ObjectInstance inst : this.instances) {
      if (AnnotationUtils.matches(inst.definition.getQualifiers(), childQualifiers)) {

        sub.add(inst);
      }
    }
    return new InstanceImpl<>(this.type, childQualifiers, sub);
  }

  @Override
  public <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
    Arguments.notNull(subtype);
    Arguments.isTrue(this.type.isAssignableFrom(subtype));

    if ((this.type == subtype) && (qualifiers.length == 0)) {
      return ObjectUtils.cast(this);
    }
    var childQualifiers = getChildQualifiers(qualifiers);
    List<ObjectInstance> sub = new ArrayList<>();

    for (ObjectInstance inst : this.instances) {
      if (AnnotationUtils.matches(inst.definition.getQualifiers(), childQualifiers)) {

        if (inst.definition.hasType(subtype)) {
          sub.add(inst);
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
    // No-op
  }

  private Annotation[] getChildQualifiers(Annotation[] qualifiers) {
    Set<Annotation> anns = CollectionUtils.toSet(new LinkedHashSet<>(), this.qualifiers);
    CollectionUtils.toSet(anns, qualifiers);
    return anns.toArray(new Annotation[anns.size()]);
  }

  @Override
  public Handle<T> getHandle() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Iterable<? extends Handle<T>> handles() {
    throw new UnsupportedOperationException();
  }
}
