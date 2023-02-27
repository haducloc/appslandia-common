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
import com.appslandia.common.json.JsonException;
import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtProcessor extends InitializeObject {

    protected JsonProcessor jsonProcessor;
    protected JwtSigner jwtSigner;

    protected String type = "JWT";
    protected String issuer;
    protected String kid;

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.type, "type is required.");
	Asserts.notNull(this.jsonProcessor, "jsonProcessor is required.");

	this.jwtSigner = ValueUtils.valueOrAlt(this.jwtSigner, JwtSigner.NONE);
    }

    @Override
    public void destroy() throws DestroyException {
	if (this.jsonProcessor != null) {
	    this.jsonProcessor.destroy();
	}
	if (this.jwtSigner != null) {
	    this.jwtSigner.destroy();
	}
    }

    public JwtHeader newHeader() {
	this.initialize();
	JwtHeader header = new JwtHeader().setType(this.type).setAlgorithm(this.jwtSigner.getAlg());

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

	String header = BaseEncoder.BASE64_URL.encode(this.jsonProcessor.toByteArray(jwt.getHeader()));
	String payload = BaseEncoder.BASE64_URL.encode(this.jsonProcessor.toByteArray(jwt.getPayload()));

	return this.jwtSigner.sign(header, payload);
    }

    public JwtToken verifyJwt(String jwt) throws CryptoException, JsonException, JwtException {
	this.initialize();
	Asserts.notNull(jwt);

	String[] parts = JwtUtils.parseParts(jwt);
	Asserts.notNull(parts, () -> STR.fmt("The jwt '{}' is invalid format.", jwt));

	// Verify Signature
	if (parts[2] == null) {
	    if (this.jwtSigner != JwtSigner.NONE) {
		throw new JwtException("JWT signature verification failed. jwtSigner must be JwtSigner.NONE.");
	    }
	} else {
	    if (this.jwtSigner == JwtSigner.NONE) {
		throw new JwtException("JWT signature verification failed. jwtSigner must be not JwtSigner.NONE.");
	    }

	    if (!this.jwtSigner.verify(parts[0], parts[1], parts[2])) {
		throw new JwtException("JWT signature verification failed.");
	    }
	}
	JwtToken token = doParseJwt(parts);

	if (!Objects.equals(this.type, token.getHeader().getType())) {
	    throw new JwtException("JWT verification failed. typ didn't match.");
	}
	if (!Objects.equals(this.kid, token.getHeader().getKid())) {
	    throw new JwtException("JWT verification failed. kid didn't match.");
	}
	if (!Objects.equals(this.issuer, token.getPayload().getIssuer())) {
	    throw new JwtException("JWT verification failed. iss didn't match.");
	}
	return token;
    }

    public JwtToken parseJwt(String jwt) throws JsonException {
	this.initialize();

	Asserts.notNull(jwt);

	String[] parts = JwtUtils.parseParts(jwt);
	Asserts.notNull(parts, () -> STR.fmt("The jwt '{}' is invalid format.", jwt));

	return doParseJwt(parts);
    }

    protected JwtToken doParseJwt(String[] jwtParts) throws JsonException {
	// Header
	String headerJson = new String(BaseEncoder.BASE64_URL.decode(jwtParts[0]), StandardCharsets.UTF_8);
	JwtHeader header = this.jsonProcessor.read(new StringReader(headerJson), JwtHeader.class);

	// PAYLOAD
	String payloadJson = new String(BaseEncoder.BASE64_URL.decode(jwtParts[1]), StandardCharsets.UTF_8);
	JwtPayload payload = this.jsonProcessor.read(new StringReader(payloadJson), JwtPayload.class);

	return new JwtToken(header, payload, jwtParts[0], jwtParts[1], jwtParts[2]);
    }

    public JsonProcessor getJsonProcessor() {
	this.initialize();
	return this.jsonProcessor;
    }

    public JwtProcessor setJsonProcessor(JsonProcessor jsonProcessor) {
	assertNotInitialized();
	this.jsonProcessor = jsonProcessor;
	return this;
    }

    public JwtSigner getJwtSigner() {
	this.initialize();
	return this.jwtSigner;
    }

    public JwtProcessor setJwtSigner(JwtSigner jwtSigner) {
	assertNotInitialized();
	this.jwtSigner = jwtSigner;
	return this;
    }

    public JwtProcessor setIssuer(String issuer) {
	assertNotInitialized();
	this.issuer = issuer;
	return this;
    }

    public JwtProcessor setKid(String kid) {
	assertNotInitialized();
	this.kid = kid;
	return this;
    }
}
