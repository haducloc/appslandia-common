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

package com.appslandia.common.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class JpaQueryTest {

  @Test
  public void test() {
    var pQuery = "SELECT r FROM User r";
    var query = new JpaQuery(pQuery);
    Assertions.assertEquals(pQuery, query.getTranslatedQuery());
  }

  @Test
  public void test_params() {
    var pQuery = "SELECT r FROM User r WHERE r.userId=:id";
    var query = new JpaQuery(pQuery);

    Assertions.assertEquals("SELECT r FROM User r WHERE r.userId=:id", query.getTranslatedQuery());

    Assertions.assertTrue(query.isParam("id"));
    Assertions.assertFalse(query.isArrayParam("id"));
  }

  @Test
  public void test_indexedParams() {
    var pQuery = "SELECT r FROM User r WHERE r.userId=:0";
    var query = new JpaQuery(pQuery);

    Assertions.assertEquals("SELECT r FROM User r WHERE r.userId=:0", query.getTranslatedQuery());

    Assertions.assertTrue(query.isParam("0"));
    Assertions.assertFalse(query.isArrayParam("0"));
  }

  @Test
  public void test_repeatedParams() {
    var pQuery = "SELECT r FROM User r WHERE :userName='' OR r.userName LIKE :userName";
    var query = new JpaQuery(pQuery);

    Assertions.assertEquals("SELECT r FROM User r WHERE :userName='' OR r.userName LIKE :userName",
        query.getTranslatedQuery());

    Assertions.assertTrue(query.isParam("userName"));
    Assertions.assertFalse(query.isArrayParam("userName"));
  }

  @Test
  public void test_IN() {
    var pQuery = "SELECT r FROM User r WHERE r.userId IN :ids";
    var query = new JpaQuery(pQuery).arrayLen("ids", 3);

    Assertions.assertEquals("SELECT r FROM User r WHERE r.userId IN (:ids__0, :ids__1, :ids__2)",
        query.getTranslatedQuery());

    try {
      var len = query.getArrayLen("ids");
      Assertions.assertEquals(3, len);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertTrue(query.isParam("ids"));
    Assertions.assertTrue(query.isArrayParam("ids"));
  }

  @Test
  public void test_LIKE_ANY() {
    var pQuery = "SELECT r FROM User r WHERE r.userName LIKE_ANY :names";
    var query = new JpaQuery(pQuery).arrayLen("names", 3);

    Assertions.assertEquals(
        "SELECT r FROM User r WHERE r.userName LIKE :names__0 OR r.userName LIKE :names__1 OR r.userName LIKE :names__2",
        query.getTranslatedQuery());

    try {
      var len = query.getArrayLen("names");
      Assertions.assertEquals(3, len);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertTrue(query.isParam("names"));
    Assertions.assertTrue(query.isArrayParam("names"));
  }

  @Test
  public void test_mixed() {
    var pQuery = "SELECT r FROM User r WHERE r.userId=:id AND r.userType IN :userTypes AND (r.name LIKE_ANY :names)";
    var query = new JpaQuery(pQuery).arrayLen("userTypes", 3).arrayLen("names", 3);

    Assertions.assertTrue(query.isParam("id"));
    Assertions.assertTrue(query.isParam("userTypes"));
    Assertions.assertTrue(query.isParam("names"));

    Assertions.assertFalse(query.isArrayParam("id"));
    Assertions.assertTrue(query.isArrayParam("userTypes"));
    Assertions.assertTrue(query.isArrayParam("names"));

    Assertions.assertEquals(
        "SELECT r FROM User r WHERE r.userId=:id AND r.userType IN (:userTypes__0, :userTypes__1, :userTypes__2) AND (r.name LIKE :names__0 OR r.name LIKE :names__1 OR r.name LIKE :names__2)",
        query.getTranslatedQuery());
  }
}
