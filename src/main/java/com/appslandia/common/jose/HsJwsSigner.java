// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import com.appslandia.common.crypto.MacSigner;
import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class HsJwsSigner<P> {

  protected Class<P> payloadClass;
  protected JsonProcessor jsonProcessor;

  protected String alg;
  protected String kid;

  protected MacSigner signer;

  public HsJwsSigner(String jwsAlgorithm, String macAlgorithm, Class<P> payloadClass) {
    alg = Arguments.notNull(jwsAlgorithm);
    signer = new MacSigner().setAlgorithm(Arguments.notNull(macAlgorithm));
    this.payloadClass = Arguments.notNull(payloadClass);
  }

  public HsJwsSigner<P> setMacProvider(String macProvider) {
    signer.setProvider(macProvider);
    return this;
  }

  public HsJwsSigner<P> setJsonProcessor(JsonProcessor jsonProcessor) {
    this.jsonProcessor = jsonProcessor;
    return this;
  }

  public HsJwsSigner<P> setSecret(byte[] secret) {
    signer.setSecret(secret);
    return this;
  }

  public HsJwsSigner<P> setSecret(String secretExpr) {
    signer.setSecret(secretExpr);
    return this;
  }

  public HsJwsSigner<P> setKid(String kid) {
    this.kid = kid;
    return this;
  }

  public JwsSigner<P> build() {
    Arguments.notNull(jsonProcessor);
    return new JwsSigner<>(payloadClass).setJsonProcessor(jsonProcessor).setSigner(signer).setAlg(alg).setKid(kid)
        .initialize();
  }

  public static <P> HsJwsSigner<P> HS256(Class<P> payloadClass) {
    return new HsJwsSigner<>("HS256", "HmacSHA256", payloadClass);
  }

  public static <P> HsJwsSigner<P> HS384(Class<P> payloadClass) {
    return new HsJwsSigner<>("HS384", "HmacSHA384", payloadClass);
  }

  public static <P> HsJwsSigner<P> HS512(Class<P> payloadClass) {
    return new HsJwsSigner<>("HS512", "HmacSHA512", payloadClass);
  }
}
