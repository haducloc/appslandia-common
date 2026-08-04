// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.DestroyingSupport;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.cdi.CDIEventListener;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.STR;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

/**
 *
 * @author Loc Ha
 *
 */
public class ObjectFactory extends InitializingObject implements DestroyingSupport {

  final List<ObjectInstance> instances = new ArrayList<>();
  final Map<EventKey, List<Class<? extends CDIEventListener<?>>>> listenerMap = new HashMap<>();

  @Override
  protected void init() throws Exception {
    validateFactory();
  }

  private void validateFactory() {
    for (ObjectInstance inst : instances) {
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
          validateInject(parameter.getType(), QualifierUtils.parseQualifiers(parameter), parameter);
        }

        @Override
        public void onField(Field field) {
          validateInject(field.getType(), QualifierUtils.parseQualifiers(field), field);
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
          if (ObjectFactory.class.isAssignableFrom(type) || (type == Instance.class) || (type == Event.class)) {
            return;
          }
          var count = countMatchesForInject(type, qualifiers);
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
    var count = 0;
    for (var inst : instances) {
      if (inst.definition.hasExportedType(type)
          && QualifierUtils.matchesQualifiers(inst.definition.getQualifiers(), qualifiers)) {
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

    var produceMth = FactoryUtils.getProduceMethod(producer);

    for (Class<?> type : types) {
      if (!type.isAssignableFrom(produceMth.getReturnType())) {
        throw new IllegalArgumentException("The type must be assignable from the producer method's return type.");
      }
    }

    if (scope == null) {
      scope = FactoryUtils.parseScope(produceMth);
    }
    if (qualifiers == null) {
      qualifiers = QualifierUtils.parseQualifiers(produceMth);
    }
    var inst = new ObjectInstance(this,
        new ObjectDefinition().setTypes(types).setQualifiers(qualifiers).setScope(scope).setProducer(producer),
        (d) -> produceObject(d));
    instances.add(inst);
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

    for (Class<?> type : types) {
      if (!type.isAssignableFrom(implClass)) {
        throw new IllegalArgumentException("The type must be assignable from the implClass.");
      }
    }

    Set<Class<?>> expTypes = CollectionUtils.toSet(types);
    expTypes.add(implClass);
    expTypes.add(Object.class);

    if (scope == null) {
      scope = FactoryUtils.parseScope(implClass);
    }
    if (qualifiers == null) {
      qualifiers = QualifierUtils.parseQualifiers(implClass);
    }

    var inst = new ObjectInstance(this, new ObjectDefinition().setTypes(expTypes.toArray(new Class<?>[expTypes.size()]))
        .setQualifiers(qualifiers).setScope(scope).setImplClass(implClass), (d) -> produceObject(d));
    instances.add(inst);
    return this;
  }

  public ObjectFactory unregister(Class<?> implClass, Annotation... qualifiers) {
    assertNotInitialized();
    Arguments.notNull(implClass);

    var iter = instances.iterator();
    while (iter.hasNext()) {
      var inst = iter.next();

      if ((inst.definition.getImplClass() == implClass)
          && QualifierUtils.matchesQualifiers(inst.definition.getQualifiers(), qualifiers)) {
        iter.remove();
      }
    }
    return this;
  }

  public ObjectFactory registerEventListener(Class<? extends CDIEventListener<?>> listenerType, Class<?> eventType,
      Annotation... qualifiers) {
    assertNotInitialized();

    listenerMap.computeIfAbsent(new EventKey(eventType, qualifiers), k -> new ArrayList<>()).add(listenerType);
    return this;
  }

  public ObjectFactory unregisterEventListener(Class<? extends CDIEventListener<?>> listenerType, Class<?> eventType,
      Annotation... qualifiers) {
    assertNotInitialized();

    var types = listenerMap.get(new EventKey(eventType, qualifiers));
    if (types != null) {
      types.remove(listenerType);
    }
    return this;
  }

  public ObjectFactory unregisterEventListener(Class<?> eventType, Annotation... qualifiers) {
    assertNotInitialized();
    listenerMap.remove(new EventKey(eventType, qualifiers));
    return this;
  }

  public <T> Instance<T> select(Class<T> type, Annotation... qualifiers) {
    initialize();
    var insts = getObjectInsts(type, qualifiers);
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

      @SuppressWarnings({ "unchecked", "rawtypes" })
      @Override
      public void onField(Field field) {
        var qualifiers = QualifierUtils.parseQualifiers(field);

        // @Inject @Instance<T>
        if (field.getType() == Instance.class) {
          Asserts.isTrue(field.getGenericType() instanceof ParameterizedType);
          Class<?> type = ReflectionUtils.getArgTypes1(field.getGenericType());
          Asserts.notNull(type);

          ReflectionUtils.set(field, obj, select(type, qualifiers));
          return;
        }

        // @Inject @Event<T>
        if (field.getType() == Event.class) {
          Asserts.isTrue(field.getGenericType() instanceof ParameterizedType);
          Class<?> type = ReflectionUtils.getArgTypes1(field.getGenericType());
          Asserts.notNull(type);

          ReflectionUtils.set(field, obj, new EventImpl(ObjectFactory.this, type, qualifiers));
          return;
        }

        // Default
        Object value = getObject(field.getType(), qualifiers);
        ReflectionUtils.set(field, obj, value);
      }

      @Override
      public void onMethod(Method method) {
        ReflectionUtils.invoke(method, obj, createArguments(method.getParameters()));
      }
    }.traverse(obj.getClass());
    return this;
  }

  private Object[] createArguments(Parameter[] parameters) {
    var args = new Object[parameters.length];
    for (var i = 0; i < parameters.length; i++) {

      var parameter = parameters[i];
      args[i] = getObject(parameter.getType(), QualifierUtils.parseQualifiers(parameter));
    }
    return args;
  }

  private Object produceObject(ObjectDefinition definition) {
    // Producer-created objects are returned as-is.
    // Like CDI producer methods, the producer is responsible for custom initialization.
    if (definition.getProducer() != null) {
      return definition.getProducer().produce(this);
    }

    // Constructor
    Constructor<?> emptyCtor = null, injectCtor = null;
    for (var ctor : definition.getImplClass().getDeclaredConstructors()) {

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
      var args = createArguments(injectCtor.getParameters());
      instance = ReflectionUtils.newInstance(injectCtor, args);
    } else {
      instance = ReflectionUtils.newInstance(emptyCtor);
    }

    // Inject
    inject(instance);
    postConstruct(instance);

    return instance;
  }

  private List<ObjectInstance> getObjectInsts(Class<?> type, Annotation[] qualifiers) {
    List<ObjectInstance> insts = new ArrayList<>();
    for (var inst : instances) {

      if (inst.definition.hasExportedType(type)
          && QualifierUtils.matchesQualifiers(inst.definition.getQualifiers(), qualifiers)) {

        insts.add(inst);
      }
    }
    return insts;
  }

  private ObjectInstance getObjectInst(Class<?> type, Annotation[] qualifiers) {
    ObjectInstance obj = null;
    for (var inst : instances) {

      if (inst.definition.hasExportedType(type)
          && QualifierUtils.matchesQualifiers(inst.definition.getQualifiers(), qualifiers)) {

        if (obj != null) {
          throw new ObjectException(
              STR.fmt("Ambiguous dependency: type={}, qualifiers={}.", type, Arrays.toString(qualifiers)));
        }
        obj = inst;
      }
    }
    if (obj == null) {
      throw new ObjectException(
          STR.fmt("Unsatisfied dependency: type={}, qualifiers={}.", type, Arrays.toString(qualifiers)));
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

    var inst = getObjectInst(type, qualifiers);
    return ObjectUtils.cast(inst.get());
  }

  public <T> T postConstruct(T obj) {
    initialize();
    return FactoryUtils.postConstruct(obj);
  }

  @Override
  public void destroy() throws DestroyingException {
    // initialize();
    for (var inst : instances) {
      inst.destroyAll();
    }
  }

  public Iterator<ObjectDefinition> getDefinitionIterator() {
    initialize();
    return new Iterator<>() {
      int index = -1;

      @Override
      public ObjectDefinition next() {
        var inst = instances.get(++index);
        return inst.definition;
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

  static abstract class InjectTraverser {

    public abstract boolean isValidationContext();

    public abstract void onParameter(Parameter parameter);

    public abstract void onField(Field field);

    public abstract void onMethod(Method method);

    public void traverse(Class<?> implClass) {
      Class<?> clazz = null;
      if (isValidationContext()) {

        // Constructor
        for (var ctor : implClass.getDeclaredConstructors()) {
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
          var methods = clazz.getDeclaredMethods();

          for (var method : methods) {
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
        var fields = clazz.getDeclaredFields();

        for (var field : fields) {
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
          var methods = clazz.getDeclaredMethods();

          for (var method : methods) {
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
