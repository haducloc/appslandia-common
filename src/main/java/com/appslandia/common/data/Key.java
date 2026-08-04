// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.data;

import java.util.LinkedHashMap;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class Key extends CaseInsensitiveMap<Object> {
  private static final long serialVersionUID = 1L;

  public Key() {
    super(new LinkedHashMap<>());
  }

  public Key(String keyColumn, Object value) {
    super(new LinkedHashMap<>());

    super.put(keyColumn, value);
  }

  public <T> T get(String keyColumn) {
    return ObjectUtils.cast(super.get(keyColumn));
  }

  public Key set(String keyColumn, Object value) {
    super.put(keyColumn, value);
    return this;
  }
}
