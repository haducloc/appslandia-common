// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;

import com.appslandia.common.crypto.DsaSigner;
import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class DsaJwsSigner<P> {

  protected Class<P> payloadClass;
  protected JsonProcessor jsonProcessor;

  protected String alg;
  protected String kid;

  protected DsaSigner signer;

  public DsaJwsSigner(String jwsAlgorithm, String signatureAlgorithm, Class<P> payloadClass) {
    alg = Arguments.notNull(jwsAlgorithm);
    signer = new DsaSigner().setAlgorithm(Arguments.notNull(signatureAlgorithm));
    this.payloadClass = Arguments.notNull(payloadClass);
  }

  public DsaJwsSigner<P> setSignatureProvider(String signatureProvider) {
    signer.setProvider(signatureProvider);
    return this;
  }

  public DsaJwsSigner<P> setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    signer.setAlgParamSpec(algParamSpec);
    return this;
  }

  public DsaJwsSigner<P> setJsonProcessor(JsonProcessor jsonProcessor) {
    this.jsonProcessor = jsonProcessor;
    return this;
  }

  public DsaJwsSigner<P> setPrivateKey(PrivateKey key) {
    signer.setPrivateKey(key);
    return this;
  }

  public DsaJwsSigner<P> setPublicKey(PublicKey key) {
    signer.setPublicKey(key);
    return this;
  }

  public DsaJwsSigner<P> setKid(String kid) {
    this.kid = kid;
    return this;
  }

  public JwsSigner<P> build() {
    Arguments.notNull(jsonProcessor);
    return new JwsSigner<>(payloadClass).setJsonProcessor(jsonProcessor).setSigner(signer).setAlg(alg).setKid(kid)
        .initialize();
  }

  public static <P> DsaJwsSigner<P> ES256(Class<P> payloadClass) {
    return new DsaJwsSigner<>("ES256", "SHA256WithECDSAInP1363Format", payloadClass);
  }

  public static <P> DsaJwsSigner<P> ES384(Class<P> payloadClass) {
    return new DsaJwsSigner<>("ES384", "SHA384WithECDSAInP1363Format", payloadClass);
  }

  public static <P> DsaJwsSigner<P> ES512(Class<P> payloadClass) {
    return new DsaJwsSigner<>("ES512", "SHA512withECDSAinP1363Format", payloadClass);
  }

  public static <P> DsaJwsSigner<P> RS256(Class<P> payloadClass) {
    return new DsaJwsSigner<>("RS256", "SHA256withRSA", payloadClass);
  }

  public static <P> DsaJwsSigner<P> RS384(Class<P> payloadClass) {
    return new DsaJwsSigner<>("RS384", "SHA384withRSA", payloadClass);
  }

  public static <P> DsaJwsSigner<P> RS512(Class<P> payloadClass) {
    return new DsaJwsSigner<>("RS512", "SHA512withRSA", payloadClass);
  }

  public static <P> DsaJwsSigner<P> PS256(Class<P> payloadClass) {
    return new DsaJwsSigner<>("PS256", "SHA256withRSA/PSS", payloadClass);
  }

  public static <P> DsaJwsSigner<P> PS384(Class<P> payloadClass) {
    return new DsaJwsSigner<>("PS384", "SHA384withRSA/PSS", payloadClass);
  }

  public static <P> DsaJwsSigner<P> PS512(Class<P> payloadClass) {
    return new DsaJwsSigner<>("PS512", "SHA512withRSA/PSS", payloadClass);
  }
}
