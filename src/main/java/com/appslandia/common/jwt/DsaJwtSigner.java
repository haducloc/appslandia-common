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

import java.security.PrivateKey;
import java.security.PublicKey;

import com.appslandia.common.crypto.DsaDigester;
import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DsaJwtSigner {

    protected JsonProcessor jsonProcessor;

    protected String alg;
    protected String kid;
    protected String issuer;

    protected DsaDigester signer;

    public DsaJwtSigner(String jwtAlgorithm, String dsaAlgorithm) {
	this.alg = jwtAlgorithm;
	this.signer = new DsaDigester().setAlgorithm(dsaAlgorithm);
    }

    public DsaJwtSigner setProvider(String provider) {
	this.signer.setProvider(provider);
	return this;
    }

    public DsaJwtSigner setPrivateKey(PrivateKey key) {
	this.signer.setPrivateKey(key);
	return this;
    }

    public DsaJwtSigner setPublicKey(PublicKey key) {
	this.signer.setPublicKey(key);
	return this;
    }

    public DsaJwtSigner setJsonProcessor(JsonProcessor jsonProcessor) {
	this.jsonProcessor = jsonProcessor;
	return this;
    }

    public DsaJwtSigner setKid(String kid) {
	this.kid = kid;
	return this;
    }

    public DsaJwtSigner setIssuer(String issuer) {
	this.issuer = issuer;
	return this;
    }

    public JwtSigner build() {
	Asserts.notNull(this.jsonProcessor);
	return new JwtSigner().setJsonProcessor(this.jsonProcessor).setSigner(this.signer).setAlg(this.alg).setKid(this.kid).setIssuer(this.issuer).initialize();
    }

    public static DsaJwtSigner ES256() {
	return new DsaJwtSigner("ES256", "SHA256WithECDSAInP1363Format");
    }

    public static DsaJwtSigner ES384() {
	return new DsaJwtSigner("ES384", "SHA384WithECDSAInP1363Format");
    }

    public static DsaJwtSigner ES512() {
	return new DsaJwtSigner("ES512", "SHA512withECDSAinP1363Format");
    }

    public static DsaJwtSigner RS256() {
	return new DsaJwtSigner("RS256", "SHA256withRSA");
    }

    public static DsaJwtSigner RS384() {
	return new DsaJwtSigner("RS384", "SHA384withRSA");
    }

    public static DsaJwtSigner RS512() {
	return new DsaJwtSigner("RS512", "SHA512withRSA");
    }
}
