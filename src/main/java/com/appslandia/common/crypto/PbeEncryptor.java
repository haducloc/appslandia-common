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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import com.appslandia.common.base.Out;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeEncryptor extends PbeObject implements Encryptor {
    private String transformation, provider;
    private String algorithm;
    private String mode;

    private Integer ivSize;
    private Integer tagSize;

    private Cipher cipher;

    final Object mutex = new Object();
    final Random random = new SecureRandom();

    static final ConcurrentMap<Integer, byte[]> ZERO_IV_CACHE = new ConcurrentHashMap<>();

    @Override
    protected void init() throws Exception {
	super.init();

	// transformation
	AssertUtils.assertNotNull(this.transformation, "transformation is required.");

	String[] trans = this.transformation.split("/");
	AssertUtils.assertTrue(trans.length == 3, "transformation is required.");

	// algorithm
	this.algorithm = trans[0].toUpperCase(Locale.ENGLISH);
	AssertUtils.assertTrue(!"RSA".equals(this.algorithm), "RSA algorithm is not supported.");

	// mode
	this.mode = trans[1].toUpperCase(Locale.ENGLISH);

	// cipher
	if (this.provider == null) {
	    this.cipher = Cipher.getInstance(this.transformation);
	} else {
	    this.cipher = Cipher.getInstance(this.transformation, this.provider);
	}
    }

    private boolean isIVSpec() {
	return !"ECB".equals(this.mode);
    }

    private boolean isGCMMode() {
	return "GCM".equals(this.mode);
    }

    private int getIVSize() {
	return ValueUtils.valueOrAlt(this.ivSize, this.cipher.getBlockSize());
    }

    protected AlgorithmParameterSpec buildIvParameter(byte[] iv) {
	if (isGCMMode()) {
	    int tSize = ValueUtils.valueOrAlt(this.tagSize, 12);
	    return new GCMParameterSpec(tSize * 8, iv);
	}
	return new IvParameterSpec(iv);
    }

    @Override
    public byte[] encrypt(byte[] message) throws CryptoException {
	this.initialize();
	AssertUtils.assertNotNull(message, "message is required.");

	Out<byte[]> salt = new Out<>();
	byte[] encMsg = encrypt(message, salt);
	return ArrayUtils.append(salt.value, encMsg);
    }

    @Override
    public byte[] decrypt(byte[] message) throws CryptoException {
	this.initialize();

	AssertUtils.assertNotNull(message, "message is required.");
	AssertUtils.assertTrue(message.length > this.saltSize, "message is invalid.");

	byte[] salt = new byte[this.saltSize];
	ArrayUtils.copy(message, salt);
	SecretKey secretKey = buildSecretKey(salt, this.algorithm);

	byte[] iv = null;
	if (isIVSpec()) {
	    iv = ZERO_IV_CACHE.computeIfAbsent(getIVSize(), s -> new byte[s]);
	}

	try {
	    synchronized (this.mutex) {
		if (iv != null) {
		    this.cipher.init(Cipher.DECRYPT_MODE, secretKey, buildIvParameter(iv));
		} else {
		    this.cipher.init(Cipher.DECRYPT_MODE, secretKey);
		}
		return this.cipher.doFinal(message, salt.length, message.length - salt.length);
	    }
	} catch (GeneralSecurityException ex) {
	    throw new CryptoException(ex);
	} finally {
	    CryptoUtils.destroyQuietly(secretKey);
	}
    }

    @Override
    public byte[] encrypt(byte[] message, Out<byte[]> salt) throws CryptoException {
	this.initialize();
	AssertUtils.assertNotNull(message, "message is required.");
	AssertUtils.assertNotNull(salt, "salt is required.");

	salt.value = RandomUtils.nextBytes(this.saltSize, this.random);
	SecretKey secretKey = buildSecretKey(salt.value, this.algorithm);

	byte[] iv = null;
	if (isIVSpec()) {
	    iv = ZERO_IV_CACHE.computeIfAbsent(getIVSize(), s -> new byte[s]);
	}

	try {
	    synchronized (this.mutex) {
		if (iv != null) {
		    this.cipher.init(Cipher.ENCRYPT_MODE, secretKey, buildIvParameter(iv));
		} else {
		    this.cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		}
		return this.cipher.doFinal(message);
	    }

	} catch (GeneralSecurityException ex) {
	    throw new CryptoException(ex);
	} finally {
	    CryptoUtils.destroyQuietly(secretKey);
	}
    }

    @Override
    public byte[] decrypt(byte[] message, byte[] salt) throws CryptoException {
	this.initialize();
	AssertUtils.assertNotNull(message, "message is required.");
	AssertUtils.assertNotNull(salt, "salt is required.");

	AssertUtils.assertTrue(salt.length == this.saltSize, "salt is invalid.");
	SecretKey secretKey = buildSecretKey(salt, this.algorithm);

	byte[] iv = null;
	if (isIVSpec()) {
	    iv = ZERO_IV_CACHE.computeIfAbsent(getIVSize(), s -> new byte[s]);
	}

	try {
	    synchronized (this.mutex) {
		if (iv != null) {
		    this.cipher.init(Cipher.DECRYPT_MODE, secretKey, buildIvParameter(iv));
		} else {
		    this.cipher.init(Cipher.DECRYPT_MODE, secretKey);
		}
		return this.cipher.doFinal(message);
	    }
	} catch (GeneralSecurityException ex) {
	    throw new CryptoException(ex);
	} finally {
	    CryptoUtils.destroyQuietly(secretKey);
	}
    }

    public PbeEncryptor setTransformation(String transformation) {
	this.assertNotInitialized();
	this.transformation = transformation;
	return this;
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

    public PbeEncryptor setTagSize(int tagSize) {
	assertNotInitialized();
	this.tagSize = tagSize;
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
	impl.tagSize = this.tagSize;
	return impl;
    }
}
