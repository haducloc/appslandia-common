// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;

import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class DSecretKeySpec implements SecretKey, KeySpec {
  private static final long serialVersionUID = 1L;

  final byte[] key;
  final String format;
  final String algorithm;

  final AtomicBoolean destroyed = new AtomicBoolean(false);

  public DSecretKeySpec(byte[] key, String algorithm) {
    this(key, "RAW", algorithm);
  }

  public DSecretKeySpec(byte[] key, String format, String algorithm) {
    Arguments.notNull(key);
    Arguments.notNull(format);
    Arguments.notNull(algorithm);

    this.key = key.clone();
    this.format = format;
    this.algorithm = algorithm;
  }

  public DSecretKeySpec(SecretKey sourceKey) {
    Arguments.notNull(sourceKey);

    key = sourceKey.getEncoded();
    format = sourceKey.getFormat();
    algorithm = sourceKey.getAlgorithm();
  }

  @Override
  public String getAlgorithm() {
    return algorithm;
  }

  @Override
  public String getFormat() {
    return format;
  }

  @Override
  public byte[] getEncoded() {
    if (isDestroyed()) {
      throw new IllegalStateException("The key is destroyed.");
    }
    return key.clone();
  }

  @Override
  public boolean isDestroyed() {
    return destroyed.get();
  }

  @Override
  public void destroy() throws DestroyFailedException {
    if (destroyed.compareAndSet(false, true)) {
      CryptoUtils.clear(key);
    }
  }

  @Override
  public int hashCode() {
    var retval = 0;
    for (var i = 1; i < key.length; i++) {
      retval += key[i] * i;
    }
    if (algorithm.equalsIgnoreCase("TripleDES")) {
      return (retval ^= "desede".hashCode());
    } else {
      return (retval ^= algorithm.toLowerCase(Locale.ENGLISH).hashCode());
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (isDestroyed() || !(obj instanceof SecretKey that)
        || !(algorithm.equalsIgnoreCase(that.getAlgorithm())
            || (algorithm.equalsIgnoreCase("TripleDES") && that.getAlgorithm().equalsIgnoreCase("DESede"))
            || (algorithm.equalsIgnoreCase("DESede") && that.getAlgorithm().equalsIgnoreCase("TripleDES")))) {
      return false;
    }

    var thatKey = that.getEncoded();
    var eq = MessageDigest.isEqual(key, thatKey);
    CryptoUtils.clear(thatKey);
    return eq;
  }
}
