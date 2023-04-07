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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.models.EntityBase;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ReflectionException;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.ValueUtils;

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
public class EntityGenerator extends InitializeObject {

    private String classPackage;
    private ClassLoader classLoader;
    private File classPath;

    @Override
    protected void init() throws Exception {
	if (this.classPath != null) {
	    Asserts.isTrue(this.classPath.isDirectory());
	}
    }

    public EntityGenerator setClassLoader(ClassLoader classLoader) {
	assertNotInitialized();
	this.classLoader = classLoader;
	return this;
    }

    public EntityGenerator setClassPackage(String classPackage) {
	assertNotInitialized();
	this.classPackage = classPackage;
	return this;
    }

    public EntityGenerator setClassPackage(Class<?> clazz) {
	return setClassPackage(clazz.getPackageName());
    }

    public EntityGenerator setClassPath(File classPath) {
	assertNotInitialized();
	this.classPath = classPath;
	return this;
    }

    public Class<?> generateRecordClass(Table table) throws Exception {
	initialize();

	Asserts.notNull(table);
	Asserts.notNull(table.getSingleKey(), "Only single key supported.");

	String fullClass = this.classPackage != null ? this.classPackage + "." + table.getRecordClassName() : table.getRecordClassName();

	// EntityBase base
	var builder = new ByteBuddy().subclass(EntityBase.class).name(fullClass)
		.annotateType(AnnotationDescription.Builder.ofType(TableMtdt.class).define("catalog", ValueUtils.valueOrAlt(table.getTableCat(), StringUtils.EMPTY_STRING))
			.define("schema", ValueUtils.valueOrAlt(table.getTableSchema(), StringUtils.EMPTY_STRING)).define("table", table.getTableName())
			.define("key", table.getSingleKey().getName()).build());

	for (Field field : table.getFields()) {

	    // Field
	    builder = builder.defineField(field.getName(), field.getJavaType(), Modifier.PUBLIC);

	    // Getter
	    builder = builder.defineMethod(getGetterName(field.getName(), field.getJavaType()), field.getJavaType(), Visibility.PUBLIC).intercept(FieldAccessor.ofBeanProperty());

	    // Setter
	    builder = builder.defineMethod(getSetterName(field.getName(), field.getJavaType()), void.class, Visibility.PUBLIC).withParameter(field.getJavaType())
		    .intercept(FieldAccessor.ofBeanProperty());
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

    static ClassLoader getDefaultClassLoader() {
	ClassLoader cl = null;
	try {
	    cl = Thread.currentThread().getContextClassLoader();
	} catch (Exception ex) {
	}
	if (cl == null) {
	    cl = EntityGenerator.class.getClassLoader();
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
