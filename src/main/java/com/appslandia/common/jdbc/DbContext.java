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

import java.io.OutputStream;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import com.appslandia.common.base.DangerTaskConfirm;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author Loc Ha
 *
 */
public class DbContext implements AutoCloseable {

  protected final ConnectionImpl conn;
  protected final boolean bakAutoCommit;
  protected final boolean internalConn;

  protected final Map<String, PreparedStatementImpl> stats = new LinkedHashMap<>();
  final Set<String> batchedPQueries = new LinkedHashSet<>();

  /**
   * Constructs a DbContext using the current SQL connection provided by
   * {@link com.appslandia.common.jdbc.ConnectionImpl#getCurrent()} as the underlying connection.
   *
   */
  public DbContext() throws java.sql.SQLException {
    this(ConnectionImpl.getCurrent());
  }

  public DbContext(DataSource dataSource) throws java.sql.SQLException {
    this(new ConnectionImpl(dataSource), true);
  }

  public DbContext(ConnectionImpl conn) throws java.sql.SQLException {
    this(conn, false);
  }

  protected DbContext(ConnectionImpl conn, boolean internalConn) throws java.sql.SQLException {
    this.conn = conn;
    this.bakAutoCommit = conn.getAutoCommit();
    this.internalConn = internalConn;
  }

  public ConnectionImpl getConnection() {
    return this.conn;
  }

  // Update Utilities

  public int executeUpdate(String sql) throws java.sql.SQLException {
    return this.conn.executeUpdate(sql);
  }

  public int executeUpdate(String pQuery, Object... params) throws java.sql.SQLException {
    return executeUpdate(pQuery, params, false);
  }

  public int executeUpdate(String pQuery, Map<String, Object> params) throws java.sql.SQLException {
    return executeUpdate(pQuery, params, false);
  }

  public int executeUpdate(String pQuery, Object[] params, boolean addBatch) throws java.sql.SQLException {
    return executeUpdate(pQuery, JdbcUtils.toParameters(params), addBatch);
  }

  public int executeUpdate(String pQuery, Map<String, Object> params, boolean addBatch) throws java.sql.SQLException {
    var stat = prepareStatement(pQuery, params);

    if (!addBatch) {
      return stat.executeUpdate();

    } else {
      addBatch(stat, pQuery);
      return -1;
    }
  }

  public void dropTables(DangerTaskConfirm taskConfirm, String... tableNames) throws java.sql.SQLException {
    this.conn.dropTables(taskConfirm, tableNames);
  }

  public void backupTables(String... tableNames) throws java.sql.SQLException {
    this.conn.backupTables(tableNames);
  }

  public int backupTable(String originalTable, String backupTable) throws java.sql.SQLException {
    return this.conn.backupTable(originalTable, backupTable);
  }

  public void truncTables(DangerTaskConfirm taskConfirm, String... tableNames) throws java.sql.SQLException {
    this.conn.truncTables(taskConfirm, tableNames);
  }

  public void resetIdentity(String... tableNames) throws java.sql.SQLException {
    this.conn.resetIdentity(tableNames);
  }

  public String getTableNames(boolean tablePkIdentityOnly) throws java.sql.SQLException {
    return this.conn.getTableNames(tablePkIdentityOnly);
  }

  public String getTableNames(String hasColumnName) throws java.sql.SQLException {
    return this.conn.getTableNames(hasColumnName);
  }

  public String getColumnNames(String tableName) throws java.sql.SQLException {
    return this.conn.getColumnNames(tableName);
  }

  // Execute Utilities

  public String getDistinctValues(String tableName, String columnLabel) throws java.sql.SQLException {
    return this.conn.getDistinctValues(tableName, columnLabel);
  }

  public <K, V> Map<K, V> executeMap(String sql, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper)
      throws java.sql.SQLException {
    return executeMap(sql, keyMapper, valueMapper, new HashMap<>());
  }

  public <K, V> Map<K, V> executeMap(String sql, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper,
      Map<K, V> map) throws java.sql.SQLException {
    return this.conn.executeMap(sql, keyMapper, valueMapper, map);
  }

  public <K, V> Map<K, V> executeMap(String pQuery, Object[] params, ResultSetMapper<K> keyMapper,
      ResultSetMapper<V> valueMapper) throws java.sql.SQLException {
    return executeMap(pQuery, params, keyMapper, valueMapper, new HashMap<>());
  }

  public <K, V> Map<K, V> executeMap(String pQuery, Object[] params, ResultSetMapper<K> keyMapper,
      ResultSetMapper<V> valueMapper, Map<K, V> map) throws java.sql.SQLException {
    return executeMap(pQuery, JdbcUtils.toParameters(params), keyMapper, valueMapper, map);
  }

  public <K, V> Map<K, V> executeMap(String pQuery, Map<String, Object> params, ResultSetMapper<K> keyMapper,
      ResultSetMapper<V> valueMapper) throws java.sql.SQLException {
    return executeMap(pQuery, params, keyMapper, valueMapper, new HashMap<>());
  }

