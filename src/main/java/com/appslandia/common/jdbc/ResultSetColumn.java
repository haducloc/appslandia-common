// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class ResultSetColumn {

  final int index;
  final String name;
  final int sqlType;

  public ResultSetColumn(int index, String name, int sqlType) {
    this.index = index;
    this.name = name;
    this.sqlType = sqlType;
  }

  public int getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }

  public int getSqlType() {
    return sqlType;
  }

  @Override
  public String toString() {
    return STR.fmt("index={}, name={}, sqlType={}", name, index, sqlType);
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(index);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ResultSetColumn that)) {
      return false;
    }
    return index == that.index;
  }
}
