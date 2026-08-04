// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.factory;

/**
 *
 * @author Loc Ha
 *
 */
@FunctionalInterface
public interface ObjectProducer<T> {

  T produce(ObjectFactory factory);

  default void destroy(Object obj) {
  }
}
