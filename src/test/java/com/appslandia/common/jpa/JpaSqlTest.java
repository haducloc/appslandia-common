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

package com.appslandia.common.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JpaSqlTest {

  @Test
  public void test() {
    String sqlText = "SELECT r FROM User r";
    JpaSql sql = new JpaSql(sqlText);
    Assertions.assertEquals(sqlText, sql.getTranslatedSql());
  }

  @Test
  public void test_params() {
    String sqlText = "SELECT r FROM User r WHERE r.userId=:id";
    JpaSql sql = new JpaSql(sqlText);

    Assertions.assertEquals("SELECT r FROM User r WHERE r.userId=:id", sql.getTranslatedSql());

    Assertions.assertTrue(sql.isParam("id"));
    Assertions.assertFalse(sql.isArrayParam("id"));
  }

  @Test
  public void test_indexedParams() {
    String sqlText = "SELECT r FROM User r WHERE r.userId=:0";
    JpaSql sql = new JpaSql(sqlText);

    Assertions.assertEquals("SELECT r FROM User r WHERE r.userId=:0", sql.getTranslatedSql());

    Assertions.assertTrue(sql.isParam("0"));
    Assertions.assertFalse(sql.isArrayParam("0"));
  }

  @Test
  public void test_repeatedParams() {
    String sqlText = "SELECT r FROM User r WHERE :userName='' OR r.userName LIKE :userName";
    JpaSql sql = new JpaSql(sqlText);

    Assertions.assertEquals("SELECT r FROM User r WHERE :userName='' OR r.userName LIKE :userName", sql.getTranslatedSql());

    Assertions.assertTrue(sql.isParam("userName"));
    Assertions.assertFalse(sql.isArrayParam("userName"));
  }

  @Test
  public void test_IN() {
    String sqlText = "SELECT r FROM User r WHERE r.userId IN :ids";
    JpaSql sql = new JpaSql(sqlText).arrayLen("ids", 3);

    Assertions.assertEquals("SELECT r FROM User r WHERE r.userId IN (:ids__0, :ids__1, :ids__2)", sql.getTranslatedSql());

    try {
      int len = sql.getArrayLen("ids");
      Assertions.assertEquals(3, len);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertTrue(sql.isParam("ids"));
    Assertions.assertTrue(sql.isArrayParam("ids"));
  }

  @Test
  public void test_LIKE_ANY() {
    String sqlText = "SELECT r FROM User r WHERE r.userName LIKE_ANY :names";
    JpaSql sql = new JpaSql(sqlText).arrayLen("names", 3);

    Assertions.assertEquals("SELECT r FROM User r WHERE r.userName LIKE :names__0 OR r.userName LIKE :names__1 OR r.userName LIKE :names__2",
        sql.getTranslatedSql());

    try {
      int len = sql.getArrayLen("names");
      Assertions.assertEquals(3, len);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertTrue(sql.isParam("names"));
    Assertions.assertTrue(sql.isArrayParam("names"));
  }

  @Test
  public void test_mixed() {
    String sqlText = "SELECT r FROM User r WHERE r.userId=:id AND r.userType IN :userTypes AND (r.name LIKE_ANY :names)";
    JpaSql sql = new JpaSql(sqlText).arrayLen("userTypes", 3).arrayLen("names", 3);

    Assertions.assertTrue(sql.isParam("id"));
    Assertions.assertTrue(sql.isParam("userTypes"));
    Assertions.assertTrue(sql.isParam("names"));

    Assertions.assertFalse(sql.isArrayParam("id"));
    Assertions.assertTrue(sql.isArrayParam("userTypes"));
    Assertions.assertTrue(sql.isArrayParam("names"));

    Assertions.assertEquals(
        "SELECT r FROM User r WHERE r.userId=:id AND r.userType IN (:userTypes__0, :userTypes__1, :userTypes__2) AND (r.name LIKE :names__0 OR r.name LIKE :names__1 OR r.name LIKE :names__2)",
        sql.getTranslatedSql());
  }
}
