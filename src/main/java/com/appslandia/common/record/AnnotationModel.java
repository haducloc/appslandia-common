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

package com.appslandia.common.record;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.CollectionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class AnnotationModel extends InitializeObject {
    private Class<? extends Annotation> annotationType;
    private Map<String, Object> properties;

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.annotationType);

	this.properties = CollectionUtils.unmodifiable(this.properties);
    }

    public Class<? extends Annotation> getAnnotationType() {
	initialize();
	return this.annotationType;
    }

    public AnnotationModel setAnnotationType(Class<? extends Annotation> annotationType) {
	assertNotInitialized();
	this.annotationType = annotationType;
	return this;
    }

    public Map<String, Object> getProperties() {
	initialize();
	return this.properties;
    }

    protected void addProperty(String property, Object value) {
	Asserts.notNull(property);
	Asserts.notNull(value);

	if (this.properties == null) {
	    this.properties = new LinkedHashMap<>();
	}
	this.properties.put(property, value);
    }

    public AnnotationModel define(String property, String value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel define(String property, Enum<?> value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel define(String property, Class<?> value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel define(String property, boolean value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel define(String property, char value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel define(String property, byte value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel define(String property, short value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel define(String property, int value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel define(String property, long value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel define(String property, float value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel define(String property, double value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel defineArray(String property, String... value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel defineArray(String property, boolean... value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel defineArray(String property, char... value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel defineArray(String property, byte... value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel defineArray(String property, short... value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel defineArray(String property, int... value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel defineArray(String property, long... value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel defineArray(String property, float... value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }

    public AnnotationModel defineArray(String property, double... value) {
	assertNotInitialized();
	addProperty(property, value);
	return this;
    }
}
