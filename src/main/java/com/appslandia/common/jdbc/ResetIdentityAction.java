// The MIT License (MIT)
// Copyright © 2015 Loc Ha

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.jdbc;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 *
 * @author Loc Ha
 *
 */
public interface ResetIdentityAction {

  boolean resetIdentity(ConnectionImpl conn, String tableName) throws java.sql.SQLException;

  public static class MSSQLResetIdentityAction implements ResetIdentityAction {

    @Override
    public boolean resetIdentity(ConnectionImpl conn, String tableName) throws java.sql.SQLException {
      var idPk = JdbcUtils.getPkIdentity(conn, null, null, tableName);
      if (idPk == null) {
        return false;
      }

      var dbDialect = conn.getDbDialect();
      var quotedTable = dbDialect.quoteIdentifier(tableName);

      var curMaxPk = conn.executeScalar(STR.fmt("SELECT MAX({}) FROM {}", dbDialect.quoteIdentifier(idPk), quotedTable),
          Long.class);

      if (curMaxPk == null) {
        curMaxPk = 0L;
      }

      conn.executeUpdate(STR.fmt("DBCC CHECKIDENT ('{}', RESEED, {})", quotedTable, curMaxPk));
      return true;
    }
  }

  public static class MySQLResetIdentityAction implements ResetIdentityAction {

    @Override
    public boolean resetIdentity(ConnectionImpl conn, String tableName) throws java.sql.SQLException {
      var idPk = JdbcUtils.getPkIdentity(conn, null, null, tableName);
      if (idPk == null) {
        return false;
      }

      var dbDialect = conn.getDbDialect();
      var quotedTable = dbDialect.quoteIdentifier(tableName);

      var curMaxPk = conn.executeScalar(STR.fmt("SELECT MAX({}) FROM {}", dbDialect.quoteIdentifier(idPk), quotedTable),
          Long.class);

      if (curMaxPk == null) {
        curMaxPk = 0L;
      }

      conn.executeUpdate(STR.fmt("ALTER TABLE {} AUTO_INCREMENT = {}", quotedTable, curMaxPk + 1));
      return true;
    }
  }

  public static class PostgreSQLResetIdentityAction implements ResetIdentityAction {

    @Override
    public boolean resetIdentity(ConnectionImpl conn, String tableName) throws java.sql.SQLException {
      var idPk = JdbcUtils.getPkIdentity(conn, null, null, tableName);
      if (idPk == null) {
        return false;
      }

      var dbDialect = conn.getDbDialect();
      var quotedTable = dbDialect.quoteIdentifier(tableName);

      var seqName = conn.executeScalar(STR.fmt("SELECT pg_get_serial_sequence('{}', '{}') AS sequence_name",
          quotedTable, dbDialect.quoteIdentifier(idPk)), String.class);
      Asserts.notNull(seqName);

      var curMaxPk = conn.executeScalar(STR.fmt("SELECT MAX({}) FROM {}", dbDialect.quoteIdentifier(idPk), quotedTable),
          Long.class);

      if (curMaxPk == null) {
        curMaxPk = 0L;
      }

      conn.executeUpdate(STR.fmt("SELECT setval('{}', {}, false)", dbDialect.quoteIdentifier(seqName), curMaxPk + 1));
      return true;
    }
  }
}
