// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class SqlQueryTest {

  @Test
  public void test() {
    var pQuery = "SELECT * FROM User";
    var query = new SqlQuery(pQuery);
    Assertions.assertEquals(pQuery, query.getTranslatedQuery());
  }

  @Test
  public void test_namedParams() {
    var pQuery = "SELECT * FROM User WHERE userId=:id";
    var query = new SqlQuery(pQuery);

    Assertions.assertEquals("SELECT * FROM User WHERE userId=?", query.getTranslatedQuery());

    Assertions.assertEquals(Arrays.asList(1), query.getIndexes("id"));
  }

  @Test
  public void test_indexedParams() {
    var pQuery = "SELECT * FROM User WHERE userId=:0";
    var query = new SqlQuery(pQuery);

    Assertions.assertEquals("SELECT * FROM User WHERE userId=?", query.getTranslatedQuery());

    Assertions.assertEquals(Arrays.asList(1), query.getIndexes("0"));
  }

  @Test
  public void test_repeatedParams() {
    var pQuery = "SELECT * FROM User WHERE :userName='' OR userName LIKE :userName";
    var query = new SqlQuery(pQuery);

    Assertions.assertEquals("SELECT * FROM User WHERE ?='' OR userName LIKE ?", query.getTranslatedQuery());
    Assertions.assertEquals(Arrays.asList(1, 2), query.getIndexes("userName"));
  }

  @Test
  public void test_IN() {
    var pQuery = "SELECT * FROM User WHERE userId IN :ids";
    var query = new SqlQuery(pQuery).arrayLen("ids", 3);

    Assertions.assertEquals("SELECT * FROM User WHERE userId IN (?, ?, ?)", query.getTranslatedQuery());

    try {
      var len = query.getArrayLen("ids");
      Assertions.assertEquals(3, len);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertEquals(Arrays.asList(1), query.getIndexes("ids__0"));
    Assertions.assertEquals(Arrays.asList(2), query.getIndexes("ids__1"));
    Assertions.assertEquals(Arrays.asList(3), query.getIndexes("ids__2"));
  }

  @Test
  public void test_LIKE_ANY() {
    var pQuery = "SELECT * FROM User WHERE userName LIKE_ANY :names";
    var query = new SqlQuery(pQuery).arrayLen("names", 3);

    Assertions.assertEquals("SELECT * FROM User WHERE userName LIKE ? OR userName LIKE ? OR userName LIKE ?",
        query.getTranslatedQuery());

    try {
      var len = query.getArrayLen("names");
      Assertions.assertEquals(3, len);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertEquals(Arrays.asList(1), query.getIndexes("names__0"));
    Assertions.assertEquals(Arrays.asList(2), query.getIndexes("names__1"));
    Assertions.assertEquals(Arrays.asList(3), query.getIndexes("names__2"));
  }

  @Test
  public void test_mixed() {
    var pQuery = "SELECT * FROM User WHERE userType=:userType AND userId IN :userIds AND (userName LIKE_ANY :names)";
    var query = new SqlQuery(pQuery).arrayLen("names", 2).arrayLen("userIds", 2);

    Assertions.assertEquals(
        "SELECT * FROM User WHERE userType=? AND userId IN (?, ?) AND (userName LIKE ? OR userName LIKE ?)",
        query.getTranslatedQuery());

    try {
      var len = query.getArrayLen("names");
      Assertions.assertEquals(2, len);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    try {
      var len = query.getArrayLen("userIds");
      Assertions.assertEquals(2, len);
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
    Assertions.assertTrue(query.isParam("userType"));
    Assertions.assertTrue(query.isParam("userIds"));
    Assertions.assertTrue(query.isParam("names"));

    Assertions.assertFalse(query.isArrayParam("userType"));
    Assertions.assertTrue(query.isArrayParam("userIds"));
    Assertions.assertTrue(query.isArrayParam("names"));

    Assertions.assertEquals(Arrays.asList(1), query.getIndexes("userType"));
    Assertions.assertEquals(Arrays.asList(2), query.getIndexes("userIds__0"));
    Assertions.assertEquals(Arrays.asList(3), query.getIndexes("userIds__1"));

    Assertions.assertEquals(Arrays.asList(4), query.getIndexes("names__0"));
    Assertions.assertEquals(Arrays.asList(5), query.getIndexes("names__1"));
  }
}
