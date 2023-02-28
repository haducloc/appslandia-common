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

package com.appslandia.common.jwt;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.DestroyException;
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
    private String alg;
    protected String kid;
    protected String issuer;

    private Digester signer;

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.type, "type is required.");

	if (this.signer != null) {
	    Asserts.notNull(this.alg, "alg is required.");
	} else {
	    this.alg = JWT_NONE_ALG;
	}

	Asserts.notNull(this.jsonProcessor, "jsonProcessor is required.");
    }

    @Override
    public void destroy() throws DestroyException {
	if (this.jsonProcessor != null) {
	    this.jsonProcessor.destroy();
	}
    }

    public JwtHeader newHeader() {
	this.initialize();
	JwtHeader header = new JwtHeader().setType(this.type).setAlgorithm(this.alg);

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
	return payload;
    }

    public String toJwt(JwtToken jwt) throws CryptoException, JsonException {
	this.initialize();
	Asserts.notNull(jwt);
	Asserts.notNull(jwt.getHeader());
	Asserts.notNull(jwt.getPayload());

	// Verify fields
	verifyFields(jwt);

	String header = BaseEncoder.BASE64_URL.encode(this.jsonProcessor.toByteArray(jwt.getHeader()));
	String payload = BaseEncoder.BASE64_URL.encode(this.jsonProcessor.toByteArray(jwt.getPayload()));

	// No ALG
	if (this.signer == null) {
	    return JwtUtils.toJwt(header, payload, "");
	}

	String dataToSign = JwtUtils.toData(header, payload);

	// Signature
	byte[] sig = this.signer.digest(dataToSign.getBytes(StandardCharsets.UTF_8));

	return JwtUtils.toJwt(header, payload, BaseEncoder.BASE64_URL.encode(sig));
    }

    public JwtToken verifyJwt(JwtToken jwt) throws CryptoException, JwtException {
	this.initialize();
	Asserts.notNull(jwt);
	Asserts.notNull(jwt.getHeader());
	Asserts.notNull(jwt.getPayload());

	Asserts.notNull(jwt.getHeaderPart());
	Asserts.notNull(jwt.getPayloadPart());
	Asserts.notNull(jwt.getSignaturePart());

	// Verify fields
	verifyFields(jwt);

	// Verify Signature
	if (jwt.getSignaturePart().isBlank()) {
	    if (this.signer != null) {
		throw new JwtException("signature is required.");
	    }
	} else {
	    if (this.signer == null) {
		throw new JwtException("signer is required.");
	    }
	    String dataToSign = JwtUtils.toData(jwt.getHeaderPart(), jwt.getPayloadPart());

	    if (!this.signer.verify(dataToSign.getBytes(StandardCharsets.UTF_8), BaseEncoder.BASE64_URL.decode(jwt.getSignaturePart()))) {
		throw new JwtException("JWT signature verification failed.");
	    }
	}
	return jwt;
    }

    public JwtToken parseJwt(String jwt) throws JsonException {
	this.initialize();
	Asserts.notNull(jwt);

	String[] parts = JwtUtils.parseParts(jwt);
	Asserts.notNull(parts, () -> STR.fmt("The jwt '{}' is invalid format.", jwt));

	// Header
	String headerJson = new String(BaseEncoder.BASE64_URL.decode(parts[0]), StandardCharsets.UTF_8);
	JwtHeader header = this.jsonProcessor.read(new StringReader(headerJson), JwtHeader.class);

	// PAYLOAD
	String payloadJson = new String(BaseEncoder.BASE64_URL.decode(parts[1]), StandardCharsets.UTF_8);
	JwtPayload payload = this.jsonProcessor.read(new StringReader(payloadJson), JwtPayload.class);

	return new JwtToken(header, payload, parts[0], parts[1], parts[2]);
    }

    protected void verifyFields(JwtToken jwt) throws JwtException {
	// typ
	if (!Objects.equals(this.type, jwt.getHeader().getType())) {
	    throw new JwtException("type doesn't match.");
	}

	// alg
	if (!Objects.equals(this.alg, jwt.getHeader().getAlgorithm())) {
	    throw new JwtException("algorithm doesn't match.");
	}

	// kid
	if (!Objects.equals(this.kid, jwt.getHeader().getKid())) {
	    throw new JwtException("kid doesn't match.");
	}

	// iss
	if (!Objects.equals(this.issuer, jwt.getPayload().getIssuer())) {
	    throw new JwtException("issuer doesn't match.");
	}
    }

    public JwtSigner setJsonProcessor(JsonProcessor jsonProcessor) {
	assertNotInitialized();
	this.jsonProcessor = jsonProcessor;
	return this;
    }

    public JwtSigner setSigner(MacDigester signer) {
	assertNotInitialized();
	this.signer = signer;
	if (signer != null) {
	    this.alg = parseJwtAlg(signer.getAlgorithm());
	}
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

    static String parseJwtAlg(String macAlg) {
	switch (macAlg) {
	case "HmacSHA256":
	    return "HS256";

	case "HmacSHA384":
	    return "HS384";

	case "HmacSHA512":
	    return "HS512";
	default:
	    return null;
	}
    }
}
