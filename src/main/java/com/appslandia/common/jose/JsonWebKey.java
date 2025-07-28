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

import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonWebKey extends JoseMapObject {
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
    return (String) this.get(KTY);
  }

  public JsonWebKey setKty(String value) {
    this.put(KTY, value);
    return this;
  }

  public String getUse() {
    return (String) this.get(USE);
  }

  public JsonWebKey setUse(String value) {
    this.put(USE, value);
    return this;
  }

  public List<String> getKey_ops() {
    return ObjectUtils.cast(this.get(KEY_OPS));
  }

  public JsonWebKey setKey_ops(String... values) {
    this.put(KEY_OPS, values);
    return this;
  }

  public String getAlg() {
    return (String) this.get(ALG);
  }

  public JsonWebKey setAlg(String value) {
    this.put(ALG, value);
    return this;
  }

  public String getKid() {
    return (String) this.get(KID);
  }

  public JsonWebKey setKid(String value) {
    this.put(KID, value);
    return this;
  }
}
