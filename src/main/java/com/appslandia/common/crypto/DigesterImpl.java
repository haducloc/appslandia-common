// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class DigesterImpl extends InitializingObject implements Digester {

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
    Arguments.notNull(algorithm, "algorithm is required.");
  }

  @Override
  public void destroy() throws DestroyingException {
  }

  protected MessageDigest getImpl() throws GeneralSecurityException {
    MessageDigest impl = null;
    if (provider == null) {
      impl = MessageDigest.getInstance(algorithm);
    } else {
      impl = MessageDigest.getInstance(algorithm, provider);
    }
    return impl;
  }

  protected void release(MessageDigest impl) {
  }

  @Override
  public byte[] digest(byte[] message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");

    MessageDigest impl = null;
    try {
      impl = getImpl();
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
    initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(hash, "hash is required.");

    MessageDigest impl = null;
    try {
      impl = getImpl();
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
    initialize();
    return algorithm;
  }

  public DigesterImpl setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public DigesterImpl setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }
}
