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

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.IOUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class KeyFactoryUtil extends InitializeObject {

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
    Arguments.notNull(this.algorithm, "algorithm is required.");
  }

  protected KeyFactory getImpl() throws GeneralSecurityException {
    KeyFactory impl = null;
    if (this.provider == null) {
      impl = KeyFactory.getInstance(this.algorithm);
    } else {
      impl = KeyFactory.getInstance(this.algorithm, this.provider);
    }
    return impl;
  }

  protected void release(KeyFactory impl) {
  }

  public PrivateKey toPrivateKey(KeySpec keySpec) throws CryptoException {
    this.initialize();
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
    this.initialize();
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
    this.initialize();
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
    this.initialize();
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
    this.initialize();
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
    this.initialize();
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
    }
  }

  public PublicKey toPublicKey(InputStream keyInDer) throws IOException, CryptoException {
    this.initialize();
    KeyFactory impl = null;
    try {
      impl = getImpl();
      return impl.generatePublic(new X509EncodedKeySpec(IOUtils.toByteArray(keyInDer)));

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      if (impl != null) {
        release(impl);
      }
    }
  }

  public PrivateKey copy(PrivateKey key) throws CryptoException {
    this.initialize();
    Arguments.isTrue(this.algorithm.equalsIgnoreCase(key.getAlgorithm()));
    Arguments.isTrue("PKCS#8".equalsIgnoreCase(key.getFormat()), "The key is not in PKCS#8 format.");

    var der = key.getEncoded();
    Asserts.notNull(der);
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
    this.initialize();
    Arguments.isTrue(this.algorithm.equalsIgnoreCase(key.getAlgorithm()));
    Arguments.isTrue("X.509".equalsIgnoreCase(key.getFormat()), "The key is not in X.509 format.");

    var der = key.getEncoded();
    Asserts.notNull(der);
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
    return this.algorithm;
  }

  public KeyFactoryUtil setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return this.provider;
  }

  public KeyFactoryUtil setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }
}
