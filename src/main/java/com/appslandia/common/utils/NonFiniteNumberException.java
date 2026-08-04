// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

/**
 *
 * @author Loc Ha
 *
 */
public class NonFiniteNumberException extends IllegalArgumentException {
  private static final long serialVersionUID = 1L;

  public NonFiniteNumberException(String message) {
    super(message);
  }
}
