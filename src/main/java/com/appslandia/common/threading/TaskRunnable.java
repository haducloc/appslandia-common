// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.threading;

import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class TaskRunnable<T extends TaskAttributes> implements Runnable {

  protected final T attributes;

  public TaskRunnable(T attributes) {
    this.attributes = Arguments.notNull(attributes);
  }

  @Override
  public void run() {
    try {
      doRun();

    } catch (Exception ex) {
      throw new TaskException(ex.getMessage(), ex);
    }
  }

  protected abstract void doRun() throws Exception;
}
