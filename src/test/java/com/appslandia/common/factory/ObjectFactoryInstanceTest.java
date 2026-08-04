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

import com.appslandia.common.utils.ObjectUtils;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;
import jakarta.inject.Qualifier;

/**
 *
 * @author Loc Ha
 *
 */
@SuppressWarnings("all")
public class ObjectFactoryInstanceTest {

  @Test
  public void test_select_defaultAndQualified() {
    try {
      var factory = new ObjectFactory();
      registerDaos(factory);

      InstanceImpl<TestDao> testDaos = ObjectUtils.cast(factory.select(TestDao.class));
      Assertions.assertTrue(testDaos.getCount() == 1);

      InstanceImpl<TestDao> testDaos_Q1 = ObjectUtils.cast(factory.select(TestDao.class, Q1.IMPL));
      Assertions.assertTrue(testDaos_Q1.getCount() == 3);

      InstanceImpl<TestDao> testDaos_Q1Q2 = ObjectUtils.cast(testDaos_Q1.select(TestDao.class, Q2.IMPL));
      Assertions.assertTrue(testDaos_Q1Q2.getCount() == 1);

    } catch (Exception ex) {
      Assertions.fail();
    }
  }

  @Test
  public void test_inject_instance_defaultImplicit() {
    try {
      var factory = new ObjectFactory();
      registerDaos(factory);
      factory.register(TestServiceDefaultImplicit.class, TestServiceDefaultImplicit.class);

      var service = factory.getObject(TestServiceDefaultImplicit.class);
      Assertions.assertNotNull(service.testDaos);

      InstanceImpl<TestDao> testDaos = ObjectUtils.cast(service.testDaos.select());
      Assertions.assertTrue(testDaos.getCount() == 1);

    } catch (Exception ex) {
      Assertions.fail();
    }
  }

  @Test
  public void test_inject_instance_defaultExplicit() {
    try {
      var factory = new ObjectFactory();
      registerDaos(factory);
      factory.register(TestServiceDefaultExplicit.class, TestServiceDefaultExplicit.class);

      var service = factory.getObject(TestServiceDefaultExplicit.class);
      Assertions.assertNotNull(service.testDaos);

      InstanceImpl<TestDao> testDaos = ObjectUtils.cast(service.testDaos.select());
      Assertions.assertTrue(testDaos.getCount() == 1);

    } catch (Exception ex) {
      Assertions.fail();
    }
  }

  @Test
  public void test_inject_instance_q1() {
    try {
      var factory = new ObjectFactory();
      registerDaos(factory);
      factory.register(TestServiceQ1.class, TestServiceQ1.class);

      var service = factory.getObject(TestServiceQ1.class);
      Assertions.assertNotNull(service.testDaos);

      InstanceImpl<TestDao> testDaos_Q1 = ObjectUtils.cast(service.testDaos.select());
      Assertions.assertTrue(testDaos_Q1.getCount() == 3);

      InstanceImpl<TestDao> testDaos_Q1Q2 = ObjectUtils.cast(testDaos_Q1.select(TestDao.class, Q2.IMPL));
      Assertions.assertTrue(testDaos_Q1Q2.getCount() == 1);

    } catch (Exception ex) {
      Assertions.fail();
    }
  }

  @Test
  public void test_inject_instance_anyQ1() {
    try {
      var factory = new ObjectFactory();
      registerDaos(factory);
      factory.register(TestServiceAnyQ1.class, TestServiceAnyQ1.class);

      var service = factory.getObject(TestServiceAnyQ1.class);
      Assertions.assertNotNull(service.testDaos);

      InstanceImpl<TestDao> testDaos = ObjectUtils.cast(service.testDaos.select());
      Assertions.assertTrue(testDaos.getCount() == 3);

    } catch (Exception ex) {
      Assertions.fail();
    }
  }

  @Test
  public void test_inject_instance_defaultQ1() {
    try {
      var factory = new ObjectFactory();
      registerDaos(factory);
      factory.register(TestServiceDefaultQ1.class, TestServiceDefaultQ1.class);

      var service = factory.getObject(TestServiceDefaultQ1.class);
      Assertions.assertNotNull(service.testDaos);

      InstanceImpl<TestDao> testDaos = ObjectUtils.cast(service.testDaos.select());
      Assertions.assertTrue(testDaos.getCount() == 0);

    } catch (Exception ex) {
      Assertions.fail();
    }
  }

  static void registerDaos(ObjectFactory factory) {
    factory.register(TestDao.class, TestDao.class);
    factory.register(TestDao.class, TestDao1.class);
    factory.register(TestDao.class, TestDao12.class);
    factory.register(TestDao.class, TestDao13.class);
  }

  static class TestServiceDefaultImplicit {

    @Inject
    Instance<TestDao> testDaos;
  }

  static class TestServiceDefaultExplicit {

    @Inject
    @Default
    Instance<TestDao> testDaos;
  }

  static class TestServiceQ1 {

    @Inject
    @Q1
    Instance<TestDao> testDaos;
  }

  static class TestServiceAnyQ1 {

    @Inject
    @Any
    @Q1
    Instance<TestDao> testDaos;
  }

  static class TestServiceDefaultQ1 {

    @Inject
    @Default
    @Q1
    Instance<TestDao> testDaos;
  }

  static class TestDao {
  }

  @Q1
  static class TestDao1 extends TestDao {
  }

  @Q1
  @Q2
  static class TestDao12 extends TestDao {
  }

  @Q1
  @Q3
  static class TestDao13 extends TestDao {
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

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.TYPE, ElementType.FIELD })
  @Documented
  public @interface Q2 {
    public static final Q2 IMPL = new ImplLiteral();

    static class ImplLiteral extends AnnotationLiteral<Q2> implements Q2 {
      private static final long serialVersionUID = 1L;
    }
  }

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.TYPE, ElementType.FIELD })
  @Documented
  public @interface Q3 {
    public static final Q3 IMPL = new ImplLiteral();

    static class ImplLiteral extends AnnotationLiteral<Q3> implements Q3 {
      private static final long serialVersionUID = 1L;
    }
  }
}
