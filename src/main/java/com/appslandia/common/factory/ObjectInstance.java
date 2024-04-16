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

import java.util.function.Function;

import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ObjectInstance {

  final ObjectFactory factory;
  final ObjectDefinition definition;
  final Function<ObjectDefinition, Object> producer;

  volatile Object singleton;
  final Object mutex = new Object();

  public ObjectInstance(ObjectFactory factory, ObjectDefinition definition,
      Function<ObjectDefinition, Object> producer) {
    this.factory = factory;
    this.definition = definition;
    this.producer = producer;
  }

  public ObjectDefinition getDefinition() {
    return this.definition;
  }

  public Object getInstance() {
    // PROTOTYPE
    if (this.definition.getScope() == ObjectScope.PROTOTYPE) {
      return producer.apply(this.definition);
    }

    // SINGLETON
    Object obj = this.singleton;
    if (obj == null) {
      synchronized (this.mutex) {
        if ((obj = this.singleton) == null) {
          this.singleton = obj = this.producer.apply(this.definition);
        }
      }
    }
    return obj;
  }

  public boolean destroy(Object impl) {
    Asserts.notNull(impl);

    if (impl == this.singleton) {
      // SINGLETON
      Asserts.isTrue(this.definition.getScope() == ObjectScope.SINGLETON);

      if (this.definition.getProducer() != null) {
        this.definition.getProducer().destroy(impl);
      } else {
        ObjectFactoryUtils.preDestroy(impl);
      }

      this.singleton = null;
      return true;

    } else {
      // PROTOTYPE
      Asserts.isTrue(this.definition.getScope() == ObjectScope.PROTOTYPE);

      for (Class<?> type : this.definition.getTypes()) {
        if (type.isInstance(impl)) {

          ObjectFactoryUtils.preDestroy(impl);
          return true;
        }
      }
      return false;
    }
  }
}
