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

import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PbeSecretKeyGenerator extends InitializeObject {

  private String algorithm, provider;

  public PbeSecretKeyGenerator() {
  }

  public PbeSecretKeyGenerator(String algorithm) {
    this.algorithm = algorithm;
  }

  public PbeSecretKeyGenerator(String algorithm, String provider) {
    this.algorithm = algorithm;
    this.provider = provider;
  }

  @Override
  protected void init() throws Exception {
    this.algorithm = ValueUtils.valueOrAlt(this.algorithm, "PBKDF2WithHmacSHA256");
  }

  protected SecretKeyFactory getImpl() throws GeneralSecurityException {
    SecretKeyFactory impl = null;
    if (this.provider == null) {
      impl = SecretKeyFactory.getInstance(this.algorithm);
    } else {
      impl = SecretKeyFactory.getInstance(this.algorithm, this.provider);
    }
    return impl;
  }

  public byte[] generate(char[] password, byte[] salt, int iterationCount, int keySize) throws CryptoException {
    this.initialize();
    PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keySize * 8);
    try {
      SecretKey key = this.getImpl().generateSecret(keySpec);
      byte[] kBytes = key.getEncoded();
      CryptoUtils.destroy(key);
      return kBytes;

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      keySpec.clearPassword();
    }
  }

  public String getAlgorithm() {
    this.initialize();
    return this.algorithm;
  }

  public PbeSecretKeyGenerator setAlgorithm(String algorithm) {
    this.assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    this.initialize();
    return this.provider;
  }

  public PbeSecretKeyGenerator setProvider(String provider) {
    this.assertNotInitialized();
    this.provider = provider;
    return this;
  }
}
