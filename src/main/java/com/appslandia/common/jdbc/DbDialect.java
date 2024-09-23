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

import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DbDialect implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final String POSTGRESQL = "POSTGRESQL";
  public static final String MYSQL = "MYSQL";
  public static final String MARIADB = "MARIADB";
  public static final String MSSQL = "MSSQL";
  public static final String ORACLE = "ORACLE";
  public static final String DB2 = "DB2";
  public static final String SQLITE = "SQLITE";
  public static final String H2 = "H2";

  final String type;
  final String identifierQuote;
  final SqlLikeEscaper likeEscaper;

  private DbDialect(Connection conn) throws java.sql.SQLException {
    DatabaseMetaData mtdt = conn.getMetaData();

    this.type = parseDbType(mtdt.getURL());
    this.identifierQuote = mtdt.getIdentifierQuoteString();
    this.likeEscaper = new SqlLikeEscaper(mtdt.getSearchStringEscape());
  }

  public String getType() {
    return this.type;
  }

  public String getIdentifierQuote() {
    return this.identifierQuote;
  }

  public SqlLikeEscaper getLikeEscaper() {
    return this.likeEscaper;
  }

  public String quoteIdentifier(String identifier) {
    // Use identifierQuote
    if (!" ".equals(this.identifierQuote)) {
      return this.identifierQuote + identifier + this.identifierQuote;
    }
    // Default implementation
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
    return this.likeEscaper.toLikeEscape(value);
  }

  public String toLikePattern(String value, LikeType likeType) {
    return this.likeEscaper.toLikePattern(value, likeType);
  }

  public static DbDialect parse(Connection conn) throws java.sql.SQLException {
    return new DbDialect(conn);
  }

  private static final String parseDbType(String url) {
    if (url.startsWith("jdbc:postgresql")) {
      return POSTGRESQL;
    }
    if (url.startsWith("jdbc:mysql")) {
      return MYSQL;
    }
    if (url.startsWith("jdbc:mariadb")) {
      return MARIADB;
    }
    if (url.startsWith("jdbc:sqlserver")) {
      return MSSQL;
    }
    if (url.startsWith("jdbc:oracle")) {
      return ORACLE;
    }
    if (url.startsWith("jdbc:db2")) {
      return DB2;
    }
    if (url.startsWith("jdbc:sqlite")) {
      return SQLITE;
    }
    if (url.startsWith("jdbc:h2")) {
      return H2;
    }
    throw new IllegalArgumentException(STR.fmt("Failed to parse type from: {}", url));
  }
}
