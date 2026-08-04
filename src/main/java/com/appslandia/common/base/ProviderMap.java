// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.HashMap;
import java.util.function.Function;

/**
 *
 *
 * @author Loc Ha
 *
 */
public class ProviderMap<K, V> extends HashMap<K, V> {
  private static final long serialVersionUID = 1L;

  final Function<K, V> factory;

  public ProviderMap(Function<K, V> factory) {
    super();
    this.factory = factory;
  }

  @SuppressWarnings("unchecked")
  @Override
  public V get(Object key) {
    var v = super.get(key);
    if (v != null) {
      return v;
    }
    v = factory.apply((K) key);
    super.put((K) key, v);
    return v;
  }
}
