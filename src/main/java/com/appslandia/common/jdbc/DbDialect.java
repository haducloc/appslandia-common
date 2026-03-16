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
  private ResetIdentityAction resetIdentityAction;

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

  public ResetIdentityAction getResetIdentityAction() {
    initialize();
    return resetIdentityAction;
  }

  public DbDialect setResetIdentityAction(ResetIdentityAction resetIdentityAction) {
    assertNotInitialized();
    this.resetIdentityAction = resetIdentityAction;
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
    Arguments.notNull(resetIdentityAction, "resetIdentityAction is null.");
    return resetIdentityAction.resetIdentity(conn, tableName);
  }

  public static final DbDialect DIALECT_POSTGRESQL = new DbDialect().setType(DbType.POSTGRESQL).setIdQuoteChar('"')
      .setLikeEscaper(new SqlLikeEscaper('\\'))
      .setResetIdentityAction(new ResetIdentityAction.PostgreSQLResetIdentityAction());

  public static final DbDialect DIALECT_MYSQL = new DbDialect().setType(DbType.MYSQL).setIdQuoteChar('`')
      .setLikeEscaper(new SqlLikeEscaper('\\'))
      .setResetIdentityAction(new ResetIdentityAction.MySQLResetIdentityAction());

  public static final DbDialect DIALECT_MARIADB = new DbDialect().setType(DbType.MARIADB).setIdQuoteChar('`')
      .setLikeEscaper(new SqlLikeEscaper('\\'))
      .setResetIdentityAction(new ResetIdentityAction.MySQLResetIdentityAction());

  public static final DbDialect DIALECT_MSSQL = new DbDialect().setType(DbType.MSSQL).setIdQuoteChar('"')
      .setLikeEscaper(new SqlLikeEscaper('\\'))
      .setResetIdentityAction(new ResetIdentityAction.MSSQLResetIdentityAction());

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
