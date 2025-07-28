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
