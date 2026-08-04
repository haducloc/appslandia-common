// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public JsonException() {
  }

  public JsonException(String message) {
    super(message);
  }

  public JsonException(String message, Throwable cause) {
    super(message, cause);
  }

  public JsonException(Throwable cause) {
    super(cause);
  }
}
