// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

/**
 *
 * @author Loc Ha
 *
 */
public class DestroyingException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public DestroyingException() {
  }

  public DestroyingException(String message) {
    super(message);
  }

  public DestroyingException(String message, Throwable cause) {
    super(message, cause);
  }

  public DestroyingException(Throwable cause) {
    super(cause);
  }
}
