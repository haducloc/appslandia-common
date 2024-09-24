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
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import com.appslandia.common.base.InitializeException;
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
  private String identifierQuote;
  private SqlLikeEscaper likeEscaper;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.type);
    Asserts.notNull(this.identifierQuote);
    Asserts.notNull(this.likeEscaper);
  }

  @Override
  public DbDialect initialize() throws InitializeException {
    return (DbDialect) super.initialize();
  }

  public DbDialect parse(Connection conn) throws java.sql.SQLException {
    this.assertNotInitialized();
    DatabaseMetaData mtdt = conn.getMetaData();

    this.type = DbType.parseDbType(mtdt.getURL());
    this.identifierQuote = mtdt.getIdentifierQuoteString();
    this.likeEscaper = new SqlLikeEscaper(mtdt.getSearchStringEscape());

    return this;
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

  public String getIdentifierQuote() {
    this.initialize();
    return this.identifierQuote;
  }

  public DbDialect setIdentifierQuote(String identifierQuote) {
    this.assertNotInitialized();
    this.identifierQuote = identifierQuote;
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

    if (!" ".equals(this.identifierQuote)) {
      return this.identifierQuote + identifier + this.identifierQuote;
    }

    switch (this.type) {
    case MYSQL:
    case SQLITE:
      return "`" + identifier + "`";
    case MSSQL:
      return "[" + identifier + "]";
    case POSTGRESQL:
    case ORACLE:
    case DB2:
    case H2:
      return "\"" + identifier + "\"";
    default:
      return identifier;
    }
  }

  public String toLikeEscape(String value) {
    this.initialize();
    return this.likeEscaper.toLikeEscape(value);
  }

  public String toLikePattern(String value, LikeType likeType) {
    this.initialize();
    return this.likeEscaper.toLikePattern(value, likeType);
  }
}