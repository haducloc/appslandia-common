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

package com.appslandia.common.factory;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.ThreadSafeTester;

import jakarta.inject.Inject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ObjectFactoryTest {

  @Test
  public void test() {
    try {
      ObjectFactory factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);
      factory.register(TestService.class, TestService.class);

      Assertions.assertNotNull(factory.getObject(TestDao.class));
      Assertions.assertNotNull(factory.getObject(TestService.class));

      Assertions.assertSame(factory.getObject(TestDao.class), factory.getObject(TestDao.class));
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_objectType() {
    try {
      ObjectFactory factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);
      factory.register(TestService.class, TestService.class);

      factory.getObject(Object.class);
      Assertions.fail();

    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ObjectException);
    }
  }

  @Test
  public void test_type_impl() {
    try {
      ObjectFactory factory = new ObjectFactory();
      factory.register(TestDao.class, TestDaoImpl.class);

      Assertions.assertNotNull(factory.getObject(TestDao.class));
      Assertions.assertNotNull(factory.getObject(TestDaoImpl.class));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_ctor() {
    try {
      ObjectFactory factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);
      factory.register(CtorService.class, CtorService.class);

      Assertions.assertNotNull(factory.getObject(TestDao.class));
      Assertions.assertNotNull(factory.getObject(CtorService.class));
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_prototype() {
    try {
      ObjectFactory factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class, ObjectScope.PROTOTYPE);

      Assertions.assertNotNull(factory.getObject(TestDao.class));
      Assertions.assertNotSame(factory.getObject(TestDao.class), factory.getObject(TestDao.class));
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_inject() {
    try {
      ObjectFactory factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);

      UnmanagedService service = new UnmanagedService();
      factory.inject(service);
      Assertions.assertNotNull(service.testDao);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_producer() {
    try {
      ObjectFactory factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);
      factory.register(CtorService.class, new ObjectProducer<CtorService>() {

        @Override
        public CtorService produce(ObjectFactory factory) throws ObjectException {
          TestDao testDao = factory.getObject(TestDao.class);
          return new CtorService(testDao);
        }
      });

      Assertions.assertNotNull(factory.getObject(CtorService.class));
      Assertions.assertSame(factory.getObject(CtorService.class), factory.getObject(CtorService.class));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_threadSafe() {
    final ObjectFactory factory = new ObjectFactory();
    factory.register(TestDao.class, TestDao.class);
    factory.register(ThreadSafeService.class, ThreadSafeService.class);

    new ThreadSafeTester() {

      @Override
      protected Runnable newTask() {
        return new Runnable() {

          @Override
          public void run() {
            try {
              factory.getObject(ThreadSafeService.class);

            } catch (Exception ex) {
              Assertions.fail(ex.getMessage());
            } finally {
              doneTask();
            }
          }
        };
      }
    }.execute();
    Assertions.assertEquals(1, ThreadSafeService.lastSeq.get());
  }

  static class TestService {

    @Inject
    protected TestDao testDao;
  }

  static class CtorService {

    final TestDao testDao;

    @Inject
    public CtorService(TestDao testDao) {
      this.testDao = testDao;
    }
  }

  static class UnmanagedService {

    @Inject
    protected TestDao testDao;
  }

  static class ThreadSafeService {
    static final AtomicInteger lastSeq = new AtomicInteger(0);

    @Inject
    protected TestDao testDao;

    public ThreadSafeService() {
      lastSeq.incrementAndGet();
    }
  }

  static class TestDao {
  }

  static class TestDaoImpl extends TestDao {
  }
}
