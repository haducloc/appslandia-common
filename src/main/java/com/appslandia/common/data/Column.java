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

import com.appslandia.common.base.InitializeObject;
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
public class Column extends InitializeObject implements Serializable {
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
    Arguments.notNull(this.name, "name is required.");
    if (this.qName == null) {
      this.qName = this.name;
    }
    Arguments.notNull(this.sqlType, "sqlType is required.");

    if (this.javaType == null) {
      this.javaType = SqlTypeMapper.getJavaType(this.sqlType);
    }
    this.fieldName = JdbcUtils.toFieldName(this.name);

    if (this.columnType == null) {
      this.columnType = ColumnType.NON_KEY;
    }

    if (this.nullable || this.columnType == ColumnType.KEY_INCR || this.columnType == ColumnType.KEY) {
      this.javaType = TypeUtils.wrap(this.javaType);
    }

    if (this.columnType == ColumnType.KEY_INCR || this.columnType == ColumnType.KEY) {
      this.nullable = false;
      this.updatable = false;
    }
  }

  public String getParamName() {
    return ":" + getFieldName();
  }

  public Integer getScaleOrLength() {
    this.initialize();
    return (this.sqlType == Types.DECIMAL || this.sqlType == Types.NUMERIC) ? this.fractionDigits : this.columnSize;
  }

  public boolean isKeyIncr() {
    this.initialize();
    return this.columnType == ColumnType.KEY_INCR;
  }

  public boolean isKey() {
    this.initialize();
    return this.columnType == ColumnType.KEY_INCR || this.columnType == ColumnType.KEY;
  }

  public String getTableCat() {
    this.initialize();
    return this.tableCat;
  }

  public Column setTableCat(String tableCat) {
    this.assertNotInitialized();
    this.tableCat = tableCat;
    return this;
  }

  public String getTableSchema() {
    this.initialize();
    return this.tableSchema;
  }

  public Column setTableSchema(String tableSchema) {
    this.assertNotInitialized();
    this.tableSchema = tableSchema;
    return this;
  }

  public String getTableName() {
    this.initialize();
    return this.tableName;
  }

  public Column setTableName(String tableName) {
    this.assertNotInitialized();
    this.tableName = tableName;
    return this;
  }

  public String getQTableCat() {
    this.initialize();
    return this.qTableCat;
  }

  public Column setQTableCat(String qTableCat) {
    this.assertNotInitialized();
    this.qTableCat = qTableCat;
    return this;
  }

  public String getQTableSchema() {
    this.initialize();
    return this.qTableSchema;
  }

  public Column setQTableSchema(String qTableSchema) {
    this.assertNotInitialized();
    this.qTableSchema = qTableSchema;
    return this;
  }

  public String getQTableName() {
    this.initialize();
    return this.qTableName;
  }

  public Column setQTableName(String qTableName) {
    this.assertNotInitialized();
    this.qTableName = qTableName;
    return this;
  }

  public String getName() {
    this.initialize();
    return this.name;
  }

  public Column setName(String name) {
    this.assertNotInitialized();
    this.name = name;
    return this;
  }

  public String getQName() {
    this.initialize();
    return this.qName;
  }

  public Column setQName(String qName) {
    this.assertNotInitialized();
    this.qName = qName;
    return this;
  }

  public String getFieldName() {
    this.initialize();
    return this.fieldName;
  }

  public Column setFieldName(String fieldName) {
    this.assertNotInitialized();
    this.fieldName = fieldName;
    return this;
  }

  public String getTypeName() {
    this.initialize();
    return this.typeName;
  }

  public Column setTypeName(String typeName) {
    this.assertNotInitialized();
    this.typeName = typeName;
    return this;
  }

  public Integer getSqlType() {
    this.initialize();
    return this.sqlType;
  }

  public Column setSqlType(Integer sqlType) {
    this.assertNotInitialized();
    this.sqlType = sqlType;
    return this;
  }

  public Integer getColumnSize() {
    this.initialize();
    return this.columnSize;
  }

  public Column setColumnSize(Integer columnSize) {
    this.assertNotInitialized();
    this.columnSize = columnSize;
    return this;
  }

  public Integer getFractionDigits() {
    this.initialize();
    return this.fractionDigits;
  }

  public Column setFractionDigits(Integer fractionDigits) {
    this.assertNotInitialized();
    this.fractionDigits = fractionDigits;
    return this;
  }

  public boolean isNullable() {
    this.initialize();
    return this.nullable;
  }

  public Column setNullable(boolean nullable) {
    this.assertNotInitialized();
    this.nullable = nullable;
    return this;
  }

  public boolean isUpdatable() {
    this.initialize();
    return this.updatable;
  }

  public Column setUpdatable(boolean updatable) {
    this.assertNotInitialized();
    this.updatable = updatable;
    return this;
  }

  public int getPosition() {
    this.initialize();
    return this.position;
  }

  public Column setPosition(int position) {
    this.assertNotInitialized();
    this.position = position;
    return this;
  }

  public ColumnType getColumnType() {
    this.initialize();
    return this.columnType;
  }

  public Column setColumnType(ColumnType columnType) {
    this.assertNotInitialized();
    this.columnType = columnType;
    return this;
  }

  public Class<?> getJavaType() {
    this.initialize();
    return this.javaType;
  }

  public Column setJavaType(Class<?> javaType) {
    this.assertNotInitialized();
    this.javaType = javaType;
    return this;
  }

  @Override
  public String toString() {
    this.initialize();
    return STR.fmt(
        "name={}, typeName={}, sqlType={}, columnSize={?}, fractionDigits={?}, nullable={}, updatable={}, position={}, columnType={}, javaType={}, tableCat={?}, tableSchema={?}, tableName={?}",
        this.name, this.typeName, this.sqlType, this.columnSize, this.fractionDigits, this.nullable, this.updatable,
        this.position, this.columnType, this.javaType.getName(), this.tableCat, this.tableSchema, this.tableName);
  }
}
