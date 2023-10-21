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
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.util.Locale;
import java.util.Random;
import java.util.function.Function;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RsaEncryptor extends InitializeObject implements Encryptor {
    private String transformation, provider;
    private String[] algorithms;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    private Cipher encrypt;
    private Cipher decrypt;

    final Object encMutex = new Object();
    final Object decMutex = new Object();
    final Random random = new SecureRandom();

    private Function<String[], AlgorithmParameterSpec> algParamSpec;

    @Override
    protected void init() throws Exception {
	// transformation
	Asserts.notNull(this.transformation, "transformation is required.");

	this.algorithms = this.transformation.split("/");
	Asserts.isTrue(this.algorithms.length == 3, "transformation is invalid.");

	this.algorithms[0] = this.algorithms[0].toUpperCase(Locale.ENGLISH);
	this.algorithms[1] = this.algorithms[1].toUpperCase(Locale.ENGLISH);

	Asserts.isTrue("RSA".equals(this.algorithms[0]), "RSA algorithm is required.");
	Asserts.isTrue((this.privateKey != null) || (this.publicKey != null), "No key is provided.");

	// algParamSpec
	if (this.algParamSpec == null) {
	    this.algParamSpec = (algs) -> null;
	}

	// ENCRYPT
	if (this.publicKey != null) {
	    if (this.provider == null) {
		this.encrypt = Cipher.getInstance(this.transformation);
	    } else {
		this.encrypt = Cipher.getInstance(this.transformation, this.provider);
	    }

	    AlgorithmParameterSpec spec = this.algParamSpec.apply(this.algorithms);

	    if (spec == null) {
		this.encrypt.init(Cipher.ENCRYPT_MODE, this.publicKey);
	    } else {
		this.encrypt.init(Cipher.ENCRYPT_MODE, this.publicKey, spec);
	    }
	}

	// DECRYPT
	if (this.privateKey != null) {
	    if (this.provider == null) {
		this.decrypt = Cipher.getInstance(this.transformation);
	    } else {
		this.decrypt = Cipher.getInstance(this.transformation, this.provider);
	    }

	    AlgorithmParameterSpec spec = this.algParamSpec.apply(this.algorithms);

	    if (spec == null) {
		this.decrypt.init(Cipher.DECRYPT_MODE, this.privateKey, spec);
	    } else {
		this.decrypt.init(Cipher.DECRYPT_MODE, this.privateKey, spec);
	    }
	}
    }

    @Override
    public void destroy() throws DestroyException {
	if (this.privateKey != null) {
	    CryptoUtils.destroyQuietly(this.privateKey);
	}
    }

    @Override
    public byte[] encrypt(byte[] message) throws CryptoException {
	this.initialize();
	Asserts.notNull(message, "message is required.");
	Asserts.notNull(this.encrypt, "publicKey is required.");

	try {
	    synchronized (this.encMutex) {
		return this.encrypt.doFinal(message);
	    }
	} catch (GeneralSecurityException ex) {
	    throw new CryptoException(ex);
	}
    }

    @Override
    public byte[] decrypt(byte[] message) throws CryptoException {
	this.initialize();
	Asserts.notNull(message, "message is required.");
	Asserts.notNull(this.decrypt, "privateKey is required.");

	try {
	    synchronized (this.decMutex) {
		return this.decrypt.doFinal(message);
	    }
	} catch (GeneralSecurityException ex) {
	    throw new CryptoException(ex);
	}
    }

    public String getTransformation() {
	this.initialize();
	return this.transformation;
    }

    public RsaEncryptor setTransformation(String transformation) {
	this.assertNotInitialized();
	this.transformation = transformation;
	return this;
    }

    public String getProvider() {
	this.initialize();
	return this.provider;
    }

    public RsaEncryptor setProvider(String provider) {
	this.assertNotInitialized();
	this.provider = provider;
	return this;
    }

    public RsaEncryptor setPrivateKey(PrivateKey privateKey) {
	assertNotInitialized();
	if (privateKey != null) {
	    this.privateKey = new KeyFactoryUtil(privateKey.getAlgorithm()).copy(privateKey);
	}
	return this;
    }

    public RsaEncryptor setPrivateKey(String privateKeyPem) {
	assertNotInitialized();
	if (privateKeyPem != null) {
	    this.privateKey = new KeyFactoryUtil("RSA").toPrivateKey(privateKeyPem);
	}
	return this;
    }

    public RsaEncryptor setPublicKey(PublicKey publicKey) {
	assertNotInitialized();
	if (publicKey != null) {
	    this.publicKey = new KeyFactoryUtil(publicKey.getAlgorithm()).copy(publicKey);
	}
	return this;
    }

    public RsaEncryptor setPublicKey(String publicKeyPem) {
	assertNotInitialized();
	if (publicKeyPem != null) {
	    this.publicKey = new KeyFactoryUtil("RSA").toPublicKey(publicKeyPem);
	}
	return this;
    }

    public RsaEncryptor setKeyPair(KeyPair keyPair) {
	assertNotInitialized();
	if (keyPair != null) {
	    Asserts.isTrue("RSA".equals(keyPair.getPrivate().getAlgorithm()));

	    KeyFactoryUtil keyFactoryUtil = new KeyFactoryUtil("RSA");

	    this.privateKey = keyFactoryUtil.copy(keyPair.getPrivate());
	    this.publicKey = keyFactoryUtil.copy(keyPair.getPublic());
	}
	return this;
    }

    public RsaEncryptor setAlgParamSpec(Function<String[], AlgorithmParameterSpec> algParamSpec) {
	assertNotInitialized();
	this.algParamSpec = algParamSpec;
	return this;
    }

    // Optimal Asymmetric Encryption
    // OAEPPadding, OAEPWith<digest>And<mgf>Padding

    public static OAEPParameterSpec toOAEPParameterSpec(String[] algs) {
	String padding = algs[2];
	if (padding.equalsIgnoreCase("PKCS1Padding")) {
	    return null;
	}
	if (padding.equalsIgnoreCase("OAEPPadding")) {
	    return OAEPParameterSpec.DEFAULT;
	}
	if (padding.equalsIgnoreCase("OAEPWithMD5AndMGF1Padding")) {
	    return new OAEPParameterSpec("MD5", "MGF1", new MGF1ParameterSpec("MD5"), PSource.PSpecified.DEFAULT);
	}
	if (padding.equalsIgnoreCase("OAEPWithSHA-1AndMGF1Padding")) {
	    return new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
	}
	if (padding.equalsIgnoreCase("OAEPWithSHA-224AndMGF1Padding")) {
	    return new OAEPParameterSpec("SHA-224", "MGF1", MGF1ParameterSpec.SHA224, PSource.PSpecified.DEFAULT);
	}
	if (padding.equalsIgnoreCase("OAEPWithSHA-256AndMGF1Padding")) {
	    return new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
	}
	if (padding.equalsIgnoreCase("OAEPWithSHA-384AndMGF1Padding")) {
	    return new OAEPParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT);
	}
	if (padding.equalsIgnoreCase("OAEPWithSHA-512AndMGF1Padding")) {
	    return new OAEPParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT);
	}
	return null;
    }
}
