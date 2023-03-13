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

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.appslandia.common.crypto.AlgorithmParametersUtil;
import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.crypto.KeyFactoryUtil;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class EcPublicKeyJwkConverter extends JwkConverter<ECPublicKey> implements Cloneable {

    private String ecAlgParamsProvider;
    private String ecKeyFactoryProvider;

    final ConcurrentMap<String, AlgorithmParametersUtil<ECParameterSpec>> ecAlgorithmParametersUtils = new ConcurrentHashMap<>();
    private KeyFactoryUtil ecKeyFactoryUtil;

    public EcPublicKeyJwkConverter() {
	super("EC");
    }

    @Override
    protected void init() throws Exception {
	this.ecKeyFactoryUtil = new KeyFactoryUtil("EC", this.ecKeyFactoryProvider);
    }

    @Override
    public Map<String, Object> toJsonWebKey(ECPublicKey key) {
	this.initialize();
	Asserts.isTrue("EC".equals(key.getAlgorithm()));

	// jwk
	Map<String, Object> jwk = new LinkedHashMap<>();
	jwk.put("kty", this.kty);

	// curve
	String curve = getCurve(key.getParams().getCurve().getField().getFieldSize());
	jwk.put("crv", curve);

	// ecPoint
	ECPoint ecPoint = key.getW();

	byte[] xBytes = ecPoint.getAffineX().toByteArray();
	byte[] yBytes = ecPoint.getAffineY().toByteArray();

	jwk.put("x", JoseUtils.getJoseBase64().encode(xBytes));
	jwk.put("y", JoseUtils.getJoseBase64().encode(yBytes));
	return jwk;
    }

    @Override
    public ECPublicKey fromJsonWebKey(Map<String, Object> key) throws CryptoException {
	this.initialize();

	String kty = Asserts.notNull((String) key.get("kty"), "kty is required.");
	Asserts.isTrue(this.kty.equals(kty));

	String curve = Asserts.notNull((String) key.get("crv"), "crv is required.");
	String stdName = getStdName(curve);

	String x = Asserts.notNull((String) key.get("x"), "x is required.");
	String y = Asserts.notNull((String) key.get("y"), "y is required.");

	// algorithmParametersUtil
	AlgorithmParametersUtil<ECParameterSpec> algorithmParametersUtil = this.ecAlgorithmParametersUtils.computeIfAbsent(stdName, (name) -> {

	    AlgorithmParametersUtil<ECParameterSpec> impl = new AlgorithmParametersUtil<>("EC", this.ecAlgParamsProvider);
	    impl.setParamSpecClass(ECParameterSpec.class);
	    impl.setInitAlgParamSpec(new ECGenParameterSpec(name));

	    return impl;
	});

	// algorithmParametersUtil
	ECParameterSpec ecSpec = algorithmParametersUtil.getParameterSpec();

	// ecPoint
	byte[] xBytes = JoseUtils.getJoseBase64().decode(x);
	byte[] yBytes = JoseUtils.getJoseBase64().decode(y);

	ECPoint ecPoint = new ECPoint(new BigInteger(1, xBytes), new BigInteger(1, yBytes));
	ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(ecPoint, ecSpec);

	PublicKey pk = this.ecKeyFactoryUtil.toPublicKey(pubKeySpec);
	return (ECPublicKey) pk;
    }

    public EcPublicKeyJwkConverter setEcAlgParamsProvider(String ecAlgParamsProvider) {
	assertNotInitialized();
	this.ecAlgParamsProvider = ecAlgParamsProvider;
	return this;
    }

    public EcPublicKeyJwkConverter setEcKeyFactoryProvider(String ecKeyFactoryProvider) {
	assertNotInitialized();
	this.ecKeyFactoryProvider = ecKeyFactoryProvider;
	return this;
    }

    @Override
    public EcPublicKeyJwkConverter clone() {
	return new EcPublicKeyJwkConverter().setEcAlgParamsProvider(this.ecAlgParamsProvider).setEcKeyFactoryProvider(this.ecKeyFactoryProvider);
    }

    protected String getCurve(int curveFieldLength) {
	switch (curveFieldLength) {
	case 256:
	    return "P-256";
	case 384:
	    return "P-384";
	case 521:
	    return "P-521";
	default:
	    break;
	}
	throw new IllegalArgumentException(STR.fmt("Unsupported curve field length: {}", curveFieldLength));
    }

    protected String getStdName(String curve) {
	switch (curve) {
	case "P-256":
	    return "secp256r1";
	case "P-384":
	    return "secp384r1";
	case "P-521":
	    return "secp521r1";
	default:
	    break;
	}
	throw new IllegalArgumentException(STR.fmt("Unsupported curve: {}", curve));
    }
}
