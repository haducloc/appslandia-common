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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.appslandia.common.base.AssertException;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.NormalizeUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ResultSetImpl implements ResultSet {

	protected final ResultSet rs;
	protected List<ResultSetColumn> columns;

	public ResultSetImpl(ResultSet rs) {
		Asserts.isTrue(!(rs instanceof ResultSetImpl));
		this.rs = Asserts.notNull(rs);
	}

	public List<ResultSetColumn> getColumns() throws java.sql.SQLException {
		if (this.columns == null) {
			this.columns = JdbcUtils.getResultSetColumns(this.rs);
		}
		return this.columns;
	}

	public String valuesAsID(String... columnLabels) throws UncheckedSQLException {
		Asserts.hasElements(columnLabels);

		Object[] values = Arrays.stream(columnLabels).map(columnLabel -> {
			try {
				return this.rs.getObject(columnLabel);

			} catch (SQLException ex) {
				throw new UncheckedSQLException(ex);
			}
		}).toArray();

		return NormalizeUtils.valuesAsID(values);
	}

	// UpperCase/LowerCase with Locale.ROOT

	public String getStringUC(String columnLabel) throws java.sql.SQLException {
		String value = this.rs.getString(columnLabel);
		return (value != null) ? value.toUpperCase(Locale.ROOT) : null;
	}

	public String getStringUC(String columnLabel, String valueIfNull) throws java.sql.SQLException {
		Asserts.notNull(valueIfNull);

		String value = this.rs.getString(columnLabel);
		return (value != null) ? value.toUpperCase(Locale.ROOT) : valueIfNull.toUpperCase(Locale.ROOT);
	}

	public String getStringUCReq(String columnLabel) throws java.sql.SQLException {
		String value = getStringReq(columnLabel);
		return value.toUpperCase(Locale.ROOT);
	}

	public String getStringLC(String columnLabel) throws java.sql.SQLException {
		String value = this.rs.getString(columnLabel);
		return (value != null) ? value.toLowerCase(Locale.ROOT) : null;
	}

	public String getStringLC(String columnLabel, String valueIfNull) throws java.sql.SQLException {
		Asserts.notNull(valueIfNull);

		String value = this.rs.getString(columnLabel);
		return (value != null) ? value.toLowerCase(Locale.ROOT) : valueIfNull.toLowerCase(Locale.ROOT);
	}

	public String getStringLCReq(String columnLabel) throws java.sql.SQLException {
		String value = getStringReq(columnLabel);
		return value.toLowerCase(Locale.ROOT);
	}

	// Get with default values

	public boolean getBoolean(String columnLabel, boolean valueIfNull) throws java.sql.SQLException {
		boolean value = this.rs.getBoolean(columnLabel);
		return !this.rs.wasNull() ? value : valueIfNull;
	}

	public byte getByte(String columnLabel, byte valueIfNull) throws java.sql.SQLException {
		byte value = this.rs.getByte(columnLabel);
		return !this.rs.wasNull() ? value : valueIfNull;
	}

	public short getShort(String columnLabel, short valueIfNull) throws java.sql.SQLException {
		short value = this.rs.getShort(columnLabel);
		return !this.rs.wasNull() ? value : valueIfNull;
	}

	public int getInt(String columnLabel, int valueIfNull) throws java.sql.SQLException {
		int value = this.rs.getInt(columnLabel);
		return !this.rs.wasNull() ? value : valueIfNull;
	}

	public long getLong(String columnLabel, long valueIfNull) throws java.sql.SQLException {
		long value = this.rs.getLong(columnLabel);
		return !this.rs.wasNull() ? value : valueIfNull;
	}

	public float getFloat(String columnLabel, float valueIfNull) throws java.sql.SQLException {
		float value = this.rs.getFloat(columnLabel);
		return !this.rs.wasNull() ? value : valueIfNull;
	}

	public double getDouble(String columnLabel, double valueIfNull) throws java.sql.SQLException {
		double value = this.rs.getDouble(columnLabel);
		return !this.rs.wasNull() ? value : valueIfNull;
	}

	public BigDecimal getBigDecimal(String columnLabel, double valueIfNull) throws java.sql.SQLException {
		BigDecimal value = this.rs.getBigDecimal(columnLabel);
		return (value != null) ? value : new BigDecimal(Double.toString(valueIfNull));
	}

	public String getString(String columnLabel, String valueIfNull) throws java.sql.SQLException {
		Asserts.notNull(valueIfNull);

		String value = this.rs.getString(columnLabel);
		return (value != null) ? value : valueIfNull;
	}

	public String getNString(String columnLabel, String valueIfNull) throws java.sql.SQLException {
		Asserts.notNull(valueIfNull);

		String value = this.rs.getNString(columnLabel);
		return (value != null) ? value : valueIfNull;
	}

	// Get required values

	private AssertException nullValueFoundException(String columnLabel) {
		return new AssertException(STR.fmt("Null value found under the label '{}'.", columnLabel));
	}

	public boolean getBooleanReq(String columnLabel) throws java.sql.SQLException {
		boolean value = this.rs.getBoolean(columnLabel);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnLabel);
		}
		return value;
	}

	public byte getByteReq(String columnLabel) throws java.sql.SQLException {
		byte value = this.rs.getByte(columnLabel);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnLabel);
		}
		return value;
	}

	public short getShortReq(String columnLabel) throws java.sql.SQLException {
		short value = this.rs.getShort(columnLabel);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnLabel);
		}
		return value;
	}

	public int getIntReq(String columnLabel) throws java.sql.SQLException {
		int value = this.rs.getInt(columnLabel);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnLabel);
		}
		return value;
	}

	public long getLongReq(String columnLabel) throws java.sql.SQLException {
		long value = this.rs.getLong(columnLabel);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnLabel);
		}
		return value;
	}

	public float getFloatReq(String columnLabel) throws java.sql.SQLException {
		float value = this.rs.getFloat(columnLabel);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnLabel);
		}
		return value;
	}

	public double getDoubleReq(String columnLabel) throws java.sql.SQLException {
		double value = this.rs.getDouble(columnLabel);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnLabel);
		}
		return value;
	}

	public BigDecimal getBigDecimalReq(String columnLabel) throws java.sql.SQLException {
		BigDecimal value = this.rs.getBigDecimal(columnLabel);
		if (value == null) {
			throw nullValueFoundException(columnLabel);
		}
		return value;
	}

	public String getStringReq(String columnLabel) throws java.sql.SQLException {
		String value = this.rs.getString(columnLabel);
		if (value == null) {
			throw nullValueFoundException(columnLabel);
		}
		return value;
	}

	public String getNStringReq(String columnLabel) throws java.sql.SQLException {
		String value = this.rs.getNString(columnLabel);
		if (value == null) {
			throw nullValueFoundException(columnLabel);
		}
		return value;
	}

	public <T> T getObjectReq(String columnLabel, Class<T> type) throws java.sql.SQLException {
		T value = this.rs.getObject(columnLabel, type);
		if (value == null) {
			throw nullValueFoundException(columnLabel);
		}
		return value;
	}

	// Java 8 Date/Times

	public LocalDate getLocalDateReq(String columnLabel) throws java.sql.SQLException {
		return getObjectReq(columnLabel, LocalDate.class);
	}

	public LocalDate getLocalDate(String columnLabel) throws java.sql.SQLException {
		return this.rs.getObject(columnLabel, LocalDate.class);
	}

	public LocalDateTime getLocalDateTimeReq(String columnLabel) throws java.sql.SQLException {
		return getObjectReq(columnLabel, LocalDateTime.class);
	}

	public LocalDateTime getLocalDateTime(String columnLabel) throws java.sql.SQLException {
		return this.rs.getObject(columnLabel, LocalDateTime.class);
	}

	public LocalTime getLocalTimeReq(String columnLabel) throws java.sql.SQLException {
		return getObjectReq(columnLabel, LocalTime.class);
	}

	public LocalTime getLocalTime(String columnLabel) throws java.sql.SQLException {
		return this.rs.getObject(columnLabel, LocalTime.class);
	}

	public OffsetDateTime getOffsetDateTimeReq(String columnLabel) throws java.sql.SQLException {
		return getObjectReq(columnLabel, OffsetDateTime.class);
	}

	public OffsetDateTime getOffsetDateTime(String columnLabel) throws java.sql.SQLException {
		return this.rs.getObject(columnLabel, OffsetDateTime.class);
	}

	public OffsetTime getOffsetTimeReq(String columnLabel) throws java.sql.SQLException {
		return getObjectReq(columnLabel, OffsetTime.class);
	}

	public OffsetTime getOffsetTime(String columnLabel) throws java.sql.SQLException {
		return this.rs.getObject(columnLabel, OffsetTime.class);
	}

	// Get Primitive Wrappers

	public Boolean getBooleanOpt(String columnLabel) throws java.sql.SQLException {
		boolean value = this.rs.getBoolean(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Byte getByteOpt(String columnLabel) throws java.sql.SQLException {
		byte value = this.rs.getByte(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Short getShortOpt(String columnLabel) throws java.sql.SQLException {
		short value = this.rs.getShort(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Integer getIntOpt(String columnLabel) throws java.sql.SQLException {
		int value = this.rs.getInt(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Long getLongOpt(String columnLabel) throws java.sql.SQLException {
		long value = this.rs.getLong(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Float getFloatOpt(String columnLabel) throws java.sql.SQLException {
		float value = this.rs.getFloat(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	public Double getDoubleOpt(String columnLabel) throws java.sql.SQLException {
		double value = this.rs.getDouble(columnLabel);
		return !this.rs.wasNull() ? value : null;
	}

	// java.sql.ResultSet

	private AssertException nullValueFoundException(int columnIndex) {
		return new AssertException(STR.fmt("Null value found under the index {}.", columnIndex));
	}

	@Override
	public boolean getBoolean(int columnIndex) throws java.sql.SQLException {
		boolean value = this.rs.getBoolean(columnIndex);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnIndex);
		}
		return value;
	}

	@Override
	public boolean getBoolean(String columnLabel) throws java.sql.SQLException {
		return getBooleanReq(columnLabel);
	}

	@Override
	public String getString(String columnLabel) throws java.sql.SQLException {
		return this.rs.getString(columnLabel);
	}

	@Override
	public String getString(int columnIndex) throws java.sql.SQLException {
		return this.rs.getString(columnIndex);
	}

	@Override
	public String getNString(String columnLabel) throws java.sql.SQLException {
		return this.rs.getNString(columnLabel);
	}

	@Override
	public String getNString(int columnIndex) throws java.sql.SQLException {
		return this.rs.getNString(columnIndex);
	}

	@Override
	public byte getByte(String columnLabel) throws java.sql.SQLException {
		return getByteReq(columnLabel);
	}

	@Override
	public byte getByte(int columnIndex) throws java.sql.SQLException {
		byte value = this.rs.getByte(columnIndex);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnIndex);
		}
		return value;
	}

	@Override
	public byte[] getBytes(int columnIndex) throws java.sql.SQLException {
		return this.rs.getBytes(columnIndex);
	}

	@Override
	public byte[] getBytes(String columnLabel) throws java.sql.SQLException {
		return this.rs.getBytes(columnLabel);
	}

	@Override
	public short getShort(int columnIndex) throws java.sql.SQLException {
		short value = this.rs.getShort(columnIndex);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnIndex);
		}
		return value;
	}

	@Override
	public short getShort(String columnLabel) throws java.sql.SQLException {
		return getShortReq(columnLabel);
	}

	@Override
	public int getInt(int columnIndex) throws java.sql.SQLException {
		int value = this.rs.getInt(columnIndex);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnIndex);
		}
		return value;
	}

	@Override
	public int getInt(String columnLabel) throws java.sql.SQLException {
		return getIntReq(columnLabel);
	}

	@Override
	public long getLong(String columnLabel) throws java.sql.SQLException {
		return getLongReq(columnLabel);
	}

	@Override
	public long getLong(int columnIndex) throws java.sql.SQLException {
		long value = this.rs.getLong(columnIndex);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnIndex);
		}
		return value;
	}

	@Override
	public float getFloat(String columnLabel) throws java.sql.SQLException {
		return getFloatReq(columnLabel);
	}

	@Override
	public float getFloat(int columnIndex) throws java.sql.SQLException {
		float value = this.rs.getFloat(columnIndex);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnIndex);
		}
		return value;
	}

	@Override
	public double getDouble(String columnLabel) throws java.sql.SQLException {
		return getDoubleReq(columnLabel);
	}

	@Override
	public double getDouble(int columnIndex) throws java.sql.SQLException {
		double value = this.rs.getDouble(columnIndex);
		if (this.rs.wasNull()) {
			throw nullValueFoundException(columnIndex);
		}
		return value;
	}

	@Override
	@Deprecated
	public java.math.BigDecimal getBigDecimal(String columnLabel, int scale) throws java.sql.SQLException {
		return this.rs.getBigDecimal(columnLabel, scale);
	}

	@Override
	public java.math.BigDecimal getBigDecimal(String columnLabel) throws java.sql.SQLException {
		return this.rs.getBigDecimal(columnLabel);
	}

	@Override
	@Deprecated
	public java.math.BigDecimal getBigDecimal(int columnIndex, int scale) throws java.sql.SQLException {
		return this.rs.getBigDecimal(columnIndex, scale);
	}

	@Override
	public java.math.BigDecimal getBigDecimal(int columnIndex) throws java.sql.SQLException {
		return this.rs.getBigDecimal(columnIndex);
	}

	@Override
	public java.sql.Date getDate(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getDate(columnIndex, cal);
	}

	@Override
	public java.sql.Date getDate(String columnLabel) throws java.sql.SQLException {
		return this.rs.getDate(columnLabel);
	}

	@Override
	public java.sql.Date getDate(String columnLabel, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getDate(columnLabel, cal);
	}

	@Override
	public java.sql.Date getDate(int columnIndex) throws java.sql.SQLException {
		return this.rs.getDate(columnIndex);
	}

	@Override
	public java.sql.Time getTime(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getTime(columnIndex, cal);
	}

	@Override
	public java.sql.Time getTime(String columnLabel, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getTime(columnLabel, cal);
	}

	@Override
	public java.sql.Time getTime(int columnIndex) throws java.sql.SQLException {
		return this.rs.getTime(columnIndex);
	}

	@Override
	public java.sql.Time getTime(String columnLabel) throws java.sql.SQLException {
		return this.rs.getTime(columnLabel);
	}

	@Override
	public java.sql.Timestamp getTimestamp(String columnLabel) throws java.sql.SQLException {
		return this.rs.getTimestamp(columnLabel);
	}

	@Override
	public java.sql.Timestamp getTimestamp(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getTimestamp(columnIndex, cal);
	}

	@Override
	public java.sql.Timestamp getTimestamp(String columnLabel, java.util.Calendar cal) throws java.sql.SQLException {
		return this.rs.getTimestamp(columnLabel, cal);
	}

	@Override
	public java.sql.Timestamp getTimestamp(int columnIndex) throws java.sql.SQLException {
		return this.rs.getTimestamp(columnIndex);
	}

	@Override
	public Object getObject(int columnIndex, java.util.Map<String, Class<?>> map) throws java.sql.SQLException {
		return this.rs.getObject(columnIndex, map);
	}

	@Override
	public Object getObject(String columnLabel) throws java.sql.SQLException {
		int columnIndex = this.rs.findColumn(columnLabel);
		return getObject(columnIndex);
	}

	@Override
	public Object getObject(int columnIndex) throws java.sql.SQLException {
		ResultSetColumn col = this.getColumns().get(columnIndex - 1);

		switch (col.getSqlType()) {
		case Types.DATE:
			return rs.getObject(columnIndex, LocalDate.class);
		case Types.TIME:
			return rs.getObject(columnIndex, LocalTime.class);
		case Types.TIMESTAMP:
			return rs.getObject(columnIndex, LocalDateTime.class);

		case Types.TIME_WITH_TIMEZONE:
			return rs.getObject(columnIndex, OffsetTime.class);
		case Types.TIMESTAMP_WITH_TIMEZONE:
			return rs.getObject(columnIndex, OffsetDateTime.class);
		default:
			return this.rs.getObject(columnIndex);
		}
	}

	@Override
	public Object getObject(String columnLabel, java.util.Map<String, Class<?>> map) throws java.sql.SQLException {
		return this.rs.getObject(columnLabel, map);
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws java.sql.SQLException {
		return this.rs.getObject(columnIndex, type);
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type) throws java.sql.SQLException {
		return this.rs.getObject(columnLabel, type);
	}

	@Override
	public java.net.URL getURL(String columnLabel) throws java.sql.SQLException {
		return this.rs.getURL(columnLabel);
	}

	@Override
	public java.net.URL getURL(int columnIndex) throws java.sql.SQLException {
		return this.rs.getURL(columnIndex);
	}

	@Override
	public java.sql.Array getArray(String columnLabel) throws java.sql.SQLException {
		return this.rs.getArray(columnLabel);
	}

	@Override
	public java.sql.Array getArray(int columnIndex) throws java.sql.SQLException {
		return this.rs.getArray(columnIndex);
	}

	@Override
	public java.sql.SQLXML getSQLXML(String columnLabel) throws java.sql.SQLException {
		return this.rs.getSQLXML(columnLabel);
	}

	@Override
	public java.sql.SQLXML getSQLXML(int columnIndex) throws java.sql.SQLException {
		return this.rs.getSQLXML(columnIndex);
	}

	@Override
	public java.sql.Ref getRef(int columnIndex) throws java.sql.SQLException {
		return this.rs.getRef(columnIndex);
	}

	@Override
	public java.sql.Ref getRef(String columnLabel) throws java.sql.SQLException {
		return this.rs.getRef(columnLabel);
	}

	@Override
	public java.sql.RowId getRowId(String columnLabel) throws java.sql.SQLException {
		return this.rs.getRowId(columnLabel);
	}

	@Override
	public java.sql.RowId getRowId(int columnIndex) throws java.sql.SQLException {
		return this.rs.getRowId(columnIndex);
	}

	@Override
	public java.sql.Clob getClob(int columnIndex) throws java.sql.SQLException {
		return this.rs.getClob(columnIndex);
	}

	@Override
	public java.sql.Clob getClob(String columnLabel) throws java.sql.SQLException {
		return this.rs.getClob(columnLabel);
	}

	@Override
	public java.sql.NClob getNClob(int columnIndex) throws java.sql.SQLException {
		return this.rs.getNClob(columnIndex);
	}

	@Override
	public java.sql.NClob getNClob(String columnLabel) throws java.sql.SQLException {
		return this.rs.getNClob(columnLabel);
	}

	@Override
	public java.io.InputStream getAsciiStream(String columnLabel) throws java.sql.SQLException {
		return this.rs.getAsciiStream(columnLabel);
	}

	@Override
	public java.io.InputStream getAsciiStream(int columnIndex) throws java.sql.SQLException {
		return this.rs.getAsciiStream(columnIndex);
	}

	@Override
	public java.io.Reader getCharacterStream(int columnIndex) throws java.sql.SQLException {
		return this.rs.getCharacterStream(columnIndex);
	}

	@Override
	public java.io.Reader getCharacterStream(String columnLabel) throws java.sql.SQLException {
		return this.rs.getCharacterStream(columnLabel);
	}

	@Override
	public java.io.Reader getNCharacterStream(int columnIndex) throws java.sql.SQLException {
		return this.rs.getNCharacterStream(columnIndex);
	}

	@Override
	public java.io.Reader getNCharacterStream(String columnLabel) throws java.sql.SQLException {
		return this.rs.getNCharacterStream(columnLabel);
	}

	@Override
	@Deprecated
	public java.io.InputStream getUnicodeStream(int columnIndex) throws java.sql.SQLException {
		return this.rs.getUnicodeStream(columnIndex);
	}

	@Override
	@Deprecated
	public java.io.InputStream getUnicodeStream(String columnLabel) throws java.sql.SQLException {
		return this.rs.getUnicodeStream(columnLabel);
	}

	@Override
	public java.sql.Blob getBlob(String columnLabel) throws java.sql.SQLException {
		return this.rs.getBlob(columnLabel);
	}

	@Override
	public java.sql.Blob getBlob(int columnIndex) throws java.sql.SQLException {
		return this.rs.getBlob(columnIndex);
	}

	@Override
	public java.io.InputStream getBinaryStream(String columnLabel) throws java.sql.SQLException {
		return this.rs.getBinaryStream(columnLabel);
	}

	@Override
	public java.io.InputStream getBinaryStream(int columnIndex) throws java.sql.SQLException {
		return this.rs.getBinaryStream(columnIndex);
	}

	@Override
	public int getConcurrency() throws java.sql.SQLException {
		return this.rs.getConcurrency();
	}

	@Override
	public String getCursorName() throws java.sql.SQLException {
		return this.rs.getCursorName();
	}

	@Override
	public int getFetchDirection() throws java.sql.SQLException {
		return this.rs.getFetchDirection();
	}

	@Override
	public int getFetchSize() throws java.sql.SQLException {
		return this.rs.getFetchSize();
	}

	@Override
	public int getHoldability() throws java.sql.SQLException {
		return this.rs.getHoldability();
	}

	@Override
	public java.sql.ResultSetMetaData getMetaData() throws java.sql.SQLException {
		return this.rs.getMetaData();
	}

	@Override
	public int getRow() throws java.sql.SQLException {
		return this.rs.getRow();
	}

	@Override
	public java.sql.Statement getStatement() throws java.sql.SQLException {
		return this.rs.getStatement();
	}

	@Override
	public int getType() throws java.sql.SQLException {
		return this.rs.getType();
	}

	@Override
	public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
		return this.rs.getWarnings();
	}

	@Override
	public void updateBoolean(String columnLabel, boolean x) throws java.sql.SQLException {
		this.rs.updateBoolean(columnLabel, x);
	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws java.sql.SQLException {
		this.rs.updateBoolean(columnIndex, x);
	}

	@Override
	public void updateString(int columnIndex, String x) throws java.sql.SQLException {
		this.rs.updateString(columnIndex, x);
	}

	@Override
	public void updateString(String columnLabel, String x) throws java.sql.SQLException {
		this.rs.updateString(columnLabel, x);
	}

	@Override
	public void updateNString(String columnLabel, String nString) throws java.sql.SQLException {
		this.rs.updateNString(columnLabel, nString);
	}

	@Override
	public void updateNString(int columnIndex, String nString) throws java.sql.SQLException {
		this.rs.updateNString(columnIndex, nString);
	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws java.sql.SQLException {
		this.rs.updateBytes(columnLabel, x);
	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws java.sql.SQLException {
		this.rs.updateBytes(columnIndex, x);
	}

	@Override
	public void updateByte(String columnLabel, byte x) throws java.sql.SQLException {
		this.rs.updateByte(columnLabel, x);
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws java.sql.SQLException {
		this.rs.updateByte(columnIndex, x);
	}

	@Override
	public void updateShort(String columnLabel, short x) throws java.sql.SQLException {
		this.rs.updateShort(columnLabel, x);
	}

	@Override
	public void updateShort(int columnIndex, short x) throws java.sql.SQLException {
		this.rs.updateShort(columnIndex, x);
	}

	@Override
	public void updateInt(int columnIndex, int x) throws java.sql.SQLException {
		this.rs.updateInt(columnIndex, x);
	}

	@Override
	public void updateInt(String columnLabel, int x) throws java.sql.SQLException {
		this.rs.updateInt(columnLabel, x);
	}

	@Override
	public void updateLong(String columnLabel, long x) throws java.sql.SQLException {
		this.rs.updateLong(columnLabel, x);
	}

	@Override
	public void updateLong(int columnIndex, long x) throws java.sql.SQLException {
		this.rs.updateLong(columnIndex, x);
	}

	@Override
	public void updateFloat(String columnLabel, float x) throws java.sql.SQLException {
		this.rs.updateFloat(columnLabel, x);
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws java.sql.SQLException {
		this.rs.updateFloat(columnIndex, x);
	}

	@Override
	public void updateDouble(String columnLabel, double x) throws java.sql.SQLException {
		this.rs.updateDouble(columnLabel, x);
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws java.sql.SQLException {
		this.rs.updateDouble(columnIndex, x);
	}

	@Override
	public void updateBigDecimal(int columnIndex, java.math.BigDecimal x) throws java.sql.SQLException {
		this.rs.updateBigDecimal(columnIndex, x);
	}

	@Override
	public void updateBigDecimal(String columnLabel, java.math.BigDecimal x) throws java.sql.SQLException {
		this.rs.updateBigDecimal(columnLabel, x);
	}

	@Override
	public void updateAsciiStream(String columnLabel, java.io.InputStream x) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnLabel, x);
	}

	@Override
	public void updateAsciiStream(int columnIndex, java.io.InputStream x) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnIndex, x);
	}

	@Override
	public void updateAsciiStream(String columnLabel, java.io.InputStream x, int length) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateAsciiStream(String columnLabel, java.io.InputStream x, long length) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateAsciiStream(int columnIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateAsciiStream(int columnIndex, java.io.InputStream x, long length) throws java.sql.SQLException {
		this.rs.updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, java.io.InputStream x) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnLabel, x);
	}

	@Override
	public void updateBinaryStream(String columnLabel, java.io.InputStream x, int length) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, java.io.InputStream x, long length) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, java.io.InputStream x, long length) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, java.io.InputStream x) throws java.sql.SQLException {
		this.rs.updateBinaryStream(columnIndex, x);
	}

	@Override
	public void updateCharacterStream(int columnIndex, java.io.Reader x, long length) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, java.io.Reader x, int length) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, java.io.Reader x) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnIndex, x);
	}

	@Override
	public void updateCharacterStream(String columnLabel, java.io.Reader reader, int length) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateNull(int columnIndex) throws java.sql.SQLException {
		this.rs.updateNull(columnIndex);
	}

	@Override
	public void updateNull(String columnLabel) throws java.sql.SQLException {
		this.rs.updateNull(columnLabel);
	}

	@Override
	public void updateDate(int columnIndex, java.sql.Date x) throws java.sql.SQLException {
		this.rs.updateDate(columnIndex, x);
	}

	@Override
	public void updateDate(String columnLabel, java.sql.Date x) throws java.sql.SQLException {
		this.rs.updateDate(columnLabel, x);
	}

	@Override
	public void updateTime(String columnLabel, java.sql.Time x) throws java.sql.SQLException {
		this.rs.updateTime(columnLabel, x);
	}

	@Override
	public void updateTime(int columnIndex, java.sql.Time x) throws java.sql.SQLException {
		this.rs.updateTime(columnIndex, x);
	}

	@Override
	public void updateTimestamp(String columnLabel, java.sql.Timestamp x) throws java.sql.SQLException {
		this.rs.updateTimestamp(columnLabel, x);
	}

	@Override
	public void updateTimestamp(int columnIndex, java.sql.Timestamp x) throws java.sql.SQLException {
		this.rs.updateTimestamp(columnIndex, x);
	}

	@Override
	public void updateObject(int columnIndex, Object x, java.sql.SQLType targetSqlType, int scaleOrLength) throws java.sql.SQLException {
		this.rs.updateObject(columnIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void updateObject(int columnIndex, Object x, java.sql.SQLType targetSqlType) throws java.sql.SQLException {
		this.rs.updateObject(columnIndex, x, targetSqlType);
	}

	@Override
	public void updateObject(String columnLabel, Object x, java.sql.SQLType targetSqlType, int scaleOrLength) throws java.sql.SQLException {
		this.rs.updateObject(columnLabel, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void updateObject(String columnLabel, Object x, java.sql.SQLType targetSqlType) throws java.sql.SQLException {
		this.rs.updateObject(columnLabel, x, targetSqlType);
	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws java.sql.SQLException {
		this.rs.updateObject(columnLabel, x, scaleOrLength);
	}

	@Override
	public void updateObject(int columnIndex, Object x) throws java.sql.SQLException {
		this.rs.updateObject(columnIndex, x);
	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws java.sql.SQLException {
		this.rs.updateObject(columnIndex, x, scaleOrLength);
	}

	@Override
	public void updateObject(String columnLabel, Object x) throws java.sql.SQLException {
		this.rs.updateObject(columnLabel, x);
	}

	@Override
	public void updateRow() throws java.sql.SQLException {
		this.rs.updateRow();
	}

	@Override
	public void updateRef(int columnIndex, java.sql.Ref x) throws java.sql.SQLException {
		this.rs.updateRef(columnIndex, x);
	}

	@Override
	public void updateRef(String columnLabel, java.sql.Ref x) throws java.sql.SQLException {
		this.rs.updateRef(columnLabel, x);
	}

	@Override
	public void updateBlob(String columnLabel, java.io.InputStream inputStream, long length) throws java.sql.SQLException {
		this.rs.updateBlob(columnLabel, inputStream, length);
	}

	@Override
	public void updateBlob(String columnLabel, java.io.InputStream inputStream) throws java.sql.SQLException {
		this.rs.updateBlob(columnLabel, inputStream);
	}

	@Override
	public void updateBlob(int columnIndex, java.io.InputStream inputStream) throws java.sql.SQLException {
		this.rs.updateBlob(columnIndex, inputStream);
	}

	@Override
	public void updateBlob(int columnIndex, java.io.InputStream inputStream, long length) throws java.sql.SQLException {
		this.rs.updateBlob(columnIndex, inputStream, length);
	}

	@Override
	public void updateBlob(String columnLabel, java.sql.Blob x) throws java.sql.SQLException {
		this.rs.updateBlob(columnLabel, x);
	}

	@Override
	public void updateBlob(int columnIndex, java.sql.Blob x) throws java.sql.SQLException {
		this.rs.updateBlob(columnIndex, x);
	}

	@Override
	public void updateClob(String columnLabel, java.sql.Clob x) throws java.sql.SQLException {
		this.rs.updateClob(columnLabel, x);
	}

	@Override
	public void updateClob(int columnIndex, java.sql.Clob x) throws java.sql.SQLException {
		this.rs.updateClob(columnIndex, x);
	}

	@Override
	public void updateClob(int columnIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateClob(columnIndex, reader, length);
	}

	@Override
	public void updateClob(int columnIndex, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateClob(columnIndex, reader);
	}

	@Override
	public void updateClob(String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateClob(columnLabel, reader);
	}

	@Override
	public void updateClob(String columnLabel, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateClob(columnLabel, reader, length);
	}

	@Override
	public void updateArray(int columnIndex, java.sql.Array x) throws java.sql.SQLException {
		this.rs.updateArray(columnIndex, x);
	}

	@Override
	public void updateArray(String columnLabel, java.sql.Array x) throws java.sql.SQLException {
		this.rs.updateArray(columnLabel, x);
	}

	@Override
	public void updateRowId(int columnIndex, java.sql.RowId x) throws java.sql.SQLException {
		this.rs.updateRowId(columnIndex, x);
	}

	@Override
	public void updateRowId(String columnLabel, java.sql.RowId x) throws java.sql.SQLException {
		this.rs.updateRowId(columnLabel, x);
	}

	@Override
	public void updateNClob(String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateNClob(columnLabel, reader);
	}

	@Override
	public void updateNClob(int columnIndex, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateNClob(columnIndex, reader);
	}

	@Override
	public void updateNClob(int columnIndex, java.sql.NClob nClob) throws java.sql.SQLException {
		this.rs.updateNClob(columnIndex, nClob);
	}

	@Override
	public void updateNClob(String columnLabel, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateNClob(columnLabel, reader, length);
	}

	@Override
	public void updateNClob(String columnLabel, java.sql.NClob nClob) throws java.sql.SQLException {
		this.rs.updateNClob(columnLabel, nClob);
	}

	@Override
	public void updateNClob(int columnIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateNClob(columnIndex, reader, length);
	}

	@Override
	public void updateSQLXML(String columnLabel, java.sql.SQLXML xmlObject) throws java.sql.SQLException {
		this.rs.updateSQLXML(columnLabel, xmlObject);
	}

	@Override
	public void updateSQLXML(int columnIndex, java.sql.SQLXML xmlObject) throws java.sql.SQLException {
		this.rs.updateSQLXML(columnIndex, xmlObject);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, java.io.Reader x) throws java.sql.SQLException {
		this.rs.updateNCharacterStream(columnIndex, x);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
		this.rs.updateNCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.rs.updateNCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, java.io.Reader x, long length) throws java.sql.SQLException {
		this.rs.updateNCharacterStream(columnIndex, x, length);
	}

	@Override
	public boolean rowUpdated() throws java.sql.SQLException {
		return this.rs.rowUpdated();
	}

	@Override
	public void cancelRowUpdates() throws java.sql.SQLException {
		this.rs.cancelRowUpdates();
	}

	@Override
	public boolean wasNull() throws java.sql.SQLException {
		return this.rs.wasNull();
	}

	@Override
	public boolean isBeforeFirst() throws java.sql.SQLException {
		return this.rs.isBeforeFirst();
	}

	@Override
	public void beforeFirst() throws java.sql.SQLException {
		this.rs.beforeFirst();
	}

	@Override
	public void refreshRow() throws java.sql.SQLException {
		this.rs.refreshRow();
	}

	@Override
	public boolean absolute(int row) throws java.sql.SQLException {
		return this.rs.absolute(row);
	}

	@Override
	public void afterLast() throws java.sql.SQLException {
		this.rs.afterLast();
	}

	@Override
	public void clearWarnings() throws java.sql.SQLException {
		this.rs.clearWarnings();
	}

	@Override
	public void deleteRow() throws java.sql.SQLException {
		this.rs.deleteRow();
	}

	@Override
	public int findColumn(String columnLabel) throws java.sql.SQLException {
		return this.rs.findColumn(columnLabel);
	}

	@Override
	public boolean first() throws java.sql.SQLException {
		return this.rs.first();
	}

	@Override
	public void insertRow() throws java.sql.SQLException {
		this.rs.insertRow();
	}

	@Override
	public boolean isAfterLast() throws java.sql.SQLException {
		return this.rs.isAfterLast();
	}

	@Override
	public boolean isClosed() throws java.sql.SQLException {
		return this.rs.isClosed();
	}

	@Override
	public boolean isFirst() throws java.sql.SQLException {
		return this.rs.isFirst();
	}

	@Override
	public boolean isLast() throws java.sql.SQLException {
		return this.rs.isLast();
	}

	@Override
	public boolean last() throws java.sql.SQLException {
		return this.rs.last();
	}

	@Override
	public void moveToCurrentRow() throws java.sql.SQLException {
		this.rs.moveToCurrentRow();
	}

	@Override
	public void moveToInsertRow() throws java.sql.SQLException {
		this.rs.moveToInsertRow();
	}

	@Override
	public boolean next() throws java.sql.SQLException {
		return this.rs.next();
	}

	@Override
	public boolean previous() throws java.sql.SQLException {
		return this.rs.previous();
	}

	@Override
	public boolean relative(int rows) throws java.sql.SQLException {
		return this.rs.relative(rows);
	}

	@Override
	public boolean rowDeleted() throws java.sql.SQLException {
		return this.rs.rowDeleted();
	}

	@Override
	public boolean rowInserted() throws java.sql.SQLException {
		return this.rs.rowInserted();
	}

	@Override
	public void setFetchDirection(int direction) throws java.sql.SQLException {
		this.rs.setFetchDirection(direction);
	}

	@Override
	public void setFetchSize(int rows) throws java.sql.SQLException {
		this.rs.setFetchSize(rows);
	}

	// java.sql.Wrapper

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws java.sql.SQLException {
		return this.rs.isWrapperFor(arg0);
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws java.sql.SQLException {
		return this.rs.unwrap(arg0);
	}

	// AutoCloseable

	@Override
	public void close() throws java.sql.SQLException {
		this.rs.close();
	}
}
