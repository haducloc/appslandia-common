// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

/**
 *
 * @author Loc Ha
 *
 */
public class JwsSignatureException extends JoseVerificationException {
  private static final long serialVersionUID = 1L;

  public JwsSignatureException(String message) {
    super(message);
  }

  public JwsSignatureException(Throwable cause) {
    super(cause);
  }

  public JwsSignatureException(String message, Throwable cause) {
    super(message, cause);
  }
}
