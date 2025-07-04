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
import com.appslandia.common.utils.Arguments;
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
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation.Composable;
import net.bytebuddy.implementation.MethodCall;

/**
 *
 * @author Loc Ha
 *
 */
public class ModelGenerator extends InitializeObject {

  private String classPackage;
  private ClassLoader classLoader;
  private File classPath;

  private GenerationType idGenType;

  @Override
  protected void init() throws Exception {
    Arguments.isTrue((this.classPath == null) || this.classPath.isDirectory());
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
    var pkClassName = table.getEntityClassName() + "Pk";
    var fullClass = this.classPackage != null ? this.classPackage + "." + pkClassName : pkClassName;

    // Pk annotations
    List<AnnotationDescription> pkAnnotations = new ArrayList<>();

    // @Embeddable
    pkAnnotations.add(AnnotationDescription.Builder.ofType(Embeddable.class).build());
    var keys = table.getColumns().stream().filter(c -> c.isKey()).map(c -> c.getFieldName())
        .toArray(len -> new String[len]);

    // @TableMtdt
    pkAnnotations.add(AnnotationDescription.Builder.ofType(TableMtdt.class)
        .define("catalog", ValueUtils.valueOrAlt(table.getTableCat(), StringUtils.EMPTY_STRING))
        .define("schema", ValueUtils.valueOrAlt(table.getTableSchema(), StringUtils.EMPTY_STRING))
        .define("table", table.getTableName()).define("keyClass", BaseGenPk.class).defineArray("keys", keys).build());

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
          fieldAnnotations.add(
              AnnotationDescription.Builder.ofType(MaxLength.class).define("value", column.getColumnSize()).build());
        }

