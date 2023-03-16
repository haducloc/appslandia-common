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
import java.security.spec.AlgorithmParameterSpec;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

import com.appslandia.common.json.JsonProcessor;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DsaJwtSigner extends DsaJwsSigner<JwtPayload> {

    protected String issuer;
    protected Set<String> audiences;
    protected int leewaySec;

    public DsaJwtSigner(String jwsAlgorithm, String signatureAlgorithm) {
	super(jwsAlgorithm, signatureAlgorithm, JwtPayload.class);
    }

    @Override
    public DsaJwtSigner setSignAlgProvider(String signAlgProvider) {
	super.setSignAlgProvider(signAlgProvider);
	return this;
    }

    public DsaJwtSigner setAlgParamSpec(Function<String, AlgorithmParameterSpec> algParamSpec) {
	super.setAlgParamSpec(algParamSpec);
	return this;
    }

    @Override
    public DsaJwtSigner setJsonProcessor(JsonProcessor jsonProcessor) {
	super.setJsonProcessor(jsonProcessor);
	return this;
    }

    @Override
    public DsaJwtSigner setPrivateKey(PrivateKey key) {
	super.setPrivateKey(key);
	return this;
    }

    @Override
    public DsaJwtSigner setPublicKey(PublicKey key) {
	super.setPublicKey(key);
	return this;
    }

    @Override
    public DsaJwtSigner setKid(String kid) {
	super.setKid(kid);
	return this;
    }

    public DsaJwtSigner setIssuer(String issuer) {
	this.issuer = issuer;
	return this;
    }

    public DsaJwtSigner addAudience(String audience) {
	if (this.audiences == null) {
	    this.audiences = new LinkedHashSet<>();
	}
	this.audiences.add(audience);
	return this;
    }

    public DsaJwtSigner setLeewaySec(int leewaySec) {
	this.leewaySec = leewaySec;
	return this;
    }

    @Override
    public JwtSigner build() {
	Asserts.notNull(this.jsonProcessor);
	JwtSigner impl = new JwtSigner().setJsonProcessor(this.jsonProcessor).setSigner(this.signer).setAlg(this.alg).setKid(this.kid).setLeewaySec(this.leewaySec)
		.setIssuer(this.issuer);

	impl.audiences = this.audiences;
	return impl.initialize();
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

    public static DsaJwtSigner PS256() {
	return new DsaJwtSigner("PS256", "SHA256withRSA/PSS").setAlgParamSpec(DsaJwsSigner::toPSSParameterSpec);
    }

    public static DsaJwtSigner PS384() {
	return new DsaJwtSigner("PS384", "SHA384withRSA/PSS").setAlgParamSpec(DsaJwsSigner::toPSSParameterSpec);
    }

    public static DsaJwtSigner PS512() {
	return new DsaJwtSigner("PS512", "SHA512withRSA/PSS").setAlgParamSpec(DsaJwsSigner::toPSSParameterSpec);
    }
}
