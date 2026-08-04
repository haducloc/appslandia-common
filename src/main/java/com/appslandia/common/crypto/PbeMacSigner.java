// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ArrayUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class PbeMacSigner extends InitializingObject implements Digester {

  protected String algorithm, provider;
  protected AlgorithmParameterSpec algParamSpec;
  protected PbeSecretGen pbeSecretGen;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(algorithm, "algorithm is required.");
  }

  @Override
  public void destroy() throws DestroyingException {
    if (pbeSecretGen != null) {
      pbeSecretGen.destroy();
    }
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
    SecretKey key = null;
    var salt = new Out<byte[]>();
    try {
      impl = getImpl();
      key = pbeSecretGen.generate(algorithm, salt);
      if (algParamSpec == null) {
        impl.init(key);
      } else {
        impl.init(key, algParamSpec);
      }

      var storedMac = impl.doFinal(message);
      return ArrayUtils.append(salt.value, storedMac);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(key);
      if (impl != null) {
        release(impl);
      }
    }
  }

  @Override
  public boolean verify(byte[] message, byte[] digested) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(digested, "digested is required.");

    var saltSize = pbeSecretGen.getSaltSize();
    Arguments.isTrue(digested.length >= saltSize, "digested is invalid.");

    var salt = new byte[saltSize];
    var storedHash = new byte[digested.length - saltSize];
    ArrayUtils.copy(digested, salt, storedHash);

    Mac impl = null;
    SecretKey key = null;
    try {
      impl = getImpl();
      key = pbeSecretGen.generate(algorithm, salt);
      if (algParamSpec == null) {
        impl.init(key);
      } else {
        impl.init(key, algParamSpec);
      }

      var computedMac = impl.doFinal(message);
      return MessageDigest.isEqual(storedHash, computedMac);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      CryptoUtils.destroy(key);
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getAlgorithm() {
    initialize();
    return algorithm;
  }

  public PbeMacSigner setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public PbeMacSigner setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public PbeMacSigner setPbeSecretGen(PbeSecretGen pbeSecretGen) {
    assertNotInitialized();
    this.pbeSecretGen = pbeSecretGen;
    return this;
  }

  public PbeMacSigner setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
