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

package com.appslandia.common.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.appslandia.common.base.TextBuilder;
import com.appslandia.common.jdbc.ConnectionImpl;
import com.appslandia.common.jdbc.DbContext;
import com.appslandia.common.jdbc.JdbcParam;
import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.jdbc.ResultSetImpl;
import com.appslandia.common.jdbc.StatementImpl;
import com.appslandia.common.jdbc.UncheckedSQLException;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RecordContext extends DbContext {

  public RecordContext() throws java.sql.SQLException {
    super();
  }

  public RecordContext(DataSource dataSource) throws java.sql.SQLException {
    super(dataSource);
  }

  public RecordContext(ConnectionImpl conn) throws java.sql.SQLException {
    super(conn);
  }

  public long insert(String tableName, DataRecord dataRecord) throws java.sql.SQLException {
    return this.insert(tableName, dataRecord, false);
  }

  public long insert(String tableName, DataRecord dataRecord, boolean addBatch) throws java.sql.SQLException {
    // Table
    Table table = getTable(tableName);
    String pQuery = table.getInsertQuery().getPQuery();

    // StatementImpl
    StatementImpl stat = this.stats.get(pQuery);
    if (stat == null) {
      stat = this.conn.prepareStatement(table.getInsertQuery(), (table.getIncrKey() != null));
      this.stats.put(pQuery, stat);
    }

    // Parameters
    for (Column column : table.getColumns()) {
      if (column.getColumnType() != ColumnType.KEY_INCR && column.getColumnType() != ColumnType.NON_KEY_GEN) {
        Object val = dataRecord.get(column.getName());

        if (!column.isNullable()) {
          Asserts.notNull(val, () -> STR.fmt("The column value '{}' is required.", column.getName()));
        }
        stat.setObject(column.getName(), new JdbcParam(val, column.getSqlType(), column.getScaleOrLength()));
      }
    }

    // Execute
    if (!addBatch) {
      int rowAffected = stat.executeUpdate();

      // Generated Key
      if (table.getIncrKey() != null) {
        try (ResultSet rs = stat.getGeneratedKeys()) {

          if (rs.next()) {
            long generatedKey = rs.getLong(1);
            dataRecord.set(table.getIncrKey().getName(), generatedKey);

            return generatedKey;
          }
        }
      }
      return rowAffected;

    } else {
      this.assertTransactional();
      addBatch(stat, pQuery);
      return -1;
    }
  }

  public long insert(String tableName, Object entity) throws java.sql.SQLException {
    return this.insert(tableName, entity, false);
  }

  public long insert(String tableName, Object entity, boolean addBatch) throws java.sql.SQLException {
    Table table = getTable(tableName);
    DataRecord dataRecord = RecordUtils.toRecord(table, entity);
    return this.insert(tableName, dataRecord, addBatch);
  }

  public int update(String tableName, DataRecord dataRecord) throws java.sql.SQLException {
    return this.update(tableName, dataRecord, false);
  }

  public int update(String tableName, DataRecord dataRecord, boolean addBatch) throws java.sql.SQLException {
    // Table
    Table table = getTable(tableName);
    String pQuery = table.getUpdateQuery().getPQuery();
    Asserts.isTrue(!pQuery.isEmpty(),
        () -> STR.fmt("The table {} has no key. The update() operation is unsupported.", table.getTableName()));

    // StatementImpl
    StatementImpl stat = this.stats.get(pQuery);
    if (stat == null) {
      stat = this.conn.prepareStatement(table.getUpdateQuery());
      this.stats.put(pQuery, stat);
    }

    // Parameters
    for (Column column : table.getColumns()) {
      if (column.getColumnType() != ColumnType.NON_KEY_GEN) {
        Object val = dataRecord.get(column.getName());

        if (!column.isNullable()) {
          Asserts.notNull(val, () -> STR.fmt("The column value '{}' is required.", column.getName()));
        }
        stat.setObject(column.getName(), new JdbcParam(val, column.getSqlType(), column.getScaleOrLength()));
      }
    }

    // Execute
    if (!addBatch) {
      return stat.executeUpdate();

    } else {
      this.assertTransactional();
      addBatch(stat, pQuery);
      return -1;
    }
  }

  public int update(String tableName, Object entity) throws java.sql.SQLException {
    return this.update(tableName, entity, false);
  }

  public int update(String tableName, Object entity, boolean addBatch) throws java.sql.SQLException {
    Table table = getTable(tableName);
    DataRecord dataRecord = RecordUtils.toRecord(table, entity);
    return this.update(tableName, dataRecord, addBatch);
  }

  public int delete(String tableName, Key key) throws java.sql.SQLException {
    return this.delete(tableName, key, false);
  }

  public int delete(String tableName, Key key, boolean addBatch) throws java.sql.SQLException {
    // Table
    Table table = getTable(tableName);
    String pQuery = table.getDeleteQuery().getPQuery();

    // StatementImpl
    StatementImpl stat = this.stats.get(pQuery);
    if (stat == null) {
      stat = this.conn.prepareStatement(table.getDeleteQuery());
      this.stats.put(pQuery, stat);
    }

    // Parameters
    for (Column column : table.getColumns()) {
      if ((table.getKeysCount() == 0) || column.isKey()) {
        Object val = key.get(column.getName());
        if (column.isKey()) {
          Asserts.notNull(val, () -> STR.fmt("The column value '{}' is required.", column.getName()));
        }
        stat.setObject(column.getName(), new JdbcParam(val, column.getSqlType(), column.getScaleOrLength()));
      }
    }

    // Execute
    if (!addBatch) {
      return stat.executeUpdate();

    } else {
      this.assertTransactional();
      addBatch(stat, pQuery);
      return -1;
    }
  }

  public int delete(String tableName, Object pk) throws java.sql.SQLException {
    return this.delete(tableName, pk, false);
  }

  public int delete(String tableName, Object pk, boolean addBatch) throws java.sql.SQLException {
    Table table = getTable(tableName);
    Key key = RecordUtils.toKey(table, pk);
    return this.delete(tableName, key, addBatch);
  }

  public DataRecord getRecord(String tableName, Key key) throws java.sql.SQLException {
    // Table
    Table table = getTable(tableName);
    String pQuery = table.getGetQuery().getPQuery();

    // StatementImpl
    StatementImpl stat = this.stats.get(pQuery);
    if (stat == null) {
      stat = this.conn.prepareStatement(table.getGetQuery());
      this.stats.put(pQuery, stat);
    }

    // Parameters
    for (Column column : table.getColumns()) {
      if ((table.getKeysCount() == 0) || column.isKey()) {
        Object val = key.get(column.getName());
        if (column.isKey()) {
          Asserts.notNull(val, () -> STR.fmt("The column value '{}' is required.", column.getName()));
        }
        stat.setObject(column.getName(), new JdbcParam(val, column.getSqlType(), column.getScaleOrLength()));
      }
    }

    // Execute
    try (ResultSetImpl rs = stat.executeQuery()) {
      return JdbcUtils.executeSingle(rs, r -> RecordUtils.toRecord(r));
    }
  }

  public DataRecord getRecord(String tableName, Object pk) throws java.sql.SQLException {
    Table table = getTable(tableName);
    Key key = RecordUtils.toKey(table, pk);
    return this.getRecord(tableName, key);
  }

  public boolean exists(String tableName, Key key) throws java.sql.SQLException {
    // Table
    Table table = getTable(tableName);
    String pQuery = table.getExistsQuery().getPQuery();

    // StatementImpl
    StatementImpl stat = this.stats.get(pQuery);
    if (stat == null) {
      stat = this.conn.prepareStatement(table.getExistsQuery());
      this.stats.put(pQuery, stat);
    }

    // Parameters
    for (Column column : table.getColumns()) {
      if ((table.getKeysCount() == 0) || column.isKey()) {
        Object val = key.get(column.getName());
        if (column.isKey()) {
          Asserts.notNull(val, () -> STR.fmt("The column value '{}' is required.", column.getName()));
        }
        stat.setObject(column.getName(), new JdbcParam(val, column.getSqlType(), column.getScaleOrLength()));
      }
    }
    Integer count = stat.executeScalar(Integer.class);
    return count > 0;
  }

  public boolean exists(String tableName, Object pk) throws java.sql.SQLException {
    Table table = getTable(tableName);
    Key key = RecordUtils.toKey(table, pk);
    return this.exists(tableName, key);
  }

  // Record utilities

  public List<DataRecord> executeList(String sql) throws java.sql.SQLException {
    return executeList(sql, new ArrayList<>());
  }

  public List<DataRecord> executeList(String sql, List<DataRecord> list) throws java.sql.SQLException {
    try (Statement stat = this.conn.createStatement()) {
      try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

        return JdbcUtils.executeList(rs, r -> RecordUtils.toRecord(r), list);
      }
    }
  }

  public List<DataRecord> executeList(String pQuery, Object... params) throws java.sql.SQLException {
    return executeList(pQuery, params, new ArrayList<>());
  }

  public List<DataRecord> executeList(String pQuery, Object[] params, List<DataRecord> list)
      throws java.sql.SQLException {
    return executeList(pQuery, JdbcUtils.toParameters(params), list);
  }

  public List<DataRecord> executeList(String pQuery, Map<String, Object> params) throws java.sql.SQLException {
    return executeList(pQuery, params, new ArrayList<>());
  }

  public List<DataRecord> executeList(String pQuery, Map<String, Object> params, List<DataRecord> list)
      throws java.sql.SQLException {
    StatementImpl stat = prepareStatement(pQuery, params);

    try (ResultSetImpl rs = stat.executeQuery()) {

      return JdbcUtils.executeList(rs, r -> RecordUtils.toRecord(r), list);
    }
  }

  public DataRecord executeSingle(String sql) throws java.sql.SQLException {
    return executeSingle(sql, rs -> {

      return RecordUtils.toRecord(rs);
    });
  }

  public DataRecord executeSingle(String pQuery, Object... params) throws java.sql.SQLException {
    return executeSingle(pQuery, JdbcUtils.toParameters(params));
  }

  public DataRecord executeSingle(String pQuery, Map<String, Object> params) throws java.sql.SQLException {
    return executeSingle(pQuery, params, rs -> {

      return RecordUtils.toRecord(rs);
    });
  }

  public Table getTable(String tableName) throws UncheckedSQLException {
    ConcurrentMap<String, Table> tables = TABLES.computeIfAbsent(this.conn.getDataSourceId(),
        db -> new ConcurrentHashMap<>());

    return tables.computeIfAbsent(tableName, tn -> {
      try {
        Table table = RecordUtils.loadTable(this.conn, this.conn.getCatalog(), this.conn.getSchema(), tableName, null);
        if (table == null) {
          throw new IllegalArgumentException(STR.fmt("The table {} is not found.", tableName));
        }
        return table;

      } catch (SQLException ex) {
        throw new UncheckedSQLException(ex);
      }
    });
  }

  public String getColumnNames(String tableName) throws UncheckedSQLException {
    Table table = getTable(tableName);
    return table.getColumns().stream().map(c -> c.getName()).collect(Collectors.joining(", "));
  }

  public String getColumnSetters(String tableName) throws UncheckedSQLException {
    Table table = getTable(tableName);
    TextBuilder setters = new TextBuilder();

    setters.append("// ").append(tableName).appendln();
    setters.append("var dataRecord = new DataRecord();").appendln();

    for (Column col : table.getColumns()) {

      String code = null;
      if (col.isNullable()) {
        code = STR.fmt("dataRecord.set(\"{}\", {});", col.getName(), "NULLABLE");
      } else {
        code = STR.fmt("dataRecord.set(\"{}\", {});", col.getName(), "REQUIRED");
      }
      setters.append(code).appendln();

      if ((col.getPosition() + 1) % 5 == 0) {
        setters.appendln();
      }
    }
    return setters.toString();
  }

  private static final ConcurrentMap<String, ConcurrentMap<String, Table>> TABLES = new ConcurrentHashMap<>();
}
