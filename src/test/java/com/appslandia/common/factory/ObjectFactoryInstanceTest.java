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

package com.appslandia.common.factory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.utils.ObjectUtils;

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
  public void test_select() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);
      factory.register(TestDao.class, TestDao1.class);
      factory.register(TestDao.class, TestDao12.class);
      factory.register(TestDao.class, TestDao13.class);

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
  public void test_testService() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);
      factory.register(TestDao.class, TestDao1.class);
      factory.register(TestDao.class, TestDao12.class);
      factory.register(TestDao.class, TestDao13.class);
      factory.register(TestService.class, TestService.class);

      var service = factory.getObject(TestService.class);
      Assertions.assertNotNull(service.testDaos);

      InstanceImpl<TestDao> testDaos = ObjectUtils.cast(service.testDaos.select());
      Assertions.assertTrue(testDaos.getCount() == 1);

    } catch (Exception ex) {
      Assertions.fail();
    }
  }

  @Test
  public void test_testService1() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);
      factory.register(TestDao.class, TestDao1.class);
      factory.register(TestDao.class, TestDao12.class);
      factory.register(TestDao.class, TestDao13.class);

      factory.register(TestService1.class, TestService1.class);

      var service1 = factory.getObject(TestService1.class);
      Assertions.assertNotNull(service1.testDaos);

      InstanceImpl<TestDao> testDaos_Q1 = ObjectUtils.cast(service1.testDaos.select());
      Assertions.assertTrue(testDaos_Q1.getCount() == 3);

      InstanceImpl<TestDao> testDaos_Q1Q2 = ObjectUtils.cast(testDaos_Q1.select(TestDao.class, Q2.IMPL));
      Assertions.assertTrue(testDaos_Q1Q2.getCount() == 1);

    } catch (Exception ex) {
      Assertions.fail();
    }
  }

  static class TestService {

    @Inject
    Instance<TestDao> testDaos;
  }

  static class TestService1 {

    @Inject
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
