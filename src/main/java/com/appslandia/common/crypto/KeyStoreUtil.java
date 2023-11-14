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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.Entry.Attribute;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

import javax.crypto.SecretKey;

import com.appslandia.common.base.DestroyException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.SYS;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class KeyStoreUtil extends InitializeObject {

  // .jceks
  public static final String TYPE_JCEKS = "JCEKS";

  // .jks
  public static final String TYPE_JKS = "JKS";

  // .p12 or .pfx
  public static final String TYPE_PKCS12 = "PKCS12";

  private String type, provider;
  private KeyStore keyStore;

  private char[] password;
  private InputStream inputStream;

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
    Asserts.notNull(this.type, "type is required.");

    if (this.provider == null) {
      this.keyStore = KeyStore.getInstance(this.type);
    } else {
      this.keyStore = KeyStore.getInstance(this.type, this.provider);
    }

    this.keyStore.load(this.inputStream, this.password);
  }

  @Override
  public void destroy() throws DestroyException {
    if (this.password != null) {
      CryptoUtils.clear(this.password);
    }
  }

  public void save(OutputStream out) throws CryptoException, IOException {
    initialize();
    try {
      this.keyStore.store(out, this.password);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public void save(String fileName) throws CryptoException, IOException {
    try (FileOutputStream fos = new FileOutputStream(fileName)) {
      save(fos);
    }
  }

  public Enumeration<String> getEntries() throws CryptoException {
    initialize();
    try {
      return this.keyStore.aliases();
    } catch (KeyStoreException ex) {
      throw new CryptoException(ex);
    }
  }

  public boolean containsEntry(String alias) throws CryptoException {
    initialize();
    try {
      return this.keyStore.containsAlias(alias);
    } catch (KeyStoreException ex) {
      throw new CryptoException(ex);
    }
  }

  public void removeEntry(String alias) throws CryptoException {
    initialize();
    try {
      this.keyStore.deleteEntry(alias);
    } catch (KeyStoreException ex) {
      throw new CryptoException(ex);
    }
  }

  public <E extends Entry> E getEntry(String alias, char[] entryPassword) throws CryptoException {
    return getEntry(alias, new KeyStore.PasswordProtection(entryPassword));
  }

  public <E extends Entry> E getEntry(String alias, ProtectionParameter protectionParameter) throws CryptoException {
    initialize();
    try {
      Entry entry = this.keyStore.getEntry(alias, protectionParameter);
      return ObjectUtils.cast(entry);

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public KeyStoreUtil setEntry(String alias, SecretKey key, Set<Attribute> attributes, char[] entryPassword) throws CryptoException {
    return setEntry(alias, key, attributes, new KeyStore.PasswordProtection(entryPassword));
  }

  public KeyStoreUtil setEntry(String alias, SecretKey key, Set<Attribute> attributes, ProtectionParameter protectionParameter) throws CryptoException {
    initialize();
    try {
      if (attributes == null) {
        attributes = Collections.emptySet();
      }
      this.keyStore.setEntry(alias, new KeyStore.SecretKeyEntry(key, attributes), protectionParameter);
      return this;
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public KeyStoreUtil setEntry(String alias, PrivateKey privateKey, Certificate[] certificateChain, Set<Attribute> attributes, char[] entryPassword)
      throws CryptoException {
    return setEntry(alias, privateKey, certificateChain, attributes, new KeyStore.PasswordProtection(entryPassword));
  }

  public KeyStoreUtil setEntry(String alias, PrivateKey privateKey, Certificate[] certificateChain, Set<Attribute> attributes,
      ProtectionParameter protectionParameter) throws CryptoException {
    initialize();
    try {
      if (attributes == null) {
        attributes = Collections.emptySet();
      }
      this.keyStore.setEntry(alias, new KeyStore.PrivateKeyEntry(privateKey, certificateChain, attributes), protectionParameter);
      return this;
    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    }
  }

  public KeyStoreUtil setEntry(String alias, Certificate trustedCert, Set<Attribute> attributes, char[] entryPassword) throws CryptoException {
    return setEntry(alias, trustedCert, attributes, new KeyStore.PasswordProtection(entryPassword));
  }

  public KeyStoreUtil setEntry(String alias, Certificate trustedCert, Set<Attribute> attributes, ProtectionParameter protectionParameter)
      throws CryptoException {
    initialize();
    try {
      if (attributes == null) {
        attributes = Collections.emptySet();
      }
      this.keyStore.setEntry(alias, new KeyStore.TrustedCertificateEntry(trustedCert, attributes), protectionParameter);
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

  public KeyStoreUtil setPassword(char[] password) {
    assertNotInitialized();
    if (password != null) {
      this.password = Arrays.copyOf(password, password.length);
    }
    return this;
  }

  public KeyStoreUtil setPassword(String passwordOrEnv) {
    assertNotInitialized();

    if (passwordOrEnv != null) {
      String resolvedValue = SYS.resolve(passwordOrEnv);
      this.password = resolvedValue.toCharArray();
    }
    return this;
  }

  public KeyStoreUtil setInputStream(InputStream inputStream) {
    assertNotInitialized();
    this.inputStream = inputStream;
    return this;
  }
}
