// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.threading;

/**
 *
 * @author Loc Ha
 *
 */
public interface TaskAttributes {

  String getTaskId();

  long getSubmittedTime();

  default String getTaskName() {
    return getTaskId();
  }

  default String getDescription() {
    return null;
  }

  default boolean mayInterruptIfRunningOnCancel() {
    return true;
  }
}
