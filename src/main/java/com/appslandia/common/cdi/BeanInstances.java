// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.cdi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 *
 * @author Loc Ha
 *
 */
public class BeanInstances {

  private final List<BeanInstance<?>> instances = Collections.synchronizedList(new ArrayList<>());
  private final AtomicBoolean destroyed = new AtomicBoolean(false);

  public void add(BeanInstance<?> bi) {
    if (destroyed.get()) {
      throw new IllegalStateException("Cannot add() after destroy() has run.");
    }
    instances.add(bi);
  }

  public void destroy(Consumer<Exception> logger) {
    if (destroyed.compareAndSet(false, true)) {
      var it = instances.listIterator(instances.size());
      while (it.hasPrevious()) {
        try {
          var bi = it.previous();
          bi.destroy();

        } catch (Exception ex) {
          logger.accept(ex);
        }
      }
      instances.clear();
    }
  }
}
