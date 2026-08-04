// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.beans.IntrospectionException;

/**
 *
 * @author Loc Ha
 *
 */
public class ReflectionException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public ReflectionException(String message) {
    super(message);
  }

  public ReflectionException(ReflectiveOperationException cause) {
    super(cause);
  }

  public ReflectionException(IntrospectionException cause) {
    super(cause);
  }
}
