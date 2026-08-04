// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.appslandia.common.utils.EmailUtils;

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
@Constraint(validatedBy = { Emails.ConstraintValidatorImpl.class })
@Documented
public @interface Emails {

  String message() default "{com.appslandia.common.validators.Emails.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  public static class ConstraintValidatorImpl implements ConstraintValidator<Emails, String> {

    @Override
    public void initialize(Emails annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return EmailUtils.isValidEmails(value);
    }
  }
}
