// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

/**
 *
 * @author Loc Ha
 *
 */
@FunctionalInterface
public interface ResultSetHandler {

  void handle(ResultSetImpl rs) throws Exception;
}
