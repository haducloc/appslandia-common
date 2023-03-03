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

import com.appslandia.common.crypto.MacDigester;
import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class HsJwtSigner {

    protected JsonProcessor jsonProcessor;

    protected String alg;
    protected String kid;
    protected String issuer;

    protected MacDigester signer;

    public HsJwtSigner(String jwtAlgorithm, String macAlgorithm) {
	this.alg = jwtAlgorithm;
	this.signer = new MacDigester().setAlgorithm(macAlgorithm);
    }

    public HsJwtSigner setProvider(String provider) {
	this.signer.setProvider(provider);
	return this;
    }

    public HsJwtSigner setSecret(byte[] secret) {
	this.signer.setSecret(secret);
	return this;
    }

    public HsJwtSigner setSecret(String secretOrEnv) {
	this.signer.setSecret(secretOrEnv);
	return this;
    }

    public HsJwtSigner setJsonProcessor(JsonProcessor jsonProcessor) {
	this.jsonProcessor = jsonProcessor;
	return this;
    }

    public HsJwtSigner setKid(String kid) {
	this.kid = kid;
	return this;
    }

    public HsJwtSigner setIssuer(String issuer) {
	this.issuer = issuer;
	return this;
    }

    public JwtSigner build() {
	Asserts.notNull(this.jsonProcessor);
	return new JwtSigner().setJsonProcessor(this.jsonProcessor).setSigner(this.signer).setAlg(this.alg).setKid(this.kid).setIssuer(this.issuer).initialize();
    }

    public static HsJwtSigner HS256() {
	return new HsJwtSigner("HS256", "HmacSHA256");
    }

    public static HsJwtSigner HS384() {
	return new HsJwtSigner("HS384", "HmacSHA384");
    }

    public static HsJwtSigner HS512() {
	return new HsJwtSigner("HS512", "HmacSHA512");
    }
}
