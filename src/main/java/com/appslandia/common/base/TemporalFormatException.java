// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.text.ParseException;

/**
 *
 * @author Loc Ha
 *
 */
public class TemporalFormatException extends IllegalArgumentException {
  private static final long serialVersionUID = 1L;

  public TemporalFormatException(String message) {
    super(message);
  }

  public TemporalFormatException(ParseException cause) {
    super(cause.getMessage(), cause);
  }
}
