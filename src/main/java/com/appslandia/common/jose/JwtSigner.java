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

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.appslandia.common.base.InitializeException;
import com.appslandia.common.crypto.MacSigner;
import com.appslandia.common.crypto.SignatureSigner;
import com.appslandia.common.json.JsonException;
import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtSigner extends JwsSigner<JwtPayload> {

    protected String issuer;
    protected Set<String> audiences;
    protected int leewaySec;

    public JwtSigner() {
	super(JwtPayload.class);
    }

    @Override
    protected void init() throws Exception {
	super.init();

	Asserts.isTrue(this.leewaySec >= 0);

	// issuer
	this.defaultVerifiers.add((token) -> {
	    if (!Objects.equals(this.issuer, token.getPayload().getIss())) {
		throw new JoseVerificationException("issuer doesn't match.");
	    }
	});

	// exp
	this.defaultVerifiers.add((token) -> {
	    Date dt = token.getPayload().getExp();
	    if (dt != null) {
		long nt = JoseUtils.toNumericDate(dt);

		if (!JoseUtils.isFutureTime(nt, this.leewaySec)) {
		    throw new JoseVerificationException("token is expired.");
		}
	    }
	});

	// iat
	this.defaultVerifiers.add((token) -> {
	    Date dt = token.getPayload().getIat();
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

    public JwtPayload newPayload() {
	this.initialize();
	JwtPayload payload = new JwtPayload();

	if (this.issuer != null) {
	    payload.setIss(this.issuer);
	}
	if (this.audiences != null) {
	    payload.setAud(this.audiences.toArray(new String[this.audiences.size()]));
	}
	return payload;
    }

    @Override
    public JwtToken parse(String token) throws JsonException {
	JwsToken<JwtPayload> jwsToken = super.parse(token);
	return new JwtToken(jwsToken.header, jwsToken.payload, jwsToken.headerPart, jwsToken.payloadPart, jwsToken.signaturePart);
    }

    @Override
    public JwtSigner setJsonProcessor(JsonProcessor jsonProcessor) {
	super.setJsonProcessor(jsonProcessor);
	return this;
    }

    @Override
    public JwtSigner setSigner(SignatureSigner signer) {
	super.setSigner(signer);
	return this;
    }

    @Override
    public JwtSigner setSigner(MacSigner signer) {
	super.setSigner(signer);
	return this;
    }

    @Override
    public JwtSigner setAlg(String alg) {
	super.setAlg(alg);
	return this;
    }

    @Override
    public JwtSigner setKid(String kid) {
	super.setKid(kid);
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
}
