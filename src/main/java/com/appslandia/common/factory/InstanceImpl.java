// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
    return instances.size();
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<>() {
      int index = -1;

      @Override
      public T next() {
        var inst = instances.get(++index);
        return ObjectUtils.cast(inst.get());
      }

      @Override
      public boolean hasNext() {
        return index < instances.size() - 1;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  /**
   * Returns the same instance for singleton-scoped objects and a new instance for prototype-scoped objects.
   * <p>
   * The returned objects are managed by the factory and may be destroyed either implicitly by the factory or explicitly
   * by application code.
   *
   * @return the managed object instance
   */
  @Override
  public T get() {
    if (isUnsatisfied()) {
      throw new ObjectException(
          STR.fmt("Unsatisfied dependency: type={}, qualifiers={}.", type, Arrays.toString(qualifiers)));
    }
    if (isAmbiguous()) {
      throw new ObjectException(
          STR.fmt("Ambiguous dependency: type={}, qualifiers={}.", type, Arrays.toString(qualifiers)));
    }
    var obj = instances.get(0).get();
    return ObjectUtils.cast(obj);
  }

  @Override
  public Instance<T> select(Annotation... qualifiers) {
    if (qualifiers.length == 0) {
      return this;
    }
    var mergedQualifiers = mergeQualifiers(qualifiers);
    List<ObjectInstance> sub = new ArrayList<>();

    for (ObjectInstance inst : instances) {
      if (QualifierUtils.matchesQualifiers(inst.definition.getQualifiers(), mergedQualifiers)) {

        sub.add(inst);
      }
    }
    return new InstanceImpl<>(type, mergedQualifiers, sub);
  }

  @Override
  public <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
    Arguments.notNull(subtype);
    Arguments.isTrue(type.isAssignableFrom(subtype));

    if ((type == subtype) && (qualifiers.length == 0)) {
      return ObjectUtils.cast(this);
    }
    var mergedQualifiers = mergeQualifiers(qualifiers);
    List<ObjectInstance> sub = new ArrayList<>();

    for (ObjectInstance inst : instances) {
      if (QualifierUtils.matchesQualifiers(inst.definition.getQualifiers(), mergedQualifiers)) {

        if (inst.definition.hasExportedType(subtype)) {
          sub.add(inst);
        }
      }
    }
    return new InstanceImpl<>(subtype, mergedQualifiers, sub);
  }

  @Override
  public <U extends T> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isUnsatisfied() {
    return instances.isEmpty();
  }

  @Override
  public boolean isAmbiguous() {
    return instances.size() > 1;
  }

  @Override
  public void destroy(T impl) {
    if (impl == null) {
      return;
    }

    for (ObjectInstance inst : instances) {
      if (inst.isManaged(impl)) {
        inst.destroy(impl);
        return;
      }
    }

    throw new ObjectException(
        STR.fmt("Instance is not managed by this Instance: type={}, instance={}.", type, impl.getClass()));
  }

  private Annotation[] mergeQualifiers(Annotation[] qualifiers) {
    Set<Annotation> anns = CollectionUtils.addAll(new LinkedHashSet<>(), this.qualifiers);
    CollectionUtils.addAll(anns, qualifiers);
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
