// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import com.appslandia.common.base.ToStringBuilder.TSIgnore;
import com.appslandia.common.json.JsonIgnore;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class InitializingObject implements InitializingSupport {

  private volatile boolean initialized = false;

  @TSIgnore
  @JsonIgnore
  private final Object mutex = new Object();

  @Override
  public InitializingObject initialize() throws InitializingException {
    if (initialized) {
      return this;
    }
    synchronized (mutex) {
      if (initialized) {
        return this;
      }
      try {
        init();
        initialized = true;

      } catch (Exception ex) {
        throw new InitializingException(ex);
      }
    }
    return this;
  }

  protected abstract void init() throws Exception;

  protected void assertNotInitialized() {
    if (initialized) {
      throw new IllegalStateException("initialized.");
    }
  }
}