  public <K, V> Map<K, V> executeMap(String pQuery, Map<String, Object> params, ResultSetMapper<K> keyMapper,
      ResultSetMapper<V> valueMapper, Map<K, V> map) throws java.sql.SQLException {
    var stat = prepareStatement(pQuery, params);

    try (var rs = stat.executeQuery()) {
      return JdbcUtils.executeMap(rs, keyMapper, valueMapper, map);
    }
  }

  public <V> Set<V> executeSet(String sql, ResultSetMapper<V> valueMapper) throws java.sql.SQLException {
    return executeSet(sql, valueMapper, new HashSet<>());
  }

  public <V> Set<V> executeSet(String sql, ResultSetMapper<V> valueMapper, Set<V> set) throws java.sql.SQLException {
    return this.conn.executeSet(sql, valueMapper, set);
  }

  public <V> Set<V> executeSet(String pQuery, Object[] params, ResultSetMapper<V> valueMapper)
      throws java.sql.SQLException {
    return executeSet(pQuery, params, valueMapper, new HashSet<>());
  }

  public <V> Set<V> executeSet(String pQuery, Object[] params, ResultSetMapper<V> valueMapper, Set<V> set)
      throws java.sql.SQLException {
    return executeSet(pQuery, JdbcUtils.toParameters(params), valueMapper, set);
  }

  public <V> Set<V> executeSet(String pQuery, Map<String, Object> params, ResultSetMapper<V> valueMapper)
      throws java.sql.SQLException {
    return executeSet(pQuery, params, valueMapper, new HashSet<>());
  }

  public <V> Set<V> executeSet(String pQuery, Map<String, Object> params, ResultSetMapper<V> valueMapper, Set<V> set)
      throws java.sql.SQLException {
    var stat = prepareStatement(pQuery, params);

    try (var rs = stat.executeQuery()) {
      return JdbcUtils.executeSet(rs, valueMapper, set);
    }
  }

  public <T> List<T> executeList(String sql, ResultSetMapper<T> mapper) throws java.sql.SQLException {
    return executeList(sql, mapper, new ArrayList<>());
  }

  public <T> List<T> executeList(String sql, ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
    return this.conn.executeList(sql, mapper, list);
  }

  public <T> List<T> executeList(String pQuery, Object[] params, ResultSetMapper<T> mapper)
      throws java.sql.SQLException {
    return executeList(pQuery, params, mapper, new ArrayList<>());
  }

  public <T> List<T> executeList(String pQuery, Object[] params, ResultSetMapper<T> mapper, List<T> list)
      throws java.sql.SQLException {
    return executeList(pQuery, JdbcUtils.toParameters(params), mapper, list);
  }

  public <T> List<T> executeList(String pQuery, Map<String, Object> params, ResultSetMapper<T> mapper)
      throws java.sql.SQLException {
    return executeList(pQuery, params, mapper, new ArrayList<>());
  }

  public <T> List<T> executeList(String pQuery, Map<String, Object> params, ResultSetMapper<T> mapper, List<T> list)
      throws java.sql.SQLException {
    var stat = prepareStatement(pQuery, params);

    try (var rs = stat.executeQuery()) {
      return JdbcUtils.executeList(rs, mapper, list);
    }
  }

  public <T> T executeSingle(String sql, ResultSetMapper<T> mapper) throws java.sql.SQLException {
    return this.conn.executeSingle(sql, mapper);
  }

  public <T> T executeSingle(String pQuery, Object[] params, ResultSetMapper<T> mapper) throws java.sql.SQLException {
    return executeSingle(pQuery, JdbcUtils.toParameters(params), mapper);
  }

  public <T> T executeSingle(String pQuery, Map<String, Object> params, ResultSetMapper<T> mapper)
      throws java.sql.SQLException {
    var stat = prepareStatement(pQuery, params);

    try (var rs = stat.executeQuery()) {
      return JdbcUtils.executeSingle(rs, mapper);
    }
  }

  public <T> T executeScalar(String sql, Class<T> type) throws java.sql.SQLException {
    return this.conn.executeScalar(sql, type);
  }

  public <T> T executeScalar(String pQuery, Object[] params, Class<T> type) throws java.sql.SQLException {
    return executeScalar(pQuery, JdbcUtils.toParameters(params), type);
  }

  public <T> T executeScalar(String pQuery, Map<String, Object> params, Class<T> type) throws java.sql.SQLException {
    var stat = prepareStatement(pQuery, params);

    try (ResultSet rs = stat.executeQuery()) {
      return JdbcUtils.executeScalar(rs, type);
    }
  }

  public void executeQuery(String sql, ResultSetHandler handler) throws Exception {
    this.conn.executeQuery(sql, handler);
  }

  public void executeQuery(String pQuery, Object[] params, ResultSetHandler handler) throws Exception {
    executeQuery(pQuery, JdbcUtils.toParameters(params), handler);
  }

