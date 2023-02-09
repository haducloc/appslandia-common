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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ReflectionException;
import com.appslandia.common.utils.STR;

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

    public static boolean checkTable(Connection conn, String catalog, String schema, String tableName) throws SQLException {
	try (ResultSet rs = conn.getMetaData().getTables(catalog, schema, tableName, new String[] { "TABLE" })) {
	    return rs.next();
	}
    }

    public static Set<String> loadKeys(Connection conn, String catalog, String schema, String tableName) throws SQLException {
	Set<String> keys = new LinkedHashSet<>();
	try (ResultSet rs = conn.getMetaData().getPrimaryKeys(catalog, schema, tableName)) {
	    while (rs.next()) {
		keys.add(rs.getString("COLUMN_NAME"));
	    }
	}
	return keys;
    }

    public static Table loadTable(Connection conn, String catalog, String schema, String tableName) throws SQLException {
	// tableName
	if (!checkTable(conn, catalog, schema, tableName)) {
	    throw new IllegalArgumentException(STR.fmt("catalog={}, schema={}, tableName={} is not found.", catalog, schema, tableName));
	}

	// fields
	List<Field> fields = new ArrayList<>();

	// All keys
	Set<String> keys = loadKeys(conn, catalog, schema, tableName);

	// All columns
	try (ResultSet rs = conn.getMetaData().getColumns(catalog, schema, tableName, null)) {
	    while (rs.next()) {

		String columnName = rs.getString("COLUMN_NAME");
		boolean isKey = keys.contains(columnName);

		int sqlType = rs.getInt("DATA_TYPE");
		int position = rs.getInt("ORDINAL_POSITION");

		boolean nullable = "yes".equalsIgnoreCase(rs.getString("IS_NULLABLE"));
		boolean autoIncr = "yes".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT"));
		boolean genCol = "yes".equalsIgnoreCase(rs.getString("IS_GENERATEDCOLUMN"));

		Field field = new Field();
		field.setName(columnName);
		field.setSqlType(sqlType);
		field.setNullable(nullable);
		field.setPosition(position);

		if (isKey) {
		    field.setKeyType(autoIncr ? FieldType.KEY_INCR : FieldType.KEY);
		} else {
		    field.setKeyType(genCol ? FieldType.COL_GEN : FieldType.COL);
		}
		fields.add(field);
	    }
	}
	return new Table().setCatalog(catalog).setSchema(schema).setName(tableName).setFields(fields);
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

    public static Key toKey(Table table, Object pk) throws ReflectionException {
	Asserts.notNull(pk);
	Asserts.isTrue(PK_JAVA_TYPES.contains(pk.getClass()), "pk is invalid.");

	List<Field> keyFields = table.getFields().stream().filter(f -> f.getKeyType() == FieldType.KEY || f.getKeyType() == FieldType.KEY_INCR).collect(Collectors.toList());

	Asserts.isTrue(keyFields.size() == 1, "table is invalid.");

	return new Key().set(keyFields.get(0).getName(), pk);
    }

    private static final Set<Class<?>> PK_JAVA_TYPES = CollectionUtils.unmodifiableSet(Short.class, Integer.class, Long.class, Float.class, Double.class, BigDecimal.class,
	    String.class, UUID.class, java.sql.Date.class, java.sql.Timestamp.class, LocalDate.class, LocalDateTime.class, OffsetDateTime.class);
}
