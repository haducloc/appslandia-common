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

import java.nio.charset.StandardCharsets;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.crypto.Digester;
import com.appslandia.common.crypto.DsaDigester;
import com.appslandia.common.crypto.MacDigester;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtSigner extends InitializeObject {

    public static final JwtSigner NONE = new JwtSigner().setAlg("none");

    private String alg;
    private Digester signer;

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.alg, "alg is required.");

	if (this != NONE) {
	    Asserts.notNull(this.signer, "signer is required.");
	}
    }

    public String sign(String header, String payload) throws CryptoException {
	this.initialize();
	Asserts.notNull(header, "header is required.");
	Asserts.notNull(payload, "payload is required.");

	// No ALG
	if (this == NONE) {
	    return JwtUtils.toJwt(header, payload, "");
	}

	String dataToSign = JwtUtils.toData(header, payload);

	// Signature
	byte[] sig = this.signer.digest(dataToSign.getBytes(StandardCharsets.UTF_8));

	return JwtUtils.toJwt(header, payload, BaseEncoder.BASE64_URL.encode(sig));
    }

    public boolean verify(String header, String payload, String signature) throws CryptoException {
	this.initialize();
	Asserts.notNull(header, "header is required.");
	Asserts.notNull(payload, "payload is required.");
	Asserts.notNull(signature, "signature is required.");

	if (this == NONE) {
	    return signature.length() == 0;
	}

	String dataToSign = JwtUtils.toData(header, payload);
	return this.signer.verify(dataToSign.getBytes(StandardCharsets.UTF_8), BaseEncoder.BASE64_URL.decode(signature));
    }

    public String getAlg() {
	this.initialize();
	return this.alg;
    }

    public JwtSigner setAlg(String alg) {
	assertNotInitialized();
	this.alg = alg;
	return this;
    }

    public Digester getSigner() {
	this.initialize();
	return this.signer;
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

    public JwtSigner copy() {
	if (this == NONE) {
	    return NONE;
	}
	JwtSigner impl = new JwtSigner();
	impl.alg = this.alg;
	if (this.signer != null) {
	    impl.signer = this.signer.copy();
	}
	return impl;
    }
}
