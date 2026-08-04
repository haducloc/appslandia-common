// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import com.appslandia.common.base.DestroyingSupport;

/**
 *
 * @author Loc Ha
 *
 */
public interface PasswordDigester extends TextDigester, DestroyingSupport {

  @Override
  public String digest(String password) throws CryptoException;

  @Override
  public boolean verify(String password, String digested) throws CryptoException;
}
