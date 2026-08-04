// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

/**
 *
 * @author Loc Ha
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
    ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { IntRange.ConstraintValidatorImpl.class })
@Documented
public @interface IntRange {

  String message() default "{com.appslandia.common.validators.IntRange.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  int min();

  int max();

  public static class ConstraintValidatorImpl implements ConstraintValidator<IntRange, Integer> {

    private int min;
    private int max;

    @Override
    public void initialize(IntRange annotation) {
      min = annotation.min();
      max = annotation.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      if (value < min || value > max) {
        return false;
      }
      return true;
    }
  }
}
