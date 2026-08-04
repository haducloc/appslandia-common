// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

/**
 *
 * @author Loc Ha
 *
 */
public class JdbcNonUniqueResultException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public JdbcNonUniqueResultException() {
    super("Expected single result, but multiple rows were returned.");
  }

  public JdbcNonUniqueResultException(String message) {
    super(message);
  }
}
