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
import java.sql.BatchUpdateException;
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
  protected final Set<String> bQueries = new LinkedHashSet<>();

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
    bakAutoCommit = conn.getAutoCommit();
    this.internalConn = internalConn;
  }

  public ConnectionImpl getConnection() {
    return conn;
  }

  // Update Utilities

  public int executeUpdate(String sql) throws java.sql.SQLException {
    return conn.executeUpdate(sql);
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

  // Execute Utilities

  public <K, V> Map<K, V> executeMap(String sql, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper)
      throws java.sql.SQLException {
    return executeMap(sql, keyMapper, valueMapper, new HashMap<>());
  }

  public <K, V> Map<K, V> executeMap(String sql, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper,
      Map<K, V> map) throws java.sql.SQLException {
    return conn.executeMap(sql, keyMapper, valueMapper, map);
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
    return conn.executeSet(sql, valueMapper, set);
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
    return conn.executeList(sql, mapper, list);
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
    return conn.executeSingle(sql, mapper);
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

  public long executeCount(String sql) throws java.sql.SQLException {
    return executeScalar(sql, Long.class);
  }

  public long executeCount(String pQuery, Object... params) throws java.sql.SQLException {
    return executeScalar(pQuery, params, Long.class);
  }

  public long executeCount(String pQuery, Map<String, Object> params) throws java.sql.SQLException {
    return executeScalar(pQuery, params, Long.class);
  }

  public <T> T executeScalar(String sql, Class<T> type) throws java.sql.SQLException {
    return conn.executeScalar(sql, type);
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
    conn.executeQuery(sql, handler);
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
    conn.executeStream(sql, streamLabel, out, handler);
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
    conn.executeStream(sql, streamLabel, out, handler);
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
    conn.executeNStream(sql, streamLabel, out, handler);
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

  public void dropTables(DangerTaskConfirm taskConfirm, String... tableNames) throws java.sql.SQLException {
    conn.dropTables(taskConfirm, tableNames);
  }

  public void backupTables(String... tableNames) throws java.sql.SQLException {
    conn.backupTables(tableNames);
  }

  public int backupTable(String originalTable, String backupTable) throws java.sql.SQLException {
    return conn.backupTable(originalTable, backupTable);
  }

  public void truncTables(DangerTaskConfirm taskConfirm, String... tableNames) throws java.sql.SQLException {
    conn.truncTables(taskConfirm, tableNames);
  }

  public void resetIdentity(String... tableNames) throws java.sql.SQLException {
    conn.resetIdentity(tableNames);
  }

  public String getTableNames(boolean tablePkIdentityOnly) throws java.sql.SQLException {
    return conn.getTableNames(tablePkIdentityOnly);
  }

  public String getTableNames(String hasColumnName) throws java.sql.SQLException {
    return conn.getTableNames(hasColumnName);
  }

  public String getColumnNames(String tableName) throws java.sql.SQLException {
    return conn.getColumnNames(tableName);
  }

  public String getDistinctValues(String tableName, String columnLabel) throws java.sql.SQLException {
    return conn.getDistinctValues(tableName, columnLabel);
  }

  public void executeBatch() throws java.sql.SQLException {
    assertTransactional();

    try {
      for (String pQuery : bQueries) {
        var stat = Asserts.notNull(stats.get(pQuery));

        try {
          var updateCounts = stat.executeBatch();

          if (Arrays.stream(updateCounts).anyMatch(code -> code == Statement.EXECUTE_FAILED)) {
            throw new SQLException("Statement.EXECUTE_FAILED found for batch: " + pQuery);
          }

        } catch (BatchUpdateException be) {
          be.addSuppressed(new SQLException("Failed executing batch for: " + pQuery));
          throw be;
        }
      }
    } catch (SQLException ex) {
      try {
        clearBatch();
      } catch (SQLException cbEx) {
        ex.addSuppressed(cbEx);
      }
      throw ex;

    } finally {
      bQueries.clear();
    }
  }

  public void clearBatch() throws java.sql.SQLException {
    assertTransactional();
    try {
      for (String pQuery : bQueries) {
        var stat = Asserts.notNull(stats.get(pQuery));
        stat.clearBatch();
      }
    } finally {
      bQueries.clear();
    }
  }

  protected void addBatch(PreparedStatementImpl stat, String pQuery) throws java.sql.SQLException {
    assertTransactional();
    bQueries.add(pQuery);
    stat.addBatch();
  }

  public void setTransactional(boolean transactional) throws java.sql.SQLException {
    if (!transactional && !bQueries.isEmpty()) {
      throw new SQLException(
          "Pending batches exist. Call executeBatch() or clearBatch() before disabling transactional.");
    }
    conn.setAutoCommit(!transactional);
  }

  public boolean isTransactional() throws java.sql.SQLException {
    return !conn.getAutoCommit();
  }

  public void commit() throws java.sql.SQLException {
    assertTransactional();
    if (!bQueries.isEmpty()) {
      throw new SQLException("Pending batches exist. Call executeBatch() or clearBatch() before commit().");
    }
    conn.commit();
  }

  public void rollback() throws java.sql.SQLException {
    assertTransactional();
    if (!bQueries.isEmpty()) {
      throw new SQLException("Pending batches exist. Call executeBatch() or clearBatch() before rollback().");
    }
    conn.rollback();
  }

  protected void assertTransactional() throws java.sql.SQLException {
    if (conn.getAutoCommit()) {
      throw new SQLException("transactional must be enabled.");
    }
  }

  private boolean closed = false;

  @Override
  public void close() throws java.sql.SQLException {
    if (closed) {
      return;
    }

    SQLException err = null;
    try {
      // Defensive rollback if transaction still open
      try {
        if (!conn.getAutoCommit()) {
          try {
            conn.rollback();
          } catch (SQLException e) {
            err = e;
          }
        }
      } catch (SQLException e) {
        err = e;
      }

      // Close statements
      List<PreparedStatementImpl> stats = new ArrayList<>(this.stats.values());

      for (var i = stats.size() - 1; i >= 0; i--) {
        try {
          stats.get(i).close();
        } catch (SQLException e) {
          if (err == null) {
            err = e;
          } else {
            err.addSuppressed(e);
          }
        }
      }

      // Restore AutoCommit
      try {
        conn.setAutoCommit(bakAutoCommit);
      } catch (SQLException e) {
        if (err == null) {
          err = e;
        } else {
          err.addSuppressed(e);
        }
      }

      // Close connection
      if (internalConn) {
        try {
          conn.close();
        } catch (SQLException e) {
          if (err == null) {
            err = e;
          } else {
            err.addSuppressed(e);
          }
        }
      }

    } finally {
      this.stats.clear();
      this.bQueries.clear();
      closed = true;
    }

    if (err != null) {
      throw err;
    }
  }

  protected PreparedStatementImpl prepareStatement(String pQuery, Map<String, Object> params)
      throws java.sql.SQLException {
    var query = new SqlQuery(pQuery);
    var stat = stats.get(pQuery);

    if (stat == null) {
      stat = conn.prepareStatement(query);
      stats.put(pQuery, stat);
    } else {
      stat.clearParameters();
    }

    if (params != null) {
      JdbcUtils.setParameters(stat, query, params);
    }
    return stat;
  }
}
