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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.Entry.Attribute;
import java.security.KeyStore.ProtectionParameter;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

import javax.crypto.SecretKey;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class KeyStoreUtil extends InitializeObject {

  // Stores symmetric keys (e.g., AES, DES), private keys, and passwords with
  // strong protection.
  public static final String TYPE_JCEKS = "JCEKS";

  // Stores private keys, public certificates, and certificate chains for SSL/TLS
  // in Java applications.
  public static final String TYPE_JKS = "JKS";

  // Cross-platform format for storing private keys with associated public
  // certificates and certificate chains.
  public static final String TYPE_PKCS12 = "PKCS12";

  protected String type, provider;
  protected KeyStore impl;

  public KeyStoreUtil() {
  }

  public KeyStoreUtil(String type) {
    this.type = type;
  }

  public KeyStoreUtil(String type, String provider) {
    this.type = type;
    this.provider = provider;
  }

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.type, "type is required.");

    KeyStore impl;
    if (this.provider == null) {
      impl = KeyStore.getInstance(this.type);
    } else {
      impl = KeyStore.getInstance(this.type, this.provider);
    }
    impl.load(null, null);
    this.impl = impl;
  }

  @Override
  public void destroy() throws DestroyException {
    // Reset the KeyStore?
  }

  public void load(InputStream in, char[] password) throws CryptoException, IOException {
    initialize();

    try {
      this.impl.load(in, password);
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public void load(String fileName, char[] password) throws CryptoException, IOException {
    try (var fis = new FileInputStream(fileName)) {
      load(fis, password);
    }
  }

  public void save(OutputStream out, char[] password) throws CryptoException, IOException {
    initialize();

    try {
      this.impl.store(out, password);
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public void save(String fileName, char[] password) throws CryptoException, IOException {
    try (var fos = new FileOutputStream(fileName)) {
      save(fos, password);
    }
  }

  public Enumeration<String> getEntries() throws CryptoException, IOException {
    initialize();
    try {
      return this.impl.aliases();
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public boolean containsEntry(String alias) throws CryptoException, IOException {
    initialize();
    try {
      return this.impl.containsAlias(alias);
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public void removeEntry(String alias) throws CryptoException, IOException {
    initialize();
    try {
      this.impl.deleteEntry(alias);
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public <E extends Entry> E getEntry(String alias, char[] entryPassword) throws CryptoException, IOException {
    return getEntry(alias, new KeyStore.PasswordProtection(entryPassword));
  }

  public <E extends Entry> E getEntry(String alias, ProtectionParameter protectionParameter)
      throws CryptoException, IOException {
    initialize();
    try {
      var entry = this.impl.getEntry(alias, protectionParameter);
      return ObjectUtils.cast(entry);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public KeyStoreUtil setEntry(String alias, SecretKey key, Set<Attribute> attributes, char[] entryPassword)
      throws CryptoException, IOException {
    return setEntry(alias, key, attributes, new KeyStore.PasswordProtection(entryPassword));
  }

  public KeyStoreUtil setEntry(String alias, SecretKey key, Set<Attribute> attributes,
      ProtectionParameter protectionParameter) throws CryptoException, IOException {
    initialize();
    try {
      if (attributes == null) {
        attributes = Collections.emptySet();
      }
      this.impl.setEntry(alias, new KeyStore.SecretKeyEntry(key, attributes), protectionParameter);
      return this;
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public KeyStoreUtil setEntry(String alias, PrivateKey privateKey, Certificate[] certificateChain,
      Set<Attribute> attributes, char[] entryPassword) throws CryptoException, IOException {
    return setEntry(alias, privateKey, certificateChain, attributes, new KeyStore.PasswordProtection(entryPassword));
  }

  public KeyStoreUtil setEntry(String alias, PrivateKey privateKey, Certificate[] certificateChain,
      Set<Attribute> attributes, ProtectionParameter protectionParameter) throws CryptoException, IOException {
    initialize();
    try {
      if (attributes == null) {
        attributes = Collections.emptySet();
      }
      this.impl.setEntry(alias, new KeyStore.PrivateKeyEntry(privateKey, certificateChain, attributes),
          protectionParameter);
      return this;
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public KeyStoreUtil setEntry(String alias, Certificate trustedCert, Set<Attribute> attributes, char[] entryPassword)
      throws CryptoException, IOException {
    return setEntry(alias, trustedCert, attributes, new KeyStore.PasswordProtection(entryPassword));
  }

  public KeyStoreUtil setEntry(String alias, Certificate trustedCert, Set<Attribute> attributes,
      ProtectionParameter protectionParameter) throws CryptoException, IOException {
    initialize();
    try {
      if (attributes == null) {
        attributes = Collections.emptySet();
      }
      this.impl.setEntry(alias, new KeyStore.TrustedCertificateEntry(trustedCert, attributes), protectionParameter);
      return this;
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public String getType() {
    initialize();
    return this.type;
  }

  public KeyStoreUtil setType(String type) {
    assertNotInitialized();
    this.type = type;
    return this;
  }

  public String getProvider() {
    initialize();
    return this.provider;
  }

  public KeyStoreUtil setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }
}
