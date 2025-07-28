// The MIT License (MIT)
// Copyright © 2015 Loc Ha

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

import com.appslandia.common.utils.ElProcessorUtils;

import jakarta.el.ELProcessor;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

/**
 *
 * @author Loc Ha
 *
 */
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { CheckModel.ConstraintValidatorImpl.class })
@Documented
public @interface CheckModel {

  String message() default "{com.appslandia.common.validators.CheckModel.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String expr();

  String reportProperty();

  public static class ConstraintValidatorImpl implements ConstraintValidator<CheckModel, Object> {

    private static final String EL_VARIABLE = "model";

    private CheckModel rule;

    @Override
    public void initialize(CheckModel annotation) {
      this.rule = annotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }

      ELProcessor el = null;

      try {
        el = ElProcessorUtils.obtain();
        el.defineBean(EL_VARIABLE, value);

        var result = el.eval(this.rule.expr());
        if (!(result instanceof Boolean boolResult)) {
          throw new IllegalStateException("Expression must return a boolean: " + this.rule.expr());
        }

        if (!boolResult) {
          context.disableDefaultConstraintViolation();
          context.buildConstraintViolationWithTemplate(this.rule.message()).addPropertyNode(this.rule.reportProperty())
              .addConstraintViolation();
          return false;
        }
        return true;

      } finally {
        if (el != null) {
          el.defineBean(EL_VARIABLE, null);
          ElProcessorUtils.release(el);
        }
      }
    }
  }
}