        builder = addField(builder, column.getFieldName(), column.getJavaType(), fieldAnnotations);
      }
    }

    // Constructor
    Composable ctor = MethodCall.invoke(BaseGenPk.class.getDeclaredConstructor()).onSuper();

    var index = 0;
    for (Column column : table.getColumns()) {
      if (column.isKey()) {
        ctor = ctor.andThen(FieldAccessor.ofField(column.getFieldName()).setsArgumentAt(index++));
      }
    }

    List<Class<?>> argsTypes = table.getColumns().stream().filter(c -> c.isKey()).map(c -> c.getJavaType())
        .collect(Collectors.toList());
    builder = builder.defineConstructor(Visibility.PUBLIC).withParameters(argsTypes).intercept(ctor);

    return make(builder);
  }

  public Class<?> generateEntityClass(Table table) throws Exception {
    initialize();
    Arguments.notNull(table);

    Class<?> embeddedIdClass = null;

    if (table.getSingleKey() == null) {
      embeddedIdClass = generateEntityPk(table);
    }

    var fullClass = this.classPackage != null ? this.classPackage + "." + table.getEntityClassName()
        : table.getEntityClassName();
    var keys = table.getColumns().stream().filter(c -> c.isKey()).map(c -> c.getFieldName())
        .toArray(len -> new String[len]);

    // Class annotations
    List<AnnotationDescription> classAnnotations = new ArrayList<>();
    table.getAnnotations().forEach(fa -> classAnnotations.add(toAnnotationDescription(fa)));

    // @TableMtdt
    classAnnotations.add(AnnotationDescription.Builder.ofType(TableMtdt.class)
        .define("catalog", ValueUtils.valueOrAlt(table.getTableCat(), StringUtils.EMPTY_STRING))
        .define("schema", ValueUtils.valueOrAlt(table.getTableSchema(), StringUtils.EMPTY_STRING))
        .define("table", table.getTableName())
        .define("keyClass", (embeddedIdClass != null) ? embeddedIdClass : table.getSingleKey().getJavaType())
        .defineArray("keys", keys).build());

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
          .intercept(FieldAccessor.ofField(table.getSingleKey().getFieldName()));
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
              fieldAnnotations.add(AnnotationDescription.Builder.ofType(GeneratedValue.class)
                  .define("strategy", this.idGenType).build());
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
          fieldAnnotations.add(
              AnnotationDescription.Builder.ofType(MaxLength.class).define("value", column.getColumnSize()).build());
        }
      }

      if ((column.isKey() && embeddedIdClass == null) || !column.isKey()) {

        builder = addField(builder, column.getFieldName(), column.getJavaType(), fieldAnnotations);
      }
    }

    // Constructor
    Composable ctor = MethodCall.invoke(EntityBase.class.getDeclaredConstructor()).onSuper();

    List<Class<?>> argsTypes = new ArrayList<>();
    var index = 0;

    if (embeddedIdClass != null) {
      ctor = ctor.andThen(FieldAccessor.ofField("pk").setsArgumentAt(index++));
      argsTypes.add(embeddedIdClass);

      for (Column column : table.getColumns()) {
        if (!column.isKey()) {

          ctor = ctor.andThen(FieldAccessor.ofField(column.getFieldName()).setsArgumentAt(index++));
          argsTypes.add(column.getJavaType());
        }
      }
    } else {

      for (Column column : table.getColumns()) {
        ctor = ctor.andThen(FieldAccessor.ofField(column.getFieldName()).setsArgumentAt(index++));
        argsTypes.add(column.getJavaType());
      }
    }

    builder = builder.defineConstructor(Visibility.PUBLIC).withParameters(argsTypes).intercept(ctor);
    return make(builder);
  }

  public Class<?> generateModelClass(String modelClassName, List<Column> columns) throws Exception {
    initialize();
    Arguments.notNull(columns);

    var fullClass = this.classPackage != null ? this.classPackage + "." + modelClassName : modelClassName;

    // Object base
    var builder = new ByteBuddy().subclass(Object.class).name(fullClass);

    for (Column column : columns) {

      // Field annotations
      List<AnnotationDescription> fieldAnnotations = new ArrayList<>();
      column.getAnnotations().forEach(fa -> fieldAnnotations.add(toAnnotationDescription(fa)));

      builder = addField(builder, column.getFieldName(), column.getJavaType(), fieldAnnotations);
    }

    // Constructor
    Composable ctor = MethodCall.invoke(Object.class.getDeclaredConstructor()).onSuper();

    var index = 0;
    for (Column column : columns) {
      ctor = ctor.andThen(FieldAccessor.ofField(column.getFieldName()).setsArgumentAt(index++));
    }

    List<Class<?>> argsTypes = columns.stream().map(c -> c.getJavaType()).collect(Collectors.toList());
    builder = builder.defineConstructor(Visibility.PUBLIC).withParameters(argsTypes).intercept(ctor);

    return make(builder);
  }

  protected <T> Builder<T> addField(Builder<T> builder, String fieldName, Class<?> fieldType,
      List<AnnotationDescription> fieldAnnotations) {
    // Field
    builder = builder.defineField(fieldName, fieldType, Modifier.PUBLIC).annotateField(fieldAnnotations);

    // Getter
    builder = builder.defineMethod(getGetterName(fieldName, fieldType), fieldType, Visibility.PUBLIC)
        .intercept(FieldAccessor.ofField(fieldName));

    // Setter
    builder = builder.defineMethod(getSetterName(fieldName, fieldType), void.class, Visibility.PUBLIC)
        .withParameter(fieldType).intercept(FieldAccessor.ofField(fieldName));
    return builder;
  }

  protected <T> Class<?> make(Builder<T> builder) throws IOException {
    var unloaded = builder.make();
    if (this.classPath != null) {
      unloaded.saveIn(this.classPath);
    }

    var loader = this.classLoader != null ? this.classLoader : getDefaultClassLoader();
    return unloaded.load(loader).getLoaded();
  }

  static AnnotationDescription toAnnotationDescription(AnnotationModel annotationModel) {
    var builder = AnnotationDescription.Builder.ofType(annotationModel.getAnnotationType());

    for (Map.Entry<String, Object> property : annotationModel.getProperties().entrySet()) {
      var key = Asserts.notNull(property.getKey());
      var value = Asserts.notNull(property.getValue());

      // Scalars
      if (value instanceof String str) {
        builder.define(key, str);

      } else if (value instanceof Boolean b) {
        builder.define(key, b);

      } else if (value instanceof Enum<?> e) {
        builder.define(key, e);

      } else if (value instanceof Integer i) {
        builder.define(key, i);

      } else if (value instanceof Long l) {
        builder.define(key, l);

      } else if (value instanceof Double d) {
        builder.define(key, d);

      } else if (value instanceof Class<?> clazz) {
        builder.define(key, clazz);

      } else if (value instanceof Byte byt) {
        builder.define(key, byt);

      } else if (value instanceof Short s) {
        builder.define(key, s);

      } else if (value instanceof Float f) {
        builder.define(key, f);

      } else if (value instanceof Character c) {
        builder.define(key, c);

        // Arrays
      } else if (value instanceof String[] arr) {
        builder.defineArray(key, arr);

      } else if (value instanceof boolean[] arr) {
        builder.defineArray(key, arr);

      } else if (value instanceof int[] arr) {
        builder.defineArray(key, arr);

      } else if (value instanceof long[] arr) {
        builder.defineArray(key, arr);

      } else if (value instanceof double[] arr) {
        builder.defineArray(key, arr);

      } else if (value instanceof byte[] arr) {
        builder.defineArray(key, arr);

      } else if (value instanceof short[] arr) {
        builder.defineArray(key, arr);

      } else if (value instanceof float[] arr) {
        builder.defineArray(key, arr);

      } else if (value instanceof char[] arr) {
        builder.defineArray(key, arr);

      } else {
        throw new IllegalArgumentException(STR.fmt("Annotation property type '{}' is unsupported.", value.getClass()));
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
