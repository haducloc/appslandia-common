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
    this.alg = Arguments.notNull(jwsAlgorithm);
    this.signer = new MacSigner().setAlgorithm(Arguments.notNull(macAlgorithm));
    this.payloadClass = Arguments.notNull(payloadClass);
  }

  public HsJwsSigner<P> setMacProvider(String macProvider) {
    this.signer.setProvider(macProvider);
    return this;
  }

  public HsJwsSigner<P> setJsonProcessor(JsonProcessor jsonProcessor) {
    this.jsonProcessor = jsonProcessor;
    return this;
  }

  public HsJwsSigner<P> setSecret(byte[] secret) {
    this.signer.setSecret(secret);
    return this;
  }

  public HsJwsSigner<P> setSecret(String secretExpr) {
    this.signer.setSecret(secretExpr);
    return this;
  }

  public HsJwsSigner<P> setKid(String kid) {
    this.kid = kid;
    return this;
  }

  public JwsSigner<P> build() {
    Arguments.notNull(this.jsonProcessor);
    return new JwsSigner<>(this.payloadClass).setJsonProcessor(this.jsonProcessor).setSigner(this.signer)
        .setAlg(this.alg).setKid(this.kid).initialize();
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
