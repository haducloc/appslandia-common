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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.IOUtils;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JdbcUtils {

  public static String getDataSourceId(Connection conn) throws UncheckedSQLException {
    try {
      var url = conn.getMetaData().getURL();
      if (url != null) {
        return url;
      }
      throw new SQLException(STR.fmt("Couldn't determine dataSourceId: conn={}", conn));

    } catch (SQLException ex) {
      throw new UncheckedSQLException(ex);
    }
  }

  public static List<ResultSetColumn> getResultSetColumns(ResultSet rs) throws SQLException {
    ResultSetMetaData md = rs.getMetaData();
    List<ResultSetColumn> cols = new ArrayList<>(md.getColumnCount());

    for (int col = 1; col <= md.getColumnCount(); col++) {
      cols.add(new ResultSetColumn(col, md.getColumnLabel(col), md.getColumnType(col)));
    }
    return Collections.unmodifiableList(cols);
  }

  public static String getColumnNames(ConnectionImpl conn, String catalog, String schema, String tableName)
      throws SQLException {
    Arguments.notNull(conn);

    List<String> columns = new ArrayList<>();
    try (ResultSet rs = conn.getMetaData().getColumns(catalog, schema, tableName, null)) {
      while (rs.next()) {

        String columnName = rs.getString("COLUMN_NAME");
        columns.add(columnName);
      }
    }
    return columns.stream().map(c -> "\"" + c + "\"").collect(Collectors.joining(", "));
  }

  public static String getTableNames(ConnectionImpl conn, String catalog, String schema, boolean tablePkIdentityOnly)
      throws SQLException {
    Arguments.notNull(conn);
    StringBuilder result = new StringBuilder();

    // DatabaseMetaData
    DatabaseMetaData metaData = conn.getMetaData();

    // Table
    TreeSet<String> tables = new TreeSet<>();

    // Get all tables in the schema
    try (ResultSet rs = metaData.getTables(catalog, schema, null, new String[] { "TABLE" })) {
      while (rs.next()) {
        String tname = rs.getString("TABLE_NAME");
        tables.add(tname);
      }
    }

    for (String tableName : tables) {

      // Keys
      Set<String> keys = new HashSet<>();
      try (ResultSet rs = metaData.getPrimaryKeys(catalog, schema, tableName)) {
        while (rs.next()) {
          keys.add(rs.getString("COLUMN_NAME").toLowerCase(Locale.ENGLISH));
        }
      }

      if (tablePkIdentityOnly && keys.size() != 1) {
        continue;
      }

      // IS_AUTOINCREMENT
      boolean isPkIncr = false;
      try (ResultSet rs = metaData.getColumns(catalog, schema, tableName, null)) {
        while (rs.next()) {

          String columnName = rs.getString("COLUMN_NAME");
          boolean isKey = keys.contains(columnName.toLowerCase(Locale.ENGLISH));
          boolean autoIncr = "YES".equals(rs.getString("IS_AUTOINCREMENT"));

          if (isKey && autoIncr) {
            isPkIncr = true;
          }
        }
      }

      if (tablePkIdentityOnly && !isPkIncr) {
        continue;
      }

      if (result.length() > 0) {
        result.append(", ");
      }
      result.append("\"").append(tableName).append("\"");
    }
    return result.toString();
  }

  public static String getPkIdentity(ConnectionImpl conn, String catalog, String schema, String tableName)
      throws SQLException {
    Arguments.notNull(conn);
    Arguments.notNull(tableName);

    // DatabaseMetaData
    DatabaseMetaData metaData = conn.getMetaData();

    // Keys
    Set<String> keys = new HashSet<>();
    try (ResultSet rs = metaData.getPrimaryKeys(catalog, schema, tableName)) {
      while (rs.next()) {
        keys.add(rs.getString("COLUMN_NAME").toLowerCase(Locale.ENGLISH));
      }
    }

    if (keys.size() != 1) {
      return null;
    }

    // Columns
    try (ResultSet rs = metaData.getColumns(catalog, schema, tableName, null)) {
      while (rs.next()) {

        String columnName = rs.getString("COLUMN_NAME");
        boolean isKey = keys.contains(columnName.toLowerCase(Locale.ENGLISH));
        boolean autoIncr = "YES".equals(rs.getString("IS_AUTOINCREMENT"));

        if (isKey) {
          return autoIncr ? columnName : null;
        }
      }
    }
    return null;
  }

  public static String toFieldName(String dbColumnName) {
    Arguments.notNull(dbColumnName);

    // All Uppers
    if (dbColumnName.chars().allMatch(c -> Character.isUpperCase(c))) {
      return dbColumnName.toLowerCase(Locale.ENGLISH);
    }

    // All Lowers
    if (dbColumnName.chars().allMatch(c -> Character.isLowerCase(c))) {
      return dbColumnName;
    }

    // Mixed
    return StringUtils.firstLowerCase(dbColumnName, Locale.ENGLISH);
  }

  public static Map<String, Object> toParameters(Object[] params) {
    if (params == null) {
      return null;
    }
    return IntStream.range(0, params.length).boxed()
        .collect(Collectors.toMap(idx -> Integer.toString(idx), idx -> params[idx]));
  }

  public static void setParameters(StatementImpl stat, SqlQuery pQuery, Map<String, Object> params)
      throws SQLException {
    for (Map.Entry<String, Integer> np : pQuery.getParamsMap().entrySet()) {
      Asserts.isTrue(params.containsKey(np.getKey()), "The parameter '{}' is required.", np.getKey());

      // Non-array parameter
      if (np.getValue() == null) {
        stat.setObject(np.getKey(), params.get(np.getKey()));

      } else {
        // Array Parameter
        Object pv = params.get(np.getKey());
        Asserts.notNull(pv, "The array parameter '{}' is required.", np.getKey());

        boolean isArray = pv.getClass().isArray();
        boolean isCollection = !isArray && Collection.class.isAssignableFrom(pv.getClass());

        Asserts.isTrue(isArray || isCollection, "The array parameter '{}' must be an array or collection.",
            np.getKey());

        Object[] values = isArray ? ArrayUtils.toArray(pv) : ((Collection<?>) pv).toArray();
        stat.setObjectArray(np.getKey(), values);
      }
    }
  }

  public static String toLikeParamValue(String value, LikeType likeType, DbDialect dbDialect) {
    if (value != null && value.startsWith("\"") && value.endsWith("\"")) {
      return value.substring(1, value.length() - 1);
    } else {
      return dbDialect.toLikePattern(value, likeType);
    }
  }

  // Execute ResultSets

  public static String getDistinctValues(ResultSet rs, String columnLabel) throws SQLException {
    Set<String> values = new TreeSet<>();
    boolean tooMany = false;

    while (rs.next()) {
      Object obj = rs.getObject(columnLabel);
      values.add((obj != null) ? obj.toString() : null);

      if (values.size() > 255) {
        tooMany = true;
        break;
      }
    }

    String valuesAsStr = values.stream().collect(Collectors.joining(", "));
    if (tooMany) {
      return valuesAsStr + ", ...";
    }
    return valuesAsStr;
  }

  public static <K, V> Map<K, V> executeMap(ResultSetImpl rs, ResultSetMapper<K> keyMapper,
      ResultSetMapper<V> valueMapper, Map<K, V> map) throws SQLException {
    while (rs.next()) {

      K k = keyMapper.map(rs);
      V v = valueMapper.map(rs);

      map.put(k, v);
    }
    return map;
  }

  public static <V> Set<V> executeSet(ResultSetImpl rs, ResultSetMapper<V> valueMapper, Set<V> set)
      throws SQLException {
    while (rs.next()) {

      V v = valueMapper.map(rs);
      set.add(v);
    }
    return set;
  }

  public static <T> List<T> executeList(ResultSetImpl rs, ResultSetMapper<T> mapper, List<T> list) throws SQLException {
    while (rs.next()) {
      T t = mapper.map(rs);
      list.add(t);
    }
    return list;
  }

  public static <T> T executeSingle(ResultSetImpl rs, ResultSetMapper<T> mapper) throws SQLException {
    T t = null;
    boolean rsRead = false;

    while (rs.next()) {
      if (rsRead) {
        throw new NonUniqueSQLException();
      }
      rsRead = true;
      t = mapper.map(rs);
    }
    return t;
  }

  public static <T> T executeScalar(ResultSet rs, Class<T> type) throws SQLException {
    while (rs.next()) {
      return (T) rs.getObject(1, type);
    }
    return null;
  }

  public static void executeStream(ResultSetImpl rs, String streamLabel, OutputStream out, ResultSetHandler handler)
      throws Exception {
    boolean rsRead = false;
    while (rs.next()) {

      if (rsRead) {
        throw new NonUniqueSQLException();
      }
      rsRead = true;

      if (handler != null) {
        handler.handle(rs);
      }
      try (InputStream is = rs.getBinaryStream(streamLabel)) {
        IOUtils.copy(is, out);
      }
    }
  }

  public static void executeStream(ResultSetImpl rs, String streamLabel, Writer out, ResultSetHandler handler)
      throws Exception {
    boolean rsRead = false;
    while (rs.next()) {

      if (rsRead) {
        throw new NonUniqueSQLException();
      }
      rsRead = true;

      if (handler != null) {
        handler.handle(rs);
      }
      try (Reader r = rs.getCharacterStream(streamLabel)) {
        IOUtils.copy(r, out);
      }
    }
  }

  public static void executeNStream(ResultSetImpl rs, String streamLabel, Writer out, ResultSetHandler handler)
      throws Exception {
    boolean rsRead = false;
    while (rs.next()) {

      if (rsRead) {
        throw new NonUniqueSQLException();
      }
      rsRead = true;

      if (handler != null) {
        handler.handle(rs);
      }
      try (Reader r = rs.getNCharacterStream(streamLabel)) {
        IOUtils.copy(r, out);
      }
    }
  }
}