  public void executeQuery(String pQuery, Map<String, Object> params, ResultSetHandler handler) throws Exception {
    var stat = prepareStatement(pQuery, params);

    try (var rs = stat.executeQuery()) {
      while (rs.next()) {
        handler.handle(rs);
      }
    }
  }

  public void executeStream(String sql, String streamLabel, OutputStream out, ResultSetHandler handler)
      throws Exception {
    this.conn.executeStream(sql, streamLabel, out, handler);
  }

  public void executeStream(String pQuery, Object[] params, String streamLabel, OutputStream out,
      ResultSetHandler handler) throws Exception {
    executeStream(pQuery, JdbcUtils.toParameters(params), streamLabel, out, handler);
  }

  public void executeStream(String pQuery, Map<String, Object> params, String streamLabel, OutputStream out,
      ResultSetHandler handler) throws Exception {
    var stat = prepareStatement(pQuery, params);

    try (var rs = stat.executeQuery()) {
      JdbcUtils.executeStream(rs, streamLabel, out, handler);
    }
  }

  public void executeStream(String sql, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
    this.conn.executeStream(sql, streamLabel, out, handler);
  }

  public void executeStream(String pQuery, Object[] params, String streamLabel, Writer out, ResultSetHandler handler)
      throws Exception {
    executeStream(pQuery, JdbcUtils.toParameters(params), streamLabel, out, handler);
  }

  public void executeStream(String pQuery, Map<String, Object> params, String streamLabel, Writer out,
      ResultSetHandler handler) throws Exception {
    var stat = prepareStatement(pQuery, params);

    try (var rs = stat.executeQuery()) {
      JdbcUtils.executeStream(rs, streamLabel, out, handler);
    }
  }

  public void executeNStream(String sql, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
    this.conn.executeNStream(sql, streamLabel, out, handler);
  }

  public void executeNStream(String pQuery, Object[] params, String streamLabel, Writer out, ResultSetHandler handler)
      throws Exception {
    executeNStream(pQuery, JdbcUtils.toParameters(params), streamLabel, out, handler);
  }

  public void executeNStream(String pQuery, Map<String, Object> params, String streamLabel, Writer out,
      ResultSetHandler handler) throws Exception {
    var stat = prepareStatement(pQuery, params);

    try (var rs = stat.executeQuery()) {
      JdbcUtils.executeNStream(rs, streamLabel, out, handler);
    }
  }

  public void executeBatch() throws java.sql.SQLException {
    this.assertTransactional();
    doBatch("execute");
  }

  public void clearBatch() throws java.sql.SQLException {
    this.assertTransactional();
    doBatch("clear");
  }

  protected void doBatch(String action) throws java.sql.SQLException {
    Arguments.isTrue("execute".equals(action) || "clear".equals(action));

    for (String pQuery : this.batchedPQueries) {
      var stat = Asserts.notNull(this.stats.get(pQuery));

      if ("execute".equals(action)) {
        var updateCounts = stat.executeBatch();

        if (Arrays.stream(updateCounts).anyMatch(code -> code == Statement.EXECUTE_FAILED)) {
          throw new SQLException("Statement.EXECUTE_FAILED found.");
        }
      } else {
        stat.clearBatch();
      }
    }
    this.batchedPQueries.clear();
  }

  protected void addBatch(PreparedStatementImpl stat, String pQuery) throws java.sql.SQLException {
    this.assertTransactional();
    this.batchedPQueries.add(pQuery);
    stat.addBatch();
  }

  public void setTransactional(boolean transactional) throws java.sql.SQLException {
    this.conn.setAutoCommit(!transactional);
  }

  public boolean isTransactional() throws java.sql.SQLException {
    return !this.conn.getAutoCommit();
  }

  public void commit() throws java.sql.SQLException {
    this.assertTransactional();

    this.conn.commit();
  }

  public void rollback() throws java.sql.SQLException {
    this.assertTransactional();

    this.conn.rollback();
  }

  protected void assertTransactional() throws java.sql.SQLException {
    if (this.conn.getAutoCommit()) {
      throw new SQLException("transactional must be enabled.");
    }
  }

  private void closeStatements() throws java.sql.SQLException {
    List<PreparedStatementImpl> stats = new ArrayList<>(this.stats.values());

    for (var i = stats.size() - 1; i >= 0; i--) {
      stats.get(i).close();
    }
  }

  private boolean closed = false;

  @Override
  public void close() throws java.sql.SQLException {
    if (!this.closed) {
      closeStatements();

      this.conn.setAutoCommit(this.bakAutoCommit);

      if (this.internalConn) {
        this.conn.close();
      }
      this.closed = true;
    }
  }

  protected PreparedStatementImpl prepareStatement(String pQuery, Map<String, Object> params)
      throws java.sql.SQLException {
    // StatementImpl
    var query = new SqlQuery(pQuery);
    var stat = this.stats.get(pQuery);

    if (stat == null) {
      stat = this.conn.prepareStatement(query);
      this.stats.put(pQuery, stat);
    }

    // Parameters
    if (params != null) {
      JdbcUtils.setParameters(stat, query, params);
    }
    return stat;
  }
}
