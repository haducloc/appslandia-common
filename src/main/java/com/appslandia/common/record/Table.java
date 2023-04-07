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

import java.io.Serializable;
import java.util.List;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.TextBuilder;
import com.appslandia.common.base.ToStringBuilder.TSExcluded;
import com.appslandia.common.jdbc.JdbcSql;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Table extends InitializeObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tableCat;
    private String tableSchema;
    private String tableName;

    private List<Field> fields;
    private transient String recordClassName;

    @TSExcluded
    private transient Field singleKey;

    private transient JdbcSql insertSql;
    private transient JdbcSql updateSql;
    private transient JdbcSql deleteSql;

    private transient JdbcSql getSql;
    private transient JdbcSql existsSql;

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.tableName, "name is required.");
	Asserts.hasElements(this.fields, "fields are required.");

	this.recordClassName = RecordUtils.toRecordClassName(this.tableName);

	int keyIncr = (int) this.fields.stream().filter(field -> field.getFieldType() == FieldType.KEY_INCR).count();
	int keyCount = (int) this.fields.stream().filter(field -> field.getFieldType() == FieldType.KEY_INCR || field.getFieldType() == FieldType.KEY).count();

	if (keyCount == 0) {
	    throw new IllegalArgumentException("No keys found.");
	}
	if (keyIncr > 1) {
	    throw new IllegalArgumentException("More than one auto-increment keys.");
	}
	if (keyCount == 1) {
	    this.singleKey = this.fields.stream().filter(field -> field.getFieldType() == FieldType.KEY_INCR || field.getFieldType() == FieldType.KEY).findFirst().get();
	}

	this.insertSql = new JdbcSql(this.buildInsertSQL());
	this.updateSql = new JdbcSql(this.buildUpdateSQL());
	this.deleteSql = new JdbcSql(this.buildDeleteSQL());

	this.getSql = new JdbcSql(this.buildGetSQL());
	this.existsSql = new JdbcSql(this.buildExistsSQL());
    }

    public String[] getColumnLabels() {
	initialize();

	return this.fields.stream().map(f -> f.getName()).toArray(String[]::new);
    }

    public Field getIncrKey() {
	initialize();
	if (this.singleKey == null || this.singleKey.getFieldType() != FieldType.KEY_INCR) {
	    return null;
	}
	return this.singleKey;
    }

    protected String buildInsertSQL() {
	TextBuilder sb = new TextBuilder().append("INSERT INTO ").append(this.tableName);
	sb.append(" (");

	boolean isFirst = true;
	for (Field field : this.fields) {

	    if (field.getFieldType() != FieldType.KEY_INCR && field.getFieldType() != FieldType.COL_GEN) {

		if (isFirst) {
		    sb.append(field.getName());
		    isFirst = false;
		} else {
		    sb.append(", ").append(field.getName());
		}
	    }
	}
	sb.append(")");
	sb.append(" VALUES (");

	isFirst = true;
	for (Field field : this.fields) {
	    if (field.getFieldType() != FieldType.KEY_INCR && field.getFieldType() != FieldType.COL_GEN) {

		if (isFirst) {
		    sb.append(field.getParamName());
		    isFirst = false;
		} else {
		    sb.append(",").append(field.getParamName());
		}
	    }
	}
	sb.append(")");
	return sb.toString();
    }

    protected String buildUpdateSQL() {
	TextBuilder sb = new TextBuilder().append("UPDATE ").append(this.tableName);
	sb.append(" SET ");

	boolean isFirst = true;
	for (Field field : this.fields) {

	    // Don't update Key & Generated columns
	    if (field.getFieldType() == FieldType.COL) {

		if (isFirst) {
		    sb.append(field.getName()).append("=").append(field.getParamName());
		    isFirst = false;
		} else {
		    sb.append(",").append(field.getName()).append("=").append(field.getParamName());
		}
	    }
	}
	sb.append(" WHERE ");

	this.appendWhereKeyConditions(sb);
	return sb.toString();
    }

    protected String buildDeleteSQL() {
	TextBuilder sb = new TextBuilder().append("DELETE FROM ").append(this.tableName);
	sb.append(" WHERE ");

	this.appendWhereKeyConditions(sb);
	return sb.toString();
    }

    protected String buildExistsSQL() {
	TextBuilder sb = new TextBuilder().append("SELECT COUNT(1) FROM ").append(this.tableName);
	sb.append(" WHERE ");

	this.appendWhereKeyConditions(sb);
	return sb.toString();
    }

    protected String buildGetSQL() {
	TextBuilder sb = new TextBuilder().append("SELECT * FROM ").append(this.tableName);
	sb.append(" WHERE ");

	this.appendWhereKeyConditions(sb);
	return sb.toString();
    }

    protected void appendWhereKeyConditions(TextBuilder sqlBuilder) {
	boolean isFirst = true;
	for (Field field : this.fields) {
	    if (field.getFieldType() == FieldType.KEY_INCR || field.getFieldType() == FieldType.KEY) {

		if (isFirst) {
		    sqlBuilder.append(field.getName()).append("=").append(field.getParamName());
		    isFirst = false;
		} else {
		    sqlBuilder.append(" AND ").append(field.getName()).append("=").append(field.getParamName());
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
	this.initialize();
	return this.tableCat;
    }

    public Table setTableCat(String tableCat) {
	this.assertNotInitialized();
	this.tableCat = tableCat;
	return this;
    }

    public String getTableSchema() {
	this.initialize();
	return this.tableSchema;
    }

    public Table setTableSchema(String tableSchema) {
	this.assertNotInitialized();
	this.tableSchema = tableSchema;
	return this;
    }

    public String getTableName() {
	this.initialize();
	return this.tableName;
    }

    public Table setTableName(String tableName) {
	this.assertNotInitialized();
	this.tableName = tableName;
	return this;
    }

    public List<Field> getFields() {
	initialize();
	return this.fields;
    }

    public Table setFields(List<Field> fields) {
	assertNotInitialized();
	this.fields = fields;
	return this;
    }

    public String getRecordClassName() {
	initialize();
	return this.recordClassName;
    }

    public Field getSingleKey() {
	initialize();
	return this.singleKey;
    }

    public JdbcSql getInsertSql() {
	initialize();
	return this.insertSql;
    }

    public JdbcSql getUpdateSql() {
	initialize();
	return this.updateSql;
    }

    public JdbcSql getDeleteSql() {
	initialize();
	return this.deleteSql;
    }

    public JdbcSql getGetSql() {
	initialize();
	return this.getSql;
    }

    public JdbcSql getExistsSql() {
	initialize();
	return this.existsSql;
    }
}
