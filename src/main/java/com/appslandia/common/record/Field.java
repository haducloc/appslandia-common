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

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.jdbc.JdbcSql;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringFormat;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Field extends InitializeObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Integer sqlType;
    private Integer scaleOrLength;
    private boolean nullable;
    private int position;

    private FieldType keyType = FieldType.COL;

    @Override
    protected void init() throws Exception {
	AssertUtils.assertNotNull(this.name, "name is required.");
	AssertUtils.assertNotNull(this.keyType, "keyType is required.");
    }

    public String getParamName() {
	return JdbcSql.getParamPrefix() + getName();
    }

    public String getName() {
	this.initialize();
	return this.name;
    }

    public Field setName(String name) {
	this.assertNotInitialized();
	this.name = name;
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

    public FieldType getKeyType() {
	this.initialize();
	return this.keyType;
    }

    public Field setKeyType(FieldType keyType) {
	this.assertNotInitialized();
	this.keyType = keyType;
	return this;
    }

    @Override
    public String toString() {
	this.initialize();
	return StringFormat.fmt("name={}, sqlType={}, scaleOrLength={}, nullable={}, position={}, keyType={}", this.name, this.sqlType, this.scaleOrLength, this.nullable,
		this.position, this.keyType);
    }
}
