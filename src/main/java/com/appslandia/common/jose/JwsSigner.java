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

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.appslandia.common.base.InitializingException;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.crypto.Digester;
import com.appslandia.common.crypto.DsaSigner;
import com.appslandia.common.crypto.MacSigner;
import com.appslandia.common.json.JsonException;
import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class JwsSigner<P> extends InitializingObject {

  static final String JWT_NONE_ALG = "none";

  protected JsonProcessor jsonProcessor;

  protected String type = "JWT";
  protected String alg;
  protected String kid;

  protected Digester signer;

  protected final List<JwsVerifier<P>> defaultVerifiers = new ArrayList<>();
  protected List<JwsVerifier<P>> customVerifiers;

  protected Class<P> payloadClass;

  public JwsSigner(Class<P> payloadClass) {
    this.payloadClass = payloadClass;
  }

  // signatureVerifier
  protected final JwsVerifier<P> signatureVerifier = (token) -> {

    if (token.getSignaturePart().isEmpty()) {
      if (this.signer != null) {
        throw new JwsSignatureException("signature is required.");
      }
    } else {
      if (this.signer == null) {
        throw new JwsSignatureException("signer is required.");
      }
      var dataToSign = JoseUtils.toJwsData(token.getHeaderPart(), token.getPayloadPart());

      if (!this.signer.verify(dataToSign.getBytes(StandardCharsets.UTF_8),
          JoseUtils.getJoseBase64().decode(token.getSignaturePart()))) {
        throw new JwsSignatureException("JWT signature verification failed.");
      }
    }
  };

  @Override
  protected void init() throws Exception {
    Arguments.notNull(type, "type is required.");
    Arguments.notNull(jsonProcessor, "jsonProcessor is required.");

    if (signer != null) {
      Arguments.notNull(alg, "alg is required.");
    } else {
      alg = JWT_NONE_ALG;
    }

    // typ
    defaultVerifiers.add((token) -> {
      if (!Objects.equals(type, token.getHeader().getTyp())) {
        throw new JoseVerificationException("typ doesn't match.");
      }
    });

    // alg
    defaultVerifiers.add((token) -> {
      if (!Objects.equals(alg, token.getHeader().getAlg())) {
        throw new JoseVerificationException("alg doesn't match.");
      }
    });

    // kid
    defaultVerifiers.add((token) -> {
      if (!Objects.equals(kid, token.getHeader().getKid())) {
        throw new JoseVerificationException("kid doesn't match.");
      }
    });
  }

  @Override
  public JwsSigner<P> initialize() throws InitializingException {
    super.initialize();
    return this;
  }

  public JoseHeader newHeader() {
    this.initialize();
    var header = new JoseHeader().setTyp(type).setAlg(alg);

    if (kid != null) {
      header.setKid(kid);
    }
    return header;
  }

  public String sign(JwsToken<P> token) throws CryptoException, JwsSignatureException, JsonException {
    this.initialize();
    Arguments.notNull(token);
    Arguments.notNull(token.getHeader());
    Arguments.notNull(token.getPayload());

    // defaultVerifiers
    defaultVerifiers.forEach((verifier) -> verifier.verify(token));

    var header = JoseUtils.getJoseBase64().encode(jsonProcessor.toByteArray(token.getHeader()));
    String payload = null;

    if (payloadClass == byte[].class) {
      payload = JoseUtils.getJoseBase64().encode((byte[]) token.getPayload());
    } else {
      payload = JoseUtils.getJoseBase64().encode(jsonProcessor.toByteArray(token.getPayload()));
    }

    // No ALG
    if (signer == null) {
      return JoseUtils.toJwsToken(header, payload, "");
    }

    var dataToSign = JoseUtils.toJwsData(header, payload);

    // Signature
    var sig = signer.digest(dataToSign.getBytes(StandardCharsets.UTF_8));

    return JoseUtils.toJwsToken(header, payload, JoseUtils.getJoseBase64().encode(sig));
  }

  public void verify(JwsToken<P> token) throws CryptoException, JwsSignatureException {
    this.initialize();
    Arguments.notNull(token);
    Arguments.notNull(token.getHeader());
    Arguments.notNull(token.getPayload());
    Arguments.notNull(token.getHeaderPart());
    Arguments.notNull(token.getPayloadPart());
    Arguments.notNull(token.getSignaturePart());

    // defaultVerifiers
    defaultVerifiers.forEach((verifier) -> verifier.verify(token));

    // signatureVerifier
    signatureVerifier.verify(token);

    // customVerifiers
    if (customVerifiers != null) {
      customVerifiers.forEach((verifier) -> verifier.verify(token));
    }
  }

  public JwsToken<P> parse(String token) throws JsonException {
    this.initialize();
    Arguments.notNull(token);

    var parts = JoseUtils.parseJws(token);
    Arguments.notNull(parts, "The token '{}' is invalid format.", token);

    // Header
    var headerJson = new String(JoseUtils.getJoseBase64().decode(parts[0]), StandardCharsets.UTF_8);
    var header = jsonProcessor.read(new StringReader(headerJson), JoseHeader.class);

    // PAYLOAD
    var payloadBytes = JoseUtils.getJoseBase64().decode(parts[1]);
    P payload = null;

    if (payloadClass == byte[].class) {
      payload = ObjectUtils.cast(payloadBytes);
    } else {
      var payloadJson = new String(payloadBytes, StandardCharsets.UTF_8);
      payload = jsonProcessor.read(new StringReader(payloadJson), payloadClass);
    }
    return new JwsToken<>(header, payload, parts[0], parts[1], parts[2]);
  }

  public JwsSigner<P> setJsonProcessor(JsonProcessor jsonProcessor) {
    assertNotInitialized();
    this.jsonProcessor = jsonProcessor;
    return this;
  }

  public JwsSigner<P> setSigner(MacSigner signer) {
    assertNotInitialized();
    this.signer = signer;
    return this;
  }

  public JwsSigner<P> setSigner(DsaSigner signer) {
    assertNotInitialized();
    this.signer = signer;
    return this;
  }

  public JwsSigner<P> setAlg(String alg) {
    assertNotInitialized();
    this.alg = alg;
    return this;
  }

  public JwsSigner<P> setKid(String kid) {
    assertNotInitialized();
    this.kid = kid;
    return this;
  }

  public JwsSigner<P> addVerifier(JwsVerifier<P> verifier) {
    assertNotInitialized();
    if (customVerifiers == null) {
      customVerifiers = new ArrayList<>();
    }
    customVerifiers.add(verifier);
    return this;
  }
}
