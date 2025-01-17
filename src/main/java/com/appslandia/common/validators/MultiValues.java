// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.SplitUtils;
import com.appslandia.common.utils.SplittingBehavior;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
    ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { MultiValues.ConstraintValidatorImpl.class })
@Documented
public @interface MultiValues {

  String message() default "{com.appslandia.common.validators.MultiValues.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] value() default {};

  int[] ints() default {};

  Class<?> type() default String.class;

  public static class ConstraintValidatorImpl implements ConstraintValidator<MultiValues, String> {

    private String[] validValues;

    @Override
    public void initialize(MultiValues annotation) {
      if ((annotation.type() != String.class) && (annotation.type() != int.class)) {
        throw new IllegalStateException(
            STR.fmt("The given {} is invalid. type must be String.class|int.class", annotation));
      }

      String[] values = annotation.value();
      if (values.length == 0) {
        values = Arrays.stream(annotation.ints()).mapToObj(v -> Integer.toString(v)).toArray(String[]::new);
      }
      if (values.length == 0) {
        throw new IllegalStateException(STR.fmt("The given {} is invalid. value or ints is required.", annotation));
      }
      this.validValues = values;
    }

    @Override
    public boolean isValid(String values, ConstraintValidatorContext context) {
      if (values == null) {
        return true;
      }
      String[] vals = SplitUtils.splitByComma(values, SplittingBehavior.SKIP_NULL);
      for (String value : vals) {

        if (!Arrays.stream(this.validValues).anyMatch(v -> v.equalsIgnoreCase(value))) {
          return false;
        }
      }
      return true;
    }
  }

}
