// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

/**
 *
 * @author Loc Ha
 *
 */
public class JoseVerificationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public JoseVerificationException(String message) {
    super(message);
  }

  public JoseVerificationException(Throwable cause) {
    super(cause);
  }

  public JoseVerificationException(String message, Throwable cause) {
    super(message, cause);
  }
}
