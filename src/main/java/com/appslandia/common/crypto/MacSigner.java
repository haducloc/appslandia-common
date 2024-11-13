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

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.SYS;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MacSigner extends InitializeObject implements Digester {

  private String algorithm, provider;
  private byte[] secret;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.algorithm, "algorithm is required.");
    Asserts.notNull(this.secret, "secret is required.");
  }

  protected Mac getImpl() throws GeneralSecurityException {
    Mac impl = null;
    if (this.provider == null) {
      impl = Mac.getInstance(this.algorithm);
    } else {
      impl = Mac.getInstance(this.algorithm, this.provider);
    }
    // impl.reset();
    return impl;
  }

  @Override
  public void destroy() throws DestroyException {
    CryptoUtils.clear(this.secret);
  }

  @Override
  public byte[] digest(byte[] message) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");

    SecretKey key = new SecretKeySpec(this.secret, this.algorithm);
    try {
      Mac impl = getImpl();
      impl.init(key);
      return impl.doFinal(message);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex.getMessage(), ex);
    } finally {
      CryptoUtils.destroy(key);
    }
  }

  @Override
  public boolean verify(byte[] message, byte[] mac) throws CryptoException {
    this.initialize();
    Asserts.notNull(message, "message is required.");
    Asserts.notNull(mac, "mac is required.");

    SecretKey key = new SecretKeySpec(this.secret, this.algorithm);
    try {
      Mac impl = getImpl();
      impl.init(key);
      byte[] msgMac = impl.doFinal(message);
      return Arrays.equals(mac, msgMac);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex.getMessage(), ex);
    } finally {
      CryptoUtils.destroy(key);
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

  public MacSigner setSecret(byte[] secret) {
    this.assertNotInitialized();
    if (secret != null) {
      this.secret = ArrayUtils.copy(secret);
    }
    return this;
  }

  public MacSigner setSecret(String passwordExpr) {
    this.assertNotInitialized();

    if (passwordExpr != null) {
      String resolvedValue = SYS.resolve(passwordExpr);

      if (resolvedValue == null) {
        throw new IllegalArgumentException("Failed to resolve expression: " + passwordExpr);
      }
      this.secret = resolvedValue.getBytes(StandardCharsets.UTF_8);
    }
    return this;
  }
}
