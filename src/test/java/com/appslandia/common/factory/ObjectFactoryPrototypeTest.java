// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.factory;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;

/**
 *
 * @author Loc Ha
 *
 */
public class ObjectFactoryPrototypeTest {

  @BeforeEach
  void beforeEachTest() {
    TestDao.destroyCalled.set(false);
  }

  @Test
  public void test() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);

      var inst1 = factory.select(TestDao.class);
      var inst2 = factory.select(TestDao.class);

      Assertions.assertFalse(inst1.get() == inst2.get());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_destroy() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);

      var inst1 = factory.select(TestDao.class);
      var testDao1 = inst1.get();

      inst1.destroy(testDao1);
      Assertions.assertTrue(TestDao.destroyCalled.get());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_destroyAll() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);

      var inst1 = factory.select(TestDao.class);
      inst1.get();

      factory.destroy();
      Assertions.assertTrue(TestDao.destroyCalled.get());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Dependent
  static class TestDao {

    static final AtomicBoolean destroyCalled = new AtomicBoolean(false);

    @PreDestroy
    protected void destroy() {
      destroyCalled.getAndSet(true);
    }
  }
}
