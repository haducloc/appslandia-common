// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;

import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class DsaSigner extends InitializingObject implements Digester {
  protected String algorithm, provider;

  protected PrivateKey privateKey;
  protected PublicKey publicKey;

  protected AlgorithmParameterSpec algParamSpec;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(algorithm, "algorithm is required.");
    Arguments.isTrue((privateKey != null) || (publicKey != null), "No key is provided.");
  }

  @Override
  public void destroy() throws DestroyingException {
    CryptoUtils.destroy(privateKey);
  }

  protected Signature getImpl() throws GeneralSecurityException {
    Signature impl = null;
    if (provider == null) {
      impl = Signature.getInstance(algorithm);
    } else {
      impl = Signature.getInstance(algorithm, provider);
    }
    return impl;
  }

  protected void release(Signature impl) {
  }

  @Override
  public byte[] digest(byte[] message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(privateKey, "privateKey is required.");

    Signature impl = null;
    try {
      impl = getImpl();
      if (algParamSpec != null) {
        impl.setParameter(algParamSpec);
      }
      impl.initSign(privateKey);
      impl.update(message);
      return impl.sign();

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  @Override
  public boolean verify(byte[] message, byte[] signature) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(signature, "signature is required.");
    Arguments.notNull(publicKey, "publicKey is required.");

    Signature impl = null;
    try {
      impl = getImpl();
      if (algParamSpec != null) {
        impl.setParameter(algParamSpec);
      }
      impl.initVerify(publicKey);
      impl.update(message);
      return impl.verify(signature);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
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

  public DsaSigner setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public DsaSigner setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public DsaSigner setPrivateKey(PrivateKey privateKey) {
    assertNotInitialized();
    if (privateKey != null) {
      this.privateKey = new KeyFactoryUtil(privateKey.getAlgorithm()).copy(privateKey);
    }
    return this;
  }

  public DsaSigner setPublicKey(PublicKey publicKey) {
    assertNotInitialized();
    if (publicKey != null) {
      this.publicKey = new KeyFactoryUtil(publicKey.getAlgorithm()).copy(publicKey);
    }
    return this;
  }

  public DsaSigner setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
