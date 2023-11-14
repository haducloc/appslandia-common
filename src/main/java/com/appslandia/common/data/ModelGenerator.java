//The MIT License (MIT)
//Copyright © 2015 AppsLandia. All rights reserved.

//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:

//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.

//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.data;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.models.EntityBase;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.ValueUtils;
import com.appslandia.common.validators.MaxLength;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation.Composable;
import net.bytebuddy.implementation.MethodCall;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ModelGenerator extends InitializeObject {

  private String classPackage;
  private ClassLoader classLoader;
  private File classPath;

  private GenerationType idGenType;

  @Override
  protected void init() throws Exception {
    if (this.classPath != null) {
      Asserts.isTrue(this.classPath.isDirectory());
    }
  }

  public ModelGenerator setClassLoader(ClassLoader classLoader) {
    assertNotInitialized();
    this.classLoader = classLoader;
    return this;
  }

  public ModelGenerator setClassPackage(String classPackage) {
    assertNotInitialized();
    this.classPackage = classPackage;
    return this;
  }

  public ModelGenerator setClassPackage(Class<?> clazz) {
    return setClassPackage(clazz.getPackageName());
  }

  public ModelGenerator setClassPath(File classPath) {
    assertNotInitialized();
    this.classPath = classPath;
    return this;
  }

  public ModelGenerator setIdGenType(GenerationType idGenType) {
    assertNotInitialized();
    this.idGenType = idGenType;
    return this;
  }

  private Class<?> generateEntityPk(Table table) throws Exception {
    String pkClassName = table.getEntityClassName() + "Pk";
    String fullClass = this.classPackage != null ? this.classPackage + "." + pkClassName : pkClassName;

    // Pk annotations
    List<AnnotationDescription> pkAnnotations = new ArrayList<>();

    // @Embeddable
    pkAnnotations.add(AnnotationDescription.Builder.ofType(Embeddable.class).build());
    String[] keys = table.getColumns().stream().filter(f -> f.isKey()).map(f -> f.getName()).toArray(len -> new String[len]);

    // @TableMtdt
    pkAnnotations
        .add(AnnotationDescription.Builder.ofType(TableMtdt.class).define("catalog", ValueUtils.valueOrAlt(table.getTableCat(), StringUtils.EMPTY_STRING))
            .define("schema", ValueUtils.valueOrAlt(table.getTableSchema(), StringUtils.EMPTY_STRING)).define("table", table.getTableName())
            .define("keyClass", BaseGenPk.class).defineArray("keys", keys).build());

    // BaseGenPk base
    var builder = new ByteBuddy().subclass(BaseGenPk.class).name(fullClass).annotateType(pkAnnotations);

    for (Column column : table.getColumns()) {
      if (column.isKey()) {

        // Field annotations
        List<AnnotationDescription> fieldAnnotations = new ArrayList<>();
        column.getAnnotations().forEach(fa -> fieldAnnotations.add(toAnnotationDescription(fa)));

        // @NotNull
        fieldAnnotations.add(AnnotationDescription.Builder.ofType(NotNull.class).build());

        // @MaxLength
        if (column.getJavaType() == String.class && column.getColumnSize() != null) {
          fieldAnnotations.add(AnnotationDescription.Builder.ofType(MaxLength.class).define("value", column.getColumnSize()).build());
        }

        builder = addField(builder, column.getName(), column.getJavaType(), fieldAnnotations);
      }
    }

    // Constructor
    Composable ctor = MethodCall.invoke(BaseGenPk.class.getDeclaredConstructor()).onSuper();

    int index = 0;
    for (Column column : table.getColumns()) {
      if (column.isKey()) {
        ctor = ctor.andThen(FieldAccessor.ofField(column.getName()).setsArgumentAt(index++));
      }
    }

    List<Class<?>> argsTypes = table.getColumns().stream().filter(f -> f.isKey()).map(f -> f.getJavaType()).collect(Collectors.toList());
    builder = builder.defineConstructor(Visibility.PUBLIC).withParameters(argsTypes).intercept(ctor);

    return make(builder);
  }

  public Class<?> generateEntityClass(Table table) throws Exception {
    initialize();
    Asserts.notNull(table);

    Class<?> embeddedIdClass = null;

    if (table.getSingleKey() == null) {
      embeddedIdClass = generateEntityPk(table);
    }

    String fullClass = this.classPackage != null ? this.classPackage + "." + table.getEntityClassName() : table.getEntityClassName();
    String[] keys = table.getColumns().stream().filter(f -> f.isKey()).map(f -> f.getName()).toArray(len -> new String[len]);

    // Class annotations
    List<AnnotationDescription> classAnnotations = new ArrayList<>();
    table.getAnnotations().forEach(fa -> classAnnotations.add(toAnnotationDescription(fa)));

    // @TableMtdt
    classAnnotations
        .add(AnnotationDescription.Builder.ofType(TableMtdt.class).define("catalog", ValueUtils.valueOrAlt(table.getTableCat(), StringUtils.EMPTY_STRING))
            .define("schema", ValueUtils.valueOrAlt(table.getTableSchema(), StringUtils.EMPTY_STRING)).define("table", table.getTableName())
            .define("keyClass", (embeddedIdClass != null) ? embeddedIdClass : table.getSingleKey().getJavaType()).defineArray("keys", keys).build());

    // @Entity
    classAnnotations.add(AnnotationDescription.Builder.ofType(Entity.class).build());

    // EntityBase base
    var builder = new ByteBuddy().subclass(EntityBase.class).name(fullClass).annotateType(classAnnotations);

    // @EmbeddedId
    if (embeddedIdClass != null) {

      // pk
      List<AnnotationDescription> pkFieldAnnotations = new ArrayList<>();
      pkFieldAnnotations.add(AnnotationDescription.Builder.ofType(NotNull.class).build());
      pkFieldAnnotations.add(AnnotationDescription.Builder.ofType(EmbeddedId.class).build());

      builder = addField(builder, "pk", embeddedIdClass, pkFieldAnnotations);
    } else {

      // pk
      builder = builder.defineMethod("getPk", table.getSingleKey().getJavaType(), Visibility.PUBLIC)
          .intercept(FieldAccessor.ofField(table.getSingleKey().getName()));
    }

    for (Column column : table.getColumns()) {

      // Field annotations
      List<AnnotationDescription> fieldAnnotations = new ArrayList<>();
      column.getAnnotations().forEach(fa -> fieldAnnotations.add(toAnnotationDescription(fa)));

      if (column.isKey()) {
        if (embeddedIdClass == null) {

          // @Id
          fieldAnnotations.add(AnnotationDescription.Builder.ofType(Id.class).build());

          // @GeneratedValue
          if (column.isKeyIncr()) {
            if (this.idGenType == null) {
              fieldAnnotations.add(AnnotationDescription.Builder.ofType(GeneratedValue.class).build());
            } else {
              fieldAnnotations.add(AnnotationDescription.Builder.ofType(GeneratedValue.class).define("strategy", this.idGenType).build());
            }
          }
        }

      } else {

        // @NotNull
        if (!column.isNullable()) {
          fieldAnnotations.add(AnnotationDescription.Builder.ofType(NotNull.class).build());
        }
        // @MaxLength
        if (column.getJavaType() == String.class && column.getColumnSize() != null) {
          fieldAnnotations.add(AnnotationDescription.Builder.ofType(MaxLength.class).define("value", column.getColumnSize()).build());
        }
      }

      if ((column.isKey() && embeddedIdClass == null) || !column.isKey()) {

        builder = addField(builder, column.getName(), column.getJavaType(), fieldAnnotations);
      }
    }

    // Constructor
    Composable ctor = MethodCall.invoke(EntityBase.class.getDeclaredConstructor()).onSuper();

    List<Class<?>> argsTypes = new ArrayList<>();
    int index = 0;

    if (embeddedIdClass != null) {
      ctor = ctor.andThen(FieldAccessor.ofField("pk").setsArgumentAt(index++));
      argsTypes.add(embeddedIdClass);

      for (Column column : table.getColumns()) {
        if (!column.isKey()) {

          ctor = ctor.andThen(FieldAccessor.ofField(column.getName()).setsArgumentAt(index++));
          argsTypes.add(column.getJavaType());
        }
      }
    } else {

      for (Column column : table.getColumns()) {
        ctor = ctor.andThen(FieldAccessor.ofField(column.getName()).setsArgumentAt(index++));
        argsTypes.add(column.getJavaType());
      }
    }

    builder = builder.defineConstructor(Visibility.PUBLIC).withParameters(argsTypes).intercept(ctor);
    return make(builder);
  }

  public Class<?> generateModelClass(String modelClassName, List<Column> columns) throws Exception {
    initialize();
    Asserts.notNull(columns);

    String fullClass = this.classPackage != null ? this.classPackage + "." + modelClassName : modelClassName;

    // Object base
    var builder = new ByteBuddy().subclass(Object.class).name(fullClass);

    for (Column column : columns) {

      // Field annotations
      List<AnnotationDescription> fieldAnnotations = new ArrayList<>();
      column.getAnnotations().forEach(fa -> fieldAnnotations.add(toAnnotationDescription(fa)));

      builder = addField(builder, column.getName(), column.getJavaType(), fieldAnnotations);
    }

    // Constructor
    Composable ctor = MethodCall.invoke(Object.class.getDeclaredConstructor()).onSuper();

    int index = 0;
    for (Column column : columns) {
      ctor = ctor.andThen(FieldAccessor.ofField(column.getName()).setsArgumentAt(index++));
    }

    List<Class<?>> argsTypes = columns.stream().map(f -> f.getJavaType()).collect(Collectors.toList());
    builder = builder.defineConstructor(Visibility.PUBLIC).withParameters(argsTypes).intercept(ctor);

    return make(builder);
  }

  protected <T> Builder<T> addField(Builder<T> builder, String fieldName, Class<?> fieldType, List<AnnotationDescription> fieldAnnotations) {
    // Field
    builder = builder.defineField(fieldName, fieldType, Modifier.PUBLIC).annotateField(fieldAnnotations);

    // Getter
    builder = builder.defineMethod(getGetterName(fieldName, fieldType), fieldType, Visibility.PUBLIC).intercept(FieldAccessor.ofField(fieldName));

    // Setter
    builder = builder.defineMethod(getSetterName(fieldName, fieldType), void.class, Visibility.PUBLIC).withParameter(fieldType)
        .intercept(FieldAccessor.ofField(fieldName));
    return builder;
  }

  protected <T> Class<?> make(Builder<T> builder) throws IOException {
    Unloaded<T> unloaded = builder.make();
    if (this.classPath != null) {
      unloaded.saveIn(this.classPath);
    }

    ClassLoader loader = this.classLoader != null ? this.classLoader : getDefaultClassLoader();
    return unloaded.load(loader).getLoaded();
  }

  static AnnotationDescription toAnnotationDescription(AnnotationModel annotationModel) {
    var builder = AnnotationDescription.Builder.ofType(annotationModel.getAnnotationType());
    for (Map.Entry<String, Object> property : annotationModel.getProperties().entrySet()) {

      String key = Asserts.notNull(property.getKey());
      Object value = Asserts.notNull(property.getValue());
      Class<?> valueType = value.getClass();

      if (valueType == String.class) {
        builder.define(key, (String) value);

      } else if (valueType == Boolean.class) {
        builder.define(key, ((Boolean) value).booleanValue());

      } else if (valueType == Enum.class) {
        builder.define(key, ((Enum<?>) value));

      } else if (valueType == Integer.class) {
        builder.define(key, ((Integer) value).intValue());

      } else if (valueType == Long.class) {
        builder.define(key, ((Long) value).longValue());

      } else if (valueType == Double.class) {
        builder.define(key, ((Double) value).doubleValue());

      } else if (valueType == Class.class) {
        builder.define(key, ((Class<?>) value));

      } else if (valueType == Byte.class) {
        builder.define(key, ((Byte) value).byteValue());

      } else if (valueType == Short.class) {
        builder.define(key, ((Short) value).shortValue());

      } else if (valueType == Float.class) {
        builder.define(key, ((Float) value).floatValue());

      } else if (valueType == Character.class) {
        builder.define(key, ((Character) value).charValue());
      }

      // Array
      if (valueType == String[].class) {
        builder.defineArray(key, (String[]) value);

      } else if (valueType == boolean[].class) {
        builder.defineArray(key, (boolean[]) value);

      } else if (valueType == int[].class) {
        builder.defineArray(key, (int[]) value);

      } else if (valueType == long[].class) {
        builder.defineArray(key, (long[]) value);

      } else if (valueType == double[].class) {
        builder.defineArray(key, (double[]) value);

      } else if (valueType == byte[].class) {
        builder.defineArray(key, (byte[]) value);

      } else if (valueType == short[].class) {
        builder.defineArray(key, (short[]) value);

      } else if (valueType == float[].class) {
        builder.defineArray(key, (float[]) value);

      } else if (valueType == char[].class) {
        builder.defineArray(key, (char[]) value);

      } else {
        throw new IllegalArgumentException(STR.fmt("Annotation property type '{}' is unsupported.", valueType));
      }
    }
    return builder.build();
  }

  static ClassLoader getDefaultClassLoader() {
    ClassLoader cl = null;
    try {
      cl = Thread.currentThread().getContextClassLoader();
    } catch (Exception ex) {
    }
    if (cl == null) {
      cl = ModelGenerator.class.getClassLoader();
      if (cl == null) {
        try {
          cl = ClassLoader.getSystemClassLoader();
        } catch (Exception ex) {
        }
      }
    }
    return cl;
  }

  static String getGetterName(String fieldName, Class<?> type) {
    if (type != boolean.class) {
      return "get" + StringUtils.firstUpperCase(fieldName, Locale.ENGLISH);
    }
    if (fieldName.startsWith("is")) {
      return fieldName;
    }
    return "is" + StringUtils.firstUpperCase(fieldName, Locale.ENGLISH);
  }

  static String getSetterName(String fieldName, Class<?> type) {
    if (type == boolean.class && fieldName.startsWith("is")) {
      return "set" + StringUtils.firstUpperCase(fieldName.substring(2), Locale.ENGLISH);
    }
    return "set" + StringUtils.firstUpperCase(fieldName, Locale.ENGLISH);
  }
}
