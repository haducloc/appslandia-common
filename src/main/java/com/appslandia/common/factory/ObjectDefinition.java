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
import java.util.Arrays;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.ValueUtils;

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
    Arguments.notNull(types);
    Arguments.notNull(scope);
    Arguments.isTrue((implClass != null) || (producer != null));

    qualifiers = ValueUtils.valueOrAlt(qualifiers, ReflectionUtils.EMPTY_ANNOTATIONS);
  }

  public boolean hasType(Class<?> type) {
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

  public ObjectDefinition setQualifiers(Annotation... qualifiers) {
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
