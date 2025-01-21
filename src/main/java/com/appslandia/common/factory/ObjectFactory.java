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
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ReflectionException;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.STR;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ObjectFactory extends InitializeObject {

  final List<ObjectInstance> instances = new ArrayList<>();

  @Override
  protected void init() throws Exception {
    validateFactory();
  }

  private void validateFactory() {
    for (ObjectInstance inst : this.instances) {
      if (inst.definition.getProducer() != null) {
        continue;
      }
      new InjectTraverser() {

        @Override
        public boolean isValidationContext() {
          return true;
        }

        @Override
        public void onParameter(Parameter parameter) {
          validateInject(parameter.getType(), AnnotationUtils.parseQualifiers(parameter), parameter);
        }

        @Override
        public void onField(Field field) {
          validateInject(field.getType(), AnnotationUtils.parseQualifiers(field), field);
        }

        @Override
        public void onMethod(Method method) {
          throw new UnsupportedOperationException();
        }

        private String toMemberInfo(AnnotatedElement member) {
          if (member instanceof Parameter) {
            return ((Parameter) member).getDeclaringExecutable().toString();
          }
          return member.toString();
        }

        private void validateInject(Class<?> type, Annotation[] qualifiers, AnnotatedElement member) {
          if (ObjectFactory.class.isAssignableFrom(type)) {
            return;
          }
          if (type == Instance.class) {
            return;
          }
          int count = countMatchesForInject(type, qualifiers);
          if (count == 0) {
            throw new ObjectException(STR.fmt("Unsatisfied dependency: type={}, qualifiers={}, member={}.", type,
                Arrays.toString(qualifiers), toMemberInfo(member)));
          }
          if (count > 1) {
            throw new ObjectException(STR.fmt("Ambiguous dependency: type={}, qualifiers={}, member={}.", type,
                Arrays.toString(qualifiers), toMemberInfo(member)));
          }
        }
      }.traverse(inst.definition.getImplClass());
    }
  }

  private int countMatchesForInject(Class<?> type, Annotation[] qualifiers) {
    int count = 0;
    for (ObjectInstance inst : this.instances) {
      if ((inst.definition.hasType(type) || (type == Object.class))
          && AnnotationUtils.equals(inst.definition.getQualifiers(), qualifiers)) {
        count++;
      }
    }
    return count;
  }

  public ObjectFactory register(Class<?> type, ObjectProducer<?> producer) {
    return register(type, producer, null);
  }

  public ObjectFactory register(Class<?> type, ObjectProducer<?> producer, ObjectScope scope) {
    return register(type, producer, scope, null);
  }

  public ObjectFactory register(Class<?> type, ObjectProducer<?> producer, ObjectScope scope, Annotation[] qualifiers) {
    return register(new Class<?>[] { type }, producer, scope, qualifiers);
  }

  public ObjectFactory register(Class<?>[] types, ObjectProducer<?> producer) {
    return register(types, producer, null);
  }

  public ObjectFactory register(Class<?>[] types, ObjectProducer<?> producer, ObjectScope scope) {
    return register(types, producer, scope, null);
  }

  protected ObjectFactory register(Class<?>[] types, ObjectProducer<?> producer, ObjectScope scope,
      Annotation[] qualifiers) {
    assertNotInitialized();
    Arguments.notNull(types);
    Arguments.notNull(producer);

    if (scope == null) {
      scope = AnnotationUtils.parseScope(producer);
    }
    if (qualifiers == null) {
      qualifiers = AnnotationUtils.parseQualifiers(producer);
    }
    ObjectInstance inst = new ObjectInstance(this,
        new ObjectDefinition().setTypes(types).setQualifiers(qualifiers).setScope(scope).setProducer(producer),
        (d) -> produceObject(d));
    this.instances.add(inst);
    return this;
  }

  public ObjectFactory register(Class<?> type, Class<?> implClass) {
    return register(type, implClass, null);
  }

  public ObjectFactory register(Class<?> type, Class<?> implClass, ObjectScope scope) {
    return register(type, implClass, scope, null);
  }

  public ObjectFactory register(Class<?> type, Class<?> implClass, ObjectScope scope, Annotation[] qualifiers) {
    return register(new Class<?>[] { type }, implClass, scope, qualifiers);
  }

  public ObjectFactory register(Class<?>[] types, Class<?> implClass) {
    return register(types, implClass, null);
  }

  public ObjectFactory register(Class<?>[] types, Class<?> implClass, ObjectScope scope) {
    return register(types, implClass, scope, null);
  }

  protected ObjectFactory register(Class<?>[] types, Class<?> implClass, ObjectScope scope, Annotation[] qualifiers) {
    assertNotInitialized();
    Arguments.notNull(types);
    Arguments.notNull(implClass);

    Set<Class<?>> expTypes = CollectionUtils.toSet(types);
    expTypes.add(implClass);

    if (scope == null) {
      scope = AnnotationUtils.parseScope(implClass);
    }
    if (qualifiers == null) {
      qualifiers = AnnotationUtils.parseQualifiers(implClass);
    }

    ObjectInstance inst = new ObjectInstance(this,
        new ObjectDefinition().setTypes(expTypes.toArray(new Class<?>[expTypes.size()])).setQualifiers(qualifiers)
            .setScope(scope).setImplClass(implClass),
        (d) -> produceObject(d));
    this.instances.add(inst);
    return this;
  }

  public ObjectFactory unregister(Class<?> type, Annotation... qualifiers) {
    assertNotInitialized();
    Arguments.notNull(type);
    Arguments.notNull(qualifiers);

    Iterator<ObjectInstance> iter = this.instances.iterator();
    while (iter.hasNext()) {
      ObjectInstance inst = iter.next();

      if ((inst.definition.hasType(type) || (type == Object.class))
          && AnnotationUtils.equals(inst.definition.getQualifiers(), qualifiers)) {
        iter.remove();
      }
    }
    return this;
  }

  public <T> ObjectFactory unregister(Class<T> type, Class<? extends T> implClass) {
    Arguments.notNull(implClass);
    return unregister(type, AnnotationUtils.parseQualifiers(implClass));
  }

  public <T> Instance<T> select(Class<T> type, Annotation... qualifiers) {
    List<ObjectInstance> insts = getObjectInsts(type, qualifiers);
    return new InstanceImpl<>(type, qualifiers, insts);
  }

  public ObjectFactory inject(final Object obj) {
    initialize();
    Arguments.notNull(obj);

    new InjectTraverser() {

      @Override
      public boolean isValidationContext() {
        return false;
      }

      @Override
      public void onParameter(Parameter parameter) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void onField(Field field) {
        Annotation[] qualifiers = AnnotationUtils.parseQualifiers(field);

        // @Inject T t;
        if (field.getType() != Instance.class) {
          Object value = getObject(field.getType(), qualifiers);
          ReflectionUtils.set(field, obj, value);
          return;
        }

        // @Inject @Instance<T>
        Asserts.isTrue(field.getGenericType() instanceof ParameterizedType);
        Class<?> type = ReflectionUtils.getArgTypes1(field.getGenericType());
        Asserts.notNull(type);

        List<ObjectInstance> insts = getObjectInsts(type, qualifiers);
        ReflectionUtils.set(field, obj, new InstanceImpl<>(type, qualifiers, insts));
      }

      @Override
      public void onMethod(Method method) {
        ReflectionUtils.invoke(method, obj, createArguments(method.getParameters()));
      }
    }.traverse(obj.getClass());
    return this;
  }

  private Object[] createArguments(Parameter[] parameters) {
    Object[] args = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {

      Parameter parameter = parameters[i];
      args[i] = getObject(parameter.getType(), AnnotationUtils.parseQualifiers(parameter));
    }
    return args;
  }

  private Object produceObject(ObjectDefinition definition) {
    // Producer
    if (definition.getProducer() != null) {
      return definition.getProducer().produce(this);
    }

    // Constructor
    Constructor<?> emptyCtor = null, injectCtor = null;
    for (Constructor<?> ctor : definition.getImplClass().getDeclaredConstructors()) {

      if (ctor.getDeclaredAnnotation(Inject.class) != null) {
        injectCtor = ctor;
        break;
      }
      if (ctor.getParameterCount() == 0) {
        emptyCtor = ctor;
      }
    }
    if ((injectCtor == null) && (emptyCtor == null)) {
      throw new ObjectException(STR.fmt("Couldn't instantiate '{}'.", definition.getImplClass()));
    }

    Object instance = null;
    if (injectCtor != null) {
      instance = ReflectionUtils.newInstance(injectCtor, createArguments(injectCtor.getParameters()));
    } else {
      instance = ReflectionUtils.newInstance(emptyCtor);
    }

    // Inject & Invoke @PostConstruct
    return this.inject(instance).postConstruct(instance);
  }

  private List<ObjectInstance> getObjectInsts(Class<?> type, Annotation[] qualifiers) {
    List<ObjectInstance> insts = new ArrayList<>();
    for (ObjectInstance inst : this.instances) {

      if ((inst.definition.hasType(type) || (type == Object.class))
          && AnnotationUtils.hasAnnotations(inst.definition.getQualifiers(), qualifiers)) {
        insts.add(inst);
      }
    }
    return insts;
  }

  private ObjectInstance getObjectInst(Class<?> type, Annotation[] qualifiers) {
    ObjectInstance obj = null;
    for (ObjectInstance inst : this.instances) {

      if ((inst.definition.hasType(type) || (type == Object.class))
          && AnnotationUtils.equals(inst.definition.getQualifiers(), qualifiers)) {
        if (obj != null) {
          throw new ObjectException(
              STR.fmt("Ambiguous dependency: type={}, qualifiers={}.", type, Arrays.toString(qualifiers)));
        }
        obj = inst;
      }
    }
    return obj;
  }

  public <T, I extends T> I getObject(Class<T> type, Annotation... qualifiers) {
    initialize();
    Arguments.notNull(type);
    Arguments.notNull(qualifiers);

    if (ObjectFactory.class.isAssignableFrom(type)) {
      return ObjectUtils.cast(this);
    }

    ObjectInstance inst = getObjectInst(type, qualifiers);
    if (inst == null) {
      throw new ObjectException(
          STR.fmt("Unsatisfied dependency: type={}, qualifiers={}.", type, Arrays.toString(qualifiers)));
    }
    return ObjectUtils.cast(inst.getInstance());
  }

  public <T> T postConstruct(T obj) {
    initialize();
    Arguments.notNull(obj);

    ReflectionUtils.traverse(obj.getClass(), new ReflectionUtils.MethodHandler() {

      @Override
      public boolean matches(Method m) {
        return m.getDeclaredAnnotation(PostConstruct.class) != null;
      }

      @Override
      public boolean handle(Method m) throws ReflectionException {
        ReflectionUtils.invoke(m, obj);
        return false;
      }
    });
    return obj;
  }

  @Override
  public void destroy() throws DestroyException {
    for (ObjectInstance inst : this.instances) {
      Object obj = inst.singleton;

      if (obj == null) {
        continue;
      }
      inst.destroy(obj);
    }
  }

  public Iterator<ObjectDefinition> getDefinitionIterator() {
    initialize();
    return new Iterator<ObjectDefinition>() {
      int index = -1;

      @Override
      public ObjectDefinition next() {
        ObjectInstance inst = instances.get(++this.index);
        return inst.definition;
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

  static abstract class InjectTraverser {

    public abstract boolean isValidationContext();

    public abstract void onParameter(Parameter parameter);

    public abstract void onField(Field field);

    public abstract void onMethod(Method method);

    public void traverse(Class<?> implClass) {
      Class<?> clazz = null;
      if (isValidationContext()) {

        // Constructor
        for (Constructor<?> ctor : implClass.getDeclaredConstructors()) {
          if (ctor.getDeclaredAnnotation(Inject.class) != null) {

            for (Parameter parameter : ctor.getParameters()) {
              onParameter(parameter);
            }
            break;
          }
        }

        // Method
        clazz = implClass;
        while (clazz != Object.class) {
          Method[] methods = clazz.getDeclaredMethods();

          for (Method method : methods) {
            if (method.getDeclaredAnnotation(Inject.class) != null) {
              for (Parameter parameter : method.getParameters()) {
                onParameter(parameter);
              }
            }
          }
          clazz = clazz.getSuperclass();
        }
      }

      // Fields
      clazz = implClass;
      while (clazz != Object.class) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
          if (field.getDeclaredAnnotation(Inject.class) != null) {
            onField(field);
          }
        }
        clazz = clazz.getSuperclass();
      }

      // Injection Context
      if (!isValidationContext()) {
        clazz = implClass;
        while (clazz != Object.class) {
          Method[] methods = clazz.getDeclaredMethods();

          for (Method method : methods) {
            if (method.getDeclaredAnnotation(Inject.class) != null) {
              onMethod(method);
            }
          }
          clazz = clazz.getSuperclass();
        }
      }
    }
  }
}
