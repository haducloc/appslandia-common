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

import com.appslandia.common.jdbc.ConnectionImpl;
import com.appslandia.common.jdbc.DbContext;
import com.appslandia.common.jdbc.JdbcParam;
import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.jdbc.ResultSetImpl;
import com.appslandia.common.jdbc.StatementImpl;
import com.appslandia.common.jdbc.UncheckedSQLException;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RecordContext extends DbContext {

    public RecordContext() {
	super();
    }

    public RecordContext(ConnectionImpl conn) {
	super(conn);
    }

    public long insert(String tableName, DataRecord dataRecord) throws java.sql.SQLException {
	return this.insert(tableName, dataRecord, false);
    }

    public long insert(String tableName, DataRecord dataRecord, boolean addBatch) throws java.sql.SQLException {
	// StatementImpl
	Table table = getTable(tableName);
	StatementImpl stat = this.stats.get(table.getInsertSql().getPSql());

	if (stat == null) {
	    stat = this.conn.prepareStatement(table.getInsertSql(), (table.getIncrKey() != null));
	    this.stats.put(table.getInsertSql().getPSql(), stat);
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
	    addBatch(stat, table.getInsertSql().getPSql());
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
	// StatementImpl
	Table table = getTable(tableName);
	StatementImpl stat = this.stats.get(table.getUpdateSql().getPSql());

	if (stat == null) {
	    stat = this.conn.prepareStatement(table.getUpdateSql());
	    this.stats.put(table.getUpdateSql().getPSql(), stat);
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
	    addBatch(stat, table.getUpdateSql().getPSql());
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
	// StatementImpl
	Table table = getTable(tableName);
	StatementImpl stat = this.stats.get(table.getDeleteSql().getPSql());

	if (stat == null) {
	    stat = this.conn.prepareStatement(table.getDeleteSql());
	    this.stats.put(table.getDeleteSql().getPSql(), stat);
	}

	// Parameters
	for (Column column : table.getColumns()) {
	    if (column.isKey()) {

		Object val = key.get(column.getName());
		Asserts.notNull(val, () -> STR.fmt("The column value '{}' is required.", column.getName()));

		stat.setObject(column.getName(), new JdbcParam(val, column.getSqlType(), column.getScaleOrLength()));
	    }
	}

	// Execute
	if (!addBatch) {
	    return stat.executeUpdate();

	} else {
	    this.assertTransactional();
	    addBatch(stat, table.getDeleteSql().getPSql());
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
	// StatementImpl
	Table table = getTable(tableName);
	StatementImpl stat = this.stats.get(table.getGetSql().getPSql());

	if (stat == null) {
	    stat = this.conn.prepareStatement(table.getGetSql());
	    this.stats.put(table.getGetSql().getPSql(), stat);
	}

	// Parameters
	for (Column column : table.getColumns()) {
	    if (column.isKey()) {

		Object val = key.get(column.getName());
		Asserts.notNull(val, () -> STR.fmt("The column value '{}' is required.", column.getName()));

		stat.setObject(column.getName(), new JdbcParam(val, column.getSqlType(), column.getScaleOrLength()));
	    }
	}

	// Execute
	try (ResultSetImpl rs = stat.executeQuery()) {

	    String[] columnLabels = JdbcUtils.getColumnLabels(rs);
	    return JdbcUtils.executeSingle(rs, r -> RecordUtils.toRecord(r, columnLabels));
	}
    }

    public DataRecord getRecord(String tableName, Object pk) throws java.sql.SQLException {
	Table table = getTable(tableName);
	Key key = RecordUtils.toKey(table, pk);
	return this.getRecord(tableName, key);
    }

    public boolean exists(String tableName, Key key) throws java.sql.SQLException {
	// StatementImpl
	Table table = getTable(tableName);
	StatementImpl stat = this.stats.get(table.getExistsSql().getPSql());

	if (stat == null) {
	    stat = this.conn.prepareStatement(table.getExistsSql());
	    this.stats.put(table.getExistsSql().getPSql(), stat);
	}

	// Parameters
	for (Column column : table.getColumns()) {
	    if (column.isKey()) {

		Object val = key.get(column.getName());
		Asserts.notNull(val, () -> STR.fmt("The column value '{}' is required.", column.getName()));

		stat.setObject(column.getName(), new JdbcParam(val, column.getSqlType(), column.getScaleOrLength()));
	    }
	}

	// Execute
	Number count = stat.executeScalar();
	if (count == null) {
	    return false;
	}
	if (count.longValue() > 1) {
	    throw new SQLException("Duplicated keys.");
	}
	return true;
    }

    public boolean exists(String tableName, Object pk) throws java.sql.SQLException {
	Table table = getTable(tableName);
	Key key = RecordUtils.toKey(table, pk);
	return this.exists(tableName, key);
    }

    // Record utilities

    public List<DataRecord> executeList(String sql) throws java.sql.SQLException {
	try (Statement stat = this.conn.createStatement()) {
	    try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

		String[] columnLabels = JdbcUtils.getColumnLabels(rs);
		return JdbcUtils.executeList(rs, r -> RecordUtils.toRecord(r, columnLabels), new ArrayList<>());
	    }
	}
    }

    public <T> List<DataRecord> executeList(String pSql, Object... params) throws java.sql.SQLException {
	return executeList(pSql, JdbcUtils.toParameters(params));
    }

    public <T> List<DataRecord> executeList(String pSql, Map<String, Object> params) throws java.sql.SQLException {
	StatementImpl stat = prepareStatement(pSql, params);

	try (ResultSetImpl rs = stat.executeQuery()) {

	    String[] columnLabels = JdbcUtils.getColumnLabels(rs);
	    return JdbcUtils.executeList(rs, r -> RecordUtils.toRecord(r, columnLabels), new ArrayList<>());
	}
    }

    public DataRecord executeSingle(String sql) throws java.sql.SQLException {
	return executeSingle(sql, rs -> {

	    String[] columnLabels = JdbcUtils.getColumnLabels(rs);
	    return RecordUtils.toRecord(rs, columnLabels);
	});
    }

    public DataRecord executeSingle(String pSql, Object... params) throws java.sql.SQLException {
	return executeSingle(pSql, JdbcUtils.toParameters(params));
    }

    public DataRecord executeSingle(String pSql, Map<String, Object> params) throws java.sql.SQLException {
	return executeSingle(pSql, params, rs -> {

	    String[] columnLabels = JdbcUtils.getColumnLabels(rs);
	    return RecordUtils.toRecord(rs, columnLabels);
	});
    }

    protected String buildDataSourceID() throws UncheckedSQLException {
	try {
	    if (!StringUtils.isNullOrEmpty(this.conn.getDsName())) {
		return this.conn.getDsName();
	    }
	    return this.conn.getMetaData().getURL();

	} catch (SQLException ex) {
	    throw new UncheckedSQLException(ex);
	}
    }

    public Table getTable(String tableName) throws UncheckedSQLException {
	ConcurrentMap<String, Table> tables = TABLES.computeIfAbsent(buildDataSourceID(), db -> new ConcurrentHashMap<>());

	return tables.computeIfAbsent(tableName, tn -> {
	    try {
		return RecordUtils.loadTable(this.conn, null, null, tableName, null);

	    } catch (SQLException ex) {
		throw new UncheckedSQLException(ex);
	    }
	});
    }

    private static final ConcurrentMap<String, ConcurrentMap<String, Table>> TABLES = new ConcurrentHashMap<>();
}
