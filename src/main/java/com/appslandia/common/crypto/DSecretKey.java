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

import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;

import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DSecretKey implements SecretKey, KeySpec {
  private static final long serialVersionUID = 1L;

  final byte[] key;
  final String format;
  final String algorithm;

  final AtomicBoolean destroyed = new AtomicBoolean(false);

  public DSecretKey(byte[] key, String algorithm) {
    this(key, "RAW", algorithm);
  }

  public DSecretKey(byte[] key, String format, String algorithm) {
    Asserts.notNull(key);
    Asserts.notNull(format);
    Asserts.notNull(algorithm);

    this.key = key.clone();
    this.format = format;
    this.algorithm = algorithm;
  }

  public DSecretKey(Key keyToCopy) {
    this(Asserts.notNull(keyToCopy.getEncoded(), "The keyToCopy must support encoding."), keyToCopy.getFormat(),
        keyToCopy.getAlgorithm());
  }

  @Override
  public String getAlgorithm() {
    return this.algorithm;
  }

  @Override
  public String getFormat() {
    return this.format;
  }

  @Override
  public byte[] getEncoded() {
    if (isDestroyed()) {
      throw new IllegalStateException("The key is destroyed.");
    }
    return this.key.clone();
  }

  @Override
  public boolean isDestroyed() {
    return this.destroyed.get();
  }

  @Override
  public void destroy() throws DestroyFailedException {
    if (this.destroyed.compareAndSet(false, true)) {
      CryptoUtils.clear(this.key);
    }
  }

  @Override
  public int hashCode() {
    int retval = 0;
    for (int i = 1; i < this.key.length; i++) {
      retval += this.key[i] * i;
    }
    if (this.algorithm.equalsIgnoreCase("TripleDES")) {
      return (retval ^= "desede".hashCode());
    } else {
      return (retval ^= this.algorithm.toLowerCase(Locale.ENGLISH).hashCode());
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof SecretKey)) {
      return false;
    }
    String thatAlg = ((SecretKey) obj).getAlgorithm();

    if (!(this.algorithm.equalsIgnoreCase(thatAlg)
        || (this.algorithm.equalsIgnoreCase("TripleDES") && thatAlg.equalsIgnoreCase("DESede"))
        || (this.algorithm.equalsIgnoreCase("DESede") && thatAlg.equalsIgnoreCase("TripleDES")))) {
      return false;
    }

    byte[] thatKey = ((SecretKey) obj).getEncoded();
    boolean eq = MessageDigest.isEqual(this.key, thatKey);

    CryptoUtils.clear(thatKey);
    return eq;
  }
}
