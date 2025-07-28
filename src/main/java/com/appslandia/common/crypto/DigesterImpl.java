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

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class DigesterImpl extends InitializeObject implements Digester {

  protected String algorithm, provider;

  public DigesterImpl() {
  }

  public DigesterImpl(String algorithm) {
    this.algorithm = algorithm;
  }

  public DigesterImpl(String algorithm, String provider) {
    this.algorithm = algorithm;
    this.provider = provider;
  }

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.algorithm, "algorithm is required.");
  }

  protected MessageDigest getImpl() throws GeneralSecurityException {
    MessageDigest impl = null;
    if (this.provider == null) {
      impl = MessageDigest.getInstance(this.algorithm);
    } else {
      impl = MessageDigest.getInstance(this.algorithm, this.provider);
    }
    return impl;
  }

  protected void release(MessageDigest impl) {
  }

  @Override
  public byte[] digest(byte[] message) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");

    MessageDigest impl = null;
    try {
      impl = this.getImpl();
      return impl.digest(message);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex.getMessage(), ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  @Override
  public boolean verify(byte[] message, byte[] hash) throws CryptoException {
    this.initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(hash, "hash is required.");

    MessageDigest impl = null;
    try {
      impl = this.getImpl();
      var computedHash = impl.digest(message);
      return MessageDigest.isEqual(hash, computedHash);

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

  public DigesterImpl setAlgorithm(String algorithm) {
    this.assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public DigesterImpl setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }
}
