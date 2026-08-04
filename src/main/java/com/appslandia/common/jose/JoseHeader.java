// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author Loc Ha
 *
 */
public class JoseHeader extends JoseMap {
  private static final long serialVersionUID = 1L;

  public static final String TYP = "typ";
  public static final String ALG = "alg";
  public static final String KID = "kid";

  public static final String JKU = "jku";
  public static final String JWK = "jwk";

  public JoseHeader() {
  }

  public JoseHeader(Map<String, Object> map) {
    super(map);
  }

  @Override
  public JoseHeader set(String key, Object value) {
    super.set(key, value);
    return this;
  }

  @Override
  public JoseHeader setNumericDate(String key, Date value) {
    super.setNumericDate(key, value);
    return this;
  }

  @Override
  public JoseHeader setNumericDate(String key, long timeInMs) {
    super.setNumericDate(key, timeInMs);
    return this;
  }

  public String getTyp() {
    return (String) get(TYP);
  }

  public JoseHeader setTyp(String value) {
    set(TYP, value);
    return this;
  }

  public String getAlg() {
    return (String) get(ALG);
  }

  public JoseHeader setAlg(String value) {
    set(ALG, value);
    return this;
  }

  public String getKid() {
    return (String) get(KID);
  }

  public JoseHeader setKid(String value) {
    set(KID, value);
    return this;
  }

  public String getJku() {
    return (String) get(JKU);
  }

  public JoseHeader setJku(String value) {
    set(JKU, value);
    return this;
  }
}
