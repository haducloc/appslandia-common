// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonValueException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public JsonValueException() {
  }

  public JsonValueException(String message) {
    super(message);
  }

  public JsonValueException(String message, Throwable cause) {
    super(message, cause);
  }

  public JsonValueException(Throwable cause) {
    super(cause);
  }
}
