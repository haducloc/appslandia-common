// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

import com.appslandia.common.utils.SplitUtils;
import com.appslandia.common.utils.SplittingBehavior;

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
@Constraint(validatedBy = { MultiString.ConstraintValidatorImpl.class })
@Documented
public @interface MultiString {

  String message() default "{com.appslandia.common.validators.MultiString.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] value();

  public static class ConstraintValidatorImpl implements ConstraintValidator<MultiString, String> {

    private String[] validValues;

    @Override
    public void initialize(MultiString annotation) {
      validValues = annotation.value();
    }

    @Override
    public boolean isValid(String values, ConstraintValidatorContext context) {
      if (values == null) {
        return true;
      }
      var vals = SplitUtils.splitByComma(values, SplittingBehavior.SKIP_NULL);
      for (String value : vals) {

        if (!Arrays.stream(validValues).anyMatch(vv -> vv.equalsIgnoreCase(value))) {
          return false;
        }
      }
      return true;
    }
  }
}
