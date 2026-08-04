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
@Constraint(validatedBy = { MinLength.ConstraintValidatorImpl.class })
@Documented
public @interface MinLength {

  String message() default "{com.appslandia.common.validators.MinLength.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  int value();

  public static class ConstraintValidatorImpl implements ConstraintValidator<MinLength, String> {

    private int minLength;

    @Override
    public void initialize(MinLength annotation) {
      minLength = annotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      if (value.length() < minLength) {
        return false;
      }
      return true;
    }
  }
}
