// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class Out<T> {

  public T value;

  public Out() {
  }

  public Out(T value) {
    this.value = value;
  }

  public T get() {
    if (value == null) {
      throw new IllegalStateException("The value is required.");
    }
    return value;
  }

  public T orElse(T defaultValue) {
    return (value != null) ? value : defaultValue;
  }

  public Out<T> set(T value) {
    this.value = value;
    return this;
  }

  @Override
  public String toString() {
    var ts = ObjectUtils.toIdHash(this);
    return ts + "(value=" + value + ")";
  }
}
