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
public class Field extends InitializeObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tableCat;
    private String tableSchema;
    private String tableName;

    private String name;
    private Integer sqlType;
    private Integer scaleOrLength;
    private boolean nullable = true;
    private int position;

    private Class<?> javaType;

    private FieldType fieldType = FieldType.COL;
    private List<AnnotationModel> annotations;

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.name, "name is required.");
	Asserts.notNull(this.fieldType, "fieldType is required.");

	if (this.javaType == null) {
	    Class<?> javaType = (this.sqlType != null) ? SqlTypes.getJavaType(this.sqlType) : null;

	    if (javaType != null) {

		if (this.fieldType == FieldType.KEY_INCR || this.fieldType == FieldType.KEY) {
		    this.javaType = TypeUtils.wrap(javaType);

		} else {
		    this.javaType = this.nullable ? TypeUtils.wrap(javaType) : javaType;
		}
	    }
	}

	this.annotations = CollectionUtils.unmodifiableList(this.annotations);
    }

    public String getParamName() {
	return JdbcSql.getParamPrefix() + getName();
    }

    public boolean isKeyIncr() {
	this.initialize();

	return this.fieldType == FieldType.KEY_INCR;
    }

    public boolean isKey() {
	this.initialize();

	return this.fieldType == FieldType.KEY_INCR || this.fieldType == FieldType.KEY;
    }

    public String getTableCat() {
	this.initialize();
	return this.tableCat;
    }

    public Field setTableCat(String tableCat) {
	this.assertNotInitialized();
	this.tableCat = tableCat;
	return this;
    }

    public String getTableSchema() {
	this.initialize();
	return this.tableSchema;
    }

    public Field setTableSchema(String tableSchema) {
	this.assertNotInitialized();
	this.tableSchema = tableSchema;
	return this;
    }

    public String getTableName() {
	this.initialize();
	return this.tableName;
    }

    public Field setTableName(String tableName) {
	this.assertNotInitialized();
	this.tableName = tableName;
	return this;
    }

    public String getName() {
	this.initialize();
	return this.name;
    }

    public Field setName(String name) {
	this.assertNotInitialized();
	if (name != null) {
	    this.name = RecordUtils.toFieldName(name);
	}
	return this;
    }

    public Integer getSqlType() {
	this.initialize();
	return this.sqlType;
    }

    public Field setSqlType(Integer sqlType) {
	this.assertNotInitialized();
	this.sqlType = sqlType;
	return this;
    }

    public Integer getScaleOrLength() {
	this.initialize();
	return this.scaleOrLength;
    }

    public Field setScaleOrLength(Integer scaleOrLength) {
	this.assertNotInitialized();
	this.scaleOrLength = scaleOrLength;
	return this;
    }

    public boolean isNullable() {
	this.initialize();
	return this.nullable;
    }

    public Field setNullable(boolean nullable) {
	this.assertNotInitialized();
	this.nullable = nullable;
	return this;
    }

    public int getPosition() {
	this.initialize();
	return this.position;
    }

    public Field setPosition(int position) {
	this.assertNotInitialized();
	this.position = position;
	return this;
    }

    public FieldType getFieldType() {
	this.initialize();
	return this.fieldType;
    }

    public Field setFieldType(FieldType fieldType) {
	this.assertNotInitialized();
	this.fieldType = fieldType;
	return this;
    }

    public Class<?> getJavaType() {
	this.initialize();
	return this.javaType;
    }

    public Field setJavaType(Class<?> javaType) {
	this.assertNotInitialized();
	this.javaType = javaType;
	return this;
    }

    public List<AnnotationModel> getAnnotations() {
	initialize();
	return this.annotations;
    }

    public Field setAnnotations(List<AnnotationModel> annotations) {
	assertNotInitialized();
	this.annotations = annotations;
	return this;
    }

    @Override
    public String toString() {
	this.initialize();
	return STR.fmt("name={}, sqlType={}, scaleOrLength={}, nullable={}, position={}, fieldType={}, javaType={}, tableCat={}, tableSchema={}, tableName={}", this.name,
		this.sqlType, this.scaleOrLength, this.nullable, this.position, this.fieldType, this.javaType, this.tableCat, this.tableSchema, this.tableName);
    }
}
