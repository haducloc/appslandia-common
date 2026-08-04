// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.cdi;

/**
 *
 * @author Loc Ha
 *
 */
// @Supplier(T.class)
public interface CDISupplier {

  // Return a data structure related to T.class, such as Collection<T>,
  // Map<String, T>, or similar.

  Object get();
}
