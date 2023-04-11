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

package com.appslandia.common.record;

import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.models.EntityBase;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ReflectionException;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.ValueUtils;
import com.appslandia.common.validators.MaxLength;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation.Composable;
import net.bytebuddy.implementation.MethodCall;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PojoGenerator extends InitializeObject {

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

    public PojoGenerator setClassLoader(ClassLoader classLoader) {
	assertNotInitialized();
	this.classLoader = classLoader;
	return this;
    }

    public PojoGenerator setClassPackage(String classPackage) {
	assertNotInitialized();
	this.classPackage = classPackage;
	return this;
    }

    public PojoGenerator setClassPackage(Class<?> clazz) {
	return setClassPackage(clazz.getPackageName());
    }

    public PojoGenerator setClassPath(File classPath) {
	assertNotInitialized();
	this.classPath = classPath;
	return this;
    }

    public PojoGenerator setIdGenType(GenerationType idGenType) {
	assertNotInitialized();
	this.idGenType = idGenType;
	return this;
    }

    public Class<?> generateEntityClass(Table table) throws Exception {
	initialize();

	Asserts.notNull(table);
	Asserts.notNull(table.getSingleKey(), "Only single key supported.");

	String fullClass = this.classPackage != null ? this.classPackage + "." + table.getEntityClassName() : table.getEntityClassName();

	// Class annotations
	List<AnnotationDescription> classAnnotations = new ArrayList<>();
	table.getAnnotations().forEach(fa -> classAnnotations.add(toAnnotationDescription(fa)));

	// @TableMtdt
	classAnnotations.add(AnnotationDescription.Builder.ofType(TableMtdt.class).define("catalog", ValueUtils.valueOrAlt(table.getTableCat(), StringUtils.EMPTY_STRING))
		.define("schema", ValueUtils.valueOrAlt(table.getTableSchema(), StringUtils.EMPTY_STRING)).define("table", table.getTableName())
		.define("key", table.getSingleKey().getName()).build());

	// @Entity
	classAnnotations.add(AnnotationDescription.Builder.ofType(Entity.class).build());

	// EntityBase base
	var builder = new ByteBuddy().subclass(EntityBase.class).name(fullClass).annotateType(classAnnotations);

	for (Field field : table.getFields()) {

	    // Field annotations
	    List<AnnotationDescription> fieldAnnotations = new ArrayList<>();
	    field.getAnnotations().forEach(fa -> fieldAnnotations.add(toAnnotationDescription(fa)));

	    if (field.isKey()) {
		// @Id
		fieldAnnotations.add(AnnotationDescription.Builder.ofType(Id.class).build());

		// @GeneratedValue
		if (field.isKeyIncr()) {
		    if (this.idGenType == null) {
			fieldAnnotations.add(AnnotationDescription.Builder.ofType(GeneratedValue.class).build());
		    } else {
			fieldAnnotations.add(AnnotationDescription.Builder.ofType(GeneratedValue.class).define("strategy", this.idGenType).build());
		    }
		}
	    } else {
		// @NotNull
		if (!field.isNullable()) {
		    fieldAnnotations.add(AnnotationDescription.Builder.ofType(NotNull.class).build());
		}
		// @MaxLength
		if (field.getJavaType() == String.class && field.getScaleOrLength() != null) {
		    fieldAnnotations.add(AnnotationDescription.Builder.ofType(MaxLength.class).define("value", field.getScaleOrLength()).build());
		}
	    }

	    // Field
	    builder = builder.defineField(field.getName(), field.getJavaType(), Modifier.PUBLIC).annotateField(fieldAnnotations);

	    // Getter
	    builder = builder.defineMethod(getGetterName(field.getName(), field.getJavaType()), field.getJavaType(), Visibility.PUBLIC)
		    .intercept(FieldAccessor.ofField(field.getName()));

	    // Setter
	    builder = builder.defineMethod(getSetterName(field.getName(), field.getJavaType()), void.class, Visibility.PUBLIC).withParameter(field.getJavaType())
		    .intercept(FieldAccessor.ofField(field.getName()));
	}

	// pk
	builder = builder.defineMethod("getPk", table.getSingleKey().getJavaType(), Visibility.PUBLIC).intercept(FieldAccessor.ofField(table.getSingleKey().getName()));

	// Constructor
	Composable ctor = MethodCall.invoke(EntityBase.class.getDeclaredConstructor()).onSuper();

	int index = 0;
	for (Field field : table.getFields()) {
	    ctor = ctor.andThen(FieldAccessor.ofField(field.getName()).setsArgumentAt(index++));
	}

	List<Class<?>> fieldTypes = table.getFields().stream().map(f -> f.getJavaType()).collect(Collectors.toList());
	builder = builder.defineConstructor(Visibility.PUBLIC).withParameters(fieldTypes).intercept(ctor);

	Unloaded<EntityBase> unloaded = builder.make();
	if (this.classPath != null) {
	    unloaded.saveIn(this.classPath);
	}

	ClassLoader loader = this.classLoader != null ? this.classLoader : getDefaultClassLoader();
	return unloaded.load(loader).getLoaded();
    }

    public Class<?> generateRecordClass(String recordClassName, List<Field> fields) throws Exception {
	initialize();

	Asserts.notNull(fields);

	String fullClass = this.classPackage != null ? this.classPackage + "." + recordClassName : recordClassName;

	// Object base
	var builder = new ByteBuddy().subclass(Object.class).name(fullClass);

	for (Field field : fields) {

	    // Field
	    builder = builder.defineField(field.getName(), field.getJavaType(), Modifier.PUBLIC);

	    // Getter
	    builder = builder.defineMethod(getGetterName(field.getName(), field.getJavaType()), field.getJavaType(), Visibility.PUBLIC)
		    .intercept(FieldAccessor.ofField(field.getName()));

	    // Setter
	    builder = builder.defineMethod(getSetterName(field.getName(), field.getJavaType()), void.class, Visibility.PUBLIC).withParameter(field.getJavaType())
		    .intercept(FieldAccessor.ofField(field.getName()));
	}

	// Constructor
	Composable ctor = MethodCall.invoke(Object.class.getDeclaredConstructor()).onSuper();

	int index = 0;
	for (Field field : fields) {
	    ctor = ctor.andThen(FieldAccessor.ofField(field.getName()).setsArgumentAt(index++));
	}

	List<Class<?>> fieldTypes = fields.stream().map(f -> f.getJavaType()).collect(Collectors.toList());
	builder = builder.defineConstructor(Visibility.PUBLIC).withParameters(fieldTypes).intercept(ctor);

	Unloaded<Object> unloaded = builder.make();
	if (this.classPath != null) {
	    unloaded.saveIn(this.classPath);
	}

	ClassLoader loader = this.classLoader != null ? this.classLoader : getDefaultClassLoader();
	return unloaded.load(loader).getLoaded();
    }

    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TableMtdt {

	String catalog();

	String schema();

	String table();

	String key();
    }

    public static class Metadata {

	final TableMtdt tableMtdt;
	final Constructor<?> emptyConstructor;
	final Constructor<?> paramConstructor;
	final java.lang.reflect.Field pkField;

	public Metadata(TableMtdt tableMtdt, Constructor<?> emptyConstructor, Constructor<?> paramConstructor, java.lang.reflect.Field pkField) {
	    this.tableMtdt = tableMtdt;
	    this.emptyConstructor = emptyConstructor;
	    this.paramConstructor = paramConstructor;
	    this.pkField = pkField;
	}

	public TableMtdt getTableMtdt() {
	    return this.tableMtdt;
	}

	public Constructor<?> getEmptyConstructor() {
	    return this.emptyConstructor;
	}

	public Constructor<?> getParamConstructor() {
	    return this.paramConstructor;
	}

	public java.lang.reflect.Field getPkField() {
	    return this.pkField;
	}
    }

    static final ConcurrentMap<Class<?>, Metadata> METADATAS = new ConcurrentHashMap<>();

    public static Metadata getMetadata(Class<?> genClass) {
	return METADATAS.computeIfAbsent(genClass, clazz -> {

	    // TableMtdt
	    TableMtdt tableMtdt = clazz.getDeclaredAnnotation(TableMtdt.class);
	    Asserts.notNull(tableMtdt);

	    try {
		java.lang.reflect.Field pkField = clazz.getDeclaredField(tableMtdt.key());

		Constructor<?>[] ctors = clazz.getDeclaredConstructors();
		Asserts.isTrue(ctors.length == 2);

		Constructor<?> emptyConstructor = Arrays.stream(ctors).filter(c -> c.getParameterCount() == 0).findFirst().get();
		Constructor<?> paramConstructor = Arrays.stream(ctors).filter(c -> c.getParameterCount() != 0).findFirst().get();

		return new Metadata(tableMtdt, emptyConstructor, paramConstructor, pkField);

	    } catch (NoSuchFieldException ex) {
		throw new Error(ex);
	    }
	});
    }

    public static Object getPk(Object obj) {
	try {
	    return getMetadata(obj.getClass()).pkField.get(obj);

	} catch (IllegalAccessException ex) {
	    throw new ReflectionException(ex);
	}
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
	    cl = PojoGenerator.class.getClassLoader();
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
