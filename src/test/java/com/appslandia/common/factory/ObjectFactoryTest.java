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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.ThreadSafeTester;

import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

/**
 *
 * @author Loc Ha
 *
 */
public class ObjectFactoryTest {

  @Test
  public void test() {
    try {
      var factory = new ObjectFactory();
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
  public void test_object() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);
      factory.register(TestService.class, TestService.class);

      factory.getObject(Object.class);
      Assertions.fail();

    } catch (Exception ex) {
      Assertions.assertTrue(ex instanceof ObjectException);
    }
  }

  @Test
  public void test_impl() {
    try {
      var factory = new ObjectFactory();
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
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);
      factory.register(CtorService.class, CtorService.class);

      Assertions.assertNotNull(factory.getObject(TestDao.class));
      Assertions.assertNotNull(factory.getObject(CtorService.class));
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_inject() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);

      var service = new UnmanagedService();
      factory.inject(service);
      Assertions.assertNotNull(service.testDao);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_destroy() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);
      factory.getObject(TestDao.class);

      TestDao.destroyCalled.set(false);
      factory.destroy();
      Assertions.assertTrue(TestDao.destroyCalled.get());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_producer() {
    try {
      var factory = new ObjectFactory();
      factory.register(TestDao.class, TestDao.class);
      factory.register(CtorService.class, new ObjectProducer<CtorService>() {

        @Override
        public CtorService produce(ObjectFactory factory) throws ObjectException {
          var testDao = factory.getObject(TestDao.class);
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
    final var factory = new ObjectFactory();
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

    static final AtomicBoolean destroyCalled = new AtomicBoolean(false);

    @PreDestroy
    protected void destroy() {
      destroyCalled.set(true);
    }
  }

  static class TestDaoImpl extends TestDao {
  }
}
