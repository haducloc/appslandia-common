// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.validators;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 *
 * @author Loc Ha
 *
 */
public class ValidatorUtils {

  private static class ValidatorFactoryHolder {
    static final ValidatorFactory impl;
    static {
      var obj = Validation.buildDefaultValidatorFactory();

      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          obj.close();
        }
      });
      impl = obj;
    }
  }

  public static Validator getValidator() {
    return ValidatorFactoryHolder.impl.getValidator();
  }
}
