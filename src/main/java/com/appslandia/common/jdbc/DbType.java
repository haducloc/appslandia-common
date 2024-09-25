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

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public enum DbType {

  // @formatter:off
  POSTGRESQL("jdbc:postgresql:"),
  MYSQL("jdbc:mysql:"),
  MARIADB("jdbc:mariadb:"),
  MSSQL("jdbc:sqlserver:"),
  SQLITE("jdbc:sqlite:"),
  H2("jdbc:h2:"),
  ORACLE("jdbc:oracle:"),
  DB2("jdbc:db2:"),
  SAP_HANA("jdbc:sap:");
  // @formatter:on

  private final String urlPrefix;

  DbType(String urlPrefix) {
    this.urlPrefix = urlPrefix;
  }

  public String getUrlPrefix() {
    return this.urlPrefix;
  }

  public static DbType parseDbType(String databaseUrl) {
    for (DbType dbType : DbType.values()) {
      if (databaseUrl.startsWith(dbType.getUrlPrefix())) {
        return dbType;
      }
    }
    throw new IllegalArgumentException("Failed to parse type from: " + databaseUrl);
  }
}