// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

/**
 *
 * @author Loc Ha
 *
 */
public class CryptoException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public CryptoException() {
  }

  public CryptoException(String message) {
    super(message);
  }

  public CryptoException(String message, Throwable cause) {
    super(message, cause);
  }

  public CryptoException(Throwable cause) {
    super(cause);
  }
}
