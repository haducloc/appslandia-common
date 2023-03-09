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
public class DsaJwsSigner<P> {

    protected Class<P> payloadClass;
    protected JsonProcessor jsonProcessor;

    protected String alg;
    protected String kid;

    protected DsaDigester signer;

    public DsaJwsSigner(String jwsAlgorithm, String dsaAlgorithm, Class<P> payloadClass) {
	this.alg = jwsAlgorithm;
	this.signer = new DsaDigester().setAlgorithm(dsaAlgorithm);
	this.payloadClass = payloadClass;
    }

    public DsaJwsSigner<P> setProvider(String provider) {
	this.signer.setProvider(provider);
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

    public DsaJwsSigner<P> setJsonProcessor(JsonProcessor jsonProcessor) {
	this.jsonProcessor = jsonProcessor;
	return this;
    }

    public DsaJwsSigner<P> setKid(String kid) {
	this.kid = kid;
	return this;
    }

    public JwsSigner<P> build() {
	Asserts.notNull(this.jsonProcessor);
	return new JwsSigner<>(this.payloadClass).setJsonProcessor(this.jsonProcessor).setSigner(this.signer).setAlg(this.alg).setKid(this.kid).initialize();
    }

    public static <P> DsaJwsSigner<P> ES256(Class<P> payloadClass) {
	return new DsaJwsSigner<P>("ES256", "SHA256WithECDSAInP1363Format", payloadClass);
    }

    public static <P> DsaJwsSigner<P> ES384(Class<P> payloadClass) {
	return new DsaJwsSigner<P>("ES384", "SHA384WithECDSAInP1363Format", payloadClass);
    }

    public static <P> DsaJwsSigner<P> ES512(Class<P> payloadClass) {
	return new DsaJwsSigner<P>("ES512", "SHA512withECDSAinP1363Format", payloadClass);
    }

    public static <P> DsaJwsSigner<P> RS256(Class<P> payloadClass) {
	return new DsaJwsSigner<P>("RS256", "SHA256withRSA", payloadClass);
    }

    public static <P> DsaJwsSigner<P> RS384(Class<P> payloadClass) {
	return new DsaJwsSigner<P>("RS384", "SHA384withRSA", payloadClass);
    }

    public static <P> DsaJwsSigner<P> RS512(Class<P> payloadClass) {
	return new DsaJwsSigner<P>("RS512", "SHA512withRSA", payloadClass);
    }
}
