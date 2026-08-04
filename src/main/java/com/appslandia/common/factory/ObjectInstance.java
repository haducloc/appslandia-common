// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.factory;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.Function;

/**
 *
 * @author Loc Ha
 *
 */
public class ObjectInstance {

  final ObjectFactory factory;
  final ObjectDefinition definition;
  final Function<ObjectDefinition, Object> producer;

  final Set<Object> prototypes = Collections.newSetFromMap(new IdentityHashMap<>());

  volatile Object singleton;
  final Object mutex = new Object();

  public ObjectInstance(ObjectFactory factory, ObjectDefinition definition,
      Function<ObjectDefinition, Object> producer) {
    this.factory = factory;
    this.definition = definition;
    this.producer = producer;
  }

  /**
   * Returns the same instance for singleton-scoped objects and a new instance for prototype-scoped objects.
   * <p>
   * The returned objects are managed by the factory and may be destroyed either implicitly by the factory or explicitly
   * by application code.
   *
   * @return the managed object instance
   */
  public Object get() {
    // Prototype
    if (definition.getScope() == ObjectScope.PROTOTYPE) {
      var obj = producer.apply(definition);

      synchronized (prototypes) {
        prototypes.add(obj);
      }
      return obj;
    }

    // Singleton
    var obj = singleton;
    if (obj == null) {
      synchronized (mutex) {
        if ((obj = singleton) == null) {
          singleton = obj = producer.apply(definition);
        }
      }
    }
    return obj;
  }

  public boolean isManaged(Object obj) {
    if (obj == null) {
      return false;
    }

    if (definition.getScope() == ObjectScope.SINGLETON) {
      return singleton == obj;
    }

    synchronized (prototypes) {
      return prototypes.contains(obj);
    }
  }

  public void destroyAll() {
    // Singleton
    var obj = singleton;
    if (obj != null) {
      synchronized (mutex) {
        if ((obj = singleton) != null) {
          destroyObject(obj);
          singleton = null;
        }
      }
    }

    // Prototypes
    Object[] objs;
    synchronized (prototypes) {
      objs = prototypes.toArray();
      prototypes.clear();
    }

    for (Object prototype : objs) {
      destroyObject(prototype);
    }
  }

  public void destroy(Object obj) {
    // Singleton
    if ((obj == null) || (definition.getScope() == ObjectScope.SINGLETON)) {
      return;
    }

    // Prototypes
    synchronized (prototypes) {
      if (!prototypes.remove(obj)) {
        throw new ObjectException("Prototype instance is not managed.");
      }
    }
    destroyObject(obj);
  }

  private void destroyObject(Object obj) {
    if (definition.getProducer() != null) {
      definition.getProducer().destroy(obj);
    } else {
      FactoryUtils.preDestroy(obj);
    }
  }
}