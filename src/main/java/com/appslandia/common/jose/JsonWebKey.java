// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonWebKey extends JoseMap {
  private static final long serialVersionUID = 1L;

  public static final String KTY = "kty";
  public static final String USE = "use";
  public static final String KEY_OPS = "key_ops";
  public static final String ALG = "alg";
  public static final String KID = "kid";

  public JsonWebKey() {
  }

  public JsonWebKey(Map<String, Object> map) {
    super(map);
  }

  @Override
  public JsonWebKey set(String key, Object value) {
    super.set(key, value);
    return this;
  }

  @Override
  public JsonWebKey setNumericDate(String key, Date value) {
    super.setNumericDate(key, value);
    return this;
  }

  @Override
  public JsonWebKey setNumericDate(String key, long timeInMs) {
    super.setNumericDate(key, timeInMs);
    return this;
  }

  public String getKty() {
    return (String) get(KTY);
  }

  public JsonWebKey setKty(String value) {
    put(KTY, value);
    return this;
  }

  public String getUse() {
    return (String) get(USE);
  }

  public JsonWebKey setUse(String value) {
    put(USE, value);
    return this;
  }

  public List<String> getKey_ops() {
    return ObjectUtils.cast(get(KEY_OPS));
  }

  public JsonWebKey setKey_ops(String... values) {
    put(KEY_OPS, values);
    return this;
  }

  public String getAlg() {
    return (String) get(ALG);
  }

  public JsonWebKey setAlg(String value) {
    put(ALG, value);
    return this;
  }

  public String getKid() {
    return (String) get(KID);
  }

  public JsonWebKey setKid(String value) {
    put(KID, value);
    return this;
  }
}
