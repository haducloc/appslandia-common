// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class UncheckedException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public UncheckedException(Throwable throwable) {
    super(throwable);
    Arguments.isTrue(!(throwable instanceof RuntimeException),
        "The throwable must be java.lang.Error or check exception.");
  }
}
