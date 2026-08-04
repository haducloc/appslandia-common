// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.security.Key;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.crypto.CryptoException;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class JwkConverter<K extends Key> extends InitializingObject {

  // type of cryptographic key, such as "RSA", "EC", or "oct" (for symmetric
  // keys).
  protected final String kty;

  public JwkConverter(String kty) {
    this.kty = kty;
  }

  public abstract JsonWebKey toJsonWebKey(K key);

  public abstract K fromJsonWebKey(JsonWebKey jwk) throws CryptoException;
}
