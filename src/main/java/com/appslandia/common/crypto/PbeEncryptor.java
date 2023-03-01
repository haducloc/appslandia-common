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
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;
import java.util.Random;
import java.util.function.BiFunction;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeEncryptor extends PbeObject implements Encryptor {
    private String transformation, provider;
    private String[] algorithms;

    private Integer ivSize;
    private BiFunction<String[], byte[], AlgorithmParameterSpec> algSpecFunc;

    private Cipher cipher;

    final Object mutex = new Object();
    final Random random = new SecureRandom();

    @Override
    protected void init() throws Exception {
	super.init();

	// transformation
	Asserts.notNull(this.transformation, "transformation is required.");

	this.algorithms = this.transformation.split("/");
	Asserts.isTrue(algorithms.length == 3, "transformation is invalid.");

	this.algorithms[0] = this.algorithms[0].toUpperCase(Locale.ENGLISH);
	this.algorithms[1] = this.algorithms[1].toUpperCase(Locale.ENGLISH);

	Asserts.isTrue(!"RSA".equals(this.algorithms[0]), "Use RsaEncryptor instead.");

	// algSpecFunc
	if (this.algSpecFunc == null) {
	    this.algSpecFunc = (algs, iv) -> null;
	}

	// cipher
	if (this.provider == null) {
	    this.cipher = Cipher.getInstance(this.transformation);
	} else {
	    this.cipher = Cipher.getInstance(this.transformation, this.provider);
	}
    }

    private boolean isIVSpec() {
	return !"ECB".equals(this.algorithms[1]);
    }

    private int getIVSize() {
	return ValueUtils.valueOrAlt(this.ivSize, this.cipher.getBlockSize());
    }

    @Override
    public byte[] encrypt(byte[] message) throws CryptoException {
	this.initialize();
	Asserts.notNull(message, "message is required.");

	byte[] salt = RandomUtils.nextBytes(this.saltSize, this.random);
	SecretKey secretKey = buildSecretKey(salt, this.algorithms[0]);
	byte[] iv = isIVSpec() ? RandomUtils.nextBytes(this.getIVSize(), this.random) : null;

	try {
	    synchronized (this.mutex) {
		AlgorithmParameterSpec spec = this.algSpecFunc.apply(this.algorithms, iv);

		if (spec == null) {
		    this.cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		} else {
		    this.cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
		}

		byte[] encMsg = this.cipher.doFinal(message);
		return (iv != null) ? ArrayUtils.append(iv, salt, encMsg) : ArrayUtils.append(salt, encMsg);
	    }
	} catch (GeneralSecurityException ex) {
	    throw new CryptoException(ex);
	} finally {
	    CryptoUtils.destroyQuietly(secretKey);
	}
    }

    @Override
    public byte[] decrypt(byte[] message) throws CryptoException {
	this.initialize();

	Asserts.notNull(message, "message is required.");
	if (isIVSpec()) {
	    Asserts.isTrue(message.length >= this.getIVSize() + this.saltSize, "message is invalid.");
	} else {
	    Asserts.isTrue(message.length >= this.saltSize, "message is invalid.");
	}

	byte[] salt = new byte[this.saltSize];
	byte[] iv = isIVSpec() ? new byte[this.getIVSize()] : null;

	if (iv == null) {
	    ArrayUtils.copy(message, salt);
	} else {
	    ArrayUtils.copy(message, iv, salt);
	}
	SecretKey secretKey = buildSecretKey(salt, this.algorithms[0]);

	try {
	    synchronized (this.mutex) {
		AlgorithmParameterSpec spec = this.algSpecFunc.apply(this.algorithms, iv);

		if (spec == null) {
		    this.cipher.init(Cipher.DECRYPT_MODE, secretKey);
		} else {
		    this.cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
		}

		if (iv == null) {
		    return this.cipher.doFinal(message, salt.length, message.length - salt.length);
		} else {
		    return this.cipher.doFinal(message, iv.length + salt.length, message.length - iv.length - salt.length);
		}
	    }
	} catch (GeneralSecurityException ex) {
	    throw new CryptoException(ex);
	} finally {
	    CryptoUtils.destroyQuietly(secretKey);
	}
    }

    public String getTransformation() {
	this.initialize();
	return this.transformation;
    }

    public PbeEncryptor setTransformation(String transformation) {
	this.assertNotInitialized();
	this.transformation = transformation;
	return this;
    }

    public String getProvider() {
	this.initialize();
	return this.provider;
    }

    public PbeEncryptor setProvider(String provider) {
	this.assertNotInitialized();
	this.provider = provider;
	return this;
    }

    @Override
    public PbeEncryptor setSaltSize(int saltSize) {
	super.setSaltSize(saltSize);
	return this;
    }

    @Override
    public PbeEncryptor setIterationCount(int iterationCount) {
	super.setIterationCount(iterationCount);
	return this;
    }

    @Override
    public PbeEncryptor setKeySize(int keySize) {
	super.setKeySize(keySize);
	return this;
    }

    @Override
    public PbeEncryptor setPassword(char[] password) {
	super.setPassword(password);
	return this;
    }

    @Override
    public PbeEncryptor setPassword(String passwordOrEnv) {
	super.setPassword(passwordOrEnv);
	return this;
    }

    @Override
    public PbeEncryptor setSecretKeyGenerator(SecretKeyGenerator secretKeyGenerator) {
	super.setSecretKeyGenerator(secretKeyGenerator);
	return this;
    }

    public PbeEncryptor setIvSize(int ivSize) {
	assertNotInitialized();
	this.ivSize = ivSize;
	return this;
    }

    public PbeEncryptor setAlgSpecFunc(BiFunction<String[], byte[], AlgorithmParameterSpec> algSpecFunc) {
	assertNotInitialized();
	this.algSpecFunc = algSpecFunc;
	return this;
    }

    @Override
    public PbeEncryptor copy() {
	PbeEncryptor impl = new PbeEncryptor().setTransformation(this.transformation).setProvider(this.provider);
	impl.setSaltSize(this.saltSize).setIterationCount(this.iterationCount).setKeySize(this.keySize);

	if (this.password != null) {
	    impl.setPassword(this.password);
	}
	if (this.secretKeyGenerator != null) {
	    impl.secretKeyGenerator = this.secretKeyGenerator.copy();
	}
	impl.ivSize = this.ivSize;
	impl.algSpecFunc = this.algSpecFunc;
	return impl;
    }

    public static AlgorithmParameterSpec IvParameterSpec(String[] algs, byte[] iv) {
	return new IvParameterSpec(iv);
    }

    public static AlgorithmParameterSpec GCMParameterSpec(String[] algs, byte[] iv) {
	final int tSize = 16;
	return new GCMParameterSpec(tSize * 8, iv);
    }
}
