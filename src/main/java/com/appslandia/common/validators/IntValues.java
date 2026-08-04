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
@Constraint(validatedBy = { IntValues.ConstraintValidatorImpl.class })
@Documented
public @interface IntValues {

  String message() default "{com.appslandia.common.validators.IntValues.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  int[] value();

  public static class ConstraintValidatorImpl implements ConstraintValidator<IntValues, Integer> {

    private int[] validValues;

    @Override
    public void initialize(IntValues annotation) {
      this.validValues = annotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      for (var validValue : validValues) {
        if (validValue == value) {
          return true;
        }
      }
      return false;
    }
  }
}
