// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.json.JsonMap;

/**
 *
 * @author Loc Ha
 *
 */
public class JoseMap extends JsonMap {
  private static final long serialVersionUID = 1L;

  public JoseMap() {
    super(new LinkedHashMap<>());
  }

  public JoseMap(Map<String, Object> map) {
    super(map);
  }

  @Override
  public JoseMap set(String key, Object value) {
    super.set(key, value);
    return this;
  }

  public Date getNumericDate(String key) {
    var nd = getLongOpt(key);
    return (nd != null) ? JoseUtils.toDate(nd) : null;
  }

  public JoseMap setNumericDate(String key, Date value) {
    set(key, JoseUtils.toNumericDate(value));
    return this;
  }

  public JoseMap setNumericDate(String key, long timeInMs) {
    set(key, JoseUtils.toNumericDate(timeInMs));
    return this;
  }
}
