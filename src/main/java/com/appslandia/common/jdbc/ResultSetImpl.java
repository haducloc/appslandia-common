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

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.NormalizeUtils;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class ResultSetImpl implements ResultSet {

  protected final ResultSet rs;
  protected List<ResultSetColumn> columns;

  public ResultSetImpl(ResultSet rs) {
    Arguments.isTrue(!(rs instanceof ResultSetImpl));
    this.rs = Arguments.notNull(rs);
  }

  public List<ResultSetColumn> getColumns() throws java.sql.SQLException {
    if (columns == null) {
      columns = JdbcUtils.getResultSetColumns(rs);
    }
    return columns;
  }

  public String valuesAsID(String... columnLabels) throws UncheckedSQLException {
    Arguments.hasElements(columnLabels);

    var values = Arrays.stream(columnLabels).map(columnLabel -> {
      try {
        return this.getObject(columnLabel);

      } catch (SQLException ex) {
        throw new UncheckedSQLException(ex);
      }
    }).toArray();

    return NormalizeUtils.valuesAsID(values);
  }

  protected IllegalStateException assertNotNull(String columnLabel) {
    return new IllegalStateException(STR.fmt("Null value found under the label '{}'.", columnLabel));
  }

  // Strings

  public String getStringReq(String columnLabel) throws java.sql.SQLException {
    var value = rs.getString(columnLabel);
    if (value == null) {
      throw assertNotNull(columnLabel);
    }
    return value;
  }

  public String getString(String columnLabel, String ifNull) throws java.sql.SQLException {
    var value = rs.getString(columnLabel);
    return (value != null) ? value : ifNull;
  }

  public String getNStringReq(String columnLabel) throws java.sql.SQLException {
    var value = rs.getNString(columnLabel);
    if (value == null) {
      throw assertNotNull(columnLabel);
    }
    return value;
  }

  public String getNString(String columnLabel, String ifNull) throws java.sql.SQLException {
    var value = rs.getNString(columnLabel);
    return (value != null) ? value : ifNull;
  }

  public String getStringUpper(String columnLabel) throws java.sql.SQLException {
    return getStringUpper(columnLabel, Locale.ROOT);
  }

  public String getStringUpper(String columnLabel, Locale locale) throws java.sql.SQLException {
    var value = rs.getString(columnLabel);
    return (value != null) ? value.toUpperCase(locale) : null;
  }

  public String getStringUpper(String columnLabel, String ifNull) throws java.sql.SQLException {
    return getStringUpper(columnLabel, ifNull, Locale.ROOT);
  }

  public String getStringUpper(String columnLabel, String ifNull, Locale locale) throws java.sql.SQLException {
    var value = rs.getString(columnLabel);
    return (value != null) ? value.toUpperCase(locale) : StringUtils.toUpperCase(ifNull, locale);
  }

  public String getStringUpperReq(String columnLabel) throws java.sql.SQLException {
    return getStringUpperReq(columnLabel, Locale.ROOT);
  }

  public String getStringUpperReq(String columnLabel, Locale locale) throws java.sql.SQLException {
    var value = getStringReq(columnLabel);
    return value.toUpperCase(locale);
  }

  public String getStringLower(String columnLabel) throws java.sql.SQLException {
    return getStringLower(columnLabel, Locale.ROOT);
  }

  public String getStringLower(String columnLabel, Locale locale) throws java.sql.SQLException {
    var value = rs.getString(columnLabel);
    return (value != null) ? value.toLowerCase(locale) : null;
  }

  public String getStringLower(String columnLabel, String ifNull) throws java.sql.SQLException {
    return getStringLower(columnLabel, ifNull, Locale.ROOT);
  }

  public String getStringLower(String columnLabel, String ifNull, Locale locale) throws java.sql.SQLException {
    var value = rs.getString(columnLabel);
    return (value != null) ? value.toLowerCase(locale) : StringUtils.toLowerCase(ifNull, locale);
  }

  public String getStringLowerReq(String columnLabel) throws java.sql.SQLException {
    return getStringLowerReq(columnLabel, Locale.ROOT);
  }

  public String getStringLowerReq(String columnLabel, Locale locale) throws java.sql.SQLException {
    var value = getStringReq(columnLabel);
    return value.toLowerCase(locale);
  }

  // Primitives & Wrappers

  public boolean getBool(String columnLabel, boolean ifNull) throws java.sql.SQLException {
    var value = rs.getBoolean(columnLabel);
    return !rs.wasNull() ? value : ifNull;
  }

  public Boolean getBoolOpt(String columnLabel) throws java.sql.SQLException {
    var value = rs.getBoolean(columnLabel);
    return !rs.wasNull() ? value : null;
  }

  public boolean getBoolReq(String columnLabel) throws java.sql.SQLException {
    var value = rs.getBoolean(columnLabel);
    if (rs.wasNull()) {
      throw assertNotNull(columnLabel);
    }
    return value;
  }

  public byte getByte(String columnLabel, byte ifNull) throws java.sql.SQLException {
    var value = rs.getByte(columnLabel);
    return !rs.wasNull() ? value : ifNull;
  }

  public Byte getByteOpt(String columnLabel) throws java.sql.SQLException {
    var value = rs.getByte(columnLabel);
    return !rs.wasNull() ? value : null;
  }

  public byte getByteReq(String columnLabel) throws java.sql.SQLException {
    var value = rs.getByte(columnLabel);
    if (rs.wasNull()) {
      throw assertNotNull(columnLabel);
    }
    return value;
  }

  public short getShort(String columnLabel, short ifNull) throws java.sql.SQLException {
    var value = rs.getShort(columnLabel);
    return !rs.wasNull() ? value : ifNull;
  }

  public Short getShortOpt(String columnLabel) throws java.sql.SQLException {
    var value = rs.getShort(columnLabel);
    return !rs.wasNull() ? value : null;
  }

  public short getShortReq(String columnLabel) throws java.sql.SQLException {
    var value = rs.getShort(columnLabel);
    if (rs.wasNull()) {
      throw assertNotNull(columnLabel);
    }
    return value;
  }

  public int getInt(String columnLabel, int ifNull) throws java.sql.SQLException {
    var value = rs.getInt(columnLabel);
    return !rs.wasNull() ? value : ifNull;
  }

  public Integer getIntOpt(String columnLabel) throws java.sql.SQLException {
    var value = rs.getInt(columnLabel);
    return !rs.wasNull() ? value : null;
  }

  public int getIntReq(String columnLabel) throws java.sql.SQLException {
    var value = rs.getInt(columnLabel);
    if (rs.wasNull()) {
      throw assertNotNull(columnLabel);
    }
    return value;
  }

  public long getLong(String columnLabel, long ifNull) throws java.sql.SQLException {
    var value = rs.getLong(columnLabel);
    return !rs.wasNull() ? value : ifNull;
  }

  public Long getLongOpt(String columnLabel) throws java.sql.SQLException {
    var value = rs.getLong(columnLabel);
    return !rs.wasNull() ? value : null;
  }

  public long getLongReq(String columnLabel) throws java.sql.SQLException {
    var value = rs.getLong(columnLabel);
    if (rs.wasNull()) {
      throw assertNotNull(columnLabel);
    }
    return value;
  }

  public float getFloat(String columnLabel, float ifNull) throws java.sql.SQLException {
    var value = rs.getFloat(columnLabel);
    return !rs.wasNull() ? value : ifNull;
  }

  public Float getFloatOpt(String columnLabel) throws java.sql.SQLException {
    var value = rs.getFloat(columnLabel);
    return !rs.wasNull() ? value : null;
  }

  public float getFloatReq(String columnLabel) throws java.sql.SQLException {
    var value = rs.getFloat(columnLabel);
    if (rs.wasNull()) {
      throw assertNotNull(columnLabel);
    }
    return value;
  }

  public double getDouble(String columnLabel, double ifNull) throws java.sql.SQLException {
    var value = rs.getDouble(columnLabel);
    return !rs.wasNull() ? value : ifNull;
  }

  public Double getDoubleOpt(String columnLabel) throws java.sql.SQLException {
    var value = rs.getDouble(columnLabel);
    return !rs.wasNull() ? value : null;
  }

  public double getDoubleReq(String columnLabel) throws java.sql.SQLException {
    var value = rs.getDouble(columnLabel);
    if (rs.wasNull()) {
      throw assertNotNull(columnLabel);
    }
    return value;
  }

  // Decimal

  public BigDecimal getDecimalReq(String columnLabel) throws java.sql.SQLException {
    var value = rs.getBigDecimal(columnLabel);
    if (value == null) {
      throw assertNotNull(columnLabel);
    }
    return value;
  }

  public BigDecimal getDecimal(String columnLabel, double ifNull) throws java.sql.SQLException {
    var value = rs.getBigDecimal(columnLabel);
    return (value != null) ? value : new BigDecimal(Double.toString(ifNull));
  }

  // Object

  public <T> T getObjectReq(String columnLabel, Class<T> type) throws java.sql.SQLException {
    var value = rs.getObject(columnLabel, type);
    if (value == null) {
      throw assertNotNull(columnLabel);
    }
    return value;
  }

  // Temporal

  public LocalDate getLocalDateReq(String columnLabel) throws java.sql.SQLException {
    return getObjectReq(columnLabel, LocalDate.class);
  }

  public LocalDate getLocalDate(String columnLabel) throws java.sql.SQLException {
    return rs.getObject(columnLabel, LocalDate.class);
  }

  public LocalDateTime getLocalDateTimeReq(String columnLabel) throws java.sql.SQLException {
    return getObjectReq(columnLabel, LocalDateTime.class);
  }

  public LocalDateTime getLocalDateTime(String columnLabel) throws java.sql.SQLException {
    return rs.getObject(columnLabel, LocalDateTime.class);
  }

  public LocalTime getLocalTimeReq(String columnLabel) throws java.sql.SQLException {
    return getObjectReq(columnLabel, LocalTime.class);
  }

  public LocalTime getLocalTime(String columnLabel) throws java.sql.SQLException {
    return rs.getObject(columnLabel, LocalTime.class);
  }

  public OffsetDateTime getOffsetDateTimeReq(String columnLabel) throws java.sql.SQLException {
    return getObjectReq(columnLabel, OffsetDateTime.class);
  }

  public OffsetDateTime getOffsetDateTime(String columnLabel) throws java.sql.SQLException {
    return rs.getObject(columnLabel, OffsetDateTime.class);
  }

  public OffsetTime getOffsetTimeReq(String columnLabel) throws java.sql.SQLException {
    return getObjectReq(columnLabel, OffsetTime.class);
  }

  public OffsetTime getOffsetTime(String columnLabel) throws java.sql.SQLException {
    return rs.getObject(columnLabel, OffsetTime.class);
  }

  // java.sql.ResultSet

  @Override
  public boolean getBoolean(int columnIndex) throws java.sql.SQLException {
    return rs.getBoolean(columnIndex);
  }

  @Override
  public boolean getBoolean(String columnLabel) throws java.sql.SQLException {
    return rs.getBoolean(columnLabel);
  }

  @Override
  public String getString(String columnLabel) throws java.sql.SQLException {
    return rs.getString(columnLabel);
  }

  @Override
  public String getString(int columnIndex) throws java.sql.SQLException {
    return rs.getString(columnIndex);
  }

  @Override
  public String getNString(String columnLabel) throws java.sql.SQLException {
    return rs.getNString(columnLabel);
  }

  @Override
  public String getNString(int columnIndex) throws java.sql.SQLException {
    return rs.getNString(columnIndex);
  }

  @Override
  public byte getByte(String columnLabel) throws java.sql.SQLException {
    return rs.getByte(columnLabel);
  }

  @Override
  public byte getByte(int columnIndex) throws java.sql.SQLException {
    return rs.getByte(columnIndex);
  }

  @Override
  public byte[] getBytes(int columnIndex) throws java.sql.SQLException {
    return rs.getBytes(columnIndex);
  }

  @Override
  public byte[] getBytes(String columnLabel) throws java.sql.SQLException {
    return rs.getBytes(columnLabel);
  }

  @Override
  public short getShort(int columnIndex) throws java.sql.SQLException {
    return rs.getShort(columnIndex);
  }

  @Override
  public short getShort(String columnLabel) throws java.sql.SQLException {
    return rs.getShort(columnLabel);
  }

  @Override
  public int getInt(int columnIndex) throws java.sql.SQLException {
    return rs.getInt(columnIndex);
  }

  @Override
  public int getInt(String columnLabel) throws java.sql.SQLException {
    return rs.getInt(columnLabel);
  }

  @Override
  public long getLong(String columnLabel) throws java.sql.SQLException {
    return rs.getLong(columnLabel);
  }

  @Override
  public long getLong(int columnIndex) throws java.sql.SQLException {
    return rs.getLong(columnIndex);
  }

  @Override
  public float getFloat(String columnLabel) throws java.sql.SQLException {
    return rs.getFloat(columnLabel);
  }

  @Override
  public float getFloat(int columnIndex) throws java.sql.SQLException {
    return rs.getFloat(columnIndex);
  }

  @Override
  public double getDouble(String columnLabel) throws java.sql.SQLException {
    return rs.getDouble(columnLabel);
  }

  @Override
  public double getDouble(int columnIndex) throws java.sql.SQLException {
    return rs.getDouble(columnIndex);
  }

  @Override
  @Deprecated
  public java.math.BigDecimal getBigDecimal(String columnLabel, int scale) throws java.sql.SQLException {
    return rs.getBigDecimal(columnLabel, scale);
  }

  @Override
  public java.math.BigDecimal getBigDecimal(String columnLabel) throws java.sql.SQLException {
    return rs.getBigDecimal(columnLabel);
  }

  @Override
  @Deprecated
  public java.math.BigDecimal getBigDecimal(int columnIndex, int scale) throws java.sql.SQLException {
    return rs.getBigDecimal(columnIndex, scale);
  }

  @Override
  public java.math.BigDecimal getBigDecimal(int columnIndex) throws java.sql.SQLException {
    return rs.getBigDecimal(columnIndex);
  }

  @Override
  public java.sql.Date getDate(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
    return rs.getDate(columnIndex, cal);
  }

  @Override
  public java.sql.Date getDate(String columnLabel) throws java.sql.SQLException {
    return rs.getDate(columnLabel);
  }

  @Override
  public java.sql.Date getDate(String columnLabel, java.util.Calendar cal) throws java.sql.SQLException {
    return rs.getDate(columnLabel, cal);
  }

  @Override
  public java.sql.Date getDate(int columnIndex) throws java.sql.SQLException {
    return rs.getDate(columnIndex);
  }

  @Override
  public java.sql.Time getTime(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
    return rs.getTime(columnIndex, cal);
  }

  @Override
  public java.sql.Time getTime(String columnLabel, java.util.Calendar cal) throws java.sql.SQLException {
    return rs.getTime(columnLabel, cal);
  }

  @Override
  public java.sql.Time getTime(int columnIndex) throws java.sql.SQLException {
    return rs.getTime(columnIndex);
  }

  @Override
  public java.sql.Time getTime(String columnLabel) throws java.sql.SQLException {
    return rs.getTime(columnLabel);
  }

  @Override
  public java.sql.Timestamp getTimestamp(String columnLabel) throws java.sql.SQLException {
    return rs.getTimestamp(columnLabel);
  }

  @Override
  public java.sql.Timestamp getTimestamp(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
    return rs.getTimestamp(columnIndex, cal);
  }

  @Override
  public java.sql.Timestamp getTimestamp(String columnLabel, java.util.Calendar cal) throws java.sql.SQLException {
    return rs.getTimestamp(columnLabel, cal);
  }

  @Override
  public java.sql.Timestamp getTimestamp(int columnIndex) throws java.sql.SQLException {
    return rs.getTimestamp(columnIndex);
  }

  @Override
  public Object getObject(int columnIndex, java.util.Map<String, Class<?>> map) throws java.sql.SQLException {
    return rs.getObject(columnIndex, map);
  }

  @Override
  public Object getObject(String columnLabel) throws java.sql.SQLException {
    var columnIndex = rs.findColumn(columnLabel);
    return getObject(columnIndex);
  }

  @Override
  public Object getObject(int columnIndex) throws java.sql.SQLException {
    var col = getColumns().get(columnIndex - 1);

    return switch (col.getSqlType()) {
    case Types.DATE -> rs.getObject(columnIndex, LocalDate.class);
    case Types.TIME -> rs.getObject(columnIndex, LocalTime.class);
    case Types.TIMESTAMP -> rs.getObject(columnIndex, LocalDateTime.class);
    case Types.TIME_WITH_TIMEZONE -> rs.getObject(columnIndex, OffsetTime.class);
    case Types.TIMESTAMP_WITH_TIMEZONE -> rs.getObject(columnIndex, OffsetDateTime.class);
    default -> rs.getObject(columnIndex);
    };
  }

  @Override
  public Object getObject(String columnLabel, java.util.Map<String, Class<?>> map) throws java.sql.SQLException {
    return rs.getObject(columnLabel, map);
  }

  @Override
  public <T> T getObject(int columnIndex, Class<T> type) throws java.sql.SQLException {
    return rs.getObject(columnIndex, type);
  }

  @Override
  public <T> T getObject(String columnLabel, Class<T> type) throws java.sql.SQLException {
    return rs.getObject(columnLabel, type);
  }

  @Override
  public java.net.URL getURL(String columnLabel) throws java.sql.SQLException {
    return rs.getURL(columnLabel);
  }

  @Override
  public java.net.URL getURL(int columnIndex) throws java.sql.SQLException {
    return rs.getURL(columnIndex);
  }

  @Override
  public java.sql.Array getArray(String columnLabel) throws java.sql.SQLException {
    return rs.getArray(columnLabel);
  }

  @Override
  public java.sql.Array getArray(int columnIndex) throws java.sql.SQLException {
    return rs.getArray(columnIndex);
  }

  @Override
  public java.sql.SQLXML getSQLXML(String columnLabel) throws java.sql.SQLException {
    return rs.getSQLXML(columnLabel);
  }

  @Override
  public java.sql.SQLXML getSQLXML(int columnIndex) throws java.sql.SQLException {
    return rs.getSQLXML(columnIndex);
  }

  @Override
  public java.sql.Ref getRef(int columnIndex) throws java.sql.SQLException {
    return rs.getRef(columnIndex);
  }

  @Override
  public java.sql.Ref getRef(String columnLabel) throws java.sql.SQLException {
    return rs.getRef(columnLabel);
  }

  @Override
  public java.sql.RowId getRowId(String columnLabel) throws java.sql.SQLException {
    return rs.getRowId(columnLabel);
  }

  @Override
  public java.sql.RowId getRowId(int columnIndex) throws java.sql.SQLException {
    return rs.getRowId(columnIndex);
  }

  @Override
  public java.sql.Clob getClob(int columnIndex) throws java.sql.SQLException {
    return rs.getClob(columnIndex);
  }

  @Override
  public java.sql.Clob getClob(String columnLabel) throws java.sql.SQLException {
    return rs.getClob(columnLabel);
  }

  @Override
  public java.sql.NClob getNClob(int columnIndex) throws java.sql.SQLException {
    return rs.getNClob(columnIndex);
  }

  @Override
  public java.sql.NClob getNClob(String columnLabel) throws java.sql.SQLException {
    return rs.getNClob(columnLabel);
  }

  @Override
  public java.io.InputStream getAsciiStream(String columnLabel) throws java.sql.SQLException {
    return rs.getAsciiStream(columnLabel);
  }

  @Override
  public java.io.InputStream getAsciiStream(int columnIndex) throws java.sql.SQLException {
    return rs.getAsciiStream(columnIndex);
  }

  @Override
  public java.io.Reader getCharacterStream(int columnIndex) throws java.sql.SQLException {
    return rs.getCharacterStream(columnIndex);
  }

  @Override
  public java.io.Reader getCharacterStream(String columnLabel) throws java.sql.SQLException {
    return rs.getCharacterStream(columnLabel);
  }

  @Override
  public java.io.Reader getNCharacterStream(int columnIndex) throws java.sql.SQLException {
    return rs.getNCharacterStream(columnIndex);
  }

  @Override
  public java.io.Reader getNCharacterStream(String columnLabel) throws java.sql.SQLException {
    return rs.getNCharacterStream(columnLabel);
  }

  @Override
  @Deprecated
  public java.io.InputStream getUnicodeStream(int columnIndex) throws java.sql.SQLException {
    return rs.getUnicodeStream(columnIndex);
  }

  @Override
  @Deprecated
  public java.io.InputStream getUnicodeStream(String columnLabel) throws java.sql.SQLException {
    return rs.getUnicodeStream(columnLabel);
  }

  @Override
  public java.sql.Blob getBlob(String columnLabel) throws java.sql.SQLException {
    return rs.getBlob(columnLabel);
  }

  @Override
  public java.sql.Blob getBlob(int columnIndex) throws java.sql.SQLException {
    return rs.getBlob(columnIndex);
  }

  @Override
  public java.io.InputStream getBinaryStream(String columnLabel) throws java.sql.SQLException {
    return rs.getBinaryStream(columnLabel);
  }

  @Override
  public java.io.InputStream getBinaryStream(int columnIndex) throws java.sql.SQLException {
    return rs.getBinaryStream(columnIndex);
  }

  @Override
  public int getConcurrency() throws java.sql.SQLException {
    return rs.getConcurrency();
  }

  @Override
  public String getCursorName() throws java.sql.SQLException {
    return rs.getCursorName();
  }

  @Override
  public int getFetchDirection() throws java.sql.SQLException {
    return rs.getFetchDirection();
  }

  @Override
  public int getFetchSize() throws java.sql.SQLException {
    return rs.getFetchSize();
  }

  @Override
  public int getHoldability() throws java.sql.SQLException {
    return rs.getHoldability();
  }

  @Override
  public java.sql.ResultSetMetaData getMetaData() throws java.sql.SQLException {
    return rs.getMetaData();
  }

  @Override
  public int getRow() throws java.sql.SQLException {
    return rs.getRow();
  }

  @Override
  public java.sql.Statement getStatement() throws java.sql.SQLException {
    return rs.getStatement();
  }

  @Override
  public int getType() throws java.sql.SQLException {
    return rs.getType();
  }

  @Override
  public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
    return rs.getWarnings();
  }

  @Override
  public void updateBoolean(String columnLabel, boolean x) throws java.sql.SQLException {
    rs.updateBoolean(columnLabel, x);
  }

  @Override
  public void updateBoolean(int columnIndex, boolean x) throws java.sql.SQLException {
    rs.updateBoolean(columnIndex, x);
  }

  @Override
  public void updateString(int columnIndex, String x) throws java.sql.SQLException {
    rs.updateString(columnIndex, x);
  }

  @Override
  public void updateString(String columnLabel, String x) throws java.sql.SQLException {
    rs.updateString(columnLabel, x);
  }

  @Override
  public void updateNString(String columnLabel, String nString) throws java.sql.SQLException {
    rs.updateNString(columnLabel, nString);
  }

  @Override
  public void updateNString(int columnIndex, String nString) throws java.sql.SQLException {
    rs.updateNString(columnIndex, nString);
  }

  @Override
  public void updateBytes(String columnLabel, byte[] x) throws java.sql.SQLException {
    rs.updateBytes(columnLabel, x);
  }

  @Override
  public void updateBytes(int columnIndex, byte[] x) throws java.sql.SQLException {
    rs.updateBytes(columnIndex, x);
  }

  @Override
  public void updateByte(String columnLabel, byte x) throws java.sql.SQLException {
    rs.updateByte(columnLabel, x);
  }

  @Override
  public void updateByte(int columnIndex, byte x) throws java.sql.SQLException {
    rs.updateByte(columnIndex, x);
  }

  @Override
  public void updateShort(String columnLabel, short x) throws java.sql.SQLException {
    rs.updateShort(columnLabel, x);
  }

  @Override
  public void updateShort(int columnIndex, short x) throws java.sql.SQLException {
    rs.updateShort(columnIndex, x);
  }

  @Override
  public void updateInt(int columnIndex, int x) throws java.sql.SQLException {
    rs.updateInt(columnIndex, x);
  }

  @Override
  public void updateInt(String columnLabel, int x) throws java.sql.SQLException {
    rs.updateInt(columnLabel, x);
  }

  @Override
  public void updateLong(String columnLabel, long x) throws java.sql.SQLException {
    rs.updateLong(columnLabel, x);
  }

  @Override
  public void updateLong(int columnIndex, long x) throws java.sql.SQLException {
    rs.updateLong(columnIndex, x);
  }

  @Override
  public void updateFloat(String columnLabel, float x) throws java.sql.SQLException {
    rs.updateFloat(columnLabel, x);
  }

  @Override
  public void updateFloat(int columnIndex, float x) throws java.sql.SQLException {
    rs.updateFloat(columnIndex, x);
  }

  @Override
  public void updateDouble(String columnLabel, double x) throws java.sql.SQLException {
    rs.updateDouble(columnLabel, x);
  }

  @Override
  public void updateDouble(int columnIndex, double x) throws java.sql.SQLException {
    rs.updateDouble(columnIndex, x);
  }

  @Override
  public void updateBigDecimal(int columnIndex, java.math.BigDecimal x) throws java.sql.SQLException {
    rs.updateBigDecimal(columnIndex, x);
  }

  @Override
  public void updateBigDecimal(String columnLabel, java.math.BigDecimal x) throws java.sql.SQLException {
    rs.updateBigDecimal(columnLabel, x);
  }

  @Override
  public void updateAsciiStream(String columnLabel, java.io.InputStream x) throws java.sql.SQLException {
    rs.updateAsciiStream(columnLabel, x);
  }

  @Override
  public void updateAsciiStream(int columnIndex, java.io.InputStream x) throws java.sql.SQLException {
    rs.updateAsciiStream(columnIndex, x);
  }

  @Override
  public void updateAsciiStream(String columnLabel, java.io.InputStream x, int length) throws java.sql.SQLException {
    rs.updateAsciiStream(columnLabel, x, length);
  }

  @Override
  public void updateAsciiStream(String columnLabel, java.io.InputStream x, long length) throws java.sql.SQLException {
    rs.updateAsciiStream(columnLabel, x, length);
  }

  @Override
  public void updateAsciiStream(int columnIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
    rs.updateAsciiStream(columnIndex, x, length);
  }

  @Override
  public void updateAsciiStream(int columnIndex, java.io.InputStream x, long length) throws java.sql.SQLException {
    rs.updateAsciiStream(columnIndex, x, length);
  }

  @Override
  public void updateBinaryStream(String columnLabel, java.io.InputStream x) throws java.sql.SQLException {
    rs.updateBinaryStream(columnLabel, x);
  }

  @Override
  public void updateBinaryStream(String columnLabel, java.io.InputStream x, int length) throws java.sql.SQLException {
    rs.updateBinaryStream(columnLabel, x, length);
  }

  @Override
  public void updateBinaryStream(String columnLabel, java.io.InputStream x, long length) throws java.sql.SQLException {
    rs.updateBinaryStream(columnLabel, x, length);
  }

  @Override
  public void updateBinaryStream(int columnIndex, java.io.InputStream x, long length) throws java.sql.SQLException {
    rs.updateBinaryStream(columnIndex, x, length);
  }

  @Override
  public void updateBinaryStream(int columnIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
    rs.updateBinaryStream(columnIndex, x, length);
  }

  @Override
  public void updateBinaryStream(int columnIndex, java.io.InputStream x) throws java.sql.SQLException {
    rs.updateBinaryStream(columnIndex, x);
  }

  @Override
  public void updateCharacterStream(int columnIndex, java.io.Reader x, long length) throws java.sql.SQLException {
    rs.updateCharacterStream(columnIndex, x, length);
  }

  @Override
  public void updateCharacterStream(String columnLabel, java.io.Reader reader, long length)
      throws java.sql.SQLException {
    rs.updateCharacterStream(columnLabel, reader, length);
  }

  @Override
  public void updateCharacterStream(int columnIndex, java.io.Reader x, int length) throws java.sql.SQLException {
    rs.updateCharacterStream(columnIndex, x, length);
  }

  @Override
  public void updateCharacterStream(int columnIndex, java.io.Reader x) throws java.sql.SQLException {
    rs.updateCharacterStream(columnIndex, x);
  }

  @Override
  public void updateCharacterStream(String columnLabel, java.io.Reader reader, int length)
      throws java.sql.SQLException {
    rs.updateCharacterStream(columnLabel, reader, length);
  }

  @Override
  public void updateCharacterStream(String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
    rs.updateCharacterStream(columnLabel, reader);
  }

  @Override
  public void updateNull(int columnIndex) throws java.sql.SQLException {
    rs.updateNull(columnIndex);
  }

  @Override
  public void updateNull(String columnLabel) throws java.sql.SQLException {
    rs.updateNull(columnLabel);
  }

  @Override
  public void updateDate(int columnIndex, java.sql.Date x) throws java.sql.SQLException {
    rs.updateDate(columnIndex, x);
  }

  @Override
  public void updateDate(String columnLabel, java.sql.Date x) throws java.sql.SQLException {
    rs.updateDate(columnLabel, x);
  }

  @Override
  public void updateTime(String columnLabel, java.sql.Time x) throws java.sql.SQLException {
    rs.updateTime(columnLabel, x);
  }

  @Override
  public void updateTime(int columnIndex, java.sql.Time x) throws java.sql.SQLException {
    rs.updateTime(columnIndex, x);
  }

  @Override
  public void updateTimestamp(String columnLabel, java.sql.Timestamp x) throws java.sql.SQLException {
    rs.updateTimestamp(columnLabel, x);
  }

  @Override
  public void updateTimestamp(int columnIndex, java.sql.Timestamp x) throws java.sql.SQLException {
    rs.updateTimestamp(columnIndex, x);
  }

  @Override
  public void updateObject(int columnIndex, Object x, java.sql.SQLType targetSqlType, int scaleOrLength)
      throws java.sql.SQLException {
    rs.updateObject(columnIndex, x, targetSqlType, scaleOrLength);
  }

  @Override
  public void updateObject(int columnIndex, Object x, java.sql.SQLType targetSqlType) throws java.sql.SQLException {
    rs.updateObject(columnIndex, x, targetSqlType);
  }

  @Override
  public void updateObject(String columnLabel, Object x, java.sql.SQLType targetSqlType, int scaleOrLength)
      throws java.sql.SQLException {
    rs.updateObject(columnLabel, x, targetSqlType, scaleOrLength);
  }

  @Override
  public void updateObject(String columnLabel, Object x, java.sql.SQLType targetSqlType) throws java.sql.SQLException {
    rs.updateObject(columnLabel, x, targetSqlType);
  }

  @Override
  public void updateObject(String columnLabel, Object x, int scaleOrLength) throws java.sql.SQLException {
    rs.updateObject(columnLabel, x, scaleOrLength);
  }

  @Override
  public void updateObject(int columnIndex, Object x) throws java.sql.SQLException {
    rs.updateObject(columnIndex, x);
  }

  @Override
  public void updateObject(int columnIndex, Object x, int scaleOrLength) throws java.sql.SQLException {
    rs.updateObject(columnIndex, x, scaleOrLength);
  }

  @Override
  public void updateObject(String columnLabel, Object x) throws java.sql.SQLException {
    rs.updateObject(columnLabel, x);
  }

  @Override
  public void updateRow() throws java.sql.SQLException {
    rs.updateRow();
  }

  @Override
  public void updateRef(int columnIndex, java.sql.Ref x) throws java.sql.SQLException {
    rs.updateRef(columnIndex, x);
  }

  @Override
  public void updateRef(String columnLabel, java.sql.Ref x) throws java.sql.SQLException {
    rs.updateRef(columnLabel, x);
  }

  @Override
  public void updateBlob(String columnLabel, java.io.InputStream inputStream, long length)
      throws java.sql.SQLException {
    rs.updateBlob(columnLabel, inputStream, length);
  }

  @Override
  public void updateBlob(String columnLabel, java.io.InputStream inputStream) throws java.sql.SQLException {
    rs.updateBlob(columnLabel, inputStream);
  }

  @Override
  public void updateBlob(int columnIndex, java.io.InputStream inputStream) throws java.sql.SQLException {
    rs.updateBlob(columnIndex, inputStream);
  }

  @Override
  public void updateBlob(int columnIndex, java.io.InputStream inputStream, long length) throws java.sql.SQLException {
    rs.updateBlob(columnIndex, inputStream, length);
  }

  @Override
  public void updateBlob(String columnLabel, java.sql.Blob x) throws java.sql.SQLException {
    rs.updateBlob(columnLabel, x);
  }

  @Override
  public void updateBlob(int columnIndex, java.sql.Blob x) throws java.sql.SQLException {
    rs.updateBlob(columnIndex, x);
  }

  @Override
  public void updateClob(String columnLabel, java.sql.Clob x) throws java.sql.SQLException {
    rs.updateClob(columnLabel, x);
  }

  @Override
  public void updateClob(int columnIndex, java.sql.Clob x) throws java.sql.SQLException {
    rs.updateClob(columnIndex, x);
  }

  @Override
  public void updateClob(int columnIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
    rs.updateClob(columnIndex, reader, length);
  }

  @Override
  public void updateClob(int columnIndex, java.io.Reader reader) throws java.sql.SQLException {
    rs.updateClob(columnIndex, reader);
  }

  @Override
  public void updateClob(String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
    rs.updateClob(columnLabel, reader);
  }

  @Override
  public void updateClob(String columnLabel, java.io.Reader reader, long length) throws java.sql.SQLException {
    rs.updateClob(columnLabel, reader, length);
  }

  @Override
  public void updateArray(int columnIndex, java.sql.Array x) throws java.sql.SQLException {
    rs.updateArray(columnIndex, x);
  }

  @Override
  public void updateArray(String columnLabel, java.sql.Array x) throws java.sql.SQLException {
    rs.updateArray(columnLabel, x);
  }

  @Override
  public void updateRowId(int columnIndex, java.sql.RowId x) throws java.sql.SQLException {
    rs.updateRowId(columnIndex, x);
  }

  @Override
  public void updateRowId(String columnLabel, java.sql.RowId x) throws java.sql.SQLException {
    rs.updateRowId(columnLabel, x);
  }

  @Override
  public void updateNClob(String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
    rs.updateNClob(columnLabel, reader);
  }

  @Override
  public void updateNClob(int columnIndex, java.io.Reader reader) throws java.sql.SQLException {
    rs.updateNClob(columnIndex, reader);
  }

  @Override
  public void updateNClob(int columnIndex, java.sql.NClob nClob) throws java.sql.SQLException {
    rs.updateNClob(columnIndex, nClob);
  }

  @Override
  public void updateNClob(String columnLabel, java.io.Reader reader, long length) throws java.sql.SQLException {
    rs.updateNClob(columnLabel, reader, length);
  }

  @Override
  public void updateNClob(String columnLabel, java.sql.NClob nClob) throws java.sql.SQLException {
    rs.updateNClob(columnLabel, nClob);
  }

  @Override
  public void updateNClob(int columnIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
    rs.updateNClob(columnIndex, reader, length);
  }

  @Override
  public void updateSQLXML(String columnLabel, java.sql.SQLXML xmlObject) throws java.sql.SQLException {
    rs.updateSQLXML(columnLabel, xmlObject);
  }

  @Override
  public void updateSQLXML(int columnIndex, java.sql.SQLXML xmlObject) throws java.sql.SQLException {
    rs.updateSQLXML(columnIndex, xmlObject);
  }

  @Override
  public void updateNCharacterStream(int columnIndex, java.io.Reader x) throws java.sql.SQLException {
    rs.updateNCharacterStream(columnIndex, x);
  }

  @Override
  public void updateNCharacterStream(String columnLabel, java.io.Reader reader) throws java.sql.SQLException {
    rs.updateNCharacterStream(columnLabel, reader);
  }

  @Override
  public void updateNCharacterStream(String columnLabel, java.io.Reader reader, long length)
      throws java.sql.SQLException {
    rs.updateNCharacterStream(columnLabel, reader, length);
  }

  @Override
  public void updateNCharacterStream(int columnIndex, java.io.Reader x, long length) throws java.sql.SQLException {
    rs.updateNCharacterStream(columnIndex, x, length);
  }

  @Override
  public boolean rowUpdated() throws java.sql.SQLException {
    return rs.rowUpdated();
  }

  @Override
  public void cancelRowUpdates() throws java.sql.SQLException {
    rs.cancelRowUpdates();
  }

  @Override
  public boolean wasNull() throws java.sql.SQLException {
    return rs.wasNull();
  }

  @Override
  public boolean isBeforeFirst() throws java.sql.SQLException {
    return rs.isBeforeFirst();
  }

  @Override
  public void beforeFirst() throws java.sql.SQLException {
    rs.beforeFirst();
  }

  @Override
  public void refreshRow() throws java.sql.SQLException {
    rs.refreshRow();
  }

  @Override
  public boolean absolute(int row) throws java.sql.SQLException {
    return rs.absolute(row);
  }

  @Override
  public void afterLast() throws java.sql.SQLException {
    rs.afterLast();
  }

  @Override
  public void clearWarnings() throws java.sql.SQLException {
    rs.clearWarnings();
  }

  @Override
  public void deleteRow() throws java.sql.SQLException {
    rs.deleteRow();
  }

  @Override
  public int findColumn(String columnLabel) throws java.sql.SQLException {
    return rs.findColumn(columnLabel);
  }

  @Override
  public boolean first() throws java.sql.SQLException {
    return rs.first();
  }

  @Override
  public void insertRow() throws java.sql.SQLException {
    rs.insertRow();
  }

  @Override
  public boolean isAfterLast() throws java.sql.SQLException {
    return rs.isAfterLast();
  }

  @Override
  public boolean isClosed() throws java.sql.SQLException {
    return rs.isClosed();
  }

  @Override
  public boolean isFirst() throws java.sql.SQLException {
    return rs.isFirst();
  }

  @Override
  public boolean isLast() throws java.sql.SQLException {
    return rs.isLast();
  }

  @Override
  public boolean last() throws java.sql.SQLException {
    return rs.last();
  }

  @Override
  public void moveToCurrentRow() throws java.sql.SQLException {
    rs.moveToCurrentRow();
  }

  @Override
  public void moveToInsertRow() throws java.sql.SQLException {
    rs.moveToInsertRow();
  }

  @Override
  public boolean next() throws java.sql.SQLException {
    return rs.next();
  }

  @Override
  public boolean previous() throws java.sql.SQLException {
    return rs.previous();
  }

  @Override
  public boolean relative(int rows) throws java.sql.SQLException {
    return rs.relative(rows);
  }

  @Override
  public boolean rowDeleted() throws java.sql.SQLException {
    return rs.rowDeleted();
  }

  @Override
  public boolean rowInserted() throws java.sql.SQLException {
    return rs.rowInserted();
  }

  @Override
  public void setFetchDirection(int direction) throws java.sql.SQLException {
    rs.setFetchDirection(direction);
  }

  @Override
  public void setFetchSize(int rows) throws java.sql.SQLException {
    rs.setFetchSize(rows);
  }

  // java.sql.Wrapper

  @Override
  public boolean isWrapperFor(Class<?> arg0) throws java.sql.SQLException {
    return rs.isWrapperFor(arg0);
  }

  @Override
  public <T> T unwrap(Class<T> arg0) throws java.sql.SQLException {
    return rs.unwrap(arg0);
  }

  // AutoCloseable

  @Override
  public void close() throws java.sql.SQLException {
    rs.close();
  }
}
