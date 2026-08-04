// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class ModelUtilsTest {

  @Test
  public void test_copy() {
    var src = new User(1, "user1");
    var dest = new User();

    try {
      ModelUtils.copy(dest, src, "id", "name");

      Assertions.assertEquals(1, dest.getId());
      Assertions.assertEquals("user1", dest.getName());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_copyProps() {
    var src = new User(1, "user1");
    var dest = new Member();

    try {
      ModelUtils.copyProps(dest, src, "id", "name");

      Assertions.assertEquals(1, dest.getId());
      Assertions.assertEquals("user1", dest.getName());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  static class User {
    private int id;
    private String name;

    public User() {
    }

    public User(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  static class Member {
    private int id;
    private String name;

    public Member() {
    }

    public Member(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
