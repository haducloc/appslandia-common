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

package com.appslandia.common.data;

import java.io.Serializable;
import java.sql.Types;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.jdbc.SqlTypeMapper;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.TypeUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class Column extends InitializingObject implements Serializable {
  private static final long serialVersionUID = 1L;

  private String tableCat;
  private String tableSchema;
  private String tableName;

  private String qTableCat;
  private String qTableSchema;
  private String qTableName;

  private String name;
  private String qName;
  private String fieldName;

  private String typeName;
  private Integer sqlType;
  private Integer columnSize;
  private Integer fractionDigits;

  private boolean nullable = true;
  private boolean updatable = true;
  private int position;

  private Class<?> javaType;
  private ColumnType columnType;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(name, "name is required.");
    if (qName == null) {
      qName = name;
    }
    Arguments.notNull(sqlType, "sqlType is required.");

    if (javaType == null) {
      javaType = SqlTypeMapper.getJavaType(sqlType);
    }
    fieldName = JdbcUtils.toFieldName(name);

    if (columnType == null) {
      columnType = ColumnType.NON_KEY;
    }

    if (nullable || columnType == ColumnType.KEY_INCR || columnType == ColumnType.KEY) {
      javaType = TypeUtils.wrap(javaType);
    }

    if (columnType == ColumnType.KEY_INCR || columnType == ColumnType.KEY) {
      nullable = false;
      updatable = false;
    }
  }

  public String getParamName() {
    return ":" + getFieldName();
  }

  public Integer getScaleOrLength() {
    initialize();
    return (sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) ? fractionDigits : columnSize;
  }

  public boolean isKeyIncr() {
    initialize();
    return columnType == ColumnType.KEY_INCR;
  }

  public boolean isKey() {
    initialize();
    return columnType == ColumnType.KEY_INCR || columnType == ColumnType.KEY;
  }

  public String getTableCat() {
    initialize();
    return tableCat;
  }

  public Column setTableCat(String tableCat) {
    assertNotInitialized();
    this.tableCat = tableCat;
    return this;
  }

  public String getTableSchema() {
    initialize();
    return tableSchema;
  }

  public Column setTableSchema(String tableSchema) {
    assertNotInitialized();
    this.tableSchema = tableSchema;
    return this;
  }

  public String getTableName() {
    initialize();
    return tableName;
  }

  public Column setTableName(String tableName) {
    assertNotInitialized();
    this.tableName = tableName;
    return this;
  }

  public String getQTableCat() {
    initialize();
    return qTableCat;
  }

  public Column setQTableCat(String qTableCat) {
    assertNotInitialized();
    this.qTableCat = qTableCat;
    return this;
  }

  public String getQTableSchema() {
    initialize();
    return qTableSchema;
  }

  public Column setQTableSchema(String qTableSchema) {
    assertNotInitialized();
    this.qTableSchema = qTableSchema;
    return this;
  }

  public String getQTableName() {
    initialize();
    return qTableName;
  }

  public Column setQTableName(String qTableName) {
    assertNotInitialized();
    this.qTableName = qTableName;
    return this;
  }

  public String getName() {
    initialize();
    return name;
  }

  public Column setName(String name) {
    assertNotInitialized();
    this.name = name;
    return this;
  }

  public String getQName() {
    initialize();
    return qName;
  }

  public Column setQName(String qName) {
    assertNotInitialized();
    this.qName = qName;
    return this;
  }

  public String getFieldName() {
    initialize();
    return fieldName;
  }

  public Column setFieldName(String fieldName) {
    assertNotInitialized();
    this.fieldName = fieldName;
    return this;
  }

  public String getTypeName() {
    initialize();
    return typeName;
  }

  public Column setTypeName(String typeName) {
    assertNotInitialized();
    this.typeName = typeName;
    return this;
  }

  public Integer getSqlType() {
    initialize();
    return sqlType;
  }

  public Column setSqlType(Integer sqlType) {
    assertNotInitialized();
    this.sqlType = sqlType;
    return this;
  }

  public Integer getColumnSize() {
    initialize();
    return columnSize;
  }

  public Column setColumnSize(Integer columnSize) {
    assertNotInitialized();
    this.columnSize = columnSize;
    return this;
  }

  public Integer getFractionDigits() {
    initialize();
    return fractionDigits;
  }

  public Column setFractionDigits(Integer fractionDigits) {
    assertNotInitialized();
    this.fractionDigits = fractionDigits;
    return this;
  }

  public boolean isNullable() {
    initialize();
    return nullable;
  }

  public Column setNullable(boolean nullable) {
    assertNotInitialized();
    this.nullable = nullable;
    return this;
  }

  public boolean isUpdatable() {
    initialize();
    return updatable;
  }

  public Column setUpdatable(boolean updatable) {
    assertNotInitialized();
    this.updatable = updatable;
    return this;
  }

  public int getPosition() {
    initialize();
    return position;
  }

  public Column setPosition(int position) {
    assertNotInitialized();
    this.position = position;
    return this;
  }

  public ColumnType getColumnType() {
    initialize();
    return columnType;
  }

  public Column setColumnType(ColumnType columnType) {
    assertNotInitialized();
    this.columnType = columnType;
    return this;
  }

  public Class<?> getJavaType() {
    initialize();
    return javaType;
  }

  public Column setJavaType(Class<?> javaType) {
    assertNotInitialized();
    this.javaType = javaType;
    return this;
  }

  @Override
  public String toString() {
    initialize();
    return STR.fmt(
        "name={}, typeName={}, sqlType={}, columnSize={?}, fractionDigits={?}, nullable={}, updatable={}, position={}, columnType={}, javaType={}, tableCat={?}, tableSchema={?}, tableName={?}",
        name, typeName, sqlType, columnSize, fractionDigits, nullable, updatable, position, columnType,
        javaType.getName(), tableCat, tableSchema, tableName);
  }
}
