// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.cdi;

/**
 *
 * @author Loc Ha
 *
 */
public interface CDIEventListener<E> {

  void onEvent(E event);

  void onEventAsync(E event);
}
