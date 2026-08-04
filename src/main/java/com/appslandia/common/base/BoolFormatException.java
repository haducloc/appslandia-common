// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

/**
 *
 * @author Loc Ha
 *
 */
public class BoolFormatException extends IllegalArgumentException {
  private static final long serialVersionUID = 1L;

  public BoolFormatException(String value) {
    super("For input string: \"" + value + "\"");
  }
}
