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
@Constraint(validatedBy = { MaxLength.ConstraintValidatorImpl.class })
@Documented
public @interface MaxLength {

  String message() default "{com.appslandia.common.validators.MaxLength.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  int value();

  public static class ConstraintValidatorImpl implements ConstraintValidator<MaxLength, String> {

    private int maxLength;

    @Override
    public void initialize(MaxLength annotation) {
      maxLength = annotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      if (value.length() > maxLength) {
        return false;
      }
      return true;
    }
  }
}
