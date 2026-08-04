// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
      this.aud = CollectionUtils.addAll(new LinkedHashSet<>(), aud);
    }
    return this;
  }

  public HsJwtSigner setLeewaySec(int leewaySec) {
    this.leewaySec = leewaySec;
    return this;
  }

  @Override
  public JwtSigner build() {
    Arguments.notNull(jsonProcessor);
    var impl = new JwtSigner().setJsonProcessor(jsonProcessor).setSigner(signer).setAlg(alg).setKid(kid)
        .setLeewaySec(leewaySec).setIss(iss);

    impl.aud = aud;
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
