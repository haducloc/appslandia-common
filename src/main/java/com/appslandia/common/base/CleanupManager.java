// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class CleanupManager {

  private static final List<Runnable> hooks = Collections.synchronizedList(new ArrayList<>());
  private static final AtomicBoolean cleaned = new AtomicBoolean(false);

  public static void register(Runnable cleanupHook) {
    Arguments.notNull(cleanupHook);

    if (cleaned.get()) {
      throw new IllegalStateException("Cannot register() after cleanup() has run.");
    }
    hooks.add(cleanupHook);
  }

  public static void cleanup() {
    if (cleaned.compareAndSet(false, true)) {
      var it = hooks.listIterator(hooks.size());
      while (it.hasPrevious()) {
        try {
          var hook = it.previous();
          hook.run();

        } catch (Exception ex) {
          // Ignore
        }
      }
      hooks.clear();
    }
  }
}
