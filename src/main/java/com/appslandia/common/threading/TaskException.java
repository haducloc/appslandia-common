// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.threading;

/**
 *
 * @author Loc Ha
 *
 */
public class TaskException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public TaskException() {
  }

  public TaskException(String message) {
    super(message);
  }

  public TaskException(String message, Throwable cause) {
    super(message, cause);
  }

  public TaskException(Throwable cause) {
    super(cause);
  }
}
