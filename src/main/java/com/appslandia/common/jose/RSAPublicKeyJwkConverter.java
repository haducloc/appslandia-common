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
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.crypto.CryptoUtils;
import com.appslandia.common.crypto.KeyFactoryUtil;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RSAPublicKeyJwkConverter extends JwkConverter<RSAPublicKey> implements Cloneable {

    private String rsaKeyFactoryProvider;
    private KeyFactoryUtil rsaKeyFactoryUtil;

    public RSAPublicKeyJwkConverter() {
	super("RSA");
    }

    @Override
    protected void init() throws Exception {
	this.rsaKeyFactoryUtil = new KeyFactoryUtil("RSA", this.rsaKeyFactoryProvider);
    }

    @Override
    public Map<String, Object> toJsonWebKey(RSAPublicKey key) {
	this.initialize();
	Asserts.isTrue("RSA".equals(key.getAlgorithm()));

	// jwk
	Map<String, Object> jwk = new LinkedHashMap<>();
	jwk.put("kty", this.kty);

	byte[] nBytes = CryptoUtils.stripLeadingZeros(key.getModulus().toByteArray());
	byte[] eBytes = CryptoUtils.stripLeadingZeros(key.getPublicExponent().toByteArray());

	jwk.put("n", JoseUtils.getJoseBase64().encode(nBytes));
	jwk.put("e", JoseUtils.getJoseBase64().encode(eBytes));

	return jwk;
    }

    @Override
    public RSAPublicKey fromJsonWebKey(Map<String, Object> key) throws CryptoException {
	this.initialize();

	String kty = Asserts.notNull((String) key.get("kty"), "kty is required.");
	Asserts.isTrue(this.kty.equals(kty));

	String n = Asserts.notNull((String) key.get("n"), "n is required.");
	String e = Asserts.notNull((String) key.get("e"), "e is required.");

	byte[] nBytes = JoseUtils.getJoseBase64().decode(n);
	byte[] eBytes = JoseUtils.getJoseBase64().decode(e);

	RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes), new BigInteger(1, eBytes));
	PublicKey publicKey = this.rsaKeyFactoryUtil.toPublicKey(keySpec);

	return (RSAPublicKey) publicKey;
    }

    public RSAPublicKeyJwkConverter setRsaKeyFactoryProvider(String rsaKeyFactoryProvider) {
	assertNotInitialized();
	this.rsaKeyFactoryProvider = rsaKeyFactoryProvider;
	return this;
    }

    @Override
    public RSAPublicKeyJwkConverter clone() {
	return new RSAPublicKeyJwkConverter().setRsaKeyFactoryProvider(this.rsaKeyFactoryProvider);
    }
}
