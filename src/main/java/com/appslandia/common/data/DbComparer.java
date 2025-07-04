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

package com.appslandia.common.data;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import com.appslandia.common.base.TextBuilder;
import com.appslandia.common.jdbc.ConnectionImpl;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class DbComparer {

  public static String compare(DataSource ds1, DataSource ds2) throws SQLException {
    Arguments.notNull(ds1);
    Arguments.notNull(ds2);

    try (var rc1 = new RecordContext(ds1); var rc2 = new RecordContext(ds2)) {

      var result = new TextBuilder(512);

      result.appendln("***** Data Source 1 vs Data Source 2 *****");
      result.appendln();

      if (!rc1.getConnection().getMetaData().getURL().equalsIgnoreCase(rc2.getConnection().getMetaData().getURL())) {
        result.appendln(rc1.getConnection().getMetaData().getURL());
        result.appendln("!=");
        result.appendln(rc2.getConnection().getMetaData().getURL());
        result.appendln();
      }

      var tableNames = getTableNames(rc1.getConnection(), rc2.getConnection());
      for (String tableName : tableNames) {

        var table1 = rc1.getTable(tableName);
        var table2 = rc2.getTable(tableName);

        result.appendln(STR.fmt("***** {} *****", tableName));
        result.appendln();

        if (table1 == null || table2 == null) {

          if (table1 != null) {
            result.appendln(table1.getName());
            result.appendln("!=");
            result.appendln("_____");
            result.appendln();

          } else if (table2 != null) {
            result.appendln("_____");
            result.appendln("!=");
            result.appendln(table2.getName());
            result.appendln();
          }
          continue;
        }

        var colIndex = 0;

        // Columns
        for (Column col1 : table1.getColumns()) {
          var col2 = table2.getColumns().stream().filter(c -> c.getName().equalsIgnoreCase(col1.getName())).findFirst()
              .orElse(null);

          if (col2 == null) {
            colIndex++;

            result.appendln(STR.fmt("{}. {}", colIndex, toString(col1)));
            result.appendln("!=");
            result.appendln(STR.fmt("{}. {}", colIndex, "_____"));
            result.appendln();
          }
        }

        for (Column col2 : table2.getColumns()) {
          var col1 = table1.getColumns().stream().filter(c -> c.getName().equalsIgnoreCase(col2.getName())).findFirst()
              .orElse(null);

          if (col1 == null) {
            colIndex++;

            result.appendln(STR.fmt("{}. {}", colIndex, "_____"));
            result.appendln("!=");
            result.appendln(STR.fmt("{}. {}", colIndex, toString(col2)));
            result.appendln();
          }
        }

        for (Column col1 : table1.getColumns()) {
          var col2 = table2.getColumns().stream().filter(c -> c.getName().equalsIgnoreCase(col1.getName())).findFirst()
              .orElse(null);

          if (col2 != null) {
            colIndex++;

            var col1Str = toString(col1);
            var col2Str = toString(col2);

            if (!col1Str.equalsIgnoreCase(col2Str)) {
              result.appendln(STR.fmt("{}. {}", colIndex, toString(col1)));
              result.appendln("!=");
              result.appendln(STR.fmt("{}. {}", colIndex, toString(col2)));
              result.appendln();
            } else {
              result.appendln(STR.fmt("{}. {} matched.", colIndex, col1.getName()));
              result.appendln();
            }
          }
        }
      }
      return result.toString();
    }
  }

  static Set<String> getTableNames(ConnectionImpl conn1, ConnectionImpl conn2) throws SQLException {
    Set<String> tables = new TreeSet<>();

    try (
        var rs = conn1.getMetaData().getTables(conn1.getCatalog(), conn1.getSchema(), null, new String[] { "TABLE" })) {
      while (rs.next()) {
        tables.add(rs.getString("TABLE_NAME"));
      }
    }

    try (
        var rs = conn2.getMetaData().getTables(conn2.getCatalog(), conn2.getSchema(), null, new String[] { "TABLE" })) {
      while (rs.next()) {
        tables.add(rs.getString("TABLE_NAME"));
      }
    }
    return tables;
  }

  static String toString(Column col) {
    return STR.fmt("name={}, typeName={}, sqlType={}, columnSize={?}, fractionDigits={?}, nullable={}, columnType={}",
        col.getName(), col.getTypeName(), col.getSqlType(), col.getColumnSize(), col.getFractionDigits(),
        col.isNullable(), col.getColumnType());
  }
}
