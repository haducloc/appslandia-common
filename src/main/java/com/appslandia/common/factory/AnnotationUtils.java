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
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.appslandia.common.utils.ReflectionUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Qualifier;

/**
 *
 * @author Loc Ha
 *
 */
public class AnnotationUtils {

  public static boolean matches(Annotation[] srcQualifiers, Annotation[] qualifiers) {
    // qualifiers: @Default
    if ((qualifiers.length == 0) || ((qualifiers.length == 1) && Default.Literal.INSTANCE.equals(qualifiers[0]))) {
      return (srcQualifiers.length == 0)
          || ((srcQualifiers.length == 1) && Default.Literal.INSTANCE.equals(srcQualifiers[0]));
    }

    // qualifiers: @Any @Q1*
    if (Arrays.stream(qualifiers).anyMatch(q -> Any.Literal.INSTANCE.equals(q))) {
      return Arrays.stream(qualifiers).allMatch(
          q -> Any.Literal.INSTANCE.equals(q) || Arrays.stream(srcQualifiers).anyMatch(srcQ -> srcQ.equals(q)));
    }

    // qualifiers: @Q1 @Q2
    return Arrays.stream(qualifiers).allMatch(q -> Arrays.stream(srcQualifiers).anyMatch(srcQ -> srcQ.equals(q)));
  }

  public static Annotation[] parseQualifiers(Annotation[] annotations) {
    List<Annotation> qualifiers = new ArrayList<>(3);
    for (Annotation ann : annotations) {
      if (ann.annotationType().getDeclaredAnnotation(Qualifier.class) != null) {
        qualifiers.add(ann);
      }
    }
    return !qualifiers.isEmpty() ? qualifiers.toArray(new Annotation[qualifiers.size()])
        : ReflectionUtils.EMPTY_ANNOTATIONS;
  }

  public static Annotation[] parseQualifiers(AnnotatedElement element) {
    return parseQualifiers(element.getDeclaredAnnotations());
  }

  public static Annotation[] parseQualifiers(ObjectProducer<?> producer) {
    var m = ReflectionUtils.getDeclaredMethod(producer.getClass(), "produce", ObjectFactory.class);
    return parseQualifiers(m);
  }

  public static ObjectScope parseScope(ObjectProducer<?> producer) {
    var m = ReflectionUtils.getDeclaredMethod(producer.getClass(), "produce", ObjectFactory.class);
    return parseScope(m);
  }

  public static ObjectScope parseScope(AnnotatedElement element) {
    if (element.getDeclaredAnnotation(ApplicationScoped.class) != null) {
      return ObjectScope.SINGLETON;
    }
    if (element.getDeclaredAnnotation(SessionScoped.class) != null
        || element.getDeclaredAnnotation(RequestScoped.class) != null
        || element.getDeclaredAnnotation(Dependent.class) != null) {
      throw new IllegalArgumentException("SessionScoped/RequestScoped/Dependent is unsupported.");
    }
    return ObjectScope.SINGLETON;
  }
}
