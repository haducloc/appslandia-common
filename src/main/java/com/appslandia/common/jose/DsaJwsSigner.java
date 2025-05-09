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
    this.alg = Arguments.notNull(jwsAlgorithm);
    this.signer = new DsaSigner().setAlgorithm(Arguments.notNull(signatureAlgorithm));
    this.payloadClass = Arguments.notNull(payloadClass);
  }

  public DsaJwsSigner<P> setSignatureProvider(String signatureProvider) {
    this.signer.setProvider(signatureProvider);
    return this;
  }

  public DsaJwsSigner<P> setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    this.signer.setAlgParamSpec(algParamSpec);
    return this;
  }

  public DsaJwsSigner<P> setJsonProcessor(JsonProcessor jsonProcessor) {
    this.jsonProcessor = jsonProcessor;
    return this;
  }

  public DsaJwsSigner<P> setPrivateKey(PrivateKey key) {
    this.signer.setPrivateKey(key);
    return this;
  }

  public DsaJwsSigner<P> setPublicKey(PublicKey key) {
    this.signer.setPublicKey(key);
    return this;
  }

  public DsaJwsSigner<P> setKid(String kid) {
    this.kid = kid;
    return this;
  }

  public JwsSigner<P> build() {
    Arguments.notNull(this.jsonProcessor);
    return new JwsSigner<>(this.payloadClass).setJsonProcessor(this.jsonProcessor).setSigner(this.signer)
        .setAlg(this.alg).setKid(this.kid).initialize();
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
