// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.IOUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class KeyFactoryUtil extends InitializingObject {

  // DiffieHellman, DSA, EC, EdDSA, Ed25519, Ed448,
  // RSA, RSASSA-PSS, XDH, X25519, X448, etc.
  protected String algorithm, provider;

  public KeyFactoryUtil() {
  }

  public KeyFactoryUtil(String algorithm) {
    this.algorithm = algorithm;
  }

  public KeyFactoryUtil(String algorithm, String provider) {
    this.algorithm = algorithm;
    this.provider = provider;
  }

  @Override
  protected void init() throws Exception {
    Arguments.notNull(algorithm, "algorithm is required.");
  }

  protected KeyFactory getImpl() throws GeneralSecurityException {
    KeyFactory impl = null;
    if (provider == null) {
      impl = KeyFactory.getInstance(algorithm);
    } else {
      impl = KeyFactory.getInstance(algorithm, provider);
    }
    return impl;
  }

  protected void release(KeyFactory impl) {
  }

  public PrivateKey toPrivateKey(KeySpec keySpec) throws CryptoException {
    initialize();
    KeyFactory impl = null;
    try {
      impl = getImpl();
      return impl.generatePrivate(keySpec);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  public PublicKey toPublicKey(KeySpec keySpec) throws CryptoException {
    initialize();
    KeyFactory impl = null;
    try {
      impl = getImpl();
      return impl.generatePublic(keySpec);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  // PKCS#8/ASN.1 encoding is a standard format for encoding private key

  public PrivateKey toPrivateKey(String keyInPem) throws CryptoException {
    initialize();
    var der = PKIUtils.toDerEncoded(keyInPem);
    KeyFactory impl = null;
    try {
      impl = getImpl();
      return impl.generatePrivate(new PKCS8EncodedKeySpec(der));

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
      CryptoUtils.clear(der);
    }
  }

  public PrivateKey toPrivateKey(InputStream keyInDer) throws IOException, CryptoException {
    initialize();
    var der = IOUtils.toByteArray(keyInDer);
    KeyFactory impl = null;
    try {
      impl = getImpl();
      return impl.generatePrivate(new PKCS8EncodedKeySpec(der));

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
      CryptoUtils.clear(der);
    }
  }

  public PrivateKey toPrivateKey(byte[] keyInDer) throws CryptoException {
    initialize();
    KeyFactory impl = null;
    try {
      impl = getImpl();
      return impl.generatePrivate(new PKCS8EncodedKeySpec(keyInDer));

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  // X509/ASN.1 encoding is a standard format for encoding public key

  public PublicKey toPublicKey(String keyInPem) throws CryptoException {
    initialize();
    var der = PKIUtils.toDerEncoded(keyInPem);
    KeyFactory impl = null;
    try {
      impl = getImpl();
      return impl.generatePublic(new X509EncodedKeySpec(der));

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
      CryptoUtils.clear(der);
    }
  }

  public PublicKey toPublicKey(InputStream keyInDer) throws IOException, CryptoException {
    initialize();
    var der = IOUtils.toByteArray(keyInDer);
    KeyFactory impl = null;
    try {
      impl = getImpl();
      return impl.generatePublic(new X509EncodedKeySpec(der));

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
      CryptoUtils.clear(der);
    }
  }

  public PrivateKey copy(PrivateKey key) throws CryptoException {
    initialize();

    Arguments.notNull(key, "key is required.");
    Arguments.isTrue(algorithm.equalsIgnoreCase(key.getAlgorithm()));
    Arguments.isTrue("PKCS#8".equalsIgnoreCase(key.getFormat()), "The key is not in PKCS#8 format.");

    var der = key.getEncoded();
    Arguments.notNull(der, "key.getEncoded() must not be null.");

    KeyFactory impl = null;
    try {
      impl = getImpl();
      return impl.generatePrivate(new PKCS8EncodedKeySpec(der));

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
      CryptoUtils.clear(der);
    }
  }

  public PublicKey copy(PublicKey key) throws CryptoException {
    initialize();

    Arguments.notNull(key, "key is required.");
    Arguments.isTrue(algorithm.equalsIgnoreCase(key.getAlgorithm()));
    Arguments.isTrue("X.509".equalsIgnoreCase(key.getFormat()), "The key is not in X.509 format.");

    var der = key.getEncoded();
    Arguments.notNull(der, "key.getEncoded() must not be null.");

    KeyFactory impl = null;
    try {
      impl = getImpl();
      return impl.generatePublic(new X509EncodedKeySpec(der));

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

  public KeyFactoryUtil setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public KeyFactoryUtil setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }
}
