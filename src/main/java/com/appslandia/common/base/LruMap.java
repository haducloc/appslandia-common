// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Loc Ha
 *
 */
public class LruMap<K, V> extends LinkedHashMap<K, V> {
  private static final long serialVersionUID = 1L;

  final int size;

  public LruMap(int size) {
    this.size = size;
  }

  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > size;
  }
}
