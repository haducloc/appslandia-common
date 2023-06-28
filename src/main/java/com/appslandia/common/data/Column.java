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

package com.appslandia.common.data;

import java.io.Serializable;
import java.util.List;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.jdbc.JdbcSql;
import com.appslandia.common.jdbc.SqlTypes;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.TypeUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Column extends InitializeObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tableCat;
    private String tableSchema;
    private String tableName;

    private String name;
    private Integer sqlType;
    private Integer scaleOrLength;
    private boolean nullable = true;
    private boolean updatable = true;
    private int position;

    private Class<?> javaType;

    private ColumnType columnType = ColumnType.NON_KEY;
    private List<AnnotationModel> annotations;

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.name, "name is required.");
	Asserts.notNull(this.columnType, "columnType is required.");

	if (this.javaType == null) {
	    Class<?> javaType = (this.sqlType != null) ? SqlTypes.getJavaType(this.sqlType) : null;

	    if (javaType != null) {

		if (this.columnType == ColumnType.KEY_INCR || this.columnType == ColumnType.KEY) {
		    this.javaType = TypeUtils.wrap(javaType);

		} else {
		    this.javaType = this.nullable ? TypeUtils.wrap(javaType) : javaType;
		}
	    }
	}

	this.annotations = CollectionUtils.unmodifiable(this.annotations);
    }

    public String getParamName() {
	return JdbcSql.getParamPrefix() + getName();
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

    public String getName() {
	this.initialize();
	return this.name;
    }

    public Column setName(String name) {
	this.assertNotInitialized();
	if (name != null) {
	    this.name = RecordUtils.toColumnName(name);
	}
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

    public Integer getScaleOrLength() {
	this.initialize();
	return this.scaleOrLength;
    }

    public Column setScaleOrLength(Integer scaleOrLength) {
	this.assertNotInitialized();
	this.scaleOrLength = scaleOrLength;
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

    public List<AnnotationModel> getAnnotations() {
	initialize();
	return this.annotations;
    }

    public Column setAnnotations(List<AnnotationModel> annotations) {
	assertNotInitialized();
	this.annotations = annotations;
	return this;
    }

    @Override
    public String toString() {
	this.initialize();
	return STR.fmt("name={}, sqlType={}, scaleOrLength={}, nullable={}, position={}, columnType={}, javaType={}, tableCat={}, tableSchema={}, tableName={}", this.name,
		this.sqlType, this.scaleOrLength, this.nullable, this.position, this.columnType, this.javaType, this.tableCat, this.tableSchema, this.tableName);
    }
}
