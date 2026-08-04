// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.factory;

/**
 *
 * @author Loc Ha
 *
 */
public class ObjectException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public ObjectException(String message) {
    super(message);
  }

  public ObjectException(String message, Throwable cause) {
    super(message, cause);
  }

  public ObjectException(Throwable cause) {
    super(cause);
  }
}
