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

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public enum SqlEngine {

  POSTGRESQL, MYSQL, MARIADB, MSSQL, ORACLE, DB2, SQLITE, H2;

  private SqlEngine() {
  }

  public static SqlEngine parse(String url) {
    Asserts.notNull(url);

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
    throw new IllegalArgumentException(STR.fmt("Failed to parse SqlEngine from: {}", url));
  }
}
