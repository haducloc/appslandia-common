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
import java.sql.Types;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SqlTypes {

    static final Set<Integer> TYPES;

    static {
	Set<Integer> types = new LinkedHashSet<>();

	types.add(Types.BIT);
	types.add(Types.BOOLEAN);

	types.add(Types.TINYINT);
	types.add(Types.SMALLINT);
	types.add(Types.INTEGER);
	types.add(Types.BIGINT);

	types.add(Types.FLOAT);
	types.add(Types.REAL);
	types.add(Types.DOUBLE);
	types.add(Types.NUMERIC);
	types.add(Types.DECIMAL);

	types.add(Types.CHAR);
	types.add(Types.VARCHAR);
	types.add(Types.LONGVARCHAR);
	types.add(Types.CLOB);

	types.add(Types.NCHAR);
	types.add(Types.NVARCHAR);
	types.add(Types.LONGNVARCHAR);
	types.add(Types.NCLOB);

	types.add(Types.DATE);
	types.add(Types.TIME);
	types.add(Types.TIMESTAMP);
	types.add(Types.TIME_WITH_TIMEZONE);
	types.add(Types.TIMESTAMP_WITH_TIMEZONE);

	types.add(Types.BINARY);
	types.add(Types.VARBINARY);
	types.add(Types.LONGVARBINARY);
	types.add(Types.BLOB);

	types.add(Types.NULL);
	types.add(Types.OTHER);
	types.add(Types.JAVA_OBJECT);
	types.add(Types.DISTINCT);
	types.add(Types.STRUCT);
	types.add(Types.ARRAY);

	types.add(Types.REF);
	types.add(Types.DATALINK);
	types.add(Types.ROWID);

	types.add(Types.SQLXML);
	types.add(Types.REF_CURSOR);

	TYPES = Collections.unmodifiableSet(types);
    }

    public static boolean isSqlType(int sqlType) {
	return TYPES.contains(sqlType);
    }

    private static final Map<Integer, Class<?>> SQL_TYPE_JAVA_TYPES;
    static {
	Map<Integer, Class<?>> map = new HashMap<>();

	map.put(Types.BIT, boolean.class);
	map.put(Types.BOOLEAN, boolean.class);

	map.put(Types.TINYINT, byte.class);
	map.put(Types.SMALLINT, short.class);
	map.put(Types.INTEGER, int.class);
	map.put(Types.BIGINT, long.class);

	map.put(Types.REAL, float.class);
	map.put(Types.FLOAT, double.class);
	map.put(Types.DOUBLE, double.class);
	map.put(Types.NUMERIC, BigDecimal.class);
	map.put(Types.DECIMAL, BigDecimal.class);

	map.put(Types.CHAR, String.class);
	map.put(Types.VARCHAR, String.class);
	map.put(Types.LONGVARCHAR, String.class);
	map.put(Types.CLOB, java.io.Reader.class);

	map.put(Types.NCHAR, String.class);
	map.put(Types.NVARCHAR, String.class);
	map.put(Types.LONGNVARCHAR, String.class);
	map.put(Types.NCLOB, java.io.Reader.class);

	map.put(Types.DATE, java.sql.Date.class);
	map.put(Types.TIME, java.sql.Time.class);
	map.put(Types.TIMESTAMP, java.sql.Timestamp.class);
	map.put(Types.TIME_WITH_TIMEZONE, OffsetTime.class);
	map.put(Types.TIMESTAMP_WITH_TIMEZONE, OffsetDateTime.class);

	map.put(Types.BINARY, byte[].class);
	map.put(Types.VARBINARY, byte[].class);
	map.put(Types.LONGVARBINARY, byte[].class);
	map.put(Types.BLOB, java.io.InputStream.class);

	SQL_TYPE_JAVA_TYPES = Collections.unmodifiableMap(map);
    }

    public static Class<?> getJavaType(int sqlType) {
	Class<?> type = SQL_TYPE_JAVA_TYPES.get(sqlType);
	return Asserts.notNull(type, () -> STR.fmt("Failed to getJavaType({})", sqlType));
    }
}
