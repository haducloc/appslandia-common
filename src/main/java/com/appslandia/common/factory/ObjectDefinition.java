// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.factory;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class ObjectDefinition extends InitializingObject {

  private Class<?>[] types;
  private Annotation[] qualifiers;
  private ObjectScope scope;

  private Class<?> implClass;
  private ObjectProducer<?> producer;

  @Override
  protected void init() throws Exception {
    Arguments.hasElements(types);
    Arguments.notNull(qualifiers);

    Arguments.notNull(scope);
    Arguments.isTrue((implClass != null) || (producer != null));
  }

  public boolean hasExportedType(Class<?> type) {
    initialize();
    return Arrays.stream(types).anyMatch(t -> t == type);
  }

  public Class<?>[] getTypes() {
    initialize();
    return types.clone();
  }

  public ObjectDefinition setTypes(Class<?>[] types) {
    assertNotInitialized();
    this.types = types;
    return this;
  }

  public Annotation[] getQualifiers() {
    initialize();
    return qualifiers;
  }

  public ObjectDefinition setQualifiers(Annotation[] qualifiers) {
    assertNotInitialized();
    this.qualifiers = qualifiers;
    return this;
  }

  public ObjectScope getScope() {
    initialize();
    return scope;
  }

  public ObjectDefinition setScope(ObjectScope scope) {
    assertNotInitialized();
    this.scope = scope;
    return this;
  }

  public Class<?> getImplClass() {
    initialize();
    return implClass;
  }

  public ObjectDefinition setImplClass(Class<?> implClass) {
    assertNotInitialized();
    this.implClass = implClass;
    return this;
  }

  public ObjectProducer<?> getProducer() {
    initialize();
    return producer;
  }

  public ObjectDefinition setProducer(ObjectProducer<?> producer) {
    assertNotInitialized();
    this.producer = producer;
    return this;
  }
}
