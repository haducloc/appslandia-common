// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *
 * @author Loc Ha
 *
 */
public class SEQ {

  private static final AtomicInteger seq = new AtomicInteger(0);

  public static int next() {
    return seq.incrementAndGet();
  }

  public static int current() {
    return seq.get();
  }
}
