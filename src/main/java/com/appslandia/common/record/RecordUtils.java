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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

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

    public static Record toRecord(ResultSet rs, String[] columnLabels) throws SQLException {
	Record record = new Record();
	for (int col = 1; col <= columnLabels.length; col++) {
	    record.set(columnLabels[col - 1], rs.getObject(col));
	}
	return record;
    }

    public static Table loadTable(Connection conn, String catalog, String schema, String tableName) throws SQLException {

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

	// Keys
	Set<String> keys = new LinkedHashSet<>();
	try (ResultSet rs = conn.getMetaData().getPrimaryKeys(catalog, schema, tableName)) {
	    while (rs.next()) {
		keys.add(rs.getString("COLUMN_NAME"));
	    }
	}

	// fields
	List<Field> fields = new ArrayList<>();

	try (ResultSet rs = conn.getMetaData().getColumns(catalog, schema, tableName, null)) {
	    while (rs.next()) {

		String columnName = rs.getString("COLUMN_NAME");
		boolean isKey = keys.contains(columnName);

		boolean autoIncr = "YES".equals(rs.getString("IS_AUTOINCREMENT"));
		boolean genCol = "YES".equals(rs.getString("IS_GENERATEDCOLUMN"));

		Field field = new Field();
		field.setName(columnName);

		field.setSqlType(rs.getInt("DATA_TYPE"));
		field.setNullable("YES".equals(rs.getString("IS_NULLABLE")));
		field.setPosition(rs.getInt("ORDINAL_POSITION"));

		field.setTableCat(rs.getString("TABLE_CAT"));
		field.setTableSchema(rs.getString("TABLE_SCHEM"));
		field.setTableName(rs.getString("TABLE_NAME"));

		if (isKey) {
		    field.setFieldType(autoIncr ? FieldType.KEY_INCR : FieldType.KEY);
		} else {
		    field.setFieldType(genCol ? FieldType.COL_GEN : FieldType.COL);
		}
		fields.add(field);
	    }
	}
	return table.setFields(fields);
    }

    public static Record toRecord(Table table, Object entity) throws ReflectionException {
	try {
	    Record r = new Record();
	    for (Field field : table.getFields()) {
		for (PropertyDescriptor dpd : Introspector.getBeanInfo(entity.getClass()).getPropertyDescriptors()) {

		    if (!field.getName().equalsIgnoreCase(dpd.getName())) {
			continue;
		    }
		    Asserts.notNull(dpd.getReadMethod());
		    Asserts.notNull(dpd.getWriteMethod());

		    r.put(field.getName(), dpd.getReadMethod().invoke(entity));
		}
	    }
	    return r;

	} catch (ReflectiveOperationException ex) {
	    throw new ReflectionException(ex);

	} catch (IntrospectionException ex) {
	    throw new ReflectionException(ex);
	}
    }

    public static Key toKey(Table table, Object pk) {
	Asserts.notNull(pk);
	Asserts.isTrue(PK_JAVA_TYPES.contains(pk.getClass()), "pk is invalid.");

	Field keyField = table.getSingleKey();
	Asserts.notNull(keyField, "table is invalid.");

	return new Key().set(keyField.getName(), pk);
    }

    private static final Set<Class<?>> PK_JAVA_TYPES = CollectionUtils.unmodifiableSet(Short.class, Integer.class, Long.class, Float.class, Double.class, BigDecimal.class,
	    String.class, UUID.class, java.sql.Date.class, java.sql.Timestamp.class, LocalDate.class, LocalDateTime.class, OffsetDateTime.class);

    public static String toFieldName(String dbFieldName) {
	Asserts.notNull(dbFieldName);

	// All Uppers
	if (dbFieldName.equals(dbFieldName.toUpperCase(Locale.ENGLISH))) {
	    return dbFieldName.toLowerCase(Locale.ENGLISH);
	}

	// All Lowers
	if (dbFieldName.equals(dbFieldName.toLowerCase(Locale.ENGLISH))) {
	    return dbFieldName;
	}

	// Mixed
	return StringUtils.firstLowerCase(dbFieldName, Locale.ENGLISH);
    }

    public static String toRecordClassName(String tableName) {
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

    public static List<Field> getFields(ResultSet rs) throws SQLException {
	ResultSetMetaData md = rs.getMetaData();
	List<Field> fields = new ArrayList<>(md.getColumnCount());

	for (int col = 1; col <= md.getColumnCount(); col++) {
	    Field field = new Field();

	    field.setName(md.getColumnLabel(col));
	    field.setFieldType(FieldType.COL);
	    field.setSqlType(md.getColumnType(col));

	    field.setTableCat(md.getCatalogName(col));
	    field.setTableSchema(md.getSchemaName(col));
	    field.setTableName(md.getTableName(col));

	    fields.add(field);
	}

	return fields;
    }
}
