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
import java.util.Objects;
import java.util.Set;

import com.appslandia.common.base.InitializeException;
import com.appslandia.common.crypto.DsaSigner;
import com.appslandia.common.crypto.MacSigner;
import com.appslandia.common.json.JsonException;
import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.CollectionUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class JwtSigner extends JwsSigner<JwtPayload> {

  protected String iss;
  protected Set<String> aud;
  protected int leewaySec;

  public JwtSigner() {
    super(JwtPayload.class);
  }

  @Override
  protected void init() throws Exception {
    super.init();
    Arguments.isTrue(this.leewaySec >= 0);

    // iss
    this.defaultVerifiers.add((token) -> {
      if (!Objects.equals(this.iss, token.getPayload().getIss())) {
        throw new JoseVerificationException("iss doesn't match.");
      }
    });

    // exp
    this.defaultVerifiers.add((token) -> {
      var dt = token.getPayload().getExp();
      if (dt != null) {
        long nt = JoseUtils.toNumericDate(dt);

        if (!JoseUtils.isFutureTime(nt, this.leewaySec)) {
          throw new JoseVerificationException("token is expired.");
        }
      }
    });

    // iat
    this.defaultVerifiers.add((token) -> {
      var dt = token.getPayload().getIat();
      if (dt != null) {
        long nt = JoseUtils.toNumericDate(dt);

        if (JoseUtils.isFutureTime(nt, 0)) {
          throw new JoseVerificationException("iat must be a past date/time.");
        }
      }
    });
  }

  @Override
  public JwtSigner initialize() throws InitializeException {
    super.initialize();
    return this;
  }

  public JwtPayload newPayload() {
    this.initialize();
    var payload = new JwtPayload();

    if (this.iss != null) {
      payload.setIss(this.iss);
    }
    if (this.aud != null) {
      payload.setAud(this.aud.toArray(new String[this.aud.size()]));
    }
    return payload;
  }

  @Override
  public JwtToken parse(String token) throws JsonException {
    var jwsToken = super.parse(token);
    return new JwtToken(jwsToken.header, jwsToken.payload, jwsToken.headerPart, jwsToken.payloadPart,
        jwsToken.signaturePart);
  }

  @Override
  public JwtSigner setJsonProcessor(JsonProcessor jsonProcessor) {
    super.setJsonProcessor(jsonProcessor);
    return this;
  }

  @Override
  public JwtSigner setSigner(DsaSigner signer) {
    super.setSigner(signer);
    return this;
  }

  @Override
  public JwtSigner setSigner(MacSigner signer) {
    super.setSigner(signer);
    return this;
  }

  @Override
  public JwtSigner setAlg(String alg) {
    super.setAlg(alg);
    return this;
  }

  @Override
  public JwtSigner setKid(String kid) {
    super.setKid(kid);
    return this;
  }

  public JwtSigner setIss(String iss) {
    assertNotInitialized();
    this.iss = iss;
    return this;
  }

  public JwtSigner setAud(String... aud) {
    assertNotInitialized();
    if ((aud != null) && (aud.length > 0)) {
      this.aud = CollectionUtils.toSet(new LinkedHashSet<>(), aud);
    }
    return this;
  }

  public JwtSigner setLeewaySec(int leewaySec) {
    assertNotInitialized();
    this.leewaySec = leewaySec;
    return this;
  }
}
