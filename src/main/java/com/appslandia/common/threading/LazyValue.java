// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.threading;

import java.util.function.Supplier;

import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class LazyValue<T> {

  private volatile T value;
  final Object mutex = new Object();

  public T get(Supplier<T> factory) {
    var obj = value;
    if (obj == null) {
      synchronized (mutex) {
        if ((obj = value) == null) {
          value = obj = factory.get();
        }
      }
    }
    return obj;
  }

  @Override
  public String toString() {
    var ts = ObjectUtils.toIdHash(this);
    return ts + "(value=" + value + ")";
  }
}
