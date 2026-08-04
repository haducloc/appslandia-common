// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

/**
 *
 * @author Loc Ha
 *
 */
public class InitializingException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InitializingException() {
  }

  public InitializingException(String message) {
    super(message);
  }

  public InitializingException(String message, Throwable cause) {
    super(message, cause);
  }

  public InitializingException(Throwable cause) {
    super(cause);
  }
}
