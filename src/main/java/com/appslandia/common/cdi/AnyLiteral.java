// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.cdi;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.util.AnnotationLiteral;

/**
 *
 * @author Loc Ha
 *
 */
@SuppressWarnings("all")
public class AnyLiteral extends AnnotationLiteral<Any> implements Any {
  private static final long serialVersionUID = 1L;

  public static final Any IMPL = new AnyLiteral();

  private AnyLiteral() {
  }
}
