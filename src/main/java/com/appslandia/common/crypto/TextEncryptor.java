// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import com.appslandia.common.base.DestroyingSupport;

/**
 *
 * @author Loc Ha
 *
 */
public interface TextEncryptor extends DestroyingSupport {

  public String encrypt(String message) throws CryptoException;

  public String decrypt(String message) throws CryptoException;
}
