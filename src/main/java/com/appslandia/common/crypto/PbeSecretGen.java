// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.appslandia.common.base.DestroyingException;
import com.appslandia.common.base.DestroyingSupport;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.SYS;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class PbeSecretGen extends InitializingObject implements DestroyingSupport {
  protected String algorithm, provider;

  protected char[] password;
  protected Integer saltSize;
  protected Integer iterationCount;
  protected Integer keySize;

  public PbeSecretGen() {
  }

  public PbeSecretGen(String algorithm) {
    this.algorithm = algorithm;
  }

  public PbeSecretGen(String algorithm, String provider) {
    this.algorithm = algorithm;
    this.provider = provider;
  }

  @Override
  protected void init() throws Exception {
    Arguments.notNull(password, "password is required.");

    algorithm = ValueUtils.valueOrAlt(algorithm, "PBKDF2WithHmacSHA256");
    saltSize = ValueUtils.valueOrAlt(saltSize, 16);
    iterationCount = ValueUtils.valueOrAlt(iterationCount, 100_000);
    keySize = ValueUtils.valueOrAlt(keySize, 32);
  }

  @Override
  public void destroy() throws DestroyingException {
    CryptoUtils.clear(password);
  }

  protected SecretKeyFactory getImpl() throws GeneralSecurityException {
    SecretKeyFactory impl = null;
    if (provider == null) {
      impl = SecretKeyFactory.getInstance(algorithm);
    } else {
      impl = SecretKeyFactory.getInstance(algorithm, provider);
    }
    return impl;
  }

  protected void release(SecretKeyFactory impl) {
  }

  public SecretKey generate(String algorithm, Out<byte[]> genSalt) throws CryptoException {
    initialize();
    genSalt.value = CryptoUtils.randomBytes(saltSize);
    return generate(algorithm, genSalt.value);
  }

  public SecretKey generate(String algorithm, byte[] salt) throws CryptoException {
    initialize();

    var keySpec = new PBEKeySpec(password, salt, iterationCount, keySize * 8);
    SecretKeyFactory impl = null;
    try {
      impl = getImpl();
      var secret = impl.generateSecret(keySpec);
      var kBytes = secret.getEncoded();
      CryptoUtils.destroy(secret);

      SecretKey key = new DSecretKeySpec(kBytes, algorithm);
      CryptoUtils.clear(kBytes);
      return key;

    } catch (GeneralSecurityException ex) {
      throw new CryptoException(ex);
    } finally {
      keySpec.clearPassword();
      if (impl != null) {
        release(impl);
      }
    }
  }

  public String getAlgorithm() {
    initialize();
    return algorithm;
  }

  public PbeSecretGen setAlgorithm(String algorithm) {
    assertNotInitialized();
    this.algorithm = algorithm;
    return this;
  }

  public String getProvider() {
    initialize();
    return provider;
  }

  public PbeSecretGen setProvider(String provider) {
    assertNotInitialized();
    this.provider = provider;
    return this;
  }

  public int getSaltSize() {
    initialize();
    return saltSize;
  }

  public PbeSecretGen setSaltSize(Integer saltSize) {
    assertNotInitialized();
    this.saltSize = saltSize;
    return this;
  }

  public int getIterationCount() {
    initialize();
    return iterationCount;
  }

  public PbeSecretGen setIterationCount(Integer iterationCount) {
    assertNotInitialized();
    this.iterationCount = iterationCount;
    return this;
  }

  public int getKeySize() {
    initialize();
    return keySize;
  }

  public PbeSecretGen setKeySize(Integer keySize) {
    assertNotInitialized();
    this.keySize = keySize;
    return this;
  }

  public PbeSecretGen setPassword(char[] password) {
    assertNotInitialized();
    if (password != null) {
      this.password = Arrays.copyOf(password, password.length);
    }
    return this;
  }

  public PbeSecretGen setPassword(String passwordExpr) {
    assertNotInitialized();

    if (passwordExpr != null) {
      var resolvedValue = SYS.resolve(passwordExpr);

      if (resolvedValue == null) {
        throw new IllegalArgumentException("Failed to resolve expression: " + passwordExpr);
      }
      password = resolvedValue.toCharArray();
    }
    return this;
  }
}
