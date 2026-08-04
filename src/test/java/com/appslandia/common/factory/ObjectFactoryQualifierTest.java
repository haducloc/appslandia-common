// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.factory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

/**
 *
 * @author Loc Ha
 *
 */
@SuppressWarnings("all")
public class ObjectFactoryQualifierTest {

  @Test
  public void test() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDaoImpl.class);
      Assertions.assertNotNull(factory.getObject(TestDao.class, Q1.IMPL));

      factory.getObject(TestDao.class);
      Assertions.fail();

    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ObjectException);
    }
  }

  @Test
  public void test_object() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDaoImpl.class);
      Assertions.assertNotNull(factory.getObject(Object.class, Q1.IMPL));

      factory.getObject(Object.class);
      Assertions.fail();

    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ObjectException);
    }
  }

  static class TestDao {
  }

  @Q1
  static class TestDaoImpl extends TestDao {
  }

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.TYPE, ElementType.FIELD })
  @Documented
  public @interface Q1 {
    public static final Q1 IMPL = new ImplLiteral();

    static class ImplLiteral extends AnnotationLiteral<Q1> implements Q1 {
      private static final long serialVersionUID = 1L;
    }
  }
}
