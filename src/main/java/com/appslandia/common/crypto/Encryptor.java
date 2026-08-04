// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import com.appslandia.common.base.DestroyingSupport;

/**
 *
 * @author Loc Ha
 *
 */
public interface Encryptor extends DestroyingSupport {

  byte[] encrypt(byte[] message) throws CryptoException;

  byte[] decrypt(byte[] message) throws CryptoException;
}
