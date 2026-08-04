// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
import java.util.UUID;

import com.appslandia.common.utils.Asserts;

/**
 *
 * @author Loc Ha
 *
 */
public class SqlTypeMapper {

  private static final Map<Integer, Class<?>> TYPES_MAP;
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
    map.put(Types.OTHER, Object.class);

    TYPES_MAP = Collections.unmodifiableMap(map);
  }

  public static Class<?> getJavaType(int sqlType) {
    Class<?> type = TYPES_MAP.get(sqlType);
    return (type != null) ? type : Object.class;
  }

  public static Class<?> getJavaType(int sqlType, DbDialect dbDialect) {
    Asserts.notNull(dbDialect);

    Class<?> type = TYPES_MAP.get(sqlType);
    if (type != null) {
      return type;
    }
    switch (dbDialect.getType()) {
    case MSSQL:
      type = MssqlTypeMapper.getJavaType(sqlType);
      break;

    case MYSQL:
      break;

    case MARIADB:
      break;
    case POSTGRESQL:
      break;

    case ORACLE:
      break;

    case SQLITE:
      break;
    case H2:

      break;
    case DB2:

      break;
    case SAP_HANA:
      break;

    default:
      throw new Error("Unsupported database type: " + type);
    }
    return (type != null) ? type : Object.class;
  }

  static class MssqlTypeMapper {

    private static final Map<Integer, Class<?>> TYPES_MAP;
    static {
      Map<Integer, Class<?>> map = new HashMap<>();

      // DATETIMEOFFSET
      map.put(-155, OffsetDateTime.class);

      // DATETIME & SMALLDATETIME
      map.put(-151, LocalDateTime.class);
      map.put(-150, LocalDateTime.class);

      // MONEY & SMALLMONEY
      map.put(-148, BigDecimal.class);
      map.put(-146, BigDecimal.class);

      // UNIQUEIDENTIFIER
      map.put(-145, UUID.class);

      TYPES_MAP = Collections.unmodifiableMap(map);
    }

    public static Class<?> getJavaType(int sqlType) {
      return TYPES_MAP.get(sqlType);
    }
  }
}
