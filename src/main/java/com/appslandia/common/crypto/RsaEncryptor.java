// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;

import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class RsaEncryptor extends InitializingObject implements Encryptor {
  protected String transformation, provider;
  protected CipherOps cipherOps;

  protected PublicKey publicKey;
  protected PrivateKey privateKey;

  protected AlgorithmParameterSpec algParamSpec;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(transformation, "transformation is required.");
    Arguments.isTrue("RSA".equalsIgnoreCase(cipherOps.getAlgorithm()), "RSA algorithm is required.");

    if (privateKey != null) {
      Arguments.isTrue("RSA".equalsIgnoreCase(privateKey.getAlgorithm()), "The privateKey.algorithm must be RSA.");
    }
    if (publicKey != null) {
      Arguments.isTrue("RSA".equalsIgnoreCase(publicKey.getAlgorithm()), "The publicKey.algorithm must be RSA.");
    }
  }

  @Override
  public void destroy() throws DestroyingException {
    CryptoUtils.destroy(privateKey);
  }

  protected Cipher getImpl() throws GeneralSecurityException {
    Cipher impl = null;
    if (provider == null) {
      impl = Cipher.getInstance(transformation);
    } else {
      impl = Cipher.getInstance(transformation, provider);
    }
    return impl;
  }

  protected void release(Cipher impl) {
  }

  @Override
  public byte[] encrypt(byte[] message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(publicKey, "publicKey is required.");

    Cipher impl = null;
    try {
      impl = getImpl();
      if (algParamSpec == null) {
        impl.init(Cipher.ENCRYPT_MODE, publicKey);
      } else {
        impl.init(Cipher.ENCRYPT_MODE, publicKey, algParamSpec);
      }
      return impl.doFinal(message);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  @Override
  public byte[] decrypt(byte[] message) throws CryptoException {
    initialize();
    Arguments.notNull(message, "message is required.");
    Arguments.notNull(privateKey, "privateKey is required.");

    Cipher impl = null;
    try {
      impl = getImpl();
      if (algParamSpec == null) {
        impl.init(Cipher.DECRYPT_MODE, privateKey);
      } else {
        impl.init(Cipher.DECRYPT_MODE, privateKey, algParamSpec);
      }
      return impl.doFinal(message);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getTransformation() {
    initialize();
    return transformation;
  }

  public RsaEncryptor setTransformation(String transformation) {
    assertNotInitialized();
    if (transformation != null) {
      cipherOps = new CipherOps(transformation);
      this.transformation = transformation;
    }
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public RsaEncryptor setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public RsaEncryptor setPrivateKey(PrivateKey privateKey) {
    assertNotInitialized();
    if (privateKey != null) {
      this.privateKey = new KeyFactoryUtil(privateKey.getAlgorithm()).copy(privateKey);
    }
    return this;
  }

  public RsaEncryptor setPublicKey(PublicKey publicKey) {
    assertNotInitialized();
    if (publicKey != null) {
      this.publicKey = new KeyFactoryUtil(publicKey.getAlgorithm()).copy(publicKey);
    }
    return this;
  }

  public RsaEncryptor setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
    assertNotInitialized();
    this.algParamSpec = algParamSpec;
    return this;
  }
}
