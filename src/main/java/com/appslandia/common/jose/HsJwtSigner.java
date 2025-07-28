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

import java.util.LinkedHashSet;
import java.util.Set;

import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.CollectionUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class HsJwtSigner extends HsJwsSigner<JwtPayload> {

  protected String iss;
  protected Set<String> aud;
  protected int leewaySec;

  public HsJwtSigner(String jwsAlgorithm, String macAlgorithm) {
    super(jwsAlgorithm, macAlgorithm, JwtPayload.class);
  }

  @Override
  public HsJwsSigner<JwtPayload> setMacProvider(String macProvider) {
    super.setMacProvider(macProvider);
    return this;
  }

  @Override
  public HsJwtSigner setJsonProcessor(JsonProcessor jsonProcessor) {
    super.setJsonProcessor(jsonProcessor);
    return this;
  }

  @Override
  public HsJwtSigner setSecret(byte[] secret) {
    super.setSecret(secret);
    return this;
  }

  @Override
  public HsJwtSigner setSecret(String secretExpr) {
    super.setSecret(secretExpr);
    return this;
  }

  @Override
  public HsJwtSigner setKid(String kid) {
    super.setKid(kid);
    return this;
  }

  public HsJwtSigner setIss(String iss) {
    this.iss = iss;
    return this;
  }

  public HsJwtSigner setAud(String... aud) {
    if ((aud != null) && (aud.length > 0)) {
      this.aud = CollectionUtils.toSet(new LinkedHashSet<>(), aud);
    }
    return this;
  }

  public HsJwtSigner setLeewaySec(int leewaySec) {
    this.leewaySec = leewaySec;
    return this;
  }

  @Override
  public JwtSigner build() {
    Arguments.notNull(this.jsonProcessor);
    var impl = new JwtSigner().setJsonProcessor(this.jsonProcessor).setSigner(this.signer).setAlg(this.alg)
        .setKid(this.kid).setLeewaySec(this.leewaySec).setIss(this.iss);

    impl.aud = this.aud;
    return impl.initialize();
  }

  public static HsJwtSigner HS256() {
    return new HsJwtSigner("HS256", "HmacSHA256");
  }

  public static HsJwtSigner HS384() {
    return new HsJwtSigner("HS384", "HmacSHA384");
  }

  public static HsJwtSigner HS512() {
    return new HsJwtSigner("HS512", "HmacSHA512");
  }
}
