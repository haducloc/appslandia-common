// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

/**
 *
 * @author Loc Ha
 *
 */
@FunctionalInterface
public interface ResultSetMapper<T> {

  T map(ResultSetImpl rs) throws java.sql.SQLException;
}
