// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import com.appslandia.common.base.UncheckedException;

/**
 *
 * @author Loc Ha
 *
 */
public class ThreadUtils {

  public static void sleepInMs(long milliseconds) {
    try {
      Thread.sleep(milliseconds);

    } catch (InterruptedException ex) {
      throw new UncheckedException(ex);
    }
  }

  public static void sleepInSec(int seconds) {
    sleepInMs(seconds * 1000);
  }
}
