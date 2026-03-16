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
import java.util.Collections;
import java.util.List;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.base.TextBuilder;
import com.appslandia.common.base.ToStringBuilder.TSIdHash;
import com.appslandia.common.jdbc.SqlQuery;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class Table extends InitializingObject implements Serializable {
  private static final long serialVersionUID = 1L;

  private String tableCat;
  private String tableSchema;
  private String tableName;

  private String qTableCat;
  private String qTableSchema;
  private String qTableName;

  private List<Column> columns;
  private transient int keysCount;
  private transient String entityClassName;

  @TSIdHash
  private transient Column singleKey;

  private transient SqlQuery insertQuery;
  private transient SqlQuery updateQuery;
  private transient SqlQuery deleteQuery;

  private transient SqlQuery getQuery;
  private transient SqlQuery existsQuery;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(tableName, "tableName is required.");

    if (qTableName == null) {
      qTableName = tableName;
    }
    Arguments.hasElements(columns, "columns are required.");
    entityClassName = RecordUtils.toEntityClassName(tableName);

    // Validate key
    if (columns.stream().filter(column -> column.getColumnType() == ColumnType.KEY_INCR).count() > 1) {
      throw new IllegalArgumentException("More than one auto-increment key found.");
    }

    keysCount = (int) columns.stream().filter(column -> column.isKey()).count();
    if (keysCount == 1) {
      singleKey = columns.stream().filter(column -> column.isKey()).findFirst().get();
    }

    insertQuery = new SqlQuery(buildInsertQuery());
    updateQuery = new SqlQuery(buildUpdateQuery());
    deleteQuery = new SqlQuery(buildDeleteQuery());

    getQuery = new SqlQuery(buildGetQuery());
    existsQuery = new SqlQuery(buildExistsQuery());

    columns = Collections.unmodifiableList(columns);
  }

  public String[] getColumnLabels() {
    initialize();

    return columns.stream().map(f -> f.getName()).toArray(String[]::new);
  }

  public Column getIncrKey() {
    initialize();
    if (singleKey == null || singleKey.getColumnType() != ColumnType.KEY_INCR) {
      return null;
    }
    return singleKey;
  }

  public boolean hasKeys() {
    initialize();
    return keysCount > 0;
  }

  protected String buildInsertQuery() {
    var sb = new TextBuilder().append("INSERT INTO ").append(qTableName);
    sb.append(" (");

    var isFirst = true;
    for (Column column : columns) {
      if (column.getColumnType() != ColumnType.KEY_INCR && column.getColumnType() != ColumnType.NON_KEY_GEN) {

        if (isFirst) {
          sb.append(column.getQName());
          isFirst = false;
        } else {
          sb.append(", ").append(column.getQName());
        }
      }
    }
    sb.append(')');
    sb.append(" VALUES (");

    isFirst = true;
    for (Column column : columns) {
      if (column.getColumnType() != ColumnType.KEY_INCR && column.getColumnType() != ColumnType.NON_KEY_GEN) {

        if (isFirst) {
          sb.append(column.getParamName());
          isFirst = false;
        } else {
          sb.append(", ").append(column.getParamName());
        }
      }
    }
    sb.append(')');
    return sb.toString();
  }

  protected String buildUpdateQuery() {
    var sb = new TextBuilder().append("UPDATE ").append(qTableName);
    sb.append(" SET ");

    var isFirst = true;
    for (Column column : columns) {
      if (column.getColumnType() == ColumnType.NON_KEY) {

        if (isFirst) {
          sb.append(column.getQName()).append('=').append(column.getParamName());
          isFirst = false;
        } else {
          sb.append(", ").append(column.getQName()).append('=').append(column.getParamName());
        }
      }
    }
    sb.append(" WHERE ");

    appendWhereKeyConditions(sb);
    return sb.toString();
  }

  protected String buildDeleteQuery() {
    var sb = new TextBuilder().append("DELETE FROM ").append(qTableName);
    sb.append(" WHERE ");

    appendWhereKeyConditions(sb);
    return sb.toString();
  }

  protected String buildExistsQuery() {
    var sb = new TextBuilder().append("SELECT COUNT(1) FROM ").append(qTableName);
    sb.append(" WHERE ");

    appendWhereKeyConditions(sb);
    return sb.toString();
  }

  protected String buildGetQuery() {
    var sb = new TextBuilder().append("SELECT * FROM ").append(qTableName);
    sb.append(" WHERE ");

    appendWhereKeyConditions(sb);
    return sb.toString();
  }

  protected void appendWhereKeyConditions(TextBuilder sqlBuilder) {
    var isFirst = true;
    for (Column column : columns) {
      if ((keysCount == 0) || column.isKey()) {

        if (isFirst) {
          sqlBuilder.append(column.getQName()).append('=').append(column.getParamName());
          isFirst = false;
        } else {
          sqlBuilder.append(" AND ").append(column.getQName()).append('=').append(column.getParamName());
        }
      }
    }
  }

  public String getName() {
    return getTableName();
  }

  public Table setName(String name) {
    return setTableName(name);
  }

  public String getTableCat() {
    initialize();
    return tableCat;
  }

  public Table setTableCat(String tableCat) {
    assertNotInitialized();
    this.tableCat = tableCat;
    return this;
  }

  public String getTableSchema() {
    initialize();
    return tableSchema;
  }

  public Table setTableSchema(String tableSchema) {
    assertNotInitialized();
    this.tableSchema = tableSchema;
    return this;
  }

  public String getTableName() {
    initialize();
    return tableName;
  }

  public Table setTableName(String tableName) {
    assertNotInitialized();
    this.tableName = tableName;
    return this;
  }

  public String getQTableCat() {
    initialize();
    return qTableCat;
  }

  public Table setQTableCat(String qTableCat) {
    assertNotInitialized();
    this.qTableCat = qTableCat;
    return this;
  }

  public String getQTableSchema() {
    initialize();
    return qTableSchema;
  }

  public Table setQTableSchema(String qTableSchema) {
    assertNotInitialized();
    this.qTableSchema = qTableSchema;
    return this;
  }

  public String getQTableName() {
    initialize();
    return qTableName;
  }

  public Table setQTableName(String qTableName) {
    assertNotInitialized();
    this.qTableName = qTableName;
    return this;
  }

  public List<Column> getColumns() {
    initialize();
    return columns;
  }

  public Table setColumns(List<Column> columns) {
    assertNotInitialized();
    this.columns = columns;
    return this;
  }

  public String getEntityClassName() {
    initialize();
    return entityClassName;
  }

  public Column getSingleKey() {
    initialize();
    return singleKey;
  }

  public SqlQuery getInsertQuery() {
    initialize();
    return insertQuery;
  }

  public SqlQuery getUpdateQuery() {
    initialize();
    return updateQuery;
  }

  public SqlQuery getDeleteQuery() {
    initialize();
    return deleteQuery;
  }

  public SqlQuery getGetQuery() {
    initialize();
    return getQuery;
  }

  public SqlQuery getExistsQuery() {
    initialize();
    return existsQuery;
  }
}
