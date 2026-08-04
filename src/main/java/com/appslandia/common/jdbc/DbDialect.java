// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

import java.io.Serializable;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class DbDialect extends InitializingObject implements Serializable {
  private static final long serialVersionUID = 1L;

  private DbType type;
  private Character idQuoteChar;
  private SqlLikeEscaper likeEscaper;
  private DatabaseActions databaseActions;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(type);
    Arguments.notNull(idQuoteChar);
    Arguments.notNull(likeEscaper);
  }

  public DbType getType() {
    initialize();
    return type;
  }

  public DbDialect setType(DbType type) {
    assertNotInitialized();
    this.type = type;
    return this;
  }

  public Character getIdQuoteChar() {
    initialize();
    return idQuoteChar;
  }

  public DbDialect setIdQuoteChar(char idQuoteChar) {
    assertNotInitialized();
    this.idQuoteChar = idQuoteChar;
    return this;
  }

  public SqlLikeEscaper getLikeEscaper() {
    initialize();
    return likeEscaper;
  }

  public DbDialect setLikeEscaper(SqlLikeEscaper likeEscaper) {
    assertNotInitialized();
    this.likeEscaper = likeEscaper;
    return this;
  }

  public DatabaseActions getDatabaseActions() {
    initialize();
    return databaseActions;
  }

  public DbDialect setDatabaseActions(DatabaseActions databaseActions) {
    assertNotInitialized();
    this.databaseActions = databaseActions;
    return this;
  }

  public String quoteIdentifier(String identifier) {
    initialize();
    return idQuoteChar + identifier + idQuoteChar;
  }

  public String toLikeEscape(String value) {
    initialize();
    return likeEscaper.toLikeEscape(value);
  }

  public String toLikePattern(String value, LikeType likeType) {
    initialize();
    return likeEscaper.toLikePattern(value, likeType);
  }

  public boolean resetIdentity(ConnectionImpl conn, String tableName) throws java.sql.SQLException {
    initialize();
    Arguments.notNull(databaseActions, "databaseActions is null.");
    return databaseActions.resetIdentity(conn, tableName);
  }

  public static final DbDialect DIALECT_POSTGRESQL = new DbDialect().setType(DbType.POSTGRESQL).setIdQuoteChar('"')
      .setLikeEscaper(new SqlLikeEscaper('\\')).setDatabaseActions(new DatabaseActions.PostgreSQLDatabaseActions());

  public static final DbDialect DIALECT_MYSQL = new DbDialect().setType(DbType.MYSQL).setIdQuoteChar('`')
      .setLikeEscaper(new SqlLikeEscaper('\\')).setDatabaseActions(new DatabaseActions.MySQLDatabaseActions());

  public static final DbDialect DIALECT_MARIADB = new DbDialect().setType(DbType.MARIADB).setIdQuoteChar('`')
      .setLikeEscaper(new SqlLikeEscaper('\\')).setDatabaseActions(new DatabaseActions.MySQLDatabaseActions());

  public static final DbDialect DIALECT_MSSQL = new DbDialect().setType(DbType.MSSQL).setIdQuoteChar('"')
      .setLikeEscaper(new SqlLikeEscaper('\\')).setDatabaseActions(new DatabaseActions.MSSQLDatabaseActions());

  public static final DbDialect DIALECT_SQLITE = new DbDialect().setType(DbType.SQLITE).setIdQuoteChar('"')
      .setLikeEscaper(new SqlLikeEscaper('\\'));

  public static final DbDialect DIALECT_H2 = new DbDialect().setType(DbType.H2).setIdQuoteChar('"')
      .setLikeEscaper(new SqlLikeEscaper('\\'));

  public static final DbDialect DIALECT_ORACLE = new DbDialect().setType(DbType.ORACLE).setIdQuoteChar('"')
      .setLikeEscaper(new SqlLikeEscaper('\\'));

  public static final DbDialect DIALECT_DB2 = new DbDialect().setType(DbType.DB2).setIdQuoteChar('"')
      .setLikeEscaper(new SqlLikeEscaper('\\'));

  public static final DbDialect DIALECT_SAP_HANA = new DbDialect().setType(DbType.SAP_HANA).setIdQuoteChar('"')
      .setLikeEscaper(new SqlLikeEscaper('\\'));

  public static DbDialect parse(String databaseUrl) {
    var type = DbType.parseDbType(databaseUrl);

    return switch (type) {
    case POSTGRESQL -> DIALECT_POSTGRESQL;
    case MYSQL -> DIALECT_MYSQL;
    case MARIADB -> DIALECT_MARIADB;
    case MSSQL -> DIALECT_MSSQL;
    case SQLITE -> DIALECT_SQLITE;
    case H2 -> DIALECT_H2;
    case ORACLE -> DIALECT_ORACLE;
    case DB2 -> DIALECT_DB2;
    case SAP_HANA -> DIALECT_SAP_HANA;
    default -> throw new IllegalArgumentException("Unsupported database type: " + type);
    };
  }
}
