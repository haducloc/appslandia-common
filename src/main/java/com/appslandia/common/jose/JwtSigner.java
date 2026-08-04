// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.appslandia.common.base.InitializingException;
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
    Arguments.isTrue(leewaySec >= 0);

    // iss
    defaultVerifiers.add((token) -> {
      if (!Objects.equals(iss, token.getPayload().getIss())) {
        throw new JoseVerificationException("iss doesn't match.");
      }
    });

    // exp
    defaultVerifiers.add((token) -> {
      var dt = token.getPayload().getExp();
      if (dt != null) {
        long nt = JoseUtils.toNumericDate(dt);

        if (!JoseUtils.isFutureTime(nt, leewaySec)) {
          throw new JoseVerificationException("token is expired.");
        }
      }
    });

    // iat
    defaultVerifiers.add((token) -> {
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
  public JwtSigner initialize() throws InitializingException {
    super.initialize();
    return this;
  }

  public JwtPayload newPayload() {
    initialize();
    var payload = new JwtPayload();

    if (iss != null) {
      payload.setIss(iss);
    }
    if (aud != null) {
      payload.setAud(aud.toArray(new String[aud.size()]));
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
      this.aud = CollectionUtils.addAll(new LinkedHashSet<>(), aud);
    }
    return this;
  }

  public JwtSigner setLeewaySec(int leewaySec) {
    assertNotInitialized();
    this.leewaySec = leewaySec;
    return this;
  }
}
