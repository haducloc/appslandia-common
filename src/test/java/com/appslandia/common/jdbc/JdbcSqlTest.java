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

package com.appslandia.common.jdbc;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JdbcSqlTest {

    @Test
    public void test() {
	String sqlText = "SELECT * FROM User";
	JdbcSql sql = new JdbcSql(sqlText);
	Assertions.assertEquals(sqlText, sql.getTranslatedSql());
    }

    @Test
    public void test_namedParams() {
	String sqlText = "SELECT * FROM User WHERE userId=:id";
	JdbcSql sql = new JdbcSql(sqlText);

	Assertions.assertEquals("SELECT * FROM User WHERE userId=?", sql.getTranslatedSql());

	Assertions.assertEquals(Arrays.asList(1), sql.getIndexes("id"));
    }

    @Test
    public void test_indexedParams() {
	String sqlText = "SELECT * FROM User WHERE userId=:0";
	JdbcSql sql = new JdbcSql(sqlText);

	Assertions.assertEquals("SELECT * FROM User WHERE userId=?", sql.getTranslatedSql());

	Assertions.assertEquals(Arrays.asList(1), sql.getIndexes("0"));
    }

    @Test
    public void test_repeatedParams() {
	String sqlText = "SELECT * FROM User WHERE :userName='' OR userName LIKE :userName";
	JdbcSql sql = new JdbcSql(sqlText);

	Assertions.assertEquals("SELECT * FROM User WHERE ?='' OR userName LIKE ?", sql.getTranslatedSql());
	Assertions.assertEquals(Arrays.asList(1, 2), sql.getIndexes("userName"));
    }

    @Test
    public void test_IN() {
	String sqlText = "SELECT * FROM User WHERE userId IN :ids";
	JdbcSql sql = new JdbcSql(sqlText).arrayLen("ids", 3);

	Assertions.assertEquals("SELECT * FROM User WHERE userId IN (?, ?, ?)", sql.getTranslatedSql());

	try {
	    int len = sql.getArrayLen("ids");
	    Assertions.assertEquals(3, len);
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
	Assertions.assertEquals(Arrays.asList(1), sql.getIndexes("ids__0"));
	Assertions.assertEquals(Arrays.asList(2), sql.getIndexes("ids__1"));
	Assertions.assertEquals(Arrays.asList(3), sql.getIndexes("ids__2"));
    }

    @Test
    public void test_LIKE_ANY() {
	String sqlText = "SELECT * FROM User WHERE userName LIKE_ANY :names";
	JdbcSql sql = new JdbcSql(sqlText).arrayLen("names", 3);

	Assertions.assertEquals("SELECT * FROM User WHERE userName LIKE ? OR userName LIKE ? OR userName LIKE ?", sql.getTranslatedSql());

	try {
	    int len = sql.getArrayLen("names");
	    Assertions.assertEquals(3, len);
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
	Assertions.assertEquals(Arrays.asList(1), sql.getIndexes("names__0"));
	Assertions.assertEquals(Arrays.asList(2), sql.getIndexes("names__1"));
	Assertions.assertEquals(Arrays.asList(3), sql.getIndexes("names__2"));
    }

    @Test
    public void test_mixed() {
	String sqlText = "SELECT * FROM User WHERE userType=:userType AND userId IN :userIds AND (userName LIKE_ANY :names)";
	JdbcSql sql = new JdbcSql(sqlText).arrayLen("names", 2).arrayLen("userIds", 2);

	Assertions.assertEquals("SELECT * FROM User WHERE userType=? AND userId IN (?, ?) AND (userName LIKE ? OR userName LIKE ?)", sql.getTranslatedSql());

	try {
	    int len = sql.getArrayLen("names");
	    Assertions.assertEquals(2, len);
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
	try {
	    int len = sql.getArrayLen("userIds");
	    Assertions.assertEquals(2, len);
	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
	Assertions.assertTrue(sql.isParam("userType"));
	Assertions.assertTrue(sql.isParam("userIds"));
	Assertions.assertTrue(sql.isParam("names"));

	Assertions.assertFalse(sql.isArrayParam("userType"));
	Assertions.assertTrue(sql.isArrayParam("userIds"));
	Assertions.assertTrue(sql.isArrayParam("names"));

	Assertions.assertEquals(Arrays.asList(1), sql.getIndexes("userType"));
	Assertions.assertEquals(Arrays.asList(2), sql.getIndexes("userIds__0"));
	Assertions.assertEquals(Arrays.asList(3), sql.getIndexes("userIds__1"));

	Assertions.assertEquals(Arrays.asList(4), sql.getIndexes("names__0"));
	Assertions.assertEquals(Arrays.asList(5), sql.getIndexes("names__1"));
    }
}
