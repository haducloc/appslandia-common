// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

/**
 *
 * @author Loc Ha
 *
 */
public interface InitializingSupport {

  InitializingSupport initialize() throws InitializingException;
}
