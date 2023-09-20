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

package com.appslandia.common.data;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.validators.DigitOnly;
import com.appslandia.common.validators.Email;
import com.appslandia.common.validators.FixedLength;
import com.appslandia.common.validators.MaxLength;
import com.appslandia.common.validators.MinLength;
import com.appslandia.common.validators.MultiValues;
import com.appslandia.common.validators.Password;
import com.appslandia.common.validators.ValidValues;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

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

    public static AnnotationModel notNull() {
	return new AnnotationModel().setAnnotationType(NotNull.class);
    }

    public static AnnotationModel email() {
	return new AnnotationModel().setAnnotationType(Email.class);
    }

    public static AnnotationModel password() {
	return new AnnotationModel().setAnnotationType(Password.class);
    }

    public static AnnotationModel digitOnly(int len) {
	return new AnnotationModel().setAnnotationType(DigitOnly.class).define("value", len);
    }

    public static AnnotationModel future() {
	return new AnnotationModel().setAnnotationType(Future.class);
    }

    public static AnnotationModel futureOrPresent() {
	return new AnnotationModel().setAnnotationType(FutureOrPresent.class);
    }

    public static AnnotationModel past() {
	return new AnnotationModel().setAnnotationType(Past.class);
    }

    public static AnnotationModel pastOrPresent() {
	return new AnnotationModel().setAnnotationType(PastOrPresent.class);
    }

    public static AnnotationModel positive() {
	return new AnnotationModel().setAnnotationType(Positive.class);
    }

    public static AnnotationModel positiveOrZero() {
	return new AnnotationModel().setAnnotationType(PositiveOrZero.class);
    }

    public static AnnotationModel negative() {
	return new AnnotationModel().setAnnotationType(Negative.class);
    }

    public static AnnotationModel negativeOrZero() {
	return new AnnotationModel().setAnnotationType(NegativeOrZero.class);
    }

    public static AnnotationModel fixedLength(int fixedLen) {
	return new AnnotationModel().setAnnotationType(FixedLength.class).define("value", fixedLen);
    }

    public static AnnotationModel maxLength(int maxLen) {
	return new AnnotationModel().setAnnotationType(MaxLength.class).define("value", maxLen);
    }

    public static AnnotationModel minLength(int minLen) {
	return new AnnotationModel().setAnnotationType(MinLength.class).define("value", minLen);
    }

    public static AnnotationModel decimalMax(String maxVal, boolean inclusive) {
	return new AnnotationModel().setAnnotationType(DecimalMax.class).define("value", maxVal).define("inclusive", inclusive);
    }

    public static AnnotationModel decimalMin(String minVal, boolean inclusive) {
	return new AnnotationModel().setAnnotationType(DecimalMin.class).define("value", minVal).define("inclusive", inclusive);
    }

    public static AnnotationModel max(long maxVal) {
	return new AnnotationModel().setAnnotationType(Max.class).define("value", maxVal);
    }

    public static AnnotationModel min(long minVal) {
	return new AnnotationModel().setAnnotationType(Min.class).define("value", minVal);
    }

    public static AnnotationModel digits(int integer, int fraction) {
	return new AnnotationModel().setAnnotationType(Digits.class).define("integer", integer).define("fraction", fraction);
    }

    public static AnnotationModel validValues(int... values) {
	String[] vals = Arrays.stream(values).mapToObj(v -> Integer.toString(v)).toArray(String[]::new);
	return validValues(vals);
    }

    public static AnnotationModel validValues(String... values) {
	return new AnnotationModel().setAnnotationType(ValidValues.class).defineArray("value", values);
    }

    public static AnnotationModel multiValues(int... values) {
	String[] vals = Arrays.stream(values).mapToObj(v -> Integer.toString(v)).toArray(String[]::new);
	return multiValues(vals);
    }

    public static AnnotationModel multiValues(String... values) {
	return new AnnotationModel().setAnnotationType(MultiValues.class).defineArray("value", values);
    }
}
