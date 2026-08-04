// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

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
@Constraint(validatedBy = { CardCVC.ConstraintValidatorImpl.class })
@Documented
public @interface CardCVC {

  String message() default "{com.appslandia.common.validators.CardCVC.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  static final Pattern CVC_PATTERN = Pattern.compile("^\\d{3,4}$");

  public static class ConstraintValidatorImpl implements ConstraintValidator<CardCVC, String> {

    @Override
    public void initialize(CardCVC annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return CVC_PATTERN.matcher(value).matches();
    }
  }
}
