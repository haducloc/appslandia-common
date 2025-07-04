// The MIT License (MIT)
// Copyright © 2015 Loc Ha

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

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.SYS;

/**
 *
 * @author Loc Ha
 *
 */
public class MacSigner extends InitializeObject implements Digester {

  protected String algorithm, provider;
  protected AlgorithmParameterSpec algParamSpec;

  protected SecretKey secretKey;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.algorithm, "algorithm is required.");
    Arguments.notNull(this.secretKey, "secretKey is required.");
  }

  protected Mac getImpl() throws GeneralSecurityException {
    Mac impl = null;
    if (this.provider == null) {
      impl = Mac.getInstance(this.algorithm);
    } else {
      impl = Mac.getInstance(this.algorithm, this.provider);
    }
    return impl;
  }

  protected void release(Mac impl) {
  }

  @Override
  public void destroy() throws DestroyException {
    CryptoUtils.destroy(this.secretKey);
  }

  @Override
  public byte[] digest(byte[] message) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");

    Mac impl = null;
    try {
      impl = getImpl();
      if (this.algParamSpec == null) {
        impl.init(this.secretKey);
      } else {
        impl.init(this.secretKey, this.algParamSpec);
      }
      return impl.doFinal(message);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex.getMessage(), ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  @Override
  public boolean verify(byte[] message, byte[] mac) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(mac, "mac is required.");

    Mac impl = null;
    try {
      impl = getImpl();
      if (this.algParamSpec == null) {
        impl.init(this.secretKey);
      } else {
        impl.init(this.secretKey, this.algParamSpec);
      }

      var computedMac = impl.doFinal(message);
      return MessageDigest.isEqual(mac, computedMac);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex.getMessage(), ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getAlgorithm() {
    this.initialize();
    return this.algorithm;
  }

  public MacSigner setAlgorithm(String algorithm) {
    this.assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public MacSigner setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public MacSigner setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }

  /**
   * Please make sure the algorithm is set before setting the secret.
   *
   * @param secret
   * @return
   */
  public MacSigner setSecret(byte[] secret) {
    this.assertNotInitialized();
    if (secret != null) {
      Asserts.notNull(this.algorithm);
      this.secretKey = new DSecretKeySpec(secret, this.algorithm);
    }
    return this;
  }

  /**
   * Please make sure the algorithm is set before setting the secret expression.
   *
   * @param secretExpr
   * @return
   */
  public MacSigner setSecret(String secretExpr) {
    this.assertNotInitialized();

    if (secretExpr != null) {
      var resolvedValue = SYS.resolve(secretExpr);

      if (resolvedValue == null) {
        throw new IllegalArgumentException("Failed to resolve expression: " + secretExpr);
      }
      setSecret(resolvedValue.getBytes(StandardCharsets.UTF_8));
    }
    return this;
  }

  /**
   * Please make sure the algorithm is set before setting the secret key.
   *
   * @param secretKey
   * @return
   */
  public MacSigner setSecretKey(SecretKey secretKey) {
    this.assertNotInitialized();
    if (secretKey != null) {
      Asserts.notNull(this.algorithm);
      Arguments.isTrue(this.algorithm.equalsIgnoreCase(secretKey.getAlgorithm()));

      this.secretKey = CryptoUtils.copy(secretKey);
    }
    return this;
  }
}
