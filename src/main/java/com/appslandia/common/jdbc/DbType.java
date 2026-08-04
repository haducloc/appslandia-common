// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author Loc Ha
 *
 */
public enum DbType {

  // @formatter:off
    POSTGRESQL("jdbc:postgresql:"),
    MYSQL("jdbc:mysql:"),
    MARIADB("jdbc:mariadb:"),
    MSSQL("jdbc:sqlserver:"),
    SQLITE("jdbc:sqlite:"),
    H2("jdbc:h2:"),
    ORACLE("jdbc:oracle:"),
    DB2("jdbc:db2:"),
    SAP_HANA("jdbc:sap:");
  // @formatter:on

  private final String urlPrefix;

  DbType(String urlPrefix) {
    this.urlPrefix = urlPrefix;
  }

  public String getUrlPrefix() {
    return urlPrefix;
  }

  public static DbType parseDbType(String jdbcUrl) {
    Arguments.notNull(jdbcUrl);

    for (DbType dbType : DbType.values()) {
      if (StringUtils.startsWithIgnoreCase(jdbcUrl, dbType.urlPrefix)) {
        return dbType;
      }
    }
    throw new IllegalArgumentException("Failed to parse type from: " + jdbcUrl);
  }
}
