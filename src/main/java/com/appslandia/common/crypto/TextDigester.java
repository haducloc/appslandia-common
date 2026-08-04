// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import com.appslandia.common.base.DestroyingSupport;

/**
 *
 * @author Loc Ha
 *
 */
public interface TextDigester extends DestroyingSupport {

  String digest(String message) throws CryptoException;

  boolean verify(String message, String digested) throws CryptoException;
}
