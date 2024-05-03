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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.util.Locale;
import java.util.function.Function;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RsaEncryptor extends InitializeObject implements Encryptor {
  private String transformation, provider;

  private PublicKey publicKey;
  private PrivateKey privateKey;

  private Cipher encrypt;
  private Cipher decrypt;

  final Object encMutex = new Object();
  final Object decMutex = new Object();

  private Function<CipherOps, AlgorithmParameterSpec> algParamSpec;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.transformation, "transformation is required.");
    CipherOps cipherOps = new CipherOps(this.transformation);

    Asserts.isTrue(cipherOps.isAlgorithm("RSA"), "RSA algorithm is required.");
    Asserts.isTrue((this.privateKey != null) || (this.publicKey != null), "No key is provided.");

    // algParamSpec
    if (this.algParamSpec == null) {
      this.algParamSpec = (ops) -> toAlgParamSpec(ops);
    }

    // ENCRYPT
    if (this.publicKey != null) {
      if (this.provider == null) {
        this.encrypt = Cipher.getInstance(this.transformation);
      } else {
        this.encrypt = Cipher.getInstance(this.transformation, this.provider);
      }

      AlgorithmParameterSpec spec = this.algParamSpec.apply(cipherOps);

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

      AlgorithmParameterSpec spec = this.algParamSpec.apply(cipherOps);

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
      CryptoUtils.destroy(this.privateKey);
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
    this.privateKey = privateKey;
    return this;
  }

  public RsaEncryptor setPublicKey(PublicKey publicKey) {
    assertNotInitialized();
    this.publicKey = publicKey;
    return this;
  }

  public RsaEncryptor setAlgParamSpec(Function<CipherOps, AlgorithmParameterSpec> algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }

  static AlgorithmParameterSpec toAlgParamSpec(CipherOps cipherOps) {
    String padding = cipherOps.getPadding();

    // OAEP (Optimal Asymmetric Encryption Padding): Encryption scheme
    // OAEPWith{HashAlg}AndMGF1Padding

    if (StringUtils.startsWith(padding, "OAEPWith") && StringUtils.endsWith(padding, "AndMGF1Padding")) {

      int start = "OAEPWith".length();
      int end = padding.length() - "AndMGF1Padding".length();

      String hashAlg = padding.substring(start, end).toUpperCase(Locale.ENGLISH);
      MGF1ParameterSpec mgf1Spec = MGF1ParameterSpecUtil.getInstance(hashAlg);

      return new OAEPParameterSpec(hashAlg, "MGF1", mgf1Spec, PSource.PSpecified.DEFAULT);
    }

    return null;
  }
}
