// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.appslandia.common.utils.UserProfileUtils;
import com.appslandia.common.validators.PhoneNumber.ConstraintValidatorImpl;

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
@Constraint(validatedBy = { ConstraintValidatorImpl.class })
@Documented
public @interface PhoneNumber {

  String message() default "{com.appslandia.common.validators.PhoneNumber.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  public static class ConstraintValidatorImpl implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public void initialize(PhoneNumber annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return UserProfileUtils.isValidPhoneNumber(value);
    }
  }
}
