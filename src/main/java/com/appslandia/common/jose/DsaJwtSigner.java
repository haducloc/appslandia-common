// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
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
public class DsaJwtSigner extends DsaJwsSigner<JwtPayload> {

  protected String iss;
  protected Set<String> aud;
  protected int leewaySec;

  public DsaJwtSigner(String jwsAlgorithm, String signatureAlgorithm) {
    super(jwsAlgorithm, signatureAlgorithm, JwtPayload.class);
  }

  @Override
  public DsaJwtSigner setSignatureProvider(String signatureProvider) {
    super.setSignatureProvider(signatureProvider);
    return this;
  }

  @Override
  public DsaJwtSigner setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    super.setAlgParamSpec(algParamSpec);
    return this;
  }

  @Override
  public DsaJwtSigner setJsonProcessor(JsonProcessor jsonProcessor) {
    super.setJsonProcessor(jsonProcessor);
    return this;
  }

  @Override
  public DsaJwtSigner setPrivateKey(PrivateKey key) {
    super.setPrivateKey(key);
    return this;
  }

  @Override
  public DsaJwtSigner setPublicKey(PublicKey key) {
    super.setPublicKey(key);
    return this;
  }

  @Override
  public DsaJwtSigner setKid(String kid) {
    super.setKid(kid);
    return this;
  }

  public DsaJwtSigner setIss(String iss) {
    this.iss = iss;
    return this;
  }

  public DsaJwtSigner setAud(String... aud) {
    if ((aud != null) && (aud.length > 0)) {
      this.aud = CollectionUtils.addAll(new LinkedHashSet<>(), aud);
    }
    return this;
  }

  public DsaJwtSigner setLeewaySec(int leewaySec) {
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

  public static DsaJwtSigner ES256() {
    return new DsaJwtSigner("ES256", "SHA256WithECDSAInP1363Format");
  }

  public static DsaJwtSigner ES384() {
    return new DsaJwtSigner("ES384", "SHA384WithECDSAInP1363Format");
  }

  public static DsaJwtSigner ES512() {
    return new DsaJwtSigner("ES512", "SHA512withECDSAinP1363Format");
  }

  public static DsaJwtSigner RS256() {
    return new DsaJwtSigner("RS256", "SHA256withRSA");
  }

  public static DsaJwtSigner RS384() {
    return new DsaJwtSigner("RS384", "SHA384withRSA");
  }

  public static DsaJwtSigner RS512() {
    return new DsaJwtSigner("RS512", "SHA512withRSA");
  }

  public static DsaJwtSigner PS256() {
    return new DsaJwtSigner("PS256", "SHA256withRSA/PSS");
  }

  public static DsaJwtSigner PS384() {
    return new DsaJwtSigner("PS384", "SHA384withRSA/PSS");
  }

  public static DsaJwtSigner PS512() {
    return new DsaJwtSigner("PS512", "SHA512withRSA/PSS");
  }
}
