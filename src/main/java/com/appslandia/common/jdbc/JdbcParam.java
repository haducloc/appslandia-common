// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class JdbcParam {

  final Object value;
  final Integer sqlType;
  final Integer scaleOrLength;

  public JdbcParam(Object value, Integer sqlType) {
    this(value, sqlType, null);
  }

  public JdbcParam(Object value, Integer sqlType, Integer scaleOrLength) {
    this.value = value;
    this.sqlType = sqlType;
    this.scaleOrLength = scaleOrLength;
  }

  public Object getValue() {
    return value;
  }

  public Integer getSqlType() {
    return sqlType;
  }

  public Integer getScaleOrLength() {
    return scaleOrLength;
  }

  @Override
  public String toString() {
    return STR.fmt("value={?}, sqlType={?}, scaleOrLength={?}", value, sqlType, scaleOrLength);
  }
}
