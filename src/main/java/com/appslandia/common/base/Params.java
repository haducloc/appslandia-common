// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.LinkedHashMap;

/**
 *
 * @author Loc Ha
 *
 */
public class Params extends MapWrapper<String, Object> {
  private static final long serialVersionUID = 1L;

  public Params() {
    super(new LinkedHashMap<>());
  }

  public Params set(String key, Object value) {
    map.put(key, value);
    return this;
  }
}
