// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.appslandia.common.crypto.PasswordUtil;

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
@Constraint(validatedBy = { Password.ConstraintValidatorImpl.class })
@Documented
public @interface Password {

  String message() default "{com.appslandia.common.validators.Password.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  public static class ConstraintValidatorImpl implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return PasswordUtil.isValid(value.toString());
    }
  }
}
