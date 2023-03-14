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

package com.appslandia.common.crypto;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SignatureSigner extends InitializeObject implements Digester {
    private String algorithm, provider;

    private Signature sign;
    private Signature ver;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    final Object sigMutex = new Object();
    final Object verMutex = new Object();

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.algorithm, "algorithm is required.");
	Asserts.isTrue((this.privateKey != null) || (this.publicKey != null), "No key is provided.");

	// Sign
	if (this.privateKey != null) {
	    if (this.provider == null) {
		this.sign = Signature.getInstance(this.algorithm);
	    } else {
		this.sign = Signature.getInstance(this.algorithm, this.provider);
	    }
	    this.sign.initSign(this.privateKey);
	}

	// Verify
	if (this.publicKey != null) {
	    if (this.provider == null) {
		this.ver = Signature.getInstance(this.algorithm);
	    } else {
		this.ver = Signature.getInstance(this.algorithm, this.provider);
	    }
	    this.ver.initVerify(this.publicKey);
	}
    }

    @Override
    public void destroy() throws DestroyException {
	CryptoUtils.destroyQuietly(this.privateKey);
    }

    @Override
    public byte[] digest(byte[] message) throws CryptoException {
	this.initialize();
	Asserts.notNull(message, "message is required.");
	Asserts.notNull(this.sign, "privateKey is required.");

	try {
	    synchronized (this.sigMutex) {
		this.sign.update(message);
		return this.sign.sign();
	    }
	} catch (GeneralSecurityException ex) {
	    throw new CryptoException(ex);
	}
    }

    @Override
    public boolean verify(byte[] message, byte[] signature) throws CryptoException {
	this.initialize();
	Asserts.notNull(message, "message is required.");
	Asserts.notNull(signature, "signature is required.");
	Asserts.notNull(this.ver, "publicKey is required.");

	try {
	    synchronized (this.verMutex) {
		this.ver.update(message);
		return this.ver.verify(signature);
	    }
	} catch (GeneralSecurityException ex) {
	    throw new CryptoException(ex);
	}
    }

    public String getAlgorithm() {
	this.initialize();
	return this.algorithm;
    }

    public SignatureSigner setAlgorithm(String algorithm) {
	assertNotInitialized();
	this.algorithm = algorithm;
	return this;
    }

    public String getProvider() {
	this.initialize();
	return this.provider;
    }

    public SignatureSigner setProvider(String provider) {
	assertNotInitialized();
	this.provider = provider;
	return this;
    }

    public SignatureSigner setPrivateKey(PrivateKey privateKey) {
	assertNotInitialized();
	if (privateKey != null) {
	    this.privateKey = new KeyFactoryUtil(privateKey.getAlgorithm()).copy(privateKey);
	}
	return this;
    }

    public SignatureSigner setPublicKey(PublicKey publicKey) {
	assertNotInitialized();
	if (publicKey != null) {
	    this.publicKey = new KeyFactoryUtil(publicKey.getAlgorithm()).copy(publicKey);
	}
	return this;
    }

    public SignatureSigner setKeyPair(KeyPair keyPair) {
	assertNotInitialized();
	if (keyPair != null) {
	    KeyFactoryUtil keyFactoryUtil = new KeyFactoryUtil(keyPair.getPrivate().getAlgorithm());

	    this.privateKey = keyFactoryUtil.copy(keyPair.getPrivate());
	    this.publicKey = keyFactoryUtil.copy(keyPair.getPublic());
	}
	return this;
    }

    @Override
    public SignatureSigner clone() {
	SignatureSigner impl = new SignatureSigner().setAlgorithm(this.algorithm).setProvider(this.provider);

	if (this.privateKey != null) {
	    impl.privateKey = new KeyFactoryUtil(this.privateKey.getAlgorithm()).copy(this.privateKey);
	}
	if (this.publicKey != null) {
	    impl.publicKey = new KeyFactoryUtil(this.publicKey.getAlgorithm()).copy(this.publicKey);
	}
	return impl;
    }
}
