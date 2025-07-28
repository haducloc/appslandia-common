// The MIT License (MIT)
// Copyright © 2015 Loc Ha

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

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

  private static final AppLogger logger = new AppLogger(CleanupManager.class.getName());

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
          logger.warn(ex.getMessage(), ex);
        }
      }
      hooks.clear();
    }
  }
}
