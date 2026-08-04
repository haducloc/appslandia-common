// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.security.SecureRandom;

/**
 *
 * @author Loc Ha
 *
 */
public class SecureRand {

  private static class Holder {
    private static final SecureRandom INSTANCE = new SecureRandom();
  }

  public static SecureRandom getInstance() {
    return Holder.INSTANCE;
  }

  public static void reseed() {
    Holder.INSTANCE.reseed();
  }
}
