// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.sql.SQLException;

/**
 *
 * @author Loc Ha
 *
 */
public class UncheckedSQLException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public UncheckedSQLException(String message, SQLException cause) {
    super(message, cause);
  }

  public UncheckedSQLException(SQLException cause) {
    super(cause);
  }

  @Override
  public SQLException getCause() {
    return (SQLException) super.getCause();
  }

  /**
   * Called to read the object from a stream.
   *
   * @throws InvalidObjectException if the object is invalid or has a cause that is not an {@code SQLException}
   */
  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
    var cause = super.getCause();
    if (!(cause instanceof SQLException)) {
      throw new InvalidObjectException("Cause must be an SQLException");
    }
  }
}
