// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import com.appslandia.common.base.DestroyingSupport;

/**
 *
 * @author Loc Ha
 *
 */
public interface Digester extends DestroyingSupport {

  byte[] digest(byte[] message) throws CryptoException;

  boolean verify(byte[] message, byte[] digested) throws CryptoException;
}
