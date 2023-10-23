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

import java.io.OutputStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.appslandia.common.base.AssertException;
import com.appslandia.common.base.Out;
import com.appslandia.common.threading.ThreadLocalStorage;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ConnectionImpl implements Connection {

    protected final Connection conn;
    protected final String dsName;
    protected ConnectionImpl outer;

    protected final SqlEngine sqlEngine;

    public ConnectionImpl(DataSource dataSource) throws java.sql.SQLException {
	this(dataSource, "");
    }

    public ConnectionImpl(DataSource dataSource, String dsName) throws java.sql.SQLException {
	ConnectionImpl outer = CONNECTION_HOLDER.get();
	if (outer != null) {
	    this.outer = outer;
	}
	this.conn = dataSource.getConnection();
	this.dsName = Asserts.notNull(dsName, "dsName must be not null.");

	this.sqlEngine = SqlEngine.parse(this.conn.getMetaData().getURL());
	CONNECTION_HOLDER.set(this);
    }

    public ConnectionImpl(DataSourceWrapper dataSource) throws java.sql.SQLException {
	this(dataSource, dataSource.getName());
    }

    public String getDsName() {
	return this.dsName;
    }

    public SqlEngine getSqlEngine() {
	return this.sqlEngine;
    }

    // PrepareStatement utilities

    public StatementImpl prepareStatement(JdbcSql sql) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql.getTranslatedSql()), sql);
    }

    public StatementImpl prepareStatement(JdbcSql sql, boolean autoGeneratedKeys) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql.getTranslatedSql(), autoGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS), sql);
    }

    public StatementImpl prepareStatement(JdbcSql sql, int[] columnIndexes) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql.getTranslatedSql(), columnIndexes), sql);
    }

    public StatementImpl prepareStatement(JdbcSql sql, String[] columnNames) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql.getTranslatedSql(), columnNames), sql);
    }

    public StatementImpl prepareStatement(JdbcSql sql, int resultSetType, int resultSetConcurrency) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql.getTranslatedSql(), resultSetType, resultSetConcurrency), sql);
    }

    public StatementImpl prepareStatement(JdbcSql sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql.getTranslatedSql(), resultSetType, resultSetConcurrency, resultSetHoldability), sql);
    }

    // Update Utilities

    public int dropTable(String tableName, long callerDateTimeID) throws java.sql.SQLException {
	Asserts.notNull(callerDateTimeID);
	Asserts.authorize(callerDateTimeID);

	return executeUpdate(STR.fmt("DROP TABLE IF EXISTS {}", tableName));
    }

    public int truncateTable(String tableName, long callerDateTimeID) throws java.sql.SQLException {
	Asserts.notNull(callerDateTimeID);
	Asserts.authorize(callerDateTimeID);

	return executeUpdate(STR.fmt("TRUNCATE TABLE {}", tableName));
    }

    public String createTable(String tableSpec) throws java.sql.SQLException {
	Out<String> tableName = new Out<>();
	String tableScript = TableUtils.toTableScript(tableSpec, tableName);

	executeUpdate(tableScript);
	return tableName.value;
    }

    public int executeUpdate(String sql) throws java.sql.SQLException {
	try (Statement stat = this.conn.createStatement()) {
	    return stat.executeUpdate(sql);
	}
    }

    public int executeUpdate(String pSql, Object... params) throws java.sql.SQLException {
	return executeUpdate(pSql, JdbcUtils.toParameters(params));
    }

    public int executeUpdate(String pSql, Map<String, Object> params) throws java.sql.SQLException {
	JdbcSql sql = new JdbcSql(pSql);
	try (StatementImpl stat = prepareStatement(sql)) {
	    if (params != null) {
		JdbcUtils.setParameters(stat, sql, params);
	    }
	    return stat.executeUpdate();
	}
    }

    // Execute utilities

    public <K, V> Map<K, V> executeMap(String sql, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map) throws java.sql.SQLException {
	try (Statement stat = this.conn.createStatement()) {
	    try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

		return JdbcUtils.executeMap(rs, keyMapper, valueMapper, map);
	    }
	}
    }

    public <K, V> Map<K, V> executeMap(String pSql, Object[] params, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map) throws java.sql.SQLException {
	return executeMap(pSql, JdbcUtils.toParameters(params), keyMapper, valueMapper, map);
    }

    public <K, V> Map<K, V> executeMap(String pSql, Map<String, Object> params, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map)
	    throws java.sql.SQLException {
	JdbcSql sql = new JdbcSql(pSql);
	try (StatementImpl stat = prepareStatement(sql)) {
	    if (params != null) {
		JdbcUtils.setParameters(stat, sql, params);
	    }
	    try (ResultSetImpl rs = stat.executeQuery()) {
		return JdbcUtils.executeMap(rs, keyMapper, valueMapper, map);
	    }
	}
    }

    public <K, V> Map<K, V> executeMap(String sql, String keyColumn, String valueColumn, Map<K, V> map) throws java.sql.SQLException {
	try (Statement stat = this.conn.createStatement()) {
	    try (ResultSet rs = stat.executeQuery(sql)) {

		return JdbcUtils.executeMap(rs, keyColumn, valueColumn, map);
	    }
	}
    }

    public <K, V> Map<K, V> executeMap(String pSql, Object[] params, String keyColumn, String valueColumn, Map<K, V> map) throws java.sql.SQLException {
	return executeMap(pSql, JdbcUtils.toParameters(params), keyColumn, valueColumn, map);
    }

    public <K, V> Map<K, V> executeMap(String pSql, Map<String, Object> params, String keyColumn, String valueColumn, Map<K, V> map) throws java.sql.SQLException {
	JdbcSql sql = new JdbcSql(pSql);
	try (StatementImpl stat = prepareStatement(sql)) {
	    if (params != null) {
		JdbcUtils.setParameters(stat, sql, params);
	    }
	    try (ResultSet rs = stat.executeQuery()) {
		return JdbcUtils.executeMap(rs, keyColumn, valueColumn, map);
	    }
	}
    }

    public <T> List<T> executeList(String sql, ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
	try (Statement stat = this.conn.createStatement()) {
	    try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

		return JdbcUtils.executeList(rs, mapper, list);
	    }
	}
    }

    public <T> List<T> executeList(String pSql, Object[] params, ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
	return executeList(pSql, JdbcUtils.toParameters(params), mapper, list);
    }

    public <T> List<T> executeList(String pSql, Map<String, Object> params, ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
	JdbcSql sql = new JdbcSql(pSql);
	try (StatementImpl stat = prepareStatement(sql)) {
	    if (params != null) {
		JdbcUtils.setParameters(stat, sql, params);
	    }
	    try (ResultSetImpl rs = stat.executeQuery()) {
		return JdbcUtils.executeList(rs, mapper, list);
	    }
	}
    }

    public <T> T executeSingle(String sql, ResultSetMapper<T> mapper) throws java.sql.SQLException {
	try (Statement stat = this.conn.createStatement()) {
	    try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

		return JdbcUtils.executeSingle(rs, mapper);
	    }
	}
    }

    public <T> T executeSingle(String pSql, Object[] params, ResultSetMapper<T> mapper) throws java.sql.SQLException {
	return executeSingle(pSql, JdbcUtils.toParameters(params), mapper);
    }

    public <T> T executeSingle(String pSql, Map<String, Object> params, ResultSetMapper<T> mapper) throws java.sql.SQLException {
	JdbcSql sql = new JdbcSql(pSql);
	try (StatementImpl stat = prepareStatement(sql)) {
	    if (params != null) {
		JdbcUtils.setParameters(stat, sql, params);
	    }
	    try (ResultSetImpl rs = stat.executeQuery()) {
		return JdbcUtils.executeSingle(rs, mapper);
	    }
	}
    }

    public <T> T executeScalar(String sql, Class<T> type) throws java.sql.SQLException {
	try (Statement stat = this.conn.createStatement()) {
	    try (ResultSet rs = stat.executeQuery(sql)) {

		return JdbcUtils.executeScalar(rs, type);
	    }
	}
    }

    public <T> T executeScalar(String pSql, Object[] params, Class<T> type) throws java.sql.SQLException {
	return executeScalar(pSql, JdbcUtils.toParameters(params), type);
    }

    public <T> T executeScalar(String pSql, Map<String, Object> params, Class<T> type) throws java.sql.SQLException {
	JdbcSql sql = new JdbcSql(pSql);
	try (StatementImpl stat = prepareStatement(sql)) {
	    if (params != null) {
		JdbcUtils.setParameters(stat, sql, params);
	    }
	    try (ResultSetImpl rs = stat.executeQuery()) {
		return JdbcUtils.executeScalar(rs, type);
	    }
	}
    }

    public void executeQuery(String sql, ResultSetHandler handler) throws Exception {
	try (Statement stat = this.conn.createStatement()) {
	    try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

		while (rs.next()) {
		    handler.handle(rs);
		}
	    }
	}
    }

    public void executeQuery(String pSql, Object[] params, ResultSetHandler handler) throws Exception {
	executeQuery(pSql, JdbcUtils.toParameters(params), handler);
    }

    public void executeQuery(String pSql, Map<String, Object> params, ResultSetHandler handler) throws Exception {
	JdbcSql sql = new JdbcSql(pSql);
	try (StatementImpl stat = prepareStatement(sql)) {
	    if (params != null) {
		JdbcUtils.setParameters(stat, sql, params);
	    }
	    try (ResultSetImpl rs = stat.executeQuery()) {
		while (rs.next()) {
		    handler.handle(rs);
		}
	    }
	}
    }

    public void executeStream(String sql, String streamLabel, OutputStream out, ResultSetHandler handler) throws Exception {
	try (Statement stat = this.conn.createStatement()) {
	    try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

		JdbcUtils.executeStream(rs, streamLabel, out, handler);
	    }
	}
    }

    public void executeStream(String pSql, Object[] params, String streamLabel, OutputStream out, ResultSetHandler handler) throws Exception {
	executeStream(pSql, JdbcUtils.toParameters(params), streamLabel, out, handler);
    }

    public void executeStream(String pSql, Map<String, Object> params, String streamLabel, OutputStream out, ResultSetHandler handler) throws Exception {
	JdbcSql sql = new JdbcSql(pSql);
	try (StatementImpl stat = prepareStatement(sql)) {
	    if (params != null) {
		JdbcUtils.setParameters(stat, sql, params);
	    }
	    try (ResultSetImpl rs = stat.executeQuery()) {
		JdbcUtils.executeStream(rs, streamLabel, out, handler);
	    }
	}
    }

    public void executeStream(String sql, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	try (Statement stat = this.conn.createStatement()) {
	    try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

		JdbcUtils.executeStream(rs, streamLabel, out, handler);
	    }
	}
    }

    public void executeStream(String pSql, Object[] params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	executeStream(pSql, JdbcUtils.toParameters(params), streamLabel, out, handler);
    }

    public void executeStream(String pSql, Map<String, Object> params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	JdbcSql sql = new JdbcSql(pSql);
	try (StatementImpl stat = prepareStatement(sql)) {
	    if (params != null) {
		JdbcUtils.setParameters(stat, sql, params);
	    }
	    try (ResultSetImpl rs = stat.executeQuery()) {
		JdbcUtils.executeStream(rs, streamLabel, out, handler);
	    }
	}
    }

    public void executeNStream(String sql, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	try (Statement stat = this.conn.createStatement()) {
	    try (ResultSetImpl rs = new ResultSetImpl(stat.executeQuery(sql))) {

		JdbcUtils.executeNStream(rs, streamLabel, out, handler);
	    }
	}
    }

    public void executeNStream(String pSql, Object[] params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	executeNStream(pSql, JdbcUtils.toParameters(params), streamLabel, out, handler);
    }

    public void executeNStream(String pSql, Map<String, Object> params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	JdbcSql sql = new JdbcSql(pSql);
	try (StatementImpl stat = prepareStatement(sql)) {
	    if (params != null) {
		JdbcUtils.setParameters(stat, sql, params);
	    }
	    try (ResultSetImpl rs = stat.executeQuery()) {
		JdbcUtils.executeNStream(rs, streamLabel, out, handler);
	    }
	}
    }

    // java.sql.Connection

    @Override
    public java.sql.CallableStatement prepareCall(String sql) throws java.sql.SQLException {
	return this.conn.prepareCall(sql);
    }

    @Override
    public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws java.sql.SQLException {
	return this.conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws java.sql.SQLException {
	return this.conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public StatementImpl prepareStatement(String sql) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql));
    }

    @Override
    public StatementImpl prepareStatement(String sql, int autoGeneratedKeys) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql, autoGeneratedKeys));
    }

    @Override
    public StatementImpl prepareStatement(String sql, int[] columnIndexes) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql, columnIndexes));
    }

    @Override
    public StatementImpl prepareStatement(String sql, String[] columnNames) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql, columnNames));
    }

    @Override
    public StatementImpl prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency));
    }

    @Override
    public StatementImpl prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws java.sql.SQLException {
	return new StatementImpl(this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    @Override
    public java.sql.Array createArrayOf(String typeName, Object[] elements) throws java.sql.SQLException {
	return this.conn.createArrayOf(typeName, elements);
    }

    @Override
    public java.sql.SQLXML createSQLXML() throws java.sql.SQLException {
	return this.conn.createSQLXML();
    }

    @Override
    public java.sql.Clob createClob() throws java.sql.SQLException {
	return this.conn.createClob();
    }

    @Override
    public java.sql.NClob createNClob() throws java.sql.SQLException {
	return this.conn.createNClob();
    }

    @Override
    public java.sql.Blob createBlob() throws java.sql.SQLException {
	return this.conn.createBlob();
    }

    @Override
    public java.sql.Statement createStatement() throws java.sql.SQLException {
	return this.conn.createStatement();
    }

    @Override
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) throws java.sql.SQLException {
	return this.conn.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws java.sql.SQLException {
	return this.conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public java.sql.Struct createStruct(String typeName, Object[] attributes) throws java.sql.SQLException {
	return this.conn.createStruct(typeName, attributes);
    }

    @Override
    public java.sql.Savepoint setSavepoint() throws java.sql.SQLException {
	return this.conn.setSavepoint();
    }

    @Override
    public java.sql.Savepoint setSavepoint(String name) throws java.sql.SQLException {
	return this.conn.setSavepoint(name);
    }

    @Override
    public void setNetworkTimeout(java.util.concurrent.Executor executor, int milliseconds) throws java.sql.SQLException {
	this.conn.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws java.sql.SQLException {
	this.conn.setAutoCommit(autoCommit);
    }

    @Override
    public void setCatalog(String catalog) throws java.sql.SQLException {
	this.conn.setCatalog(catalog);
    }

    @Override
    public void setClientInfo(java.util.Properties properties) throws java.sql.SQLClientInfoException {
	this.conn.setClientInfo(properties);
    }

    @Override
    public void setClientInfo(String name, String value) throws java.sql.SQLClientInfoException {
	this.conn.setClientInfo(name, value);
    }

    @Override
    public void setHoldability(int holdability) throws java.sql.SQLException {
	this.conn.setHoldability(holdability);
    }

    @Override
    public void setReadOnly(boolean readOnly) throws java.sql.SQLException {
	this.conn.setReadOnly(readOnly);
    }

    @Override
    public void setSchema(String schema) throws java.sql.SQLException {
	this.conn.setSchema(schema);
    }

    @Override
    public void setTransactionIsolation(int level) throws java.sql.SQLException {
	this.conn.setTransactionIsolation(level);
    }

    @Override
    public void setTypeMap(java.util.Map<String, Class<?>> map) throws java.sql.SQLException {
	this.conn.setTypeMap(map);
    }

    @Override
    public int getNetworkTimeout() throws java.sql.SQLException {
	return this.conn.getNetworkTimeout();
    }

    @Override
    public boolean getAutoCommit() throws java.sql.SQLException {
	return this.conn.getAutoCommit();
    }

    @Override
    public String getCatalog() throws java.sql.SQLException {
	return this.conn.getCatalog();
    }

    @Override
    public java.util.Properties getClientInfo() throws java.sql.SQLException {
	return this.conn.getClientInfo();
    }

    @Override
    public String getClientInfo(String name) throws java.sql.SQLException {
	return this.conn.getClientInfo(name);
    }

    @Override
    public int getHoldability() throws java.sql.SQLException {
	return this.conn.getHoldability();
    }

    @Override
    public java.sql.DatabaseMetaData getMetaData() throws java.sql.SQLException {
	return this.conn.getMetaData();
    }

    @Override
    public String getSchema() throws java.sql.SQLException {
	return this.conn.getSchema();
    }

    @Override
    public int getTransactionIsolation() throws java.sql.SQLException {
	return this.conn.getTransactionIsolation();
    }

    @Override
    public java.util.Map<String, Class<?>> getTypeMap() throws java.sql.SQLException {
	return this.conn.getTypeMap();
    }

    @Override
    public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
	return this.conn.getWarnings();
    }

    @Override
    public boolean isClosed() throws java.sql.SQLException {
	return this.conn.isClosed();
    }

    @Override
    public boolean isReadOnly() throws java.sql.SQLException {
	return this.conn.isReadOnly();
    }

    @Override
    public boolean isValid(int timeout) throws java.sql.SQLException {
	return this.conn.isValid(timeout);
    }

    @Override
    public void releaseSavepoint(java.sql.Savepoint savepoint) throws java.sql.SQLException {
	this.conn.releaseSavepoint(savepoint);
    }

    @Override
    public void abort(java.util.concurrent.Executor executor) throws java.sql.SQLException {
	this.conn.abort(executor);
    }

    @Override
    public void clearWarnings() throws java.sql.SQLException {
	this.conn.clearWarnings();
    }

    @Override
    public void commit() throws java.sql.SQLException {
	this.conn.commit();
    }

    @Override
    public String nativeSQL(String sql) throws java.sql.SQLException {
	return this.conn.nativeSQL(sql);
    }

    @Override
    public void rollback() throws java.sql.SQLException {
	this.conn.rollback();
    }

    @Override
    public void rollback(java.sql.Savepoint savepoint) throws java.sql.SQLException {
	this.conn.rollback(savepoint);
    }

    // java.sql.Wrapper

    @Override
    public boolean isWrapperFor(Class<?> iface) throws java.sql.SQLException {
	return this.conn.isWrapperFor(iface);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws java.sql.SQLException {
	return this.conn.unwrap(iface);
    }

    // AutoCloseable

    private boolean closed;

    @Override
    public void close() throws java.sql.SQLException {
	if (!this.closed) {
	    ConnectionImpl outer = this.outer;
	    this.conn.close();

	    this.outer = null;
	    CONNECTION_HOLDER.set(outer);

	    this.closed = true;
	}
    }

    @Override
    public String toString() {
	return this.conn.toString();
    }

    private static final ThreadLocalStorage<ConnectionImpl> CONNECTION_HOLDER = new ThreadLocalStorage<>();

    public static ConnectionImpl getCurrent() throws AssertException {
	ConnectionImpl conn = CONNECTION_HOLDER.get();
	return Asserts.notNull(conn, "No connection found in the current thread.");
    }

    public static boolean hasCurrent() {
	return CONNECTION_HOLDER.hasValue();
    }
}
