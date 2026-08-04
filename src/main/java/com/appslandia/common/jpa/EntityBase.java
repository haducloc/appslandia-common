// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jpa;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class EntityBase implements Serializable {
  private static final long serialVersionUID = 1L;

  public abstract Serializable getPk();

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof EntityBase that)) {
      return false;
    }
    return Objects.equals(getPk(), that.getPk());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getPk());
  }
}
