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
import java.net.URL;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SqlTypeMapper {

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
    map.put(Types.FLOAT, float.class);
    map.put(Types.DOUBLE, double.class);
    map.put(Types.NUMERIC, BigDecimal.class);
    map.put(Types.DECIMAL, BigDecimal.class);

    map.put(Types.CHAR, String.class);
    map.put(Types.VARCHAR, String.class);
    map.put(Types.LONGVARCHAR, String.class);
    map.put(Types.CLOB, String.class);

    map.put(Types.NCHAR, String.class);
    map.put(Types.NVARCHAR, String.class);
    map.put(Types.LONGNVARCHAR, String.class);
    map.put(Types.NCLOB, String.class);

    map.put(Types.DATE, LocalDate.class);
    map.put(Types.TIME, LocalTime.class);
    map.put(Types.TIMESTAMP, LocalDateTime.class);
    map.put(Types.TIME_WITH_TIMEZONE, OffsetTime.class);
    map.put(Types.TIMESTAMP_WITH_TIMEZONE, OffsetDateTime.class);

    map.put(Types.BINARY, byte[].class);
    map.put(Types.VARBINARY, byte[].class);
    map.put(Types.LONGVARBINARY, byte[].class);
    map.put(Types.BLOB, byte[].class);

    map.put(Types.SQLXML, String.class);
    map.put(Types.DATALINK, URL.class);

    SQL_TYPE_JAVA_TYPES = Collections.unmodifiableMap(map);
  }

  public static Class<?> getJavaType(int sqlType) {
    Class<?> type = SQL_TYPE_JAVA_TYPES.get(sqlType);
    if (type != null) {
      return type;
    }
    return Object.class;
  }
}
