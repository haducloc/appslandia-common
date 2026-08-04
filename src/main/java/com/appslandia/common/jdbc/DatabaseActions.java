// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 *
 * @author Loc Ha
 *
 */
public interface DatabaseActions {

  boolean resetIdentity(ConnectionImpl conn, String tableName) throws java.sql.SQLException;

  public static class MSSQLDatabaseActions implements DatabaseActions {

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

  public static class MySQLDatabaseActions implements DatabaseActions {

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

  public static class PostgreSQLDatabaseActions implements DatabaseActions {

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
