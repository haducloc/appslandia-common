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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import com.appslandia.common.jdbc.ConnectionImpl;
import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.jdbc.ResultSetColumn;
import com.appslandia.common.jdbc.ResultSetImpl;
import com.appslandia.common.jdbc.SqlTypeMapper;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ReflectionException;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public final class RecordUtils {

	public static List<Column> getColumns(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		List<Column> cols = new ArrayList<>(md.getColumnCount());

		for (int col = 1; col <= md.getColumnCount(); col++) {
			Column column = new Column();
			column.setName(JdbcUtils.toColumnName(md.getColumnLabel(col)));
			column.setPosition(col);

			int sqlType = md.getColumnType(col);
			column.setSqlType(sqlType);
			column.setJavaType(SqlTypeMapper.getJavaType(sqlType));

			column.setTableCat(md.getCatalogName(col));
			column.setTableSchema(md.getSchemaName(col));
			column.setTableName(md.getTableName(col));

			cols.add(column);
		}
		return Collections.unmodifiableList(cols);
	}

	public static Table loadTable(ConnectionImpl conn, String catalog, String schema, String tableName, Consumer<Column> columnInit) throws SQLException {
		Asserts.notNull(conn);
		Asserts.notNull(tableName);

		// Table
		Table table = null;
		try (ResultSet rs = conn.getMetaData().getTables(catalog, schema, tableName, new String[] { "TABLE" })) {
			while (rs.next()) {
				if (table != null) {
					throw new IllegalArgumentException(STR.fmt("More than one table with name '{}' returned.", tableName));
				}
				table = new Table();

				table.setTableCat(rs.getString("TABLE_CAT"));
				table.setTableSchema(rs.getString("TABLE_SCHEM"));
				table.setTableName(rs.getString("TABLE_NAME"));
			}
		}

		if (table == null) {
			return null;
		}

		// Keys
		Set<String> keys = new LinkedHashSet<>();
		try (ResultSet rs = conn.getMetaData().getPrimaryKeys(catalog, schema, tableName)) {
			while (rs.next()) {
				keys.add(rs.getString("COLUMN_NAME"));
			}
		}

		// columns
		List<Column> columns = new ArrayList<>();

		try (ResultSet rs = conn.getMetaData().getColumns(catalog, schema, tableName, null)) {
			while (rs.next()) {

				String columnName = rs.getString("COLUMN_NAME");
				boolean isKey = keys.contains(columnName);

				boolean autoIncr = "YES".equals(rs.getString("IS_AUTOINCREMENT"));
				boolean genCol = "YES".equals(rs.getString("IS_GENERATEDCOLUMN"));

				Column column = new Column();
				column.setName(columnName);
				column.setTypeName(rs.getString("TYPE_NAME"));

				int sqlType = rs.getInt("DATA_TYPE");
				column.setSqlType(sqlType);

				column.setColumnSize(rs.getInt("COLUMN_SIZE"));

				int fractionDigits = rs.getInt("DECIMAL_DIGITS");
				column.setFractionDigits(!rs.wasNull() ? fractionDigits : null);

				column.setNullable("YES".equals(rs.getString("IS_NULLABLE")));
				column.setPosition(rs.getInt("ORDINAL_POSITION"));

				column.setTableCat(rs.getString("TABLE_CAT"));
				column.setTableSchema(rs.getString("TABLE_SCHEM"));
				column.setTableName(rs.getString("TABLE_NAME"));

				// Java Type
				column.setJavaType(SqlTypeMapper.getJavaType(sqlType));

				if (isKey) {
					column.setColumnType(autoIncr ? ColumnType.KEY_INCR : ColumnType.KEY);
				} else {
					column.setColumnType(genCol ? ColumnType.NON_KEY_GEN : ColumnType.NON_KEY);
				}

				if (columnInit != null) {
					columnInit.accept(column);
				}
				columns.add(column);
			}
		}
		return table.setColumns(columns);
	}

	public static DataRecord toRecord(ResultSetImpl rs) throws SQLException {
		DataRecord dataRecord = new DataRecord();

		for (ResultSetColumn column : rs.getColumns()) {
			dataRecord.set(column.getName(), rs.getObject(column.getIndex()));
		}
		return dataRecord;
	}

	public static DataRecord toRecord(Table table, Object entity) throws ReflectionException {
		try {
			DataRecord dataRecord = new DataRecord();
			for (Column column : table.getColumns()) {
				for (PropertyDescriptor pd : Introspector.getBeanInfo(entity.getClass()).getPropertyDescriptors()) {

					if (!column.getName().equalsIgnoreCase(pd.getName())) {
						continue;
					}
					Asserts.notNull(pd.getReadMethod());
					Asserts.notNull(pd.getWriteMethod());

					dataRecord.put(column.getName(), pd.getReadMethod().invoke(entity));
				}
			}
			return dataRecord;

		} catch (ReflectiveOperationException ex) {
			throw new ReflectionException(ex);

		} catch (IntrospectionException ex) {
			throw new ReflectionException(ex);
		}
	}

	public static Key toKey(Table table, Object pk) {
		Asserts.notNull(pk);
		Asserts.isTrue(PK_JAVA_TYPES.contains(pk.getClass()), "pk is invalid.");

		Column keyColumn = table.getSingleKey();
		Asserts.notNull(keyColumn, "table is invalid.");

		return new Key().set(keyColumn.getName(), pk);
	}

	private static final Set<Class<?>> PK_JAVA_TYPES = CollectionUtils.unmodifiableSet(Short.class, Integer.class, Long.class, Float.class, Double.class,
			BigDecimal.class, String.class, UUID.class, java.sql.Date.class, java.sql.Timestamp.class, LocalDate.class, LocalDateTime.class,
			OffsetDateTime.class);

	public static String toEntityClassName(String tableName) {
		Asserts.notNull(tableName);

		// All Uppers
		if (tableName.equals(tableName.toUpperCase(Locale.ENGLISH))) {
			return StringUtils.firstUpperCase(tableName.toLowerCase(Locale.ENGLISH), Locale.ENGLISH);
		}

		// All Lowers
		if (tableName.equals(tableName.toLowerCase(Locale.ENGLISH))) {
			return StringUtils.firstUpperCase(tableName, Locale.ENGLISH);
		}

		// Mixed
		return StringUtils.firstUpperCase(tableName, Locale.ENGLISH);
	}

	public static Object getFieldValue(Object obj, String columnName) {
		try {
			Field field = obj.getClass().getDeclaredField(columnName);
			return field.get(obj);

		} catch (IllegalAccessException | NoSuchFieldException ex) {
			throw new ReflectionException(ex);
		}
	}
}
