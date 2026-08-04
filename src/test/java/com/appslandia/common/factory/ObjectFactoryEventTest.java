// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.factory;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.appslandia.common.cdi.CDIEventListener;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/**
 *
 * @author Loc Ha
 *
 */
public class ObjectFactoryEventTest {

  @BeforeEach
  void beforeEachTest() {
    TestObjectListener.onEventCalled.set(false);
    TestEventListener.onEventCalled.set(false);
  }

  private void registerListeners(ObjectFactory factory) {
    factory.register(TestObjectListener.class, TestObjectListener.class);
    factory.register(TestEventListener.class, TestEventListener.class);

    factory.registerEventListener(TestEventListener.class, TestEvent.class);
    factory.registerEventListener(TestObjectListener.class, Object.class);
  }

  @Test
  public void test() {
    try {
      var factory = new ObjectFactory();
      registerListeners(factory);
      factory.register(TestEventPublisher.class, TestEventPublisher.class);

      var publisher = factory.getObject(TestEventPublisher.class);
      publisher.publish();

      Assertions.assertTrue(TestObjectListener.onEventCalled.get());
      Assertions.assertTrue(TestEventListener.onEventCalled.get());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_publishObject() {
    try {
      var factory = new ObjectFactory();
      registerListeners(factory);
      factory.register(TestObjectPublisher.class, TestObjectPublisher.class);

      var publisher = factory.getObject(TestObjectPublisher.class);
      publisher.publish();

      Assertions.assertTrue(TestObjectListener.onEventCalled.get());
      Assertions.assertFalse(TestEventListener.onEventCalled.get());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  static class TestEventPublisher {

    @Inject
    protected Event<Object> event;

    public void publish() {
      event.fire(new TestEvent());
    }
  }

  static class TestObjectPublisher {

    @Inject
    protected Event<Object> event;

    public void publish() {
      event.fire(new Object());
    }
  }

  static class TestObjectListener implements CDIEventListener<Object> {

    static final AtomicBoolean onEventCalled = new AtomicBoolean(false);

    @Override
    public void onEvent(@Observes Object event) {
      onEventCalled.getAndSet(true);
    }

    @Override
    public void onEventAsync(Object event) {
    }
  }

  static class TestEventListener implements CDIEventListener<TestEvent> {

    static final AtomicBoolean onEventCalled = new AtomicBoolean(false);

    @Override
    public void onEvent(@Observes TestEvent event) {
      onEventCalled.getAndSet(true);
    }

    @Override
    public void onEventAsync(TestEvent event) {
    }
  }

  static class TestEvent {
  }
}
