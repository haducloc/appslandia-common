// The MIT License (MIT)
// Copyright © 2015 Loc Ha

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.jose;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class JwtPayload extends JoseMapObject {
  private static final long serialVersionUID = 1L;

  public static final String ISS = "iss";
  public static final String SUB = "sub";
  public static final String AUD = "aud";
  public static final String EXP = "exp";
  public static final String NBF = "nbf";
  public static final String IAT = "iat";
  public static final String JTI = "jti";

  public JwtPayload() {
  }

  public JwtPayload(Map<String, Object> map) {
    super(map);
  }

  public boolean hasAudience(String checkingAudience) {
    Arguments.notNull(checkingAudience);

    var aud = getAud();
    if (aud == null) {
      return false;
    }
    return aud.contains(checkingAudience);
  }

  @Override
  public JwtPayload set(String key, Object value) {
    super.set(key, value);
    return this;
  }

  @Override
  public JwtPayload setNumericDate(String key, Date value) {
    super.setNumericDate(key, value);
    return this;
  }

  @Override
  public JwtPayload setNumericDate(String key, long timeInMs) {
    super.setNumericDate(key, timeInMs);
    return this;
  }

  public String getIss() {
    return (String) this.get(ISS);
  }

  public JwtPayload setIss(String value) {
    this.set(ISS, value);
    return this;
  }

  public String getSub() {
    return (String) this.get(SUB);
  }

  public JwtPayload setSub(String value) {
    this.set(SUB, value);
    return this;
  }

  public List<String> getAud() {
    return ObjectUtils.cast(this.get(AUD));
  }

  public JwtPayload setAud(String... values) {
    this.set(AUD, values);
    return this;
  }

  public Date getExp() {
    return getNumericDate(EXP);
  }

  public JwtPayload setExp(Date value) {
    setNumericDate(EXP, value);
    return this;
  }

  public JwtPayload setExp(long expiresIn, TimeUnit unit) {
    setNumericDate(EXP, System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(expiresIn, unit));
    return this;
  }

  public Date getNbf() {
    return getNumericDate(NBF);
  }

  public JwtPayload setNbf(Date value) {
    setNumericDate(NBF, value);
    return this;
  }

  public Date getIat() {
    return getNumericDate(IAT);
  }

  public JwtPayload setIat(Date value) {
    setNumericDate(IAT, value);
    return this;
  }

  public JwtPayload setIatNow() {
    return setIat(new Date());
  }

  public String getJti() {
    return (String) this.get(JTI);
  }

  public JwtPayload setJti(String value) {
    this.set(JTI, value);
    return this;
  }
}
