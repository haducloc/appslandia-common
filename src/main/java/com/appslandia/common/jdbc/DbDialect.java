// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DbDialect extends InitializeObject implements Serializable {
  private static final long serialVersionUID = 1L;

  private DbType type;
  private DbIdentifierQuoter identifierQuoter;
  private SqlLikeEscaper likeEscaper;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.type);
    Asserts.notNull(this.identifierQuoter);
    Asserts.notNull(this.likeEscaper);
  }

  public DbType getType() {
    this.initialize();
    return this.type;
  }

  public DbDialect setType(DbType type) {
    this.assertNotInitialized();
    this.type = type;
    return this;
  }

  public DbIdentifierQuoter getIdentifierQuoter() {
    this.initialize();
    return this.identifierQuoter;
  }

  public DbDialect setIdentifierQuoter(DbIdentifierQuoter identifierQuoter) {
    this.assertNotInitialized();
    this.identifierQuoter = identifierQuoter;
    return this;
  }

  public SqlLikeEscaper getLikeEscaper() {
    this.initialize();
    return this.likeEscaper;
  }

  public DbDialect setLikeEscaper(SqlLikeEscaper likeEscaper) {
    this.assertNotInitialized();
    this.likeEscaper = likeEscaper;
    return this;
  }

  public String quoteIdentifier(String identifier) {
    this.initialize();
    return this.identifierQuoter.quoteIdentifier(identifier);
  }

  public String toLikeEscape(String value) {
    this.initialize();
    return this.likeEscaper.toLikeEscape(value);
  }

  public String toLikePattern(String value, LikeType likeType) {
    this.initialize();
    return this.likeEscaper.toLikePattern(value, likeType);
  }

  public static final DbDialect DIALECT_POSTGRESQL = new DbDialect().setType(DbType.POSTGRESQL)
      .setIdentifierQuoter(identifier -> quoteIdentifier(identifier, "\"")).setLikeEscaper(new SqlLikeEscaper("\\"));

  public static final DbDialect DIALECT_MYSQL = new DbDialect().setType(DbType.MYSQL)
      .setIdentifierQuoter(identifier -> quoteIdentifier(identifier, "`")).setLikeEscaper(new SqlLikeEscaper("\\"));

  public static final DbDialect DIALECT_MARIADB = new DbDialect().setType(DbType.MARIADB)
      .setIdentifierQuoter(identifier -> quoteIdentifier(identifier, "`")).setLikeEscaper(new SqlLikeEscaper("\\"));

  public static final DbDialect DIALECT_MSSQL = new DbDialect().setType(DbType.MSSQL)
      .setIdentifierQuoter(identifier -> quoteIdentifier(identifier, "\"")).setLikeEscaper(new SqlLikeEscaper("\\"));

  public static final DbDialect DIALECT_SQLITE = new DbDialect().setType(DbType.SQLITE)
      .setIdentifierQuoter(identifier -> quoteIdentifier(identifier, "\"")).setLikeEscaper(new SqlLikeEscaper("\\"));

  public static final DbDialect DIALECT_H2 = new DbDialect().setType(DbType.H2)
      .setIdentifierQuoter(identifier -> quoteIdentifier(identifier, "\"")).setLikeEscaper(new SqlLikeEscaper("\\"));

  public static final DbDialect DIALECT_ORACLE = new DbDialect().setType(DbType.ORACLE)
      .setIdentifierQuoter(identifier -> quoteIdentifier(identifier, "\"")).setLikeEscaper(new SqlLikeEscaper("\\"));

  public static final DbDialect DIALECT_DB2 = new DbDialect().setType(DbType.DB2)
      .setIdentifierQuoter(identifier -> quoteIdentifier(identifier, "\"")).setLikeEscaper(new SqlLikeEscaper("\\"));

  public static DbDialect parse(String databaseUrl) {
    DbType type = DbType.parseDbType(databaseUrl);

    switch (type) {
    case POSTGRESQL:
      return DIALECT_POSTGRESQL;
    case MYSQL:
      return DIALECT_MYSQL;
    case MARIADB:
      return DIALECT_MARIADB;
    case MSSQL:
      return DIALECT_MSSQL;
    case SQLITE:
      return DIALECT_SQLITE;
    case H2:
      return DIALECT_H2;
    case ORACLE:
      return DIALECT_ORACLE;
    case DB2:
      return DIALECT_DB2;
    default:
      throw new IllegalArgumentException("Unsupported database type: " + type);
    }
  }

  static String quoteIdentifier(String identifier, String byStr) {
    return byStr + identifier + byStr;
  }
}