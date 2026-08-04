// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.cdi;

import jakarta.enterprise.inject.Default;
import jakarta.enterprise.util.AnnotationLiteral;

/**
 *
 * @author Loc Ha
 *
 */
@SuppressWarnings("all")
public class DefaultLiteral extends AnnotationLiteral<Default> implements Default {
  private static final long serialVersionUID = 1L;

  public static final Default IMPL = new DefaultLiteral();

  private DefaultLiteral() {
  }
}
