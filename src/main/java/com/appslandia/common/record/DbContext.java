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

package com.appslandia.common.record;

import java.io.OutputStream;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.appslandia.common.jdbc.ConnectionImpl;
import com.appslandia.common.jdbc.JdbcParam;
import com.appslandia.common.jdbc.JdbcSql;
import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.jdbc.ResultSetHandler;
import com.appslandia.common.jdbc.ResultSetImpl;
import com.appslandia.common.jdbc.ResultSetMapper;
import com.appslandia.common.jdbc.StatementImpl;
import com.appslandia.common.jdbc.UncheckedSQLException;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DbContext implements AutoCloseable {

    final ConnectionImpl conn;
    final boolean bakAutoCommit;

    final Map<String, StatementImpl> stats = new LinkedHashMap<>();
    private Set<String> batchPSqls;

    public DbContext() {
	this(ConnectionImpl.getCurrent());
    }

    protected DbContext(ConnectionImpl conn) {
	this.conn = conn;
	this.bakAutoCommit = JdbcUtils.isAutoCommit(conn);
    }

    protected void addBatch(StatementImpl stat, String pSql) throws java.sql.SQLException {
	if (this.batchPSqls == null) {
	    this.batchPSqls = new LinkedHashSet<>();
	}
	this.batchPSqls.add(pSql);
	stat.addBatch();
    }

    public void insert(String tableName, Record record) throws java.sql.SQLException {
	this.insert(tableName, record, false);
    }

    public Object insert(String tableName, Record record, boolean addBatch) throws java.sql.SQLException {
	// StatementImpl
	Table table = getTable(tableName);
	StatementImpl stat = this.stats.get(table.getInsertSql().getPSql());

	if (stat == null) {
	    stat = this.conn.prepareStatement(table.getInsertSql(), (table.getKeyIncr() != null));
	    this.stats.put(table.getInsertSql().getPSql(), stat);
	}

	// Parameters
	for (Field field : table.getFields()) {
	    if (field.getKeyType() != FieldType.KEY_INCR && field.getKeyType() != FieldType.COL_GEN) {

		Object val = record.get(field.getName());

		if (!field.isNullable()) {
		    Asserts.notNull(val, () -> STR.fmt("The field '{}' is required.", field.getName()));
		}
		stat.setObject(field.getName(), new JdbcParam(val, field.getSqlType(), field.getScaleOrLength()));
	    }
	}

	// Execute
	if (!addBatch) {
	    int rowAffected = stat.executeUpdate();

	    // Generated Key
	    if (table.getKeyIncr() != null) {
		try (ResultSet rs = stat.getGeneratedKeys()) {

		    if (rs.next()) {
			Object generatedKey = rs.getObject(1);
			record.set(table.getKeyIncr().getName(), generatedKey);

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

    public Object insert(String tableName, Object entity) throws java.sql.SQLException {
	return this.insert(tableName, entity, false);
    }

    public Object insert(String tableName, Object entity, boolean addBatch) throws java.sql.SQLException {
	Table table = getTable(tableName);
	Record record = RecordUtils.toRecord(table, entity);
	return this.insert(tableName, record, addBatch);
    }

    public int update(String tableName, Record record) throws java.sql.SQLException {
	return this.update(tableName, record, false);
    }

    public int update(String tableName, Record record, boolean addBatch) throws java.sql.SQLException {
	// StatementImpl
	Table table = getTable(tableName);
	StatementImpl stat = this.stats.get(table.getUpdateSql().getPSql());

	if (stat == null) {
	    stat = this.conn.prepareStatement(table.getUpdateSql());
	    this.stats.put(table.getUpdateSql().getPSql(), stat);
	}

	// Parameters
	for (Field field : table.getFields()) {
	    if (field.getKeyType() != FieldType.COL_GEN) {
		Object val = record.get(field.getName());

		if (!field.isNullable()) {
		    Asserts.notNull(val, () -> STR.fmt("The field '{}' is required.", field.getName()));
		}
		stat.setObject(field.getName(), new JdbcParam(val, field.getSqlType(), field.getScaleOrLength()));
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
	Record record = RecordUtils.toRecord(table, entity);
	return this.update(tableName, record, addBatch);
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
	for (Field field : table.getFields()) {
	    if (field.getKeyType() == FieldType.KEY || field.getKeyType() == FieldType.KEY_INCR) {

		Object val = key.get(field.getName());
		Asserts.notNull(val, () -> STR.fmt("The field '{}' is required.", field.getName()));

		stat.setObject(field.getName(), new JdbcParam(val, field.getSqlType(), field.getScaleOrLength()));
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

    public Record getRecord(String tableName, Key key) throws java.sql.SQLException {
	// StatementImpl
	Table table = getTable(tableName);
	StatementImpl stat = this.stats.get(table.getGetSql().getPSql());

	if (stat == null) {
	    stat = this.conn.prepareStatement(table.getGetSql());
	    this.stats.put(table.getGetSql().getPSql(), stat);
	}

	// Parameters
	for (Field field : table.getFields()) {
	    if (field.getKeyType() == FieldType.KEY || field.getKeyType() == FieldType.KEY_INCR) {

		Object val = key.get(field.getName());
		Asserts.notNull(val, () -> STR.fmt("The field '{}' is required.", field.getName()));

		stat.setObject(field.getName(), new JdbcParam(val, field.getSqlType(), field.getScaleOrLength()));
	    }
	}

	// Execute
	try (ResultSetImpl rs = stat.executeQuery()) {

	    String[] columnLabels = JdbcUtils.getColumnLabels(rs);
	    return JdbcUtils.executeSingle(rs, r -> RecordUtils.toRecord(r, columnLabels));
	}
    }

    public Record getRecord(String tableName, Object pk) throws java.sql.SQLException {
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
	for (Field field : table.getFields()) {
	    if (field.getKeyType() == FieldType.KEY || field.getKeyType() == FieldType.KEY_INCR) {

		Object val = key.get(field.getName());
		Asserts.notNull(val, () -> STR.fmt("The field '{}' is required.", field.getName()));

		stat.setObject(field.getName(), new JdbcParam(val, field.getSqlType(), field.getScaleOrLength()));
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

    protected StatementImpl prepareStatement(String pSql, Map<String, Object> params) throws java.sql.SQLException {
	// StatementImpl
	JdbcSql sql = new JdbcSql(pSql);
	StatementImpl stat = this.stats.get(pSql);

	if (stat == null) {
	    stat = this.conn.prepareStatement(sql);
	    this.stats.put(pSql, stat);
	}

	// Parameters
	if (params != null) {
	    JdbcUtils.setParameters(stat, sql, params);
	}
	return stat;
    }

    // Record utilities

    public List<Record> executeList(String sql) throws java.sql.SQLException {
	try (Statement stat = this.conn.createStatement()) {
	    try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

		String[] columnLabels = JdbcUtils.getColumnLabels(rs);
		return JdbcUtils.executeList(rs, r -> RecordUtils.toRecord(r, columnLabels), new ArrayList<>());
	    }
	}
    }

    public <T> List<Record> executeList(String pSql, Object... params) throws java.sql.SQLException {
	return executeList(pSql, JdbcUtils.toParameters(params));
    }

    public <T> List<Record> executeList(String pSql, Map<String, Object> params) throws java.sql.SQLException {
	StatementImpl stat = prepareStatement(pSql, params);

	try (ResultSetImpl rs = stat.executeQuery()) {

	    String[] columnLabels = JdbcUtils.getColumnLabels(rs);
	    return JdbcUtils.executeList(rs, r -> RecordUtils.toRecord(r, columnLabels), new ArrayList<>());
	}
    }

    public Record executeSingle(String sql) throws java.sql.SQLException {
	return executeSingle(sql, rs -> {

	    String[] columnLabels = JdbcUtils.getColumnLabels(rs);
	    return RecordUtils.toRecord(rs, columnLabels);
	});
    }

    public Record executeSingle(String pSql, Object... params) throws java.sql.SQLException {
	return executeSingle(pSql, JdbcUtils.toParameters(params));
    }

    public Record executeSingle(String pSql, Map<String, Object> params) throws java.sql.SQLException {
	return executeSingle(pSql, params, rs -> {

	    String[] columnLabels = JdbcUtils.getColumnLabels(rs);
	    return RecordUtils.toRecord(rs, columnLabels);
	});
    }

    // Update Utilities

    public int executeUpdate(String sql) throws java.sql.SQLException {
	return this.conn.executeUpdate(sql);
    }

    public int executeUpdate(String pSql, Object... params) throws java.sql.SQLException {
	return executeUpdate(pSql, params, false);
    }

    public int executeUpdate(String pSql, Map<String, Object> params) throws java.sql.SQLException {
	return executeUpdate(pSql, params, false);
    }

    public int executeUpdate(String pSql, Object[] params, boolean addBatch) throws java.sql.SQLException {
	return executeUpdate(pSql, JdbcUtils.toParameters(params), addBatch);
    }

    public int executeUpdate(String pSql, Map<String, Object> params, boolean addBatch) throws java.sql.SQLException {
	StatementImpl stat = prepareStatement(pSql, params);

	if (!addBatch) {
	    return stat.executeUpdate();

	} else {
	    this.assertTransactional();
	    addBatch(stat, pSql);
	    return -1;
	}
    }

    // Execute Utilities

    public <K, V> Map<K, V> executeMap(String sql, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map) throws java.sql.SQLException {
	return this.conn.executeMap(sql, keyMapper, valueMapper, map);
    }

    public <K, V> Map<K, V> executeMap(String pSql, Object[] params, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map) throws java.sql.SQLException {
	return executeMap(pSql, JdbcUtils.toParameters(params), keyMapper, valueMapper, map);
    }

    public <K, V> Map<K, V> executeMap(String pSql, Map<String, Object> params, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map)
	    throws java.sql.SQLException {
	StatementImpl stat = prepareStatement(pSql, params);

	try (ResultSetImpl rs = stat.executeQuery()) {
	    return JdbcUtils.executeMap(rs, keyMapper, valueMapper, map);
	}
    }

    public <K, V> Map<K, V> executeMap(String sql, String keyColumn, String valueColumn, Map<K, V> map) throws java.sql.SQLException {
	return this.conn.executeMap(sql, keyColumn, valueColumn, map);
    }

    public <K, V> Map<K, V> executeMap(String pSql, Object[] params, String keyColumn, String valueColumn, Map<K, V> map) throws java.sql.SQLException {
	return executeMap(pSql, JdbcUtils.toParameters(params), keyColumn, valueColumn, map);
    }

    public <K, V> Map<K, V> executeMap(String pSql, Map<String, Object> params, String keyColumn, String valueColumn, Map<K, V> map) throws java.sql.SQLException {
	StatementImpl stat = prepareStatement(pSql, params);

	try (ResultSetImpl rs = stat.executeQuery()) {
	    return JdbcUtils.executeMap(rs, keyColumn, valueColumn, map);
	}
    }

    public <T> List<T> executeList(String sql, ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
	return this.conn.executeList(sql, mapper, list);
    }

    public <T> List<T> executeList(String pSql, Object[] params, ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
	return executeList(pSql, JdbcUtils.toParameters(params), mapper, list);
    }

    public <T> List<T> executeList(String pSql, Map<String, Object> params, ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
	StatementImpl stat = prepareStatement(pSql, params);

	try (ResultSetImpl rs = stat.executeQuery()) {
	    return JdbcUtils.executeList(rs, mapper, list);
	}
    }

    public <T> T executeSingle(String sql, ResultSetMapper<T> mapper) throws java.sql.SQLException {
	return this.conn.executeSingle(sql, mapper);
    }

    public <T> T executeSingle(String pSql, Object[] params, ResultSetMapper<T> mapper) throws java.sql.SQLException {
	return executeSingle(pSql, JdbcUtils.toParameters(params), mapper);
    }

    public <T> T executeSingle(String pSql, Map<String, Object> params, ResultSetMapper<T> mapper) throws java.sql.SQLException {
	StatementImpl stat = prepareStatement(pSql, params);

	try (ResultSetImpl rs = stat.executeQuery()) {
	    return JdbcUtils.executeSingle(rs, mapper);
	}
    }

    public <T> T executeScalar(String sql) throws java.sql.SQLException {
	return executeSingle(sql, rs -> ObjectUtils.cast(rs.getObject(1)));
    }

    public <T> T executeScalar(String pSql, Object... params) throws java.sql.SQLException {
	return executeScalar(pSql, JdbcUtils.toParameters(params));
    }

    public <T> T executeScalar(String pSql, Map<String, Object> params) throws java.sql.SQLException {
	return executeSingle(pSql, params, rs -> ObjectUtils.cast(rs.getObject(1)));
    }

    public boolean executeExists(String sql) throws java.sql.SQLException {
	return this.conn.executeExists(sql);
    }

    public boolean executeExists(String pSql, Object... params) throws java.sql.SQLException {
	return executeExists(pSql, JdbcUtils.toParameters(params));
    }

    public boolean executeExists(String pSql, Map<String, Object> params) throws java.sql.SQLException {
	StatementImpl stat = prepareStatement(pSql, params);

	try (ResultSetImpl rs = stat.executeQuery()) {
	    return JdbcUtils.executeExists(rs);
	}
    }

    public void executeQuery(String sql, ResultSetHandler handler) throws Exception {
	this.conn.executeQuery(sql, handler);
    }

    public void executeQuery(String pSql, Object[] params, ResultSetHandler handler) throws Exception {
	executeQuery(pSql, JdbcUtils.toParameters(params), handler);
    }

    public void executeQuery(String pSql, Map<String, Object> params, ResultSetHandler handler) throws Exception {
	StatementImpl stat = prepareStatement(pSql, params);

	try (ResultSetImpl rs = stat.executeQuery()) {
	    while (rs.next()) {
		handler.handle(rs);
	    }
	}
    }

    public void executeStream(String sql, String streamLabel, OutputStream out, ResultSetHandler handler) throws Exception {
	this.conn.executeStream(sql, streamLabel, out, handler);
    }

    public void executeStream(String pSql, Object[] params, String streamLabel, OutputStream out, ResultSetHandler handler) throws Exception {
	executeStream(pSql, JdbcUtils.toParameters(params), streamLabel, out, handler);
    }

    public void executeStream(String pSql, Map<String, Object> params, String streamLabel, OutputStream out, ResultSetHandler handler) throws Exception {
	StatementImpl stat = prepareStatement(pSql, params);

	try (ResultSetImpl rs = stat.executeQuery()) {
	    JdbcUtils.executeStream(rs, streamLabel, out, handler);
	}
    }

    public void executeStream(String sql, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	this.conn.executeStream(sql, streamLabel, out, handler);
    }

    public void executeStream(String pSql, Object[] params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	executeStream(pSql, JdbcUtils.toParameters(params), streamLabel, out, handler);
    }

    public void executeStream(String pSql, Map<String, Object> params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	StatementImpl stat = prepareStatement(pSql, params);

	try (ResultSetImpl rs = stat.executeQuery()) {
	    JdbcUtils.executeStream(rs, streamLabel, out, handler);
	}
    }

    public void executeNStream(String sql, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	this.conn.executeNStream(sql, streamLabel, out, handler);
    }

    public void executeNStream(String pSql, Object[] params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	executeNStream(pSql, JdbcUtils.toParameters(params), streamLabel, out, handler);
    }

    public void executeNStream(String pSql, Map<String, Object> params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	StatementImpl stat = prepareStatement(pSql, params);

	try (ResultSetImpl rs = stat.executeQuery()) {
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
	Asserts.isTrue("execute".equals(action) || "clear".equals(action));
	if (this.batchPSqls != null) {

	    for (String pSql : this.batchPSqls) {
		StatementImpl stat = Asserts.notNull(this.stats.get(pSql));

		if ("execute".equals(action)) {
		    stat.executeBatch();
		} else {
		    stat.clearBatch();
		}
	    }
	    this.batchPSqls.clear();
	}
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
	List<StatementImpl> stats = new ArrayList<>(this.stats.values());

	for (int i = stats.size() - 1; i >= 0; i--) {
	    stats.get(i).close();
	}
    }

    private boolean closed = false;

    @Override
    public void close() throws java.sql.SQLException {
	if (!this.closed) {
	    closeStatements();

	    this.conn.setAutoCommit(this.bakAutoCommit);
	    this.closed = true;
	}
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
		return RecordUtils.loadTable(this.conn, null, null, tableName);

	    } catch (SQLException ex) {
		throw new UncheckedSQLException(ex);
	    }
	});
    }

    private static final ConcurrentMap<String, ConcurrentMap<String, Table>> TABLES = new ConcurrentHashMap<>();
}
