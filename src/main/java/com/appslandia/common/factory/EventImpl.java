// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.factory;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.cdi.CDIEventListener;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.NotificationOptions;
import jakarta.enterprise.util.TypeLiteral;

/**
 *
 * @author Loc Ha
 *
 */
public class EventImpl<T> extends InitializingObject implements Event<T> {

  final ObjectFactory factory;

  final Class<?> type;
  final Annotation[] qualifiers;

  public EventImpl(ObjectFactory factory, Class<?> type, Annotation[] qualifiers) {
    this.factory = factory;
    this.type = type;
    this.qualifiers = qualifiers;
  }

  @Override
  protected void init() throws Exception {
  }

  @SuppressWarnings("unchecked")
  @Override
  public void fire(T event) {
    this.initialize();

    if (!this.type.isAssignableFrom(event.getClass())) {
      return;
    }

    for (Map.Entry<EventKey, List<Class<? extends CDIEventListener<?>>>> entry : factory.listenerMap.entrySet()) {
      var key = entry.getKey();

      if (!key.getType().isAssignableFrom(event.getClass())
          || !QualifierUtils.matchesQualifiers(key.getQualifiers(), this.qualifiers)) {
        continue;
      }

      for (Class<? extends CDIEventListener<?>> listenerType : entry.getValue()) {
        var listener = (CDIEventListener<T>) factory.getObject(listenerType);
        listener.onEvent(event);
      }
    }
  }

  @Override
  public <U extends T> CompletionStage<U> fireAsync(U event) {
    this.initialize();
    throw new UnsupportedOperationException();
  }

  @Override
  public <U extends T> CompletionStage<U> fireAsync(U event, NotificationOptions options) {
    this.initialize();
    throw new UnsupportedOperationException();
  }

  @Override
  public Event<T> select(Annotation... qualifiers) {
    this.initialize();

    if (qualifiers.length == 0) {
      return this;
    }
    var mergedQualifiers = mergeQualifiers(qualifiers);
    return new EventImpl<>(factory, this.type, mergedQualifiers);
  }

  @Override
  public <U extends T> Event<U> select(Class<U> subtype, Annotation... qualifiers) {
    this.initialize();

    if (this.type == subtype && qualifiers.length == 0) {
      return ObjectUtils.cast(this);
    }
    var mergedQualifiers = mergeQualifiers(qualifiers);
    return new EventImpl<>(factory, subtype, mergedQualifiers);
  }

  @Override
  public <U extends T> Event<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
    this.initialize();
    throw new UnsupportedOperationException();
  }

  private Annotation[] mergeQualifiers(Annotation[] qualifiers) {
    Set<Annotation> anns = CollectionUtils.addAll(new LinkedHashSet<>(), this.qualifiers);
    CollectionUtils.addAll(anns, qualifiers);
    return anns.toArray(new Annotation[anns.size()]);
  }
}
