// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.cdi;

/**
 *
 * @author Loc Ha
 *
 */
public interface CDIFactory<T> {

  T produce();

  void dispose(T t);
}
