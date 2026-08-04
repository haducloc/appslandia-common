// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.factory;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class EventKey implements Serializable {
  private static final long serialVersionUID = 1L;

  final Class<?> type;
  final Annotation[] qualifiers;

  public EventKey(Class<?> type, Annotation[] qualifiers) {
    this.type = Arguments.notNull(type);
    this.qualifiers = Arguments.notNull(qualifiers);
  }

  public Class<?> getType() {
    return type;
  }

  public Annotation[] getQualifiers() {
    return qualifiers;
  }

  @Override
  public int hashCode() {
    var result = Objects.hash(type);
    result = 31 * result + Set.of(qualifiers).hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof EventKey other)) {
      return false;
    }
    return Objects.equals(type, other.type) && Set.of(qualifiers).equals(Set.of(other.qualifiers));
  }

  @Override
  public String toString() {
    return STR.fmt("{}{type: {}, qualifiers: {}}", ObjectUtils.toIdHash(this), type, Set.of(qualifiers));
  }
}
