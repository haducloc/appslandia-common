// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.SYS;

/**
 *
 * @author Loc Ha
 *
 */
public class MacSigner extends InitializingObject implements Digester {

  protected String algorithm, provider;
  protected AlgorithmParameterSpec algParamSpec;

  protected SecretKey secretKey;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(algorithm, "algorithm is required.");
    Arguments.notNull(secretKey, "secretKey is required.");
  }

  @Override
  public void destroy() throws DestroyingException {
    CryptoUtils.destroy(secretKey);
  }

  protected Mac getImpl() throws GeneralSecurityException {
    Mac impl = null;
    if (provider == null) {
      impl = Mac.getInstance(algorithm);
    } else {
      impl = Mac.getInstance(algorithm, provider);
    }
    return impl;
  }

  protected void release(Mac impl) {
  }

  @Override
  public byte[] digest(byte[] message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");

    Mac impl = null;
    try {
      impl = getImpl();
      if (algParamSpec == null) {
        impl.init(secretKey);
      } else {
        impl.init(secretKey, algParamSpec);
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
    initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(mac, "mac is required.");

    Mac impl = null;
    try {
      impl = getImpl();
      if (algParamSpec == null) {
        impl.init(secretKey);
      } else {
        impl.init(secretKey, algParamSpec);
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
    initialize();
    return algorithm;
  }

  public MacSigner setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public MacSigner setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public MacSigner setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }

  public MacSigner setSecret(byte[] secret) {
    assertNotInitialized();
    if (secret != null) {
      Arguments.notNull(algorithm, "algorithm is required.");
      secretKey = new DSecretKeySpec(secret, algorithm);
    }
    return this;
  }

  public MacSigner setSecret(String secretExpr) {
    assertNotInitialized();

    if (secretExpr != null) {
      var resolvedValue = SYS.resolve(secretExpr);

      if (resolvedValue == null) {
        throw new IllegalArgumentException("Failed to resolve expression: " + secretExpr);
      }
      setSecret(resolvedValue.getBytes(StandardCharsets.UTF_8));
    }
    return this;
  }

  public MacSigner setSecretKey(SecretKey secretKey) {
    assertNotInitialized();
    if (secretKey != null) {
      Arguments.notNull(algorithm, "algorithm is required.");
      Arguments.isTrue(algorithm.equalsIgnoreCase(secretKey.getAlgorithm()));

      this.secretKey = CryptoUtils.copy(secretKey);
    }
    return this;
  }
}
