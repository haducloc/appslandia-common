// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.InitializeException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.crypto.Digester;
import com.appslandia.common.crypto.DsaDigester;
import com.appslandia.common.crypto.MacDigester;
import com.appslandia.common.json.JsonException;
import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtSigner extends InitializeObject {

    static final String JWT_NONE_ALG = "none";

    protected JsonProcessor jsonProcessor;

    protected String type = "JWT";
    protected String alg;
    protected String kid;
    protected String issuer;

    protected int leewaySec;
    protected Digester signer;

    protected final List<JoseVerifier<JwtPayload>> defaultVerifiers = new LinkedList<>();
    protected List<JoseVerifier<JwtPayload>> customVerifiers;
    protected Set<String> audiences;

    // signatureVerifier
    protected final JoseVerifier<JwtPayload> signatureVerifier = (token) -> {

	if (token.getSignaturePart().isEmpty()) {
	    if (this.signer != null) {
		throw new JoseSignatureException("signature is required.");
	    }
	} else {
	    if (this.signer == null) {
		throw new JoseSignatureException("signer is required.");
	    }
	    String dataToSign = JoseUtils.toJoseData(token.getHeaderPart(), token.getPayloadPart());

	    if (!this.signer.verify(dataToSign.getBytes(StandardCharsets.UTF_8), BaseEncoder.BASE64_URL.decode(token.getSignaturePart()))) {
		throw new JoseSignatureException("JWT signature verification failed.");
	    }
	}
    };

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.type, "type is required.");
	Asserts.notNull(this.jsonProcessor, "jsonProcessor is required.");

	if (this.signer != null) {
	    Asserts.notNull(this.alg, "alg is required.");
	} else {
	    this.alg = JWT_NONE_ALG;
	}

	Asserts.isTrue(this.leewaySec >= 0);

	// Type
	this.defaultVerifiers.add((token) -> {
	    if (!Objects.equals(this.type, token.getHeader().getType())) {
		throw new JoseVerificationException("type doesn't match.");
	    }
	});

	// Algorithm
	this.defaultVerifiers.add((token) -> {
	    if (!Objects.equals(this.alg, token.getHeader().getAlgorithm())) {
		throw new JoseVerificationException("algorithm doesn't match.");
	    }
	});

	// kid
	this.defaultVerifiers.add((token) -> {
	    if (!Objects.equals(this.kid, token.getHeader().getKid())) {
		throw new JoseVerificationException("kid doesn't match.");
	    }
	});

	// issuer
	this.defaultVerifiers.add((token) -> {
	    if (!Objects.equals(this.issuer, token.getPayload().getIssuer())) {
		throw new JoseVerificationException("issuer doesn't match.");
	    }
	});

	// exp
	this.defaultVerifiers.add((token) -> {
	    Date dt = token.getPayload().getExpiresAt();
	    if (dt != null) {
		long nt = JoseUtils.toNumericDate(dt);

		if (!JoseUtils.isFutureTime(nt, this.leewaySec)) {
		    throw new JoseVerificationException("token is expired.");
		}
	    }
	});

	// iat
	this.defaultVerifiers.add((token) -> {
	    Date dt = token.getPayload().getIssuedAt();
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

    public JoseHeader newHeader() {
	this.initialize();
	JoseHeader header = new JoseHeader().setType(this.type).setAlgorithm(this.alg);

	if (this.kid != null) {
	    header.setKid(this.kid);
	}
	return header;
    }

    public JwtPayload newPayload() {
	this.initialize();
	JwtPayload payload = new JwtPayload();

	if (this.issuer != null) {
	    payload.setIssuer(this.issuer);
	}
	if (this.audiences != null) {
	    payload.setAudiences(this.audiences.toArray(new String[this.audiences.size()]));
	}
	return payload;
    }

    public String sign(JwtToken token) throws CryptoException, JoseSignatureException, JsonException {
	this.initialize();
	Asserts.notNull(token);
	Asserts.notNull(token.getHeader());
	Asserts.notNull(token.getPayload());

	// defaultVerifiers
	this.defaultVerifiers.forEach((verifier) -> verifier.verify(token));

	String header = BaseEncoder.BASE64_URL.encode(this.jsonProcessor.toByteArray(token.getHeader()));
	String payload = BaseEncoder.BASE64_URL.encode(this.jsonProcessor.toByteArray(token.getPayload()));

	// No ALG
	if (this.signer == null) {
	    return JoseUtils.toJoseToken(header, payload, "");
	}

	String dataToSign = JoseUtils.toJoseData(header, payload);

	// Signature
	byte[] sig = this.signer.digest(dataToSign.getBytes(StandardCharsets.UTF_8));

	return JoseUtils.toJoseToken(header, payload, BaseEncoder.BASE64_URL.encode(sig));
    }

    public JwtToken verify(JwtToken token) throws CryptoException, JoseSignatureException {
	this.initialize();
	Asserts.notNull(token);
	Asserts.notNull(token.getHeader());
	Asserts.notNull(token.getPayload());

	Asserts.notNull(token.getHeaderPart());
	Asserts.notNull(token.getPayloadPart());
	Asserts.notNull(token.getSignaturePart());

	// defaultVerifiers
	this.defaultVerifiers.forEach((verifier) -> verifier.verify(token));

	// signatureVerifier
	this.signatureVerifier.verify(token);

	// customVerifiers
	if (customVerifiers != null) {
	    this.customVerifiers.forEach((verifier) -> verifier.verify(token));
	}
	return token;
    }

    public JwtToken parse(String token) throws JsonException {
	this.initialize();
	Asserts.notNull(token);

	String[] parts = JoseUtils.parseParts(token);
	Asserts.notNull(parts, () -> STR.fmt("The token '{}' is invalid format.", token));

	// Header
	String headerJson = new String(BaseEncoder.BASE64_URL.decode(parts[0]), StandardCharsets.UTF_8);
	JoseHeader header = this.jsonProcessor.read(new StringReader(headerJson), JoseHeader.class);

	// PAYLOAD
	String payloadJson = new String(BaseEncoder.BASE64_URL.decode(parts[1]), StandardCharsets.UTF_8);
	JwtPayload payload = this.jsonProcessor.read(new StringReader(payloadJson), JwtPayload.class);

	return new JwtToken(header, payload, parts[0], parts[1], parts[2]);
    }

    public JwtSigner setJsonProcessor(JsonProcessor jsonProcessor) {
	assertNotInitialized();
	this.jsonProcessor = jsonProcessor;
	return this;
    }

    public JwtSigner setSigner(MacDigester signer) {
	assertNotInitialized();
	this.signer = signer;
	return this;
    }

    public JwtSigner setSigner(DsaDigester signer) {
	assertNotInitialized();
	this.signer = signer;
	return this;
    }

    public JwtSigner setAlg(String alg) {
	assertNotInitialized();
	this.alg = alg;
	return this;
    }

    public JwtSigner setKid(String kid) {
	assertNotInitialized();
	this.kid = kid;
	return this;
    }

    public JwtSigner setIssuer(String issuer) {
	assertNotInitialized();
	this.issuer = issuer;
	return this;
    }

    public JwtSigner addAudience(String audience) {
	assertNotInitialized();
	if (this.audiences == null) {
	    this.audiences = new LinkedHashSet<>();
	}
	this.audiences.add(audience);
	return this;
    }

    public JwtSigner setLeewaySec(int leewaySec) {
	assertNotInitialized();
	this.leewaySec = leewaySec;
	return this;
    }

    public JwtSigner addVerifier(JoseVerifier<JwtPayload> verifier) {
	assertNotInitialized();
	if (this.customVerifiers == null) {
	    this.customVerifiers = new LinkedList<>();
	}
	this.customVerifiers.add(verifier);
	return this;
    }
}