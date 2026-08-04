// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Qualifier;

/**
 *
 * @author Loc Ha
 *
 */
public class QualifierUtils {

  public static boolean matchesQualifiers(Annotation[] beanQualifiers, Annotation[] injectQualifiers) {
    // @Inject Bean1
    // @Inject @Default Bean1
    if ((injectQualifiers.length == 0)
        || ((injectQualifiers.length == 1) && Default.Literal.INSTANCE.equals(injectQualifiers[0]))) {

      return (beanQualifiers.length == 0)
          || ((beanQualifiers.length == 1) && Default.Literal.INSTANCE.equals(beanQualifiers[0]));
    }

    // @Inject @Any Bean1
    if ((injectQualifiers.length == 1) && Any.Literal.INSTANCE.equals(injectQualifiers[0])) {
      return true;
    }

    // @Inject @Any @Q1 Bean1
    // @Inject @Q2 @Q1 Bean1
    return Arrays.stream(injectQualifiers).filter(q -> !Any.Literal.INSTANCE.equals(q))
        .allMatch(injectQ -> Arrays.stream(beanQualifiers).anyMatch(beanQ -> beanQ.equals(injectQ)));
  }

  public static Annotation[] parseQualifiers(Annotation[] annotations) {
    List<Annotation> qualifiers = new ArrayList<>(3);
    for (Annotation ann : annotations) {

      if (ann.annotationType().getDeclaredAnnotation(Qualifier.class) != null) {
        qualifiers.add(ann);
      }
    }
    return qualifiers.toArray(new Annotation[qualifiers.size()]);
  }

  public static Annotation[] parseQualifiers(AnnotatedElement element) {
    return parseQualifiers(element.getDeclaredAnnotations());
  }
}
