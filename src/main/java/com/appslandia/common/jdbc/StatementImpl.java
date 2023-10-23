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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;
import java.util.Map;

import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class StatementImpl implements PreparedStatement {

    protected final JdbcSql sql;
    protected final PreparedStatement stat;

    public StatementImpl(PreparedStatement stat) {
	this(stat, null);
    }

    public StatementImpl(PreparedStatement stat, JdbcSql sql) {
	Asserts.isTrue(!(stat instanceof StatementImpl));
	this.stat = stat;
	this.sql = sql;
    }

    protected JdbcSql getSql() {
	return Asserts.notNull(this.sql, "sql is required.");
    }

    // Update utilities

    public long executeGeneratedKey() throws java.sql.SQLException {
	this.stat.executeUpdate();

	try (ResultSet rs = this.stat.getGeneratedKeys()) {
	    if (rs.next()) {
		return rs.getLong(1);
	    }
	}
	throw new SQLException("executeGeneratedKey");
    }

    public long executeGeneratedKey(Object... params) throws java.sql.SQLException {
	return executeGeneratedKey(JdbcUtils.toParameters(params));
    }

    public long executeGeneratedKey(Map<String, Object> params) throws java.sql.SQLException {
	if (params != null) {
	    JdbcUtils.setParameters(this, getSql(), params);
	}
	this.stat.executeUpdate();

	try (ResultSet rs = this.stat.getGeneratedKeys()) {
	    if (rs.next()) {
		return rs.getLong(1);
	    }
	}
	throw new SQLException("executeGeneratedKey");
    }

    public int executeUpdate(Object... params) throws java.sql.SQLException {
	return executeUpdate(JdbcUtils.toParameters(params));
    }

    public int executeUpdate(Map<String, Object> params) throws java.sql.SQLException {
	if (params != null) {
	    JdbcUtils.setParameters(this, getSql(), params);
	}
	return this.stat.executeUpdate();
    }

    // Execute utilities

    public <K, V> Map<K, V> executeMap(ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map) throws java.sql.SQLException {
	try (ResultSetImpl rs = executeQuery()) {
	    return JdbcUtils.executeMap(rs, keyMapper, valueMapper, map);
	}
    }

    public <K, V> Map<K, V> executeMap(Object[] params, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map) throws java.sql.SQLException {
	return executeMap(JdbcUtils.toParameters(params), keyMapper, valueMapper, map);
    }

    public <K, V> Map<K, V> executeMap(Map<String, Object> params, ResultSetMapper<K> keyMapper, ResultSetMapper<V> valueMapper, Map<K, V> map) throws java.sql.SQLException {
	if (params != null) {
	    JdbcUtils.setParameters(this, getSql(), params);
	}
	try (ResultSetImpl rs = executeQuery()) {
	    return JdbcUtils.executeMap(rs, keyMapper, valueMapper, map);
	}
    }

    public <K, V> Map<K, V> executeMap(String keyColumn, String valueColumn, Map<K, V> map) throws java.sql.SQLException {
	try (ResultSet rs = this.stat.executeQuery()) {
	    return JdbcUtils.executeMap(rs, keyColumn, valueColumn, map);
	}
    }

    public <K, V> Map<K, V> executeMap(Object[] params, String keyColumn, String valueColumn, Map<K, V> map) throws java.sql.SQLException {
	return executeMap(JdbcUtils.toParameters(params), keyColumn, valueColumn, map);
    }

    public <K, V> Map<K, V> executeMap(Map<String, Object> params, String keyColumn, String valueColumn, Map<K, V> map) throws java.sql.SQLException {
	if (params != null) {
	    JdbcUtils.setParameters(this, getSql(), params);
	}
	try (ResultSet rs = this.stat.executeQuery()) {
	    return JdbcUtils.executeMap(rs, keyColumn, valueColumn, map);
	}
    }

    public <T> List<T> executeList(ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
	try (ResultSetImpl rs = executeQuery()) {
	    return JdbcUtils.executeList(rs, mapper, list);
	}
    }

    public <T> List<T> executeList(Object[] params, ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
	return executeList(JdbcUtils.toParameters(params), mapper, list);
    }

    public <T> List<T> executeList(Map<String, Object> params, ResultSetMapper<T> mapper, List<T> list) throws java.sql.SQLException {
	if (params != null) {
	    JdbcUtils.setParameters(this, getSql(), params);
	}
	try (ResultSetImpl rs = executeQuery()) {
	    return JdbcUtils.executeList(rs, mapper, list);
	}
    }

    public <T> T executeSingle(ResultSetMapper<T> mapper) throws java.sql.SQLException {
	try (ResultSetImpl rs = executeQuery()) {
	    return JdbcUtils.executeSingle(rs, mapper);
	}
    }

    public <T> T executeSingle(Object[] params, ResultSetMapper<T> mapper) throws java.sql.SQLException {
	return executeSingle(JdbcUtils.toParameters(params), mapper);
    }

    public <T> T executeSingle(Map<String, Object> params, ResultSetMapper<T> mapper) throws java.sql.SQLException {
	if (params != null) {
	    JdbcUtils.setParameters(this, getSql(), params);
	}
	try (ResultSetImpl rs = executeQuery()) {
	    return JdbcUtils.executeSingle(rs, mapper);
	}
    }

    public <T> T executeScalar(Class<T> type) throws java.sql.SQLException {
	try (ResultSet rs = this.stat.executeQuery()) {
	    return JdbcUtils.executeScalar(rs, type);
	}
    }

    public <T> T executeScalar(Object[] params, Class<T> type) throws java.sql.SQLException {
	return executeScalar(JdbcUtils.toParameters(params), type);
    }

    public <T> T executeScalar(Map<String, Object> params, Class<T> type) throws java.sql.SQLException {
	if (params != null) {
	    JdbcUtils.setParameters(this, getSql(), params);
	}
	try (ResultSet rs = this.stat.executeQuery()) {
	    return JdbcUtils.executeScalar(rs, type);
	}
    }

    public void executeQuery(ResultSetHandler handler) throws Exception {
	try (ResultSetImpl rs = executeQuery()) {
	    while (rs.next()) {
		handler.handle(rs);
	    }
	}
    }

    public void executeQuery(Object[] params, ResultSetHandler handler) throws Exception {
	executeQuery(JdbcUtils.toParameters(params), handler);
    }

    public void executeQuery(Map<String, Object> params, ResultSetHandler handler) throws Exception {
	if (params != null) {
	    JdbcUtils.setParameters(this, getSql(), params);
	}
	try (ResultSetImpl rs = executeQuery()) {
	    while (rs.next()) {
		handler.handle(rs);
	    }
	}
    }

    public void executeStream(String streamLabel, OutputStream out, ResultSetHandler handler) throws Exception {
	try (ResultSetImpl rs = executeQuery()) {
	    JdbcUtils.executeStream(rs, streamLabel, out, handler);
	}
    }

    public void executeStream(Object[] params, String streamLabel, OutputStream out, ResultSetHandler handler) throws Exception {
	executeStream(JdbcUtils.toParameters(params), streamLabel, out, handler);
    }

    public void executeStream(Map<String, Object> params, String streamLabel, OutputStream out, ResultSetHandler handler) throws Exception {
	if (params != null) {
	    JdbcUtils.setParameters(this, getSql(), params);
	}
	try (ResultSetImpl rs = executeQuery()) {
	    JdbcUtils.executeStream(rs, streamLabel, out, handler);
	}
    }

    public void executeStream(String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	try (ResultSetImpl rs = executeQuery()) {
	    JdbcUtils.executeStream(rs, streamLabel, out, handler);
	}
    }

    public void executeStream(Object[] params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	executeStream(JdbcUtils.toParameters(params), streamLabel, out, handler);
    }

    public void executeStream(Map<String, Object> params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	if (params != null) {
	    JdbcUtils.setParameters(this, getSql(), params);
	}
	try (ResultSetImpl rs = executeQuery()) {
	    JdbcUtils.executeStream(rs, streamLabel, out, handler);
	}
    }

    public void executeNStream(String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	try (ResultSetImpl rs = executeQuery()) {
	    JdbcUtils.executeNStream(rs, streamLabel, out, handler);
	}
    }

    public void executeNStream(Object[] params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	executeNStream(JdbcUtils.toParameters(params), streamLabel, out, handler);
    }

    public void executeNStream(Map<String, Object> params, String streamLabel, Writer out, ResultSetHandler handler) throws Exception {
	if (params != null) {
	    JdbcUtils.setParameters(this, getSql(), params);
	}
	try (ResultSetImpl rs = executeQuery()) {
	    JdbcUtils.executeNStream(rs, streamLabel, out, handler);
	}
    }

    // Set LIKE Parameters
    // :name IS NULL OR name LIKE :name
    // :name = '' OR name LIKE :name

    public void setLike(String parameterName, String value) throws java.sql.SQLException {
	setString(parameterName, SqlLikeEscaper.toLikePattern(value, LikeType.CONTAINS));
    }

    public void setLikeSW(String parameterName, String value) throws java.sql.SQLException {
	setString(parameterName, SqlLikeEscaper.toLikePattern(value, LikeType.STARTS_WITH));
    }

    public void setLikeEW(String parameterName, String value) throws java.sql.SQLException {
	setString(parameterName, SqlLikeEscaper.toLikePattern(value, LikeType.ENDS_WITH));
    }

    public void setNLike(String parameterName, String value) throws java.sql.SQLException {
	setNString(parameterName, SqlLikeEscaper.toLikePattern(value, LikeType.CONTAINS));
    }

    public void setNLikeSW(String parameterName, String value) throws java.sql.SQLException {
	setNString(parameterName, SqlLikeEscaper.toLikePattern(value, LikeType.STARTS_WITH));
    }

    public void setNLikeEW(String parameterName, String value) throws java.sql.SQLException {
	setNString(parameterName, SqlLikeEscaper.toLikePattern(value, LikeType.ENDS_WITH));
    }

    // Set LIKE_ANY Parameters
    // name LIKE_ANY :names

    public void setLikeAny(String parameterName, String[] values) throws java.sql.SQLException {
	setLikeAny(parameterName, values, LikeType.CONTAINS, null);
    }

    public void setLikeAnySW(String parameterName, String[] values) throws java.sql.SQLException {
	setLikeAny(parameterName, values, LikeType.STARTS_WITH, null);
    }

    public void setLikeAnyEW(String parameterName, String[] values) throws java.sql.SQLException {
	setLikeAny(parameterName, values, LikeType.ENDS_WITH, null);
    }

    public void setLikeAny(String parameterName, String[] values, LikeType likeType, String falsePattern) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setString(JdbcSql.toParamName(parameterName, i), (i < values.length) ? SqlLikeEscaper.toLikePattern(values[i], likeType) : falsePattern);
	}
    }

    public void setNLikeAny(String parameterName, String[] values) throws java.sql.SQLException {
	setNLikeAny(parameterName, values, LikeType.CONTAINS, null);
    }

    public void setNLikeAnySW(String parameterName, String[] values) throws java.sql.SQLException {
	setNLikeAny(parameterName, values, LikeType.STARTS_WITH, null);
    }

    public void setNLikeAnyEW(String parameterName, String[] values) throws java.sql.SQLException {
	setNLikeAny(parameterName, values, LikeType.ENDS_WITH, null);
    }

    public void setNLikeAny(String parameterName, String[] values, LikeType likeType, String falsePattern) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setNString(JdbcSql.toParamName(parameterName, i), (i < values.length) ? SqlLikeEscaper.toLikePattern(values[i], likeType) : falsePattern);
	}
    }

    // Set IN Parameters
    // type IN :types

    public void setStringArray(String parameterName, String[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setString(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    public void setNStringArray(String parameterName, String[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setNString(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    public void setBoolArray(String parameterName, boolean[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    if (i < values.length) {
		setBoolean(JdbcSql.toParamName(parameterName, i), values[i]);
	    } else {
		setBoolean2(JdbcSql.toParamName(parameterName, i), null);
	    }
	}
    }

    public void setByteArray(String parameterName, byte[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    if (i < values.length) {
		setByte(JdbcSql.toParamName(parameterName, i), values[i]);
	    } else {
		setByte2(JdbcSql.toParamName(parameterName, i), null);
	    }
	}
    }

    public void setShortArray(String parameterName, short[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    if (i < values.length) {
		setShort(JdbcSql.toParamName(parameterName, i), values[i]);
	    } else {
		setShort2(JdbcSql.toParamName(parameterName, i), null);
	    }
	}
    }

    public void setIntArray(String parameterName, int[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    if (i < values.length) {
		setInt(JdbcSql.toParamName(parameterName, i), values[i]);
	    } else {
		setInt2(JdbcSql.toParamName(parameterName, i), null);
	    }
	}
    }

    public void setLongArray(String parameterName, long[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    if (i < values.length) {
		setLong(JdbcSql.toParamName(parameterName, i), values[i]);
	    } else {
		setLong2(JdbcSql.toParamName(parameterName, i), null);
	    }
	}
    }

    public void setFloatArray(String parameterName, float[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    if (i < values.length) {
		setFloat(JdbcSql.toParamName(parameterName, i), values[i]);
	    } else {
		setFloat2(JdbcSql.toParamName(parameterName, i), null);
	    }
	}
    }

    public void setDoubleArray(String parameterName, double[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    if (i < values.length) {
		setDouble(JdbcSql.toParamName(parameterName, i), values[i]);
	    } else {
		setDouble2(JdbcSql.toParamName(parameterName, i), null);
	    }
	}
    }

    public void setBigDecimalArray(String parameterName, java.math.BigDecimal[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setBigDecimal(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    public void setDateArray(String parameterName, java.sql.Date[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setDate(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    public void setTimestampArray(String parameterName, java.sql.Timestamp[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setTimestamp(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    public void setTimeArray(String parameterName, java.sql.Time[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setTime(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    public void setLocalDateArray(String parameterName, LocalDate[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setLocalDate(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    public void setLocalDateTimeArray(String parameterName, LocalDateTime[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setLocalDateTime(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    public void setLocalTimeArray(String parameterName, LocalTime[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setLocalTime(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    public void setOffsetDateTimeArray(String parameterName, OffsetDateTime[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setOffsetDateTime(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    public void setOffsetTimeArray(String parameterName, OffsetTime[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setOffsetTime(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    public void setObjectArray(String parameterName, Object[] values) throws java.sql.SQLException {
	int arrayLen = this.getSql().getArrayLen(parameterName);
	Asserts.isTrue(values.length <= arrayLen);

	for (int i = 0; i < arrayLen; i++) {
	    setObject(JdbcSql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
	}
    }

    // Set Primitive Wrapper Parameters

    public void setBoolean2(String parameterName, Boolean value) throws java.sql.SQLException {
	if (value == null) {
	    setNull(parameterName, Types.BIT);
	} else {
	    setBoolean(parameterName, value);
	}
    }

    public void setByte2(String parameterName, Byte value) throws java.sql.SQLException {
	if (value == null) {
	    setNull(parameterName, Types.TINYINT);
	} else {
	    setByte(parameterName, value);
	}
    }

    public void setShort2(String parameterName, Short value) throws java.sql.SQLException {
	if (value == null) {
	    setNull(parameterName, Types.SMALLINT);
	} else {
	    setShort(parameterName, value);
	}
    }

    public void setInt2(String parameterName, Integer value) throws java.sql.SQLException {
	if (value == null) {
	    setNull(parameterName, Types.INTEGER);
	} else {
	    setInt(parameterName, value);
	}
    }

    public void setLong2(String parameterName, Long value) throws java.sql.SQLException {
	if (value == null) {
	    setNull(parameterName, Types.BIGINT);
	} else {
	    setLong(parameterName, value);
	}
    }

    public void setFloat2(String parameterName, Float value) throws java.sql.SQLException {
	if (value == null) {
	    setNull(parameterName, Types.REAL);
	} else {
	    setFloat(parameterName, value);
	}
    }

    public void setDouble2(String parameterName, Double value) throws java.sql.SQLException {
	if (value == null) {
	    setNull(parameterName, Types.DOUBLE);
	} else {
	    setDouble(parameterName, value);
	}
    }

    // Java 8+ Date/Time

    public void setLocalDate(String parameterName, java.time.LocalDate x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setObject(index, x, Types.DATE);
	}
    }

    public void setLocalTime(String parameterName, java.time.LocalTime x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setObject(index, x, Types.TIME);
	}
    }

    public void setLocalDateTime(String parameterName, java.time.LocalDateTime x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setObject(index, x, Types.TIMESTAMP);
	}
    }

    public void setOffsetTime(String parameterName, java.time.OffsetTime x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setObject(index, x, Types.TIME_WITH_TIMEZONE);
	}
    }

    public void setOffsetDateTime(String parameterName, java.time.OffsetDateTime x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setObject(index, x, Types.TIMESTAMP_WITH_TIMEZONE);
	}
    }

    // Set Parameter by Name

    public void setBoolean(String parameterName, boolean x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setBoolean(index, x);
	}
    }

    public void setString(String parameterName, String x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setString(index, x);
	}
    }

    public void setNString(String parameterName, String value) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setNString(index, value);
	}
    }

    public void setByte(String parameterName, byte x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setByte(index, x);
	}
    }

    public void setBytes(String parameterName, byte[] x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setBytes(index, x);
	}
    }

    public void setShort(String parameterName, short x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setShort(index, x);
	}
    }

    public void setInt(String parameterName, int x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setInt(index, x);
	}
    }

    public void setLong(String parameterName, long x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setLong(index, x);
	}
    }

    public void setFloat(String parameterName, float x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setFloat(index, x);
	}
    }

    public void setDouble(String parameterName, double x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setDouble(index, x);
	}
    }

    public void setBigDecimal(String parameterName, java.math.BigDecimal x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setBigDecimal(index, x);
	}
    }

    public void setDate(String parameterName, java.sql.Date x, java.util.Calendar cal) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setDate(index, x, cal);
	}
    }

    public void setDate(String parameterName, java.sql.Date x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setDate(index, x);
	}
    }

    public void setTimestamp(String parameterName, java.sql.Timestamp x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setTimestamp(index, x);
	}
    }

    public void setTimestamp(String parameterName, java.sql.Timestamp x, java.util.Calendar cal) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setTimestamp(index, x, cal);
	}
    }

    public void setTime(String parameterName, java.sql.Time x, java.util.Calendar cal) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setTime(index, x, cal);
	}
    }

    public void setTime(String parameterName, java.sql.Time x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setTime(index, x);
	}
    }

    public void setNull(String parameterName, int sqlType, String typeName) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setNull(index, sqlType, typeName);
	}
    }

    public void setNull(String parameterName, int sqlType) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setNull(index, sqlType);
	}
    }

    public void setObject(String parameterName, Object x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setObject(index, x);
	}
    }

    public void setObject(String parameterName, Object x, int targetSqlType, int scaleOrLength) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setObject(index, x, targetSqlType, scaleOrLength);
	}
    }

    public void setObject(String parameterName, Object x, java.sql.SQLType targetSqlType) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setObject(index, x, targetSqlType);
	}
    }

    public void setObject(String parameterName, Object x, int targetSqlType) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setObject(index, x, targetSqlType);
	}
    }

    public void setObject(String parameterName, Object x, java.sql.SQLType targetSqlType, int scaleOrLength) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setObject(index, x, targetSqlType, scaleOrLength);
	}
    }

    public void setURL(String parameterName, java.net.URL x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setURL(index, x);
	}
    }

    public void setArray(String parameterName, java.sql.Array x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setArray(index, x);
	}
    }

    public void setSQLXML(String parameterName, java.sql.SQLXML xmlObject) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setSQLXML(index, xmlObject);
	}
    }

    public void setRef(String parameterName, java.sql.Ref x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setRef(index, x);
	}
    }

    public void setRowId(String parameterName, java.sql.RowId x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setRowId(index, x);
	}
    }

    public void setClob(String parameterName, java.io.Reader reader, long length) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setClob(index, reader, length);
	}
    }

    public void setClob(String parameterName, java.sql.Clob x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setClob(index, x);
	}
    }

    public void setClob(String parameterName, java.io.Reader reader) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setClob(index, reader);
	}
    }

    public void setNClob(String parameterName, java.sql.NClob value) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setNClob(index, value);
	}
    }

    public void setNClob(String parameterName, java.io.Reader reader, long length) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setNClob(index, reader, length);
	}
    }

    public void setNClob(String parameterName, java.io.Reader reader) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setNClob(index, reader);
	}
    }

    public void setAsciiStream(String parameterName, java.io.InputStream x, int length) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setAsciiStream(index, x, length);
	}
    }

    public void setAsciiStream(String parameterName, java.io.InputStream x, long length) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setAsciiStream(index, x, length);
	}
    }

    public void setAsciiStream(String parameterName, java.io.InputStream x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setAsciiStream(index, x);
	}
    }

    public void setCharacterStream(String parameterName, java.io.Reader reader, long length) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setCharacterStream(index, reader, length);
	}
    }

    public void setCharacterStream(String parameterName, java.io.Reader reader, int length) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setCharacterStream(index, reader, length);
	}
    }

    public void setCharacterStream(String parameterName, java.io.Reader reader) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setCharacterStream(index, reader);
	}
    }

    public void setNCharacterStream(String parameterName, java.io.Reader value, long length) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setNCharacterStream(index, value, length);
	}
    }

    public void setNCharacterStream(String parameterName, java.io.Reader value) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setNCharacterStream(index, value);
	}
    }

    @Deprecated
    public void setUnicodeStream(String parameterName, java.io.InputStream x, int length) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setUnicodeStream(index, x, length);
	}
    }

    public void setBlob(String parameterName, java.sql.Blob x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setBlob(index, x);
	}
    }

    public void setBlob(String parameterName, java.io.InputStream inputStream) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setBlob(index, inputStream);
	}
    }

    public void setBlob(String parameterName, java.io.InputStream inputStream, long length) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setBlob(index, inputStream, length);
	}
    }

    public void setBinaryStream(String parameterName, java.io.InputStream x, long length) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setBinaryStream(index, x, length);
	}
    }

    public void setBinaryStream(String parameterName, java.io.InputStream x) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setBinaryStream(index, x);
	}
    }

    public void setBinaryStream(String parameterName, java.io.InputStream x, int length) throws java.sql.SQLException {
	for (int index : this.getSql().getIndexes(parameterName)) {
	    setBinaryStream(index, x, length);
	}
    }

    // java.sql.PreparedStatement

    @Override
    public int executeUpdate() throws java.sql.SQLException {
	return this.stat.executeUpdate();
    }

    @Override
    public long executeLargeUpdate() throws java.sql.SQLException {
	return this.stat.executeLargeUpdate();
    }

    @Override
    public boolean execute() throws java.sql.SQLException {
	return this.stat.execute();
    }

    @Override
    public ResultSetImpl executeQuery() throws java.sql.SQLException {
	return new ResultSetImpl(this.stat.executeQuery());
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws java.sql.SQLException {
	this.stat.setBoolean(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws java.sql.SQLException {
	this.stat.setString(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws java.sql.SQLException {
	this.stat.setNString(parameterIndex, value);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws java.sql.SQLException {
	this.stat.setByte(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws java.sql.SQLException {
	this.stat.setBytes(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws java.sql.SQLException {
	this.stat.setShort(parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws java.sql.SQLException {
	this.stat.setInt(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws java.sql.SQLException {
	this.stat.setLong(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws java.sql.SQLException {
	this.stat.setFloat(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws java.sql.SQLException {
	this.stat.setDouble(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, java.math.BigDecimal x) throws java.sql.SQLException {
	this.stat.setBigDecimal(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, java.sql.Date x, java.util.Calendar cal) throws java.sql.SQLException {
	this.stat.setDate(parameterIndex, x, cal);
    }

    @Override
    public void setDate(int parameterIndex, java.sql.Date x) throws java.sql.SQLException {
	this.stat.setDate(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, java.sql.Timestamp x) throws java.sql.SQLException {
	this.stat.setTimestamp(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, java.sql.Timestamp x, java.util.Calendar cal) throws java.sql.SQLException {
	this.stat.setTimestamp(parameterIndex, x, cal);
    }

    @Override
    public void setTime(int parameterIndex, java.sql.Time x, java.util.Calendar cal) throws java.sql.SQLException {
	this.stat.setTime(parameterIndex, x, cal);
    }

    @Override
    public void setTime(int parameterIndex, java.sql.Time x) throws java.sql.SQLException {
	this.stat.setTime(parameterIndex, x);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws java.sql.SQLException {
	this.stat.setNull(parameterIndex, sqlType, typeName);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws java.sql.SQLException {
	this.stat.setNull(parameterIndex, sqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws java.sql.SQLException {
	if (x instanceof JdbcParam) {
	    JdbcParam par = (JdbcParam) x;

	    if (par.getValue() == null) {
		if (par.getSqlType() == null) {
		    this.stat.setObject(parameterIndex, null);
		} else {
		    this.stat.setNull(parameterIndex, par.getSqlType());
		}
	    } else {
		if (par.getSqlType() == null) {
		    this.stat.setObject(parameterIndex, par.getValue());
		} else {
		    if (par.getScaleOrLength() == null) {
			this.stat.setObject(parameterIndex, par.getValue(), par.getSqlType());
		    } else {
			this.stat.setObject(parameterIndex, par.getValue(), par.getSqlType(), par.getScaleOrLength());
		    }
		}
	    }
	} else {
	    this.stat.setObject(parameterIndex, x);
	}
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws java.sql.SQLException {
	this.stat.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setObject(int parameterIndex, Object x, java.sql.SQLType targetSqlType) throws java.sql.SQLException {
	this.stat.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws java.sql.SQLException {
	this.stat.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object x, java.sql.SQLType targetSqlType, int scaleOrLength) throws java.sql.SQLException {
	this.stat.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setURL(int parameterIndex, java.net.URL x) throws java.sql.SQLException {
	this.stat.setURL(parameterIndex, x);
    }

    @Override
    public void setArray(int parameterIndex, java.sql.Array x) throws java.sql.SQLException {
	this.stat.setArray(parameterIndex, x);
    }

    @Override
    public void setSQLXML(int parameterIndex, java.sql.SQLXML xmlObject) throws java.sql.SQLException {
	this.stat.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setRef(int parameterIndex, java.sql.Ref x) throws java.sql.SQLException {
	this.stat.setRef(parameterIndex, x);
    }

    @Override
    public void setRowId(int parameterIndex, java.sql.RowId x) throws java.sql.SQLException {
	this.stat.setRowId(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
	this.stat.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setClob(int parameterIndex, java.sql.Clob x) throws java.sql.SQLException {
	this.stat.setClob(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, java.io.Reader reader) throws java.sql.SQLException {
	this.stat.setClob(parameterIndex, reader);
    }

    @Override
    public void setNClob(int parameterIndex, java.sql.NClob value) throws java.sql.SQLException {
	this.stat.setNClob(parameterIndex, value);
    }

    @Override
    public void setNClob(int parameterIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
	this.stat.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setNClob(int parameterIndex, java.io.Reader reader) throws java.sql.SQLException {
	this.stat.setNClob(parameterIndex, reader);
    }

    @Override
    public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
	this.stat.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, java.io.InputStream x, long length) throws java.sql.SQLException {
	this.stat.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, java.io.InputStream x) throws java.sql.SQLException {
	this.stat.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
	this.stat.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, java.io.Reader reader, int length) throws java.sql.SQLException {
	this.stat.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, java.io.Reader reader) throws java.sql.SQLException {
	this.stat.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, java.io.Reader value, long length) throws java.sql.SQLException {
	this.stat.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, java.io.Reader value) throws java.sql.SQLException {
	this.stat.setNCharacterStream(parameterIndex, value);
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
	this.stat.setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public void setBlob(int parameterIndex, java.sql.Blob x) throws java.sql.SQLException {
	this.stat.setBlob(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, java.io.InputStream inputStream) throws java.sql.SQLException {
	this.stat.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setBlob(int parameterIndex, java.io.InputStream inputStream, long length) throws java.sql.SQLException {
	this.stat.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, java.io.InputStream x, long length) throws java.sql.SQLException {
	this.stat.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, java.io.InputStream x) throws java.sql.SQLException {
	this.stat.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
	this.stat.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void addBatch() throws java.sql.SQLException {
	this.stat.addBatch();
    }

    @Override
    public void clearParameters() throws java.sql.SQLException {
	this.stat.clearParameters();
    }

    @Override
    public java.sql.ResultSetMetaData getMetaData() throws java.sql.SQLException {
	return this.stat.getMetaData();
    }

    @Override
    public java.sql.ParameterMetaData getParameterMetaData() throws java.sql.SQLException {
	return this.stat.getParameterMetaData();
    }

    // java.sql.Statement

    @Override
    public long executeLargeUpdate(String sql, int[] columnIndexes) throws java.sql.SQLException {
	return this.stat.executeLargeUpdate(sql, columnIndexes);
    }

    @Override
    public long executeLargeUpdate(String sql, String[] columnNames) throws java.sql.SQLException {
	return this.stat.executeLargeUpdate(sql, columnNames);
    }

    @Override
    public long executeLargeUpdate(String sql) throws java.sql.SQLException {
	return this.stat.executeLargeUpdate(sql);
    }

    @Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws java.sql.SQLException {
	return this.stat.executeLargeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws java.sql.SQLException {
	return this.stat.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws java.sql.SQLException {
	return this.stat.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql) throws java.sql.SQLException {
	return this.stat.executeUpdate(sql);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws java.sql.SQLException {
	return this.stat.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql) throws java.sql.SQLException {
	return this.stat.execute(sql);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws java.sql.SQLException {
	return this.stat.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws java.sql.SQLException {
	return this.stat.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws java.sql.SQLException {
	return this.stat.execute(sql, columnNames);
    }

    @Override
    public int[] executeBatch() throws java.sql.SQLException {
	return this.stat.executeBatch();
    }

    @Override
    public long[] executeLargeBatch() throws java.sql.SQLException {
	return this.stat.executeLargeBatch();
    }

    @Override
    public ResultSetImpl executeQuery(String sql) throws java.sql.SQLException {
	return new ResultSetImpl(this.stat.executeQuery(sql));
    }

    @Override
    public void setQueryTimeout(int seconds) throws java.sql.SQLException {
	this.stat.setQueryTimeout(seconds);
    }

    @Override
    public void setCursorName(String name) throws java.sql.SQLException {
	this.stat.setCursorName(name);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws java.sql.SQLException {
	this.stat.setEscapeProcessing(enable);
    }

    @Override
    public void setFetchDirection(int direction) throws java.sql.SQLException {
	this.stat.setFetchDirection(direction);
    }

    @Override
    public void setFetchSize(int rows) throws java.sql.SQLException {
	this.stat.setFetchSize(rows);
    }

    @Override
    public void setLargeMaxRows(long max) throws java.sql.SQLException {
	this.stat.setLargeMaxRows(max);
    }

    @Override
    public void setMaxFieldSize(int max) throws java.sql.SQLException {
	this.stat.setMaxFieldSize(max);
    }

    @Override
    public void setMaxRows(int max) throws java.sql.SQLException {
	this.stat.setMaxRows(max);
    }

    @Override
    public void setPoolable(boolean poolable) throws java.sql.SQLException {
	this.stat.setPoolable(poolable);
    }

    @Override
    public long getLargeUpdateCount() throws java.sql.SQLException {
	return this.stat.getLargeUpdateCount();
    }

    @Override
    public int getUpdateCount() throws java.sql.SQLException {
	return this.stat.getUpdateCount();
    }

    @Override
    public int getQueryTimeout() throws java.sql.SQLException {
	return this.stat.getQueryTimeout();
    }

    @Override
    public void addBatch(String sql) throws java.sql.SQLException {
	this.stat.addBatch(sql);
    }

    @Override
    public void cancel() throws java.sql.SQLException {
	this.stat.cancel();
    }

    @Override
    public void clearBatch() throws java.sql.SQLException {
	this.stat.clearBatch();
    }

    @Override
    public void clearWarnings() throws java.sql.SQLException {
	this.stat.clearWarnings();
    }

    @Override
    public void closeOnCompletion() throws java.sql.SQLException {
	this.stat.closeOnCompletion();
    }

    @Override
    public java.sql.Connection getConnection() throws java.sql.SQLException {
	return this.stat.getConnection();
    }

    @Override
    public int getFetchDirection() throws java.sql.SQLException {
	return this.stat.getFetchDirection();
    }

    @Override
    public int getFetchSize() throws java.sql.SQLException {
	return this.stat.getFetchSize();
    }

    @Override
    public java.sql.ResultSet getGeneratedKeys() throws java.sql.SQLException {
	return this.stat.getGeneratedKeys();
    }

    @Override
    public long getLargeMaxRows() throws java.sql.SQLException {
	return this.stat.getLargeMaxRows();
    }

    @Override
    public int getMaxFieldSize() throws java.sql.SQLException {
	return this.stat.getMaxFieldSize();
    }

    @Override
    public int getMaxRows() throws java.sql.SQLException {
	return this.stat.getMaxRows();
    }

    @Override
    public boolean getMoreResults() throws java.sql.SQLException {
	return this.stat.getMoreResults();
    }

    @Override
    public boolean getMoreResults(int current) throws java.sql.SQLException {
	return this.stat.getMoreResults(current);
    }

    @Override
    public java.sql.ResultSet getResultSet() throws java.sql.SQLException {
	return this.stat.getResultSet();
    }

    @Override
    public int getResultSetConcurrency() throws java.sql.SQLException {
	return this.stat.getResultSetConcurrency();
    }

    @Override
    public int getResultSetHoldability() throws java.sql.SQLException {
	return this.stat.getResultSetHoldability();
    }

    @Override
    public int getResultSetType() throws java.sql.SQLException {
	return this.stat.getResultSetType();
    }

    @Override
    public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
	return this.stat.getWarnings();
    }

    @Override
    public boolean isCloseOnCompletion() throws java.sql.SQLException {
	return this.stat.isCloseOnCompletion();
    }

    @Override
    public boolean isClosed() throws java.sql.SQLException {
	return this.stat.isClosed();
    }

    @Override
    public boolean isPoolable() throws java.sql.SQLException {
	return this.stat.isPoolable();
    }

    // java.sql.Wrapper

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws java.sql.SQLException {
	return this.stat.isWrapperFor(arg0);
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws java.sql.SQLException {
	return this.stat.unwrap(arg0);
    }

    // AutoCloseable

    @Override
    public void close() throws java.sql.SQLException {
	this.stat.close();
    }
}
